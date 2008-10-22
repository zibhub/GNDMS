package de.zib.gndms.logic.model.gorfx.c3grid;

import de.zib.gndms.kit.util.DirectoryAux;
import de.zib.gndms.logic.action.MandatoryOptionMissingException;
import de.zib.gndms.logic.action.ProcessBuilderAction;
import de.zib.gndms.logic.model.config.ConfigProvider;
import de.zib.gndms.logic.model.config.MapConfig;
import de.zib.gndms.logic.model.dspace.CreateSliceAction;
import de.zib.gndms.logic.model.gorfx.ORQTaskAction;
import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.model.dspace.MetaSubspace;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.gorfx.types.ProviderStageInResult;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQConverter;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQPropertyWriter;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQWriter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 02.10.2008 Time: 15:04:39
 */
@SuppressWarnings({ "FeatureEnvy" })
public class ProviderStageInAction extends ORQTaskAction<ProviderStageInORQ> {


    public ProviderStageInAction() {
        super();
    }


    public ProviderStageInAction(final @NotNull EntityManager em, final @NotNull Task model) {
        super(em, model);
    }


    public ProviderStageInAction(final @NotNull EntityManager em, final @NotNull String pk) {
        super(em, pk);
    }


    @SuppressWarnings({ "ThrowableInstanceNeverThrown" })
    @Override
    protected void onCreated(final Task model) {
        try {
            super.onCreated(model);    // Overridden method
        }
        catch (TransitException e) {
            if (e.isDemandingAbort()) throw e; // dont continue on failure

            try {
                MapConfig config = new MapConfig(getKey().getConfigMap());
                File stagingCommandFile = config.getFileOption("stagingCommand");
                if (! stagingCommandFile.exists() || ! stagingCommandFile.canRead() || ! stagingCommandFile.isFile())
                    fail(new IllegalArgumentException("Invalid stagingCommand script: " + stagingCommandFile.getPath()));
                createNewSlice(model);
            }
            catch (MandatoryOptionMissingException e1) {
                fail(new RuntimeException(e1));
            }
            throw e; // accept state transition decision from super
        }
    }


    @Override
    @NotNull
    protected Class<ProviderStageInORQ> getOrqClass() {
        return ProviderStageInORQ.class;
    }


    @SuppressWarnings({ "ThrowableInstanceNeverThrown"})
    @Override
    protected void onInProgress(final @NotNull Task model) {
        final Slice slice = findNewSlice(model);
        final ProcessBuilder procBuilder = createProcessBuilder(slice);
        final StringBuilder recv = new StringBuilder(8);

        try {
            final ProcessBuilderAction action = createStagingAction();

            action.setProcessBuilder(procBuilder);
            action.setOutputReceiver(recv);
            int result = action.call();
            if (result == 0)
                finish( new ProviderStageInResult( slice.getId() ) );
            else
                fail(new IllegalStateException("Staging script failed with non-zero exit code " + result));
        }
        catch (RuntimeException e) {
            maskTransitException(e);
            fail(new RuntimeException(recv.toString(), e));
        }
    }


    private ProcessBuilderAction createStagingAction() {
        final Properties props = getSFRProps();
        final ProcessBuilderAction action = new ProcessBuilderAction() {
            protected @Override void writeOutput(final @NotNull BufferedOutputStream stream)
                    throws IOException {
                props.store(stream, "ProviderStageIn");
            }
        };
        return action;
    }


    private Properties getSFRProps() {
        final Properties props = new Properties();
        final ProviderStageInORQWriter writer = new ProviderStageInORQPropertyWriter(props);
        final ProviderStageInORQConverter converter = new ProviderStageInORQConverter(writer, getOrq());
        converter.convert();
        return props;
    }


    private void createNewSlice(final Task model) throws MandatoryOptionMissingException {

        final ConfigProvider config = new MapConfig(getKey().getConfigMap());

        final EntityManager em = getEntityManager();
        final boolean wasActive = em.getTransaction().isActive();
        if (! wasActive)
            getEntityManager().getTransaction().begin();
        try {
            final ImmutableScopedName scopedName = config.getISNOption("subspace");
            final MetaSubspace metaSubspace = getEntityManager().find(
                    MetaSubspace.class,
                    scopedName);
            final @NotNull Subspace subspace = metaSubspace.getInstance();
            String slicekindKey = config.getOption("sliceKind");
            SliceKind kind = getEntityManager().find(SliceKind.class, slicekindKey);
            CreateSliceAction csa = new CreateSliceAction();
            csa.setParent(this);
            csa.setTerminationTime(getModel().getContract().getResultValidity());
            csa.setClosingEntityManagerOnCleanup(false);
            csa.setDirectoryAux(DirectoryAux.getDirectoryAux());
            csa.setUUIDGen(getUUIDGen());
            csa.setId(getUUIDGen().nextUUID());
            csa.setModel(subspace);
            csa.setSliceKind(kind);
            final Slice slice = csa.execute(getEntityManager());
            model.setData(slice.getId());
            if (! wasActive)
                getEntityManager().getTransaction().commit();
        }
        finally {
            if (! wasActive)
                if (getEntityManager().getTransaction().isActive())
                    getEntityManager().getTransaction().rollback();
        }
    }


    private Slice findNewSlice(final Task model) {
        final EntityManager em = getEntityManager();
        final boolean wasActive = em.getTransaction().isActive();

        if (! wasActive)
            getEntityManager().getTransaction().begin();
        try {
            final Slice slice = em.find(Slice.class, model.getData());
            if (! wasActive)
                getEntityManager().getTransaction().commit();
            return slice;
        }
        finally {
            if (getEntityManager().getTransaction().isActive())
                getEntityManager().getTransaction().rollback();
        }
    }


    private ProcessBuilder createProcessBuilder(final Slice sliceParam) {
        try {
            ProcessBuilder builder = new ProcessBuilder();
            ConfigProvider opts = new MapConfig(getKey().getConfigMap());
            builder.directory(new File(sliceParam.getOwner().getPathForSlice(sliceParam)));
            builder.command(opts.getFileOption("stagingCommand").getPath());
            return builder;
        }
        catch (MandatoryOptionMissingException e) {
            throw new RuntimeException(e);
        }
    }
}

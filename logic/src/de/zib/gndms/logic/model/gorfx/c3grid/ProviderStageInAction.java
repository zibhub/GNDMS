package de.zib.gndms.logic.model.gorfx.c3grid;

import de.zib.gndms.kit.util.DirectoryAux;
import de.zib.gndms.logic.action.MandatoryOptionMissingException;
import de.zib.gndms.logic.model.config.ConfigProvider;
import de.zib.gndms.logic.model.config.MapConfig;
import de.zib.gndms.logic.model.dspace.CreateSliceAction;
import de.zib.gndms.logic.model.gorfx.ORQTaskAction;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import javax.persistence.EntityManager;
import java.io.IOException;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 02.10.2008 Time: 15:04:39
 */
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
    protected void onInProgress(final @NotNull Task model) {
        try {
            // create slice
            Slice slice = createNewSlice();

            // create process
            ProcessBuilder builder = new ProcessBuilder();
            ConfigProvider opts = new MapConfig(getKey().getConfigMap());
            builder.command("/bin/sh","-c", opts.getOption("stagingCommand"));
            Process proc = builder.start();

            // todo: give process acess to proper sfr including file names
            // todo: timeout
            // todo: java plugin

            while(true) {
                try {
                    final int exitCode = proc.waitFor();
                    if (exitCode == 0)
                        finish(new ProviderStageInResult(getKey().getOfferTypeKey(), slice.getId()));
                    else
                        fail(new IllegalStateException("Non-zero exitcode " + Integer.toString(exitCode)));
                }
                catch (InterruptedException e) {
                    // do nothing
                }
            }
        }
        catch (IOException e) {
            fail(new RuntimeException(e));
        }
        catch (MandatoryOptionMissingException e) {
            fail(new RuntimeException(e));
        }

    }


    private Slice createNewSlice() throws MandatoryOptionMissingException {

        final ConfigProvider config = new MapConfig(getKey().getConfigMap());

        final boolean wasActive = getEntityManager().getTransaction().isActive();
        if (! wasActive)
            getEntityManager().getTransaction().begin();
        try {
            String subspaceKey = config.getOption("subspace");
            Subspace subspace = getEntityManager().find(Subspace.class, subspaceKey);
            String slicekindKey = config.getOption("sliceKind");
            SliceKind kind = getEntityManager().find(SliceKind.class, slicekindKey);
            CreateSliceAction csa = new CreateSliceAction();
            csa.setParent(this);
            csa.setTerminationTime(new DateTime().toGregorianCalendar());
            csa.setClosingEntityManagerOnCleanup(false);
            csa.setDirectoryAux(DirectoryAux.getDirectoryAux());
            csa.setUUIDGen(getUUIDGen());
            csa.setModel(subspace);
            csa.setSliceKind(kind);
            final Slice slice = csa.execute(getEntityManager());
            if (! wasActive)
                getEntityManager().getTransaction().commit();
            return slice;
        }
        finally {
            if (getEntityManager().getTransaction().isActive())
                getEntityManager().getTransaction().rollback();
        }
    }


    @Override
    protected @NotNull Class<ProviderStageInORQ> getOrqClass() {
        return ProviderStageInORQ.class;
    }
}

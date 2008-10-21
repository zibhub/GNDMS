package de.zib.gndms.logic.model.gorfx.c3grid;

import de.zib.gndms.kit.util.DirectoryAux;
import de.zib.gndms.logic.action.MandatoryOptionMissingException;
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
import java.io.*;
import java.util.Properties;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


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


    @SuppressWarnings({ "ThrowableInstanceNeverThrown"})
    @Override
    protected void onInProgress(final @NotNull Task model) {
        final Slice slice = findNewSlice(model);
        Process proc = null;
        StreamCollector coll = null;
        Thread collThread = null;
        Lock lock = new ReentrantLock();
        Condition cond = lock.newCondition();
        try {
            proc = createProcess(slice);
            coll = createCollector(proc, lock, cond);
//            final Process finalProc = proc;
//            new Thread(new Runnable() {
//                public void run() {
//                    try {
//                        writeOrq(finalProc);
//                    }
//                    catch (IOException e) {
//                        // whatever
//                    }
//                }
//            }).start();
            // todo: timeout
            // todo: java plugin
            lock.lock();
            try {
                collThread = new Thread(coll);
                collThread.start();
                writeOrq(proc);
                try {
                    cond.await();
                }
                catch (InterruptedException e) {
                    // ignore
                }
            }
            finally { lock.unlock(); }
            waitForProcessToFinish(slice, proc);
        }
        catch (IOException e) {
            fail(new RuntimeException(coll == null ? "" : coll.getResult().toString(), e));
        }
        catch (MandatoryOptionMissingException e) {
            fail(new RuntimeException(coll == null ? "" : coll.getResult().toString(), e));
        }

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


    private Process createProcess(final Slice sliceParam)
            throws MandatoryOptionMissingException, IOException {
        ProcessBuilder builder = new ProcessBuilder();
        ConfigProvider opts = new MapConfig(getKey().getConfigMap());
        builder.directory(new File(sliceParam.getOwner().getPathForSlice(sliceParam)));
        builder.redirectErrorStream(true);
        builder.command(opts.getFileOption("stagingCommand").getPath());
        return builder.start();
    }


    private void writeOrq(final Process procParam) throws IOException {
        Properties props = new Properties();
        ProviderStageInORQWriter writer = new ProviderStageInORQPropertyWriter(props);
        ProviderStageInORQConverter converter = new ProviderStageInORQConverter(writer, getOrq());
        converter.convert();

        BufferedOutputStream outStream = new BufferedOutputStream(procParam.getOutputStream());
        try {
            props.store(outStream, "ProviderStageIn");
        }
        finally {
            outStream.close(); // forces flush
        }
    }


    @SuppressWarnings({ "MethodMayBeStatic" })
    private StreamCollector createCollector(
            final Process procParam, final Lock lock, final Condition cond) {
        return new StreamCollector(procParam.getInputStream(), lock, cond);
    }


    @SuppressWarnings(
            { "InfiniteLoopStatement", "ObjectAllocationInLoop", "ThrowableInstanceNeverThrown" })
    private void waitForProcessToFinish(final Slice sliceParam, final Process procParam) {
        while(true) {
            try {
                final int exitCode = procParam.waitFor();
                if (exitCode == 0)
                    finish(new ProviderStageInResult(sliceParam.getId()));
                else
                    fail(new IllegalStateException("Non-zero exitcode "
                            + Integer.toString(exitCode)));
            }
            catch (InterruptedException e) {
                // do nothing
            }
        }
    }


    @Override
    protected @NotNull Class<ProviderStageInORQ> getOrqClass() {
        return ProviderStageInORQ.class;
    }


    public static class StreamCollector implements Runnable {

        // kByte
        private static final int INITIAL_COLLECTOR_SIZE = 64;
        private final InputStream inStream;
        private StringBuilder collector;
        private RuntimeException exception;
        private Lock runLock;
        private Condition doneCondition;


        public StreamCollector(final InputStream inStreamParam, final Lock lockParam, final Condition conditionParam) {
            inStream = inStreamParam;
            runLock = lockParam;
            doneCondition = conditionParam;
        }


        public void run() {
            runLock.lock();
            try {
                if (exception != null)
                    throw exception;
                if (collector != null) {
                    exception = new IllegalStateException("Can't run collector twice!");
                    throw exception;
                }
                try {
                    try {
                        StringBuilder newCollector = new StringBuilder(INITIAL_COLLECTOR_SIZE << 10);
                        collectOutput(inStream, newCollector);
                        collector = newCollector;
                    }
                    catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                catch (RuntimeException e) {
                    exception = e;
                }
            }
            finally {
                doneCondition.signalAll();
                runLock.unlock();
            }
        }

        @SuppressWarnings({ "NestedAssignment" })
        private static void collectOutput(InputStream inputStream, StringBuilder collectorParam)
                throws IOException {
            final InputStreamReader inReader = new InputStreamReader(inputStream);
            try {
                final BufferedReader reader = new BufferedReader(inReader);
                try {
                    String line;
                    while ((line = reader.readLine()) != null)
                        collectorParam.append(line);
                }
                finally {
                     reader.close();
                }
            }
            finally {
                inReader.close();
            }
        }

        public StringBuilder getResult() {
            if (collector == null) {
                if (exception == null)
                    throw new IllegalStateException("Collector has not yet been run");
                else
                    throw exception;
            }
            else
                return collector;
        }
    }
}

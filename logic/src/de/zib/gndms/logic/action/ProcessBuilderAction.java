package de.zib.gndms.logic.action;

import org.jetbrains.annotations.NotNull;

import java.io.*;


/**
 * Wrapper around ProcessBuilder that allows piping into the process and collecting
 * all output in a string builder.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 21.10.2008 Time: 15:05:18
 */
public abstract class ProcessBuilderAction extends AbstractAction<Integer> {
    private StringBuilder outputReceiver;
    private ProcessBuilder processBuilder;


    @Override
    public void initialize() {
        super.initialize();    // Overridden method
        if (outputReceiver == null)
            throw new IllegalStateException("ouputReceiver missing");
        if (processBuilder == null)
            throw new IllegalStateException("processBuilder missing");
    }


    @SuppressWarnings({ "ThrowableResultOfMethodCallIgnored" })
    @Override
    public Integer execute() {
        final FillOnce<Throwable> cause = new FillOnce<Throwable>(true);
        final FillOnce<Process> proc = new FillOnce<Process>(false);
        final FillOnce<Thread> mainThread = new FillOnce<Thread>(false);

        try {
            mainThread.set(Thread.currentThread());

            final Thread inputProvider = new Thread(new InputCollector(cause, proc, mainThread));
            final Thread outputCollector = new Thread(new OutputCollector(cause, proc, mainThread));

            inputProvider.start();
            outputCollector.start();

            // Since we only collect stdout...
            processBuilder.redirectErrorStream(true);
            final Process process = processBuilder.start();
            proc.set(process);

            final int result = process.waitFor();
            outputCollector.join();
            inputProvider.join();

            return result;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (InterruptedException e) {
            if (cause.has())
                throw new RuntimeException(cause.get());
            else
                throw new RuntimeException(e);
        }
    }

    protected abstract void writeOutput(final @NotNull BufferedOutputStream stream)
            throws IOException;


    public StringBuilder getOutputReceiver() {
        return outputReceiver;
    }


    public void setOutputReceiver(final StringBuilder outputReceiverParam) {
        outputReceiver = outputReceiverParam;
    }


    public ProcessBuilder getProcessBuilder() {
        return processBuilder;
    }


    public void setProcessBuilder(final ProcessBuilder processBuilderParam) {
        processBuilder = processBuilderParam;
    }


    private final class InputCollector extends Collector {
       InputCollector(
               final FillOnce<Throwable> causeParam,
               final FillOnce<Process> procParam,
               final FillOnce<Thread> mainThreadParam) {
           super(causeParam, procParam, mainThreadParam);
       }


        @Override
       protected void collect(final @NotNull Process process) throws IOException {
           OutputStream outStream = process.getOutputStream();
           BufferedOutputStream bufOutStream = new BufferedOutputStream(outStream);
           try {
               writeOutput(bufOutStream);
           }
           finally {
               bufOutStream.close();
           }

       }
   }

    private final class OutputCollector extends Collector {

        OutputCollector(
                final FillOnce<Throwable> causeParam,
                final FillOnce<Process> procParam,
                final FillOnce<Thread> mainThreadParam) {
            super(causeParam, procParam, mainThreadParam);
        }


        @SuppressWarnings({ "NestedAssignment" })
        @Override
        public void collect(final @NotNull Process process) throws IOException {
            InputStream inStream = process.getInputStream();
            InputStreamReader inReader = new InputStreamReader(inStream);
            BufferedReader bufReader = new BufferedReader(inReader);
            try {
                String line;
                while ((line = bufReader.readLine()) != null) {
                    outputReceiver.append(line);
                    outputReceiver.append('\n');
                }
            }
            finally {
                bufReader.close();
            }

        }
    }


    private abstract static class Collector implements Runnable {
        private final @NotNull FillOnce<Throwable> cause;
        private final @NotNull FillOnce<Process> proc;
        private final @NotNull FillOnce<Thread> mainThread;


        Collector(
                final FillOnce<Throwable> causeParam,
                final FillOnce<Process> procParam,
                final FillOnce<Thread> mainThreadParam) {
            cause = causeParam;
            proc = procParam;
            mainThread = mainThreadParam;
        }


        @SuppressWarnings({ "FeatureEnvy" })
        public void run() {
            final Thread theThread = mainThread.get();
            final Process theProc = proc.get();
            try {
                collect(theProc);
            }
            catch (IOException e) {
                cause.set(e);
                theThread.interrupt();
            }
        }


        protected abstract void collect(final @NotNull Process process) throws IOException;
    }


    public static final class FillOnce<T> {
        private final boolean ignoreConflicts;

        private volatile T value;


        public FillOnce(final boolean ignoreConflictsParam) {
            ignoreConflicts = ignoreConflictsParam;
        }


        public synchronized void set(final @NotNull T newVal) {
            if (value == null) {
                value = newVal;
                notifyAll();
            }
            else
                if (! ignoreConflicts)
                    throw new IllegalStateException("Second attempt to set value");
        }

        public synchronized T get() {
           while (value == null)
               try {
                   wait();
               }
               catch (InterruptedException e) {
                   // ignore
               }
            return value;
        }

        public synchronized boolean has() {
            return value == null;
        }

        public synchronized void reset() {
            value = null;
        }
    }
}

package de.zib.gndms.logic.action;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import de.zib.gndms.stuff.threading.Collector;
import de.zib.gndms.stuff.threading.FillOnce;
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
    private StringBuilder errorReceiver;

    private ProcessBuilder processBuilder;


	protected ProcessBuilderAction() {
		super();
	}


	@Override
    public void initialize() {
        super.initialize();    // Overridden method
        if (outputReceiver == null)
            throw new IllegalStateException("ouputReceiver missing");
	    if (errorReceiver == null)
	        throw new IllegalStateException("errorReceiver missing");
        if (processBuilder == null)
            throw new IllegalStateException("processBuilder missing");
    }


    /**
     * 
     * @return
     */
    @SuppressWarnings({ "ThrowableResultOfMethodCallIgnored" })
    @Override
    public Integer execute() {
        final FillOnce<Throwable> cause = new FillOnce<Throwable>(true);
        final FillOnce<Process> proc = new FillOnce<Process>(false);
        final FillOnce<Thread> mainThread = new FillOnce<Thread>(false);

        try {
            mainThread.set(Thread.currentThread());
            final Thread inputProvider = new Thread(new InputCollector(cause, proc, mainThread));
            final Thread outputCollector =
	              new Thread(new OutputCollector(cause, proc, mainThread, false));
            final Thread errorCollector =
	              new Thread(new OutputCollector(cause, proc, mainThread, true));

            inputProvider.start();
            outputCollector.start();
            errorCollector.start();

            final Process process = processBuilder.start();
            proc.set(process);

	        inputProvider.join();
	        errorCollector.join();
            outputCollector.join();

	        return process.waitFor();
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

    protected abstract void writeProcessStdIn(final @NotNull BufferedOutputStream stream)
            throws IOException;

    /**
     * Stores the process' outgoing streams from a process.
     * Messages will be stored in the outputreceiver as well as in the errorReceiver
     * @param line the outgoing String to be stored from a process
     */
	protected void storeProcessStdOut(String line) {
		getOutputReceiver().append(line);
		getOutputReceiver().append('\n');
		getErrorReceiver().append(line);
		getErrorReceiver().append('\n');
	}

    /**
     * Stores the process' outgoing error stream into the error receiver
     * @param line the outgoing error String to be stored from a process
     */
	protected void storeProcessStdErr(String line) {
		getErrorReceiver().append(line);
		getErrorReceiver().append('\n');
	}

    /**
     * Returns the output receiver
     * 
     * @return the output receiver
     */
    public StringBuilder getOutputReceiver() {
        return outputReceiver;
    }

    /**
     * Sets the output receiver
     *
     * @param outputReceiverParam the output receiver to be set
     */
    public void setOutputReceiver(final StringBuilder outputReceiverParam) {
        outputReceiver = outputReceiverParam;
    }

    /**
     * Returns the error receiver
     *
     * @return the error receiver
     */
	public StringBuilder getErrorReceiver() {
		return errorReceiver;
	}

    /**
     * Sets the error receiver
     *
     * @param errorReceiverParam the error receiver to be set
     */
	public void setErrorReceiver(final StringBuilder errorReceiverParam) {
		errorReceiver = errorReceiverParam;
	}

	public ProcessBuilder getProcessBuilder() {
        return processBuilder;
    }


    public void setProcessBuilder(final ProcessBuilder processBuilderParam) {
        processBuilder = processBuilderParam;
	    processBuilderParam.redirectErrorStream(false);
    }


    /**
     * An InputCollector collects the incoming stream from an process and sends it to an receiver
     */
	private final class InputCollector extends Collector {
       InputCollector(
               final FillOnce<Throwable> causeParam,
               final FillOnce<Process> procParam,
               final FillOnce<Thread> mainThreadParam) {
           super(causeParam, procParam, mainThreadParam);
       }

        /**
         * Collects incoming stream of the process and sends it to the corresponding receiver ((@link ProcessBuilderAction#writeProcessStdIn }).
         * 
         * @param process the process the incoming stream comes from.
         * @throws IOException
         */
        @Override
       protected void collect(final @NotNull Process process) throws IOException {
           OutputStream outStream = process.getOutputStream();
           BufferedOutputStream bufOutStream = new BufferedOutputStream(outStream);
           try {
               writeProcessStdIn(bufOutStream);
           }
           finally {
               bufOutStream.close();
           }

       }
   }

    /**
     * An OutputCollector collects the process' outgoing streams and sends them to their corresponding receivers.
     */
    private final class OutputCollector extends Collector {
	    final boolean collectingErrorStream;


	    OutputCollector(
                final FillOnce<Throwable> causeParam,
                final FillOnce<Process> procParam,
                final FillOnce<Thread> mainThreadParam, boolean collectStdErrParam) {
            super(causeParam, procParam, mainThreadParam);
	        collectingErrorStream = collectStdErrParam;
        }

        /**
         * Collects the outgoing streams from the process and sends it to the corresponding receiver.
         *
         * The receivers are {@link de.zib.gndms.logic.action.ProcessBuilderAction#storeProcessStdErr(String)}
         * if {@code isCollectingErrorStream()} is true
         * and else {@link ProcessBuilderAction#storeProcessStdOut(String)} 
         * @param process the process who is sending an output stream
         * @throws IOException
         */
        @SuppressWarnings({ "NestedAssignment", "HardcodedLineSeparator" })
        @Override
        public void collect(final @NotNull Process process) throws IOException {
            final InputStream inStream =
	              collectingErrorStream ? process.getErrorStream() : process.getInputStream();
            final InputStreamReader inReader = new InputStreamReader(inStream);
            final BufferedReader bufReader = new BufferedReader(inReader);
            try {
                String line;
                while ((line = bufReader.readLine()) != null) {
	                if (collectingErrorStream)
	                    storeProcessStdErr(line);
	                else
	                    storeProcessStdOut(line);
                }
            }
            finally {
                bufReader.close();
            }

        }

        /**
         * Return whether the current stream is an error stream.
         * 
         * @return whether the current stream is an error stream.
         */
	    public boolean isCollectingErrorStream() {
		    return collectingErrorStream;
	    }
    }

}

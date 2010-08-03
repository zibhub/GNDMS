package de.zib.gndms.stuff.threading;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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



import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A Collector is intended to map a system Process to Java
 */
public abstract class Collector implements Runnable {
    private final @NotNull
    FillOnce<Throwable> cause;
    private final @NotNull
    FillOnce<Process> proc;
    private final @NotNull FillOnce<Thread> mainThread;

    /**
     * Creates a new Collector and sets its fields.
     *
     * @param causeParam holds the expection if an error occurs.
     * @param procParam the process being invoked with {@code collect()}
     * @param mainThreadParam this thread will be interrupted, if an error occurs during {@code collect(process)}
     */
    public Collector(
            final FillOnce<Throwable> causeParam,
            final FillOnce<Process> procParam,
            final FillOnce<Thread> mainThreadParam) {
        cause = causeParam;
        proc = procParam;
        mainThread = mainThreadParam;
    }

    /**
     * Invokes {@code collect()] with the process, set in {@link de.zib.gndms.logic.action.ProcessBuilderAction#proc}.
     * If the method throws an exception, the expection will be saved to {@link de.zib.gndms.logic.action.ProcessBuilderAction#cause}
     * and the Thread saved in in {@link de.zib.gndms.logic.action.ProcessBuilderAction#mainThread} will be interrupted.
     *
     * Do not call this method directly, as it should be executed concurrently.
     * (see {@link Runnable#run()})
     */
    @SuppressWarnings({ "FeatureEnvy" })
    public void run() {
        final Thread theThread = mainThread.get();
        final Process theProcess = proc.get();
        try {
            collect(theProcess);
        }
        catch (IOException e) {
            cause.set(e);
            theThread.interrupt();
        }
    }

    /**
     * Implement this method to define how to handle the streams from a process.
     *
     * This method will be invoked, when this.run() is invoked.
     * @param process
     * @throws java.io.IOException
     */
    protected abstract void collect(final @NotNull Process process) throws IOException;
}

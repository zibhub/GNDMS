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



import org.jetbrains.annotations.NotNull;
import org.testng.annotations.Test;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintStream;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 21.10.2008 Time: 16:05:52
 */
public class ProcessBuilderActionTest {

    @SuppressWarnings(
            { "MethodMayBeStatic", "StringBufferWithoutInitialCapacity", "HardcodedFileSeparator" })
    @Test
    public void cat() {
        ProcessBuilder builder = new ProcessBuilder();
        builder.command("/bin/cat","-");
        ProcessBuilderAction action = new ProcessBuilderAction() {

            @Override
            protected void writeProcessStdIn(final @NotNull BufferedOutputStream stream)
                    throws IOException {
                PrintStream prStream = new PrintStream(stream);
                try {
                    prStream.print("Hello World!");
                }
                finally {
                    prStream.close();
                }
            }
        };
        action.setOutputReceiver(new StringBuilder());
        action.setErrorReceiver(new StringBuilder());
        action.setProcessBuilder(builder);
        Integer result = action.call();
        assert action.getOutputReceiver().toString().startsWith("Hello World!");
    }
}

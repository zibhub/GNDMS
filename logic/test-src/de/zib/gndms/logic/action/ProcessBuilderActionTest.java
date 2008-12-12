package de.zib.gndms.logic.action;

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

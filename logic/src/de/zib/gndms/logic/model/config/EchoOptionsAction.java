package de.zib.gndms.logic.model.config;

import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;


/**
 * Just returns all option parameters and prints them by virtue of ConfigAction.setWriteResult(true).
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 15.08.2008 Time: 11:06:40
 */
@ConfigActionHelp(shortHelp="Echoes back all parameters as provided", longHelp="Echoes back all parameters as provided without interpreting them using the newline-separated, quoted option format.")
public final class EchoOptionsAction extends ConfigAction<String> {

    @Override
    public void initialize() {
        setWriteResult(true);
        super.initialize();    // Overridden method
    }


    @Override
    public String execute(final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
        return allOptionsToString(true);
    }
}

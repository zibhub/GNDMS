package de.zib.gndms.infra.action;

import de.zib.gndms.logic.model.config.ConfigActionHelp;
import de.zib.gndms.logic.model.config.ConfigActionResult;
import de.zib.gndms.logic.model.config.ConfigOption;
import de.zib.gndms.logic.model.config.ConfigAction;
import de.zib.gndms.GNDMSVerInfo;
import de.zib.gndms.infra.system.SystemDirectory;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;

import com.google.inject.Inject;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 26.01.2009, Time: 17:06:40
 */
@ConfigActionHelp(shortHelp = "Prints the version of GNDMS", longHelp = "The version can be either print in human readable or as a uuid string.")
public class ReadGNDMSVersionAction extends ConfigAction<ConfigActionResult> implements PublicAccessible {

    public enum VersionFormat { HUMAN, UUID };

    private GNDMSVerInfo verInfo;
    private SystemDirectory sysDri;

    @ConfigOption(descr = "Output Mode (One of SITES (default), ARCHIVES, OIDPREFIXES)")
    private VersionFormat versionFormat = VersionFormat.HUMAN;


    public ConfigActionResult execute( final @NotNull EntityManager em, final @NotNull PrintWriter writer ) {

        writer.print( verInfo.readRelease() + "\n" );
        return ok();
    }
    

    @Override
    protected boolean isExecutingInsideTransaction() {
        return false;
    }


    @Inject
    public void setVerInfo( final GNDMSVerInfo verInfo ) {
        this.verInfo = verInfo;
    }
}

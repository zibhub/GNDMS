package de.zib.gndms.infra.action;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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



import javax.inject.Inject;
import de.zib.gndms.GNDMSVerInfo;
import de.zib.gndms.infra.system.SystemDirectory;
import de.zib.gndms.logic.model.config.ConfigAction;
import de.zib.gndms.logic.model.config.ConfigActionHelp;
import de.zib.gndms.logic.model.config.ConfigActionResult;
import de.zib.gndms.logic.model.config.ConfigOption;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;

/**
 * 
 * Writes the GNDMSVersion to a printwriter
 *
 * @see GNDMSVerInfo
 * @author  try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 26.01.2009, Time: 17:06:40
 */
@ConfigActionHelp(shortHelp = "Prints the version of GNDMS", longHelp = "The version can be either print in human readable or as a uuid string.")
public class ReadGNDMSVersionAction extends ConfigAction<ConfigActionResult> implements PublicAccessible {

    public enum VersionFormat { HUMAN, UUID }

    private GNDMSVerInfo verInfo;
    private SystemDirectory sysDri;

    @ConfigOption(descr = "Version text format either HUMAN or machine (UUID) readable ")
    private VersionFormat versionFormat = VersionFormat.HUMAN;


    public ConfigActionResult execute( final @NotNull EntityManager em, final @NotNull PrintWriter writer ) {

        writer.print( verInfo.readRelease() + "\n" );
        //writer.print( "javarebel test\n" );
        return ok();
    }


    /**
     * Overriden method, returning always false.
     *
     * @see de.zib.gndms.logic.model.config.ConfigAction#isExecutingInsideTransaction() 
     */
    @Override
    protected boolean isExecutingInsideTransaction() {
        return false;
    }


    @Inject
    public void setVerInfo( final GNDMSVerInfo verInfo ) {
        this.verInfo = verInfo;
    }
}

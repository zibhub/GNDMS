package de.zib.gndms.model.test;

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



import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import de.zib.gndms.model.common.types.PermissionConfigData;

import java.io.File;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 07.01.2009, Time: 17:06:13
 */
public class PermissionConfigTest {


    @Parameters( {"configFile"} )
    @Test
    public void loadAConfig( String configFile ) throws Exception{

        PermissionConfigData conf = new PermissionConfigData();

        File f = new File( configFile );
        conf.fromPropertyFile( f );

        System.out.println( conf.toString() );
    }




    public void showStringOrNull( String lab, String dat ) {
        System.out.println( lab + ": " + ( dat != null ? dat : "null" ) );
    }

}

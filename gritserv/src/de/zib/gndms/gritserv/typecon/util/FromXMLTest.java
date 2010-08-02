package de.zib.gndms.gritserv.typecon.util;

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



import de.zib.gndms.model.gorfx.types.io.xml.ORQWrapper;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQStdoutWriter;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQConverter;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.kit.application.AbstractApplication;
import org.kohsuke.args4j.Option;

import java.io.FileInputStream;

/**
 * @author: try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 09.02.2009, Time: 16:18:19
 */
public class FromXMLTest extends AbstractApplication {

    @Option( name="-fn", required=true, usage="xml request file name" )
    private String fn;

    public static void main ( String [] args ) throws Exception {
        FromXMLTest test = new FromXMLTest();
        test.run( args );
    }

    public void run () throws Exception {

        FileInputStream ifs = new FileInputStream( fn );


        ProviderStageInXMLImpl reader = new ProviderStageInXMLImpl( );
        ORQWrapper wrp = reader.fromDocument( ifs );

        ifs.close();
        
        ProviderStageInORQ orq = (ProviderStageInORQ) wrp.getOrq();

        ProviderStageInORQStdoutWriter stdout = new ProviderStageInORQStdoutWriter();
        ProviderStageInORQConverter conv = new ProviderStageInORQConverter( stdout, orq );

        conv.convert();
    }

}

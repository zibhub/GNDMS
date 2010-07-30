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



import de.zib.gndms.kit.application.AbstractApplication;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.gorfx.types.io.ContractPropertyReader;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQPropertyReader;
import org.kohsuke.args4j.Option;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 19.01.2009, Time: 15:58:58
 */
public class AxisToXMLTest extends AbstractApplication {



    @Option( name="-props", required=true, usage="staging.properties" )
    private String sfrPropFile;

    @Option( name="-cprops", required=false, usage="contract.properties" )
    private String conPropFile;

    public static void main( String[] args ) throws Exception {
        AxisToXMLTest ni = new AxisToXMLTest( );
        ni.run( args );
    }


    public void run() throws Exception {

        ProviderStageInORQ orq = ProviderStageInORQPropertyReader.readFromFile( sfrPropFile );
        ProviderStageInXMLImpl xmler = new ProviderStageInXMLImpl( );
        TransientContract con = ContractPropertyReader.readFromFile( conPropFile );
        String s = xmler.toDocument( orq, con );
        System.out.println( s );
    }


}

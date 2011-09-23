package de.zib.gndms.model.gorfx.types.io.tests;

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



import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import de.zib.gndms.model.gorfx.types.io.ProviderStageInOrderQPropertyReader;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInOrderConverter;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInOrderPropertyWriter;
import de.zib.gndms.model.gorfx.types.ProviderStageInOrder;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 03.11.2008, Time: 12:31:56
 */
public class ProviderStageInFromFile {


    @Parameters( { "fileName" })
    @Test( groups={"io"} )
    public void testIt( String fileName ) throws Exception{

        try {
            System.out.println( "\nOrq input test: ");
            System.out.println( "----------------");
            InputStream is = new FileInputStream( fileName );
            Properties prop = new Properties( );
            prop.load( is );
            is.close( );
            ProviderStageInOrderQPropertyReader reader = new ProviderStageInOrderQPropertyReader( prop );
            reader.performReading( );
            ProviderStageInOrder order =  reader.getProduct();
            ProviderStageInORQIOTest.showORQ( order );
            System.out.println( "\nOrq output test: ");
            System.out.println( "-----------------");
            Properties np = new Properties( );
            ProviderStageInOrderPropertyWriter writer = new ProviderStageInOrderPropertyWriter( np );
            ProviderStageInOrderConverter conv = new ProviderStageInOrderConverter( writer, order );
            conv.convert();

            np.store( System.out, "some props" );
        } catch ( FileNotFoundException e ) {
            System.out.println( "File not found make!" );
            System.out.println( "Please make sure your working dir contains " + fileName );
        }

    }
}

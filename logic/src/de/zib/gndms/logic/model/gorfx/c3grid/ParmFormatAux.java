package de.zib.gndms.logic.model.gorfx.c3grid;

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



import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.logic.action.ProcessBuilderAction;
import de.zib.gndms.model.common.types.FilePermissions;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.types.ProviderStageInOrder;
import de.zib.gndms.model.gorfx.types.io.ContractConverter;
import de.zib.gndms.model.gorfx.types.io.ContractPropertyReader;
import de.zib.gndms.model.gorfx.types.io.ContractPropertyWriter;
import de.zib.gndms.model.gorfx.types.io.xml.ORQWrapper;
import de.zib.gndms.model.gorfx.types.io.xml.ProviderStageInXML;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 *
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 17.11.2008, Time: 17:01:46
 */
public class ParmFormatAux {

    private static ProviderStageInXML xmlWriter;

    public static final String FORMAT_XML = "XML";
    public static final String FORMAT_PROPS = "PROPS";

    private String format=FORMAT_PROPS;


    public TransientContract getResult( StringBuilder res ) throws Exception {

        if( format.equals( FORMAT_XML) )
            return xmlToResult( res );
        else
            return propsToResult( res );
    }

    
    public ProcessBuilderAction createPBAction( ProviderStageInOrder order, TransientContract contParam, FilePermissions fp ) {

        if( format.equals( FORMAT_XML ) )
            return  createXMLParmPBAction( order, contParam, fp );
        else
            return  createDefaultPBAction( order, contParam, fp );
    }


    public void formatFromMap( MapConfig config ) {

        if( config.hasOption( "scriptIoFormat" ) )
            try {
                format = config.getOption( "scriptIoFormat" );
            } catch ( MandatoryOptionMissingException e ) { // this may not happen. really!
                e.printStackTrace();
            }
    }

    private TransientContract propsToResult( StringBuilder sb ) throws Exception {

        Properties props = new Properties();
        props.load(new ByteArrayInputStream(sb.toString().getBytes()));

        ContractPropertyReader reader = new ContractPropertyReader(props);
        reader.performReading();
        return reader.getProduct();
    }


    private TransientContract xmlToResult( StringBuilder sb ) throws Exception {

        ORQWrapper wrp = xmlWriter.fromDocument( sb.toString( ) );
        return wrp.getContract();
    }


    private ProcessBuilderAction createDefaultPBAction( ProviderStageInOrder order, TransientContract contParam, FilePermissions fp ) {

        Properties moreProps = null;
        if( contParam != null ) {
            moreProps = new Properties();
            ContractPropertyWriter writer = new ContractPropertyWriter(moreProps);
            ContractConverter conv = new ContractConverter(writer, contParam);
            conv.convert();
        }

        if( fp != null ) {
            if( moreProps == null )
                moreProps = new Properties( );
            fp.toProperties( "c3grid.CommonRequest.Permissions", moreProps );
        }

        if( moreProps != null )
            return ProviderStageInTools.createPBAction( order, moreProps);
        
        return ProviderStageInTools.createPBAction( order, null );
    }


    private ProcessBuilderAction createXMLParmPBAction( ProviderStageInOrder order, TransientContract contParam, FilePermissions fp ) {

        try{
            if( fp != null ) {
                order.getActContext().put( "user", fp.getUser( ) );
                order.getActContext().put( "group", fp.getGroup( ) );
                order.getActContext().put( "mask", fp.getAccessMask( ).toString() );
            }
            return ProviderStageInTools.createPBActionForXML(
                xmlWriter.toDocument( order, contParam ) );
        } catch ( IOException e ) {
            throw new RuntimeException( "Error while converting order to xml document", e );
        }
    }


    public static ProviderStageInXML getXmlWriter() {
        return xmlWriter;
    }


    public static void setXmlWriter( ProviderStageInXML xmlWriter ) {
        ParmFormatAux.xmlWriter = xmlWriter;
    }


    public String getFormat() {
        return format;
    }


    public void setFormat( String format ) {
        this.format = format;
    }
}

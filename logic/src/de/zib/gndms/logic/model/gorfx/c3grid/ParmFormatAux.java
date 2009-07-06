package de.zib.gndms.logic.model.gorfx.c3grid;

import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.logic.action.ProcessBuilderAction;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.common.types.FilePermissions;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.gorfx.types.io.ContractConverter;
import de.zib.gndms.model.gorfx.types.io.ContractPropertyReader;
import de.zib.gndms.model.gorfx.types.io.ContractPropertyWriter;
import de.zib.gndms.model.gorfx.types.io.xml.ORQWrapper;
import de.zib.gndms.model.gorfx.types.io.xml.ProviderStageInXML;
import de.zib.gndms.stuff.propertytree.PropertyTree;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
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

    
    public ProcessBuilderAction createPBAction( ProviderStageInORQ orq, TransientContract contParam, FilePermissions fp ) {

        if( format.equals( FORMAT_XML ) )
            return  createXMLParmPBAction( orq, contParam, fp );
        else
            return  createDefaultPBAction( orq, contParam, fp );
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


    private ProcessBuilderAction createDefaultPBAction( ProviderStageInORQ orq, TransientContract contParam, FilePermissions fp ) {

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
            return ProviderStageInTools.createPBAction( orq, moreProps);
        
        return ProviderStageInTools.createPBAction( orq, null );
    }


    private ProcessBuilderAction createXMLParmPBAction( ProviderStageInORQ orq, TransientContract contParam, FilePermissions fp ) {

        try{
            if( fp != null ) {
                orq.getActContext().put( "user", fp.getUser( ) );
                orq.getActContext().put( "group", fp.getGroup( ) );
                orq.getActContext().put( "mask", fp.getAccessMask( ).toString() );
            }
            return ProviderStageInTools.createPBActionForXML(
                xmlWriter.toDocument( orq, contParam ) );
        } catch ( IOException e ) {
            throw new RuntimeException( "Error while converting orq to xml document", e );
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

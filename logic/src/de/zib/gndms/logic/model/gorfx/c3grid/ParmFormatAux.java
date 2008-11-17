package de.zib.gndms.logic.model.gorfx.c3grid;

import de.zib.gndms.model.gorfx.types.io.xml.ProviderStageInXML;
import de.zib.gndms.model.gorfx.types.io.xml.ORQWrapper;
import de.zib.gndms.model.gorfx.types.io.ContractPropertyReader;
import de.zib.gndms.model.gorfx.types.io.ContractConverter;
import de.zib.gndms.model.gorfx.types.io.ContractPropertyWriter;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.logic.action.ProcessBuilderAction;

import java.io.ByteArrayInputStream;
import java.util.Properties;

/**
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


    public Contract getResult( StringBuilder res ) throws Exception {

        if( format.equals( FORMAT_XML) )
            return XMLToResult( res );
        else
            return PropsToResult( res );
    }

    
    public ProcessBuilderAction createPBAction( ProviderStageInORQ orq, Contract contParam ) {

        if( format.equals( FORMAT_XML ) )
            return  createXMLParmPBAction( orq, contParam );
        else
            return  createDefaultPBAction( orq, contParam );
    }


    public void formatFromMap( MapConfig config ) {

        if( config.hasOption( "scriptIoFormat" ) )
            try {
                format = config.getOption( "scriptIoFormat" );
            } catch ( MandatoryOptionMissingException e ) { // this may not happen. really!
                e.printStackTrace();
            }
    }

    private Contract PropsToResult( StringBuilder sb ) throws Exception {

        Properties props = new Properties();
        props.load(new ByteArrayInputStream(sb.toString().getBytes()));

        ContractPropertyReader reader = new ContractPropertyReader(props);
        reader.performReading();
        return reader.getProduct();
    }


    private Contract XMLToResult( StringBuilder sb ) throws Exception {

        ORQWrapper wrp = xmlWriter.fromDocument( sb.toString( ) );
        return wrp.getContract();
    }




    private ProcessBuilderAction createDefaultPBAction( ProviderStageInORQ orq, Contract contParam ) {

        if( contParam != null ) {
            final Properties moreProps = new Properties();
            ContractPropertyWriter writer = new ContractPropertyWriter(moreProps);
            ContractConverter conv = new ContractConverter(writer, contParam);
            conv.convert();
            return ProviderStageInTools.createPBAction( orq, moreProps);
        }
        
        return ProviderStageInTools.createPBAction( orq, null );
    }


    private ProcessBuilderAction createXMLParmPBAction( ProviderStageInORQ orq, Contract contParam ) {

        return ProviderStageInTools.createPBActionForXML(
            xmlWriter.toDocument( orq, contParam ) );
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

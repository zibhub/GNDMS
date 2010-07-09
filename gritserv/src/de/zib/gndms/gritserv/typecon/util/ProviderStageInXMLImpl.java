package de.zib.gndms.gritserv.typecon.util;

import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
import de.zib.gndms.model.gorfx.types.GORFXConstantURIs;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.gorfx.types.io.xml.ORQWrapper;
import de.zib.gndms.model.gorfx.types.io.xml.ProviderStageInXML;
import de.zib.gndms.gritserv.typecon.GORFXTools;
import de.zib.gndms.gritserv.typecon.types.ContextXSDTypeWriter;
import de.zib.gndms.gritserv.typecon.types.ContractXSDReader;
import de.zib.gndms.gritserv.typecon.types.ContractXSDTypeWriter;
import de.zib.gndms.gritserv.typecon.types.ProviderStageInORQXSDTypeWriter;
import types.ContextT;
import types.OfferExecutionContractT;
import types.ProviderStageInORQT;

import java.io.*;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 * <p/>
 * User: mjorra, Date: 03.12.2008, Time: 15:59:26
 */
public class ProviderStageInXMLImpl implements ProviderStageInXML {

    public String toDocument( AbstractORQ orq, TransientContract con ) throws IOException {

        Writer w = new StringWriter( );
        toDocument( w, orq, con );
        return w.toString();
    }


    public void toDocument( Writer w, AbstractORQ orq, TransientContract con ) throws IOException {

        AxisTypeFromToXML.toXML( w, createStagingInformation( orq, con ) );
    }


    public ORQWrapper fromDocument( String doc ) throws Exception {
        InputStream is = new ByteArrayInputStream( doc.getBytes(  ) );
        return fromDocument( is );
    }


    public ORQWrapper fromDocument( InputStream is ) throws Exception {

        StagingInformationT si = AxisTypeFromToXML.fromXML( is, StagingInformationT.class );
        return createORQWrapper( si );
    }


    public static StagingInformationT createStagingInformation( AbstractORQ orq, TransientContract con ) {

        StagingInformationT si = new StagingInformationT( );

        if( orq != null ) {
            if(! orq.getOfferType().equals( GORFXConstantURIs.PROVIDER_STAGE_IN_URI ) )
                throw new IllegalArgumentException( "Currently only " +  GORFXConstantURIs.PROVIDER_STAGE_IN_URI
                    + "orq's are supported" );

            ProviderStageInORQ porq = (ProviderStageInORQ ) orq;
            ProviderStageInORQT orqt = ProviderStageInORQXSDTypeWriter.write( porq );
            si.setProviderStageInORQ( orqt );

            ContextT ctx = ContextXSDTypeWriter.writeContext( orq.getActContext() );
            si.setContext( ctx );

            if ( orq.isJustEstimate() )
                si.setJustEstimate( Boolean.TRUE );

            if (! porq.getDataDescriptor().hasConstraints() )
                si.setJustDownload( Boolean.TRUE );
        }


        if( con != null ) {
            OfferExecutionContractT cont = ContractXSDTypeWriter.write( con );
            si.setOfferExecutionContract(  cont );
        }

        return si;
    }


    public static StagingInformationT createStagingInformation( ORQWrapper wrp ) {
        return createStagingInformation( wrp.getOrq(), wrp.getContract() );
    }

    /**
     * Reads the TransientContract and the AbstractORQ from {@code si} and returns an {@code QRQWrapper} containing
     * both instances.
     *
     * @see TransientContract
     * @see AbstractORQ
     * @see ORQWrapper
     * 
     * @param si
     * @return an ORQWrapper containing the AbstractORQ given in {@code si}, along with its TransientContract
     * @throws Exception
     */
    public static ORQWrapper createORQWrapper( StagingInformationT si ) throws Exception {

        ORQWrapper wrp = new ORQWrapper(  );

        if( si.getOfferExecutionContract() != null )  {
            TransientContract con = ContractXSDReader.readContract( si.getOfferExecutionContract() );
            wrp.setContract( con );
        }


        if( si.getProviderStageInORQ() != null ) {
            AbstractORQ orq = GORFXTools.convertFromORQT( si.getProviderStageInORQ(), si.getContext() );
            if( si.getJustEstimate() != null )
                orq.setJustEstimate( true );
            wrp.setOrq( orq );
        }

        return wrp;
    }
}

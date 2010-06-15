package de.zib.gndms.GORFX.action.dms;

import de.zib.gndms.GORFX.ORQ.client.ORQClient;
import de.zib.gndms.GORFX.client.GORFXClient;
import de.zib.gndms.c3resource.jaxb.Workspace;
import de.zib.gndms.comserv.delegation.DelegationAux;
import de.zib.gndms.infra.configlet.C3MDSConfiglet;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.infra.system.SystemHolder;
import de.zib.gndms.logic.model.gorfx.AbstractORQCalculator;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.types.SliceStageInORQ;
import de.zib.gndms.typecon.common.type.ContextXSDTypeWriter;
import de.zib.gndms.typecon.common.type.ContractXSDReader;
import de.zib.gndms.typecon.common.type.ContractXSDTypeWriter;
import de.zib.gndms.typecon.common.type.ProviderStageInORQXSDTypeWriter;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.gsi.GlobusCredential;
import org.jetbrains.annotations.NotNull;
import types.ContextT;
import types.OfferExecutionContractT;
import types.ProviderStageInORQT;

import java.io.StringWriter;
import java.util.Set;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 * <p/>
 * User: mjorra, Date: 11.11.2008, Time: 14:57:06
 */
public class SliceStageInORQCalculator extends
    AbstractORQCalculator<SliceStageInORQ, SliceStageInORQCalculator> implements SystemHolder {

	private GNDMSystem system;
    private static Log logger = LogFactory.getLog( SliceStageInORQCalculator.class );
    private GlobusCredential credential;


    @NotNull
	public GNDMSystem getSystem() {
		return system;
	}


	public void setSystem(@NotNull final GNDMSystem systemParam) {
		system = systemParam;
	}

    
    public SliceStageInORQCalculator( ) {
        super( SliceStageInORQ.class );
    }


    @Override
    public TransientContract createOffer() throws Exception {

        String sid = destinationURI( getORQArguments().getGridSite() ); 
        getORQArguments().setActGridSiteURI( sid );

        GORFXClient cnt = new GORFXClient( sid );


        String delfac = DelegationAux.createDelationAddress( sid );

        EndpointReferenceType epr = DelegationAux.createProxy( delfac, credential );
        logger.debug( "createOffer creds: " + credential );

        ProviderStageInORQT p_orq = ProviderStageInORQXSDTypeWriter.write( getORQArguments() );
        ContextT ctx = ContextXSDTypeWriter.writeContext( getORQArguments().getActContext() );

        DelegationAux.addDelegationEPR( ctx, epr );
        cnt.setProxy( credential );

        logger.debug( "calling createORQ" );
        ORQClient orq_cnt = new ORQClient( cnt.createOfferRequest( p_orq, ctx ) );
        logger.debug( "createORQ returned" );
        orq_cnt.setProxy( credential );
        OfferExecutionContractT con = ContractXSDTypeWriter.write( getPreferredOfferExecution() );
        OfferExecutionContractT con2 = orq_cnt.permitEstimateAndDestroyRequest( con, ctx );

        return ContractXSDReader.readContract( con2 );
    }


    public String destinationURI( String gs ) throws URI.MalformedURIException {

        logger.debug( "Looking up archive for siteId: " + gs );
        C3MDSConfiglet cfg = getConfigletProvider().getConfiglet( C3MDSConfiglet.class, "mds" );

        String[] olist = getORQArguments().getDataDescriptor().getObjectList();

        logger.debug( "with oipPrfix: " + loggalbeStringArray( olist ) );
        Set<Workspace.Archive> a = cfg.getCatalog().getArchivesByOids( gs, olist  );

        String uri = ((Workspace.Archive) a.toArray()[0]).getBaseUrl().trim();
        logger.debug( "Found: " + uri );
        return uri;
    }


    public static String loggalbeStringArray ( String [] sl ) {

        if( sl.length == 0 )
            return "null";

        StringWriter sw =  new StringWriter( );
        for ( String s : sl )
            sw.write( s );

        return sw.toString( );
    }


    public GlobusCredential getCredential() {
        return credential;
    }


    public void setCredential( final GlobusCredential credential ) {
        this.credential = credential;
    }
}



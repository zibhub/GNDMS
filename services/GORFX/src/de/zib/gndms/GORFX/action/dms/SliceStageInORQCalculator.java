package de.zib.gndms.GORFX.action.dms;

import de.zib.gndms.GORFX.ORQ.client.ORQClient;
import de.zib.gndms.GORFX.client.GORFXClient;
import de.zib.gndms.c3resource.jaxb.Workspace;
import de.zib.gndms.infra.configlet.C3MDSConfiglet;
import de.zib.gndms.logic.model.gorfx.AbstractORQCalculator;
import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.model.gorfx.types.SliceStageInORQ;
import de.zib.gndms.typecon.common.type.ContextXSDTypeWriter;
import de.zib.gndms.typecon.common.type.ContractXSDReader;
import de.zib.gndms.typecon.common.type.ContractXSDTypeWriter;
import de.zib.gndms.typecon.common.type.ProviderStageInORQXSDTypeWriter;
import org.apache.axis.types.URI;
import types.ContextT;
import types.OfferExecutionContractT;
import types.ProviderStageInORQT;

import java.util.Set;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 11.11.2008, Time: 14:57:06
 */
public class SliceStageInORQCalculator extends
    AbstractORQCalculator<SliceStageInORQ, SliceStageInORQCalculator> {


    public SliceStageInORQCalculator( ) {
        super( SliceStageInORQ.class );
    }


    @Override
    public Contract createOffer() throws Exception {

        String sid = getORQArguments().getGridSite();
        getORQArguments().setGridSiteURI( sid );

        GORFXClient cnt = new GORFXClient( sid );

        ProviderStageInORQT p_orq = ProviderStageInORQXSDTypeWriter.write( getORQArguments() );
        ContextT ctx = ContextXSDTypeWriter.writeContext( getORQArguments().getContext() );
        ORQClient orq_cnt = new ORQClient( cnt.createOfferRequest( p_orq, ctx ) );
        OfferExecutionContractT con = ContractXSDTypeWriter.fromContract( getPerferredOfferExecution() );
        OfferExecutionContractT con2 = orq_cnt.permitEstimateAndDestroyRequest( con, ctx );

        return ContractXSDReader.readContract( con2 );
    }


    public String destinationURI( String gs ) throws URI.MalformedURIException {
        C3MDSConfiglet cfg = getConfigletProvider().getConfiglet( C3MDSConfiglet.class, C3MDSConfiglet.class.getName( ) );
        Set<Workspace.Archive> a = cfg.getCatalog().getArchivesByOid().get( gs );
        
        return ((Workspace.Archive) a.toArray()[0]).getBaseUrl();
    }
}



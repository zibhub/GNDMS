package de.zib.gndms.GORFX.action;

import de.zib.gndms.model.gorfx.types.RePublishSliceORQ;
import de.zib.gndms.model.gorfx.types.GORFXConstantURIs;
import de.zib.gndms.model.gorfx.Contract;
import de.zib.gndms.logic.model.gorfx.AbstractTransferORQCalculator;
import de.zib.gndms.kit.configlet.PublishConfiglet;
import de.zib.gndms.dspace.subspace.client.SubspaceClient;
import de.zib.gndms.dspace.slice.client.SliceClient;
import de.zib.gndms.typecon.common.type.SliceRefXSDReader;
import org.globus.ftp.exception.ServerException;
import org.globus.ftp.exception.ClientException;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;
import java.util.Vector;
import java.util.Random;
import java.util.GregorianCalendar;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 11.11.2008, Time: 14:57:06
 */
public class RePublishSliceORQCalculator extends
    AbstractTransferORQCalculator<RePublishSliceORQ, RePublishSliceORQCalculator  > {

    public RePublishSliceORQCalculator( ) {
        super( RePublishSliceORQ.class );
    }


    @Override
    public Contract createOffer() throws ServerException, IOException, ClientException {

        // obtain possible target subspace
        // todo find a way to access InstanceDir
        PublishConfiglet pcl = new PublishConfiglet(); // just temporary 'til 2do is done
        Iterable<String> ist = pcl.getPublishingSites();
        List<String> sl = new Vector<String>();

        for( String s: ist )
            sl.add( s );

        SubspaceClient sscnt = chooseSubspace( sl );

        Long ts = estimateTransferSize();
        
        // create destination slice
        SliceClient tgt = sscnt.createSlice( GORFXConstantURIs.PUBLISH_SLICE_KIND_URI,
            ( new DateTime( ).plusHours( 2 ) ).toGregorianCalendar(),
            ts );

        String sp = DSpaceBindingUtils.getFtpPathForSlice( getORQArguments().getSourceSlice() );
        String dp = tgt.getSliceLocation();

        Float bw = getNetAux().getBandWidthEstimater().estimateBandWidthFromTo( sp, dp );
        setEstimatedBandWidth( bw );

        // create offer
        Contract res = calculateOffer();

        if( isJustEstimate() ) {
            // destroy slice
            tgt.setTerminationTime( new GregorianCalendar( ) );
        } else {
            tgt.setTerminationTime( res.getCurrentTerminationTime() );
            getORQArguments().setSourceURI( sp );
            getORQArguments().setTargetURI( dp );
            getORQArguments().setDestinationSlice(
                SliceRefXSDReader.fromEPR( tgt.getEndpointReference() ));
        }

        return res;
    }


    public static SubspaceClient chooseSubspace( List<String> ssuris ) {

        Random rand = new Random( new GregorianCalendar( ).getTimeInMillis( ) );
        int idx = rand.nextInt( ssuris.size() );

        try {
            return new SubspaceClient( ssuris.get( idx ) );
        } catch ( Exception e ) {
            throw new RuntimeException( e.getMessage(), e );
        }
    }
}



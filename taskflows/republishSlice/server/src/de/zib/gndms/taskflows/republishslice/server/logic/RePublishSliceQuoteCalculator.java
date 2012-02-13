package de.zib.gndms.taskflows.republishslice.server.logic;

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



import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.dspace.service.DSpaceBindingUtils;
import de.zib.gndms.kit.configlet.PublishConfiglet;
import de.zib.gndms.model.gorfx.types.GORFXConstantURIs;
import de.zib.gndms.taskflows.filetransfer.server.logic.AbstractTransferQuoteCalculator;
import de.zib.gndms.taskflows.republishslice.client.model.RePublishSliceOrder;
import org.globus.ftp.exception.ClientException;
import org.globus.ftp.exception.ServerException;
import org.joda.time.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 11.11.2008, Time: 14:57:06
 */
public class RePublishSliceQuoteCalculator extends
    AbstractTransferQuoteCalculator<RePublishSliceOrder> {

    public RePublishSliceQuoteCalculator( ) {
        super();
    }


    @Override
    public List<Quote> createQuotes() throws ServerException, IOException, ClientException {

        // obtain possible target subspace
        // todo port this against dspaceService clients
        PublishConfiglet pcl = getConfigletProvider().getConfiglet( PublishConfiglet.class,
            PublishConfiglet.class.getName() );
        Iterable<String> ist = pcl.getPublishingSites();
        List<String> sl = new Vector<String>();

        for( String s: ist )
            sl.add( s );

        /*
        SubspaceClient sscnt = chooseSubspace( sl );

        Long ts = estimateTransferSize();
        
        // create destination slice
        SliceClient tgt = sscnt.createSlice( GORFXConstantURIs.PUBLISH_SLICE_KIND_URI,
            ( new DateTime( ).plusHours( 2 ) ).toGregorianCalendar(),
            ts );

        String sp = DSpaceBindingUtils.getFtpPathForSlice( getOrderArguments().getSourceSlice() );
        String dp = tgt.getSliceLocation();

        Float bw = NetworkAuxiliariesProvider.getBandWidthEstimater().estimateBandWidthFromTo( sp, dp );
        setEstimatedBandWidth( bw );

        // create offer
        TransientContract res = calculateOffer();

        if( isJustEstimate() ) {
            // destroy slice
            tgt.setTerminationTime( new GregorianCalendar( ) );
        } else {
            tgt.setTerminationTime( WSConstants.FOREVER );
            getOrderArguments().setSourceURI( sp );
            getOrderArguments().setTargetURI( dp );
            getOrderArguments().setDestinationSlice(
                SliceRefXSDReader.fromEPR( tgt.getEndpointReference() ));
        }
        */

        return new ArrayList<Quote>(0);
    }


    /*
    public static SubspaceClient chooseSubspace( List<String> ssuris ) {

        Random rand = new Random( new GregorianCalendar( ).getTimeInMillis( ) );
        int idx = rand.nextInt( ssuris.size() );

        try {
            return new SubspaceClient( ssuris.get( idx ) );
        } catch ( Exception e ) {
            throw new RuntimeException( e.getMessage(), e );
        }
    }
    */
}



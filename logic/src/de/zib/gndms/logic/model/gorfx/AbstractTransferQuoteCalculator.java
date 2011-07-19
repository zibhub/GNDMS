package de.zib.gndms.logic.model.gorfx;

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



import de.zib.gndms.common.model.gorfx.types.FutureTime;
import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.kit.network.GNDMSFileTransfer;
import de.zib.gndms.kit.network.NetworkAuxiliariesProvider;
import de.zib.gndms.model.gorfx.types.FileTransferOrder;
import org.globus.ftp.GridFTPClient;
import org.globus.ftp.exception.ClientException;
import org.globus.ftp.exception.ServerException;
import org.joda.time.Duration;
import java.util.concurrent.TimeoutException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;


/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 30.09.2008, Time: 10:51:38
 */
public abstract class AbstractTransferQuoteCalculator<M extends FileTransferOrder>
    extends AbstractQuoteCalculator<M> {

    private Long estimatedTransferSize; // estimatedTransferSize
    private Float estimatedBandWidth;
    private String scheme;


    protected AbstractTransferQuoteCalculator( ) {
        super();
    }


    @Override
    public List<Quote> createQuotes() throws ServerException, IOException, ClientException, URISyntaxException {

        estimateTransferSize();
        estimateBandWidth();
        return Arrays.asList( calculateOffer( ) ) ;
    }


    @SuppressWarnings({ "FeatureEnvy" })
    protected Long estimateTransferSize( ) throws ServerException, IOException, ClientException, URISyntaxException {

        GridFTPClient clnt = null;
        try {
            URI suri =  new URI( getOrderBean().getSourceURI() );
            clnt =  NetworkAuxiliariesProvider.getGridFTPClientFactory().createClient( suri, getCredentialProvider() );
            GNDMSFileTransfer ft = NetworkAuxiliariesProvider.newGNDMSFileTransfer();
            ft.setSourceClient( clnt );
            ft.setSourcePath( suri.getPath( ) );
            ft.setFiles( getOrderBean().getFileMap() );
            estimatedTransferSize = ft.estimateTransferSize(  );
        } catch( TimeoutException e ) {
            throw new RuntimeException( e );
        } finally {
            if ( clnt != null )
                clnt.close( true ); // none blocking close op
        }
        return estimatedTransferSize;
    }


    /**
     * PRECONDITION estimateTransferSize must have been called before.
     * @return The estimated transfer size or NULL if it wasn't estimated yet.
     */
    public Long getEstimatedTransferSize( ) throws ServerException, IOException, ClientException {
        return estimatedTransferSize;
    }


    /**
     * Estimates the bandwidth
     */
    protected Float estimateBandWidth( ) throws IOException {
        estimatedBandWidth = NetworkAuxiliariesProvider.getBandWidthEstimater().estimateBandWidthFromTo(
            getOrderBean().getSourceURI(), getOrderBean().getDestinationURI() );

        if( estimatedBandWidth == null )
            throw new IOException( "Couldn't estimate bandwidth." );

        return estimatedBandWidth;
    }


    /**
     * PRECONDITION estimateBandWidth must have been called before.
     * @return The band width NULL if it wasn't estimated yet.
     */
    public Float getEstimatedBandWidth( ) {
        return estimatedBandWidth;
    }


    /**
     * PRECONDITION estimateTransferSize and estimateBandWidth or their associated setters
     *              must have been called before.
     * @return The band width NULL if it wasn't estimated yet.
     */
    protected Quote calculateOffer( ) {

        // may at least take 10 s to cover comunication overhead.
        long ms = NetworkAuxiliariesProvider.calculateTransferTime( estimatedTransferSize, estimatedBandWidth, 10000 );


        Quote ct = new Quote( );
        ct.setDeadline( FutureTime.atOffset( new Duration( ms ) )  );

        return ct;
    }


    /**
     * Use this method to set the download size manually.
     *
     * This is the alternativ to calling estimateTransferSize.
     *
     * @param estimatedTransferSize -- what the name implies.
     */
    protected void setEstimatedTransferSize( long estimatedTransferSize ) {
        this.estimatedTransferSize = estimatedTransferSize;
    }


    /**
     * Use this method to set the available band-width manually.
     *
     * This is the alternativ to calling estimateBandWidth.
     *
     * @param estimatedBandWidth -- Guess what.
     */
    protected void setEstimatedBandWidth( float estimatedBandWidth ) {
        this.estimatedBandWidth = estimatedBandWidth;
    }


    // todo if required return enum for finer error message.
    public boolean uriCheck( String address ) {
        try {
            URI uri = new URI( address );
            if ( scheme != null )
                return uri.getScheme().equals( scheme );
        } catch ( URISyntaxException e ) {
            return false;
        } catch ( NullPointerException e ) {
            return false;
        }
        return true;
    }


    public void setScheme( String scheme ) {
         this.scheme = scheme;
     }


    public String getScheme() {
        return scheme;
    }
}

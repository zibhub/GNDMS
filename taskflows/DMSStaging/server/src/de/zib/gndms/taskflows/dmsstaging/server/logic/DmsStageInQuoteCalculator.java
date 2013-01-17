/*
 * Copyright 2008-2012 Zuse Institute Berlin (ZIB)
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
 *
 */

package de.zib.gndms.taskflows.dmsstaging.server.logic;

import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.gndmc.gorfx.GORFXClient;
import de.zib.gndms.gndmc.gorfx.TaskFlowClient;
import de.zib.gndms.logic.model.gorfx.AbstractQuoteCalculator;
import de.zib.gndms.stuff.misc.LanguageAlgebra;
import de.zib.gndms.taskflows.dmsstaging.client.model.DmsStageInOrder;
import de.zib.gndms.taskflows.staging.client.ProviderStageInMeta;
import de.zib.gndms.voldmodel.Adis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import javax.inject.Inject;
import java.util.*;

/**
 * @date: 19.06.12
 * @time: 10:52
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class DmsStageInQuoteCalculator extends AbstractQuoteCalculator< DmsStageInOrder > {
    protected final Logger logger = LoggerFactory.getLogger( this.getClass() );

    private Adis adis;
    private TaskFlowClient taskFlowClient;
    private GORFXClient gorfxClient;

    @Override
    public List< Quote > createQuotes( ) throws Exception {
        Collection< String > gorfxIDs = getGorfxIDs();
        List< Quote > quotes = new LinkedList< Quote >();
        
        for( String gorfxID: gorfxIDs ) {
            final GORFXClient tmpGorfxClient = new GORFXClient( gorfxID );
            tmpGorfxClient.setRestTemplate( gorfxClient.getRestTemplate() );

            final TaskFlowClient tmpTaskFlowClient = new TaskFlowClient( gorfxID );
            tmpTaskFlowClient.setRestTemplate( taskFlowClient.getRestTemplate() );

            final String taskFlowId;

            // create task Flow for recovering quotes on site gorfxId
            try {
                // todo: perhaps, these taskFlows should be deleted at the end...
                final ResponseEntity< Specifier< Facets > > taskFlow = tmpGorfxClient.createTaskFlow(
                        ProviderStageInMeta.PROVIDER_STAGING_KEY,
                        getOrderBean().createProviderStagInOrder(),
                        getOrder().getDNFromContext(),
                        getOrder().getActId(),
                        LanguageAlgebra.getMultiValueMapFromMap( getOrder().getActContext( ) ) );
                
               taskFlowId = taskFlow.getBody().getUriMap().get( "id" );
            }
            catch( Exception e ) {
                logger.debug( "Could not create taskFlow for recovering quotes on site '" + gorfxID + "'" );
                continue;
            }

            // generate quotes on site
            try {
                ResponseEntity< List< Specifier< Quote > > > responseEntity = tmpTaskFlowClient.getQuotes(
                        ProviderStageInMeta.PROVIDER_STAGING_KEY,
                        taskFlowId,
                        getOrder().getDNFromContext(),
                        getOrder().getActId() );

                for( Specifier< Quote > specifier: responseEntity.getBody() ) {
                    final Quote quote = specifier.getPayload();

                    // THIS IS IT - A NEW QUOTE :)
                    quotes.add( quote );
                    
                    // make sure, site is set correctly
                    quote.setSite( gorfxID );
                }
            }
            catch( Exception e ) {
                logger.debug( "Could not get Quotes from site '" + gorfxID + "'", e );

                tmpTaskFlowClient.deleteTaskflow(
                        ProviderStageInMeta.PROVIDER_STAGING_KEY,
                        taskFlowId,
                        getOrder().getDNFromContext(),
                        getOrder().getActId() );

                continue;
            }

            tmpTaskFlowClient.deleteTaskflow(
                    ProviderStageInMeta.PROVIDER_STAGING_KEY,
                    taskFlowId,
                    getOrder().getDNFromContext(),
                    getOrder().getActId() );

        }
        
        return quotes;
    }


    /**
     * Validate if order is executable.
     *
     * @return true if at least one quote exists.
     */
    @Override
    public boolean validate( ) {
        Collection< String > gorfxIDs = getGorfxIDs();
        
        return ( 0 != gorfxIDs.size() );
    }
    
    
    private Collection< String > getGorfxIDs( ) {
        String commonPrefix = LanguageAlgebra.getGreatestCommonPrefix(
                getOrderBean().getDataDescriptor().getObjectList());

        Collection<String> gorfxIDs = adis.listGORFXbyOID(commonPrefix);

        /*for( int i = commonPrefix.length(); i > 0; --i ) {
            gorfxIDs = adis.listGORFXbyOID(commonPrefix.substring(0, i));
            if( null != gorfxIDs && 0 != gorfxIDs.size() )
                break;
        }*/

        if (gorfxIDs == null)
            return new HashSet<String>();

        return gorfxIDs;
    }


    @SuppressWarnings( "SpringJavaAutowiringInspection" )
    @Inject
    public void setAdis( final Adis adis ) {
        this.adis = adis;
    }


    @SuppressWarnings( "SpringJavaAutowiringInspection" )
    @Inject
    public void setTaskFlowClient( TaskFlowClient taskFlowClient ) {
        this.taskFlowClient = taskFlowClient;
    }


    @SuppressWarnings( "SpringJavaAutowiringInspection" )
    @Inject
    public void setGorfxClient( GORFXClient gorfxClient ) {
        this.gorfxClient = gorfxClient;
    }
}

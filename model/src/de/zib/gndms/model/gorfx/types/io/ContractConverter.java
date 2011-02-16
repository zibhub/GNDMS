package de.zib.gndms.model.gorfx.types.io;

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



import de.zib.gndms.model.common.types.FutureTime;
import de.zib.gndms.model.common.types.TransientContract;
import org.joda.time.DateTime;


/**
 * This converter operates on transient contracts.
 *
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 25.09.2008, Time: 13:16:37
 */
@SuppressWarnings({ "FeatureEnvy" })
public class ContractConverter extends GORFXConverterBase<ContractWriter, TransientContract> {

    
    public ContractConverter() {
        super();
    }


    public ContractConverter( ContractWriter writer, TransientContract model ) {
        super( writer, model );
    }


    @Override
    public void convert() {

        if( getWriter( ) == null || getModel() == null )
            throw new IllegalStateException( );

        getWriter().begin();

        DateTime dat = getModel( ).getAccepted( );
        if( dat != null )
            getWriter().writeIfDecisionBefore( dat );

        FutureTime dead = getModel().getDeadline();
        if( dead != null )
            getWriter().writeExecutionLikelyUntil( dead );

        FutureTime rv = getModel().getResultValidity();
        if( rv != null )
            getWriter().writeResultValidUntil( rv );

	    if (getModel().hasExpectedSize())
            getWriter().writeExpectedSize( getModel().getExpectedSize() );

        if ( getModel( ).hasAdditionalNotes() )
            getWriter( ).writeAdditionalNotes( getModel().getAdditionalNotes() );
        
        getWriter().done();
    }
}

package de.zib.gndms.neomodel.gorfx;
/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import de.zib.gndms.common.model.gorfx.types.FutureTime;
import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.model.common.PermissionInfo;
import de.zib.gndms.model.common.PersistentContract;
import org.joda.time.Duration;

import java.io.Serializable;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 30.01.12  11:32
 *
 * @brief A container which stores all possible attributes required when submitting a task.
 *
 *
 */
public class TaskBuilder {
    
    private final static Quote DEFAULT_QUOTE;
    private final static PermissionInfo DEFAULT_PERMISSION_INFO;

    static {
        DEFAULT_QUOTE = new Quote();
        DEFAULT_QUOTE.setDeadline( FutureTime.atOffset( Duration.standardMinutes( 60 ) ) );
        DEFAULT_QUOTE.setResultValidity( FutureTime.atOffset( Duration.standardDays( 1 ) ) );

        DEFAULT_PERMISSION_INFO = new PermissionInfo( "default", "PermissionConfiglet" );
    }
    
    private Serializable order;
    private PersistentContract contract;
    private PermissionInfo permissionInfo;


    public Serializable getOrder() {

        return order;
    }

    public boolean hasOrder() {

        return order != null;
    }


    public PersistentContract getContract() {

        return contract;
    }


    public boolean hasContract() {

        return contract != null;
    }

    public PermissionInfo getPermissionInfo() {

        return permissionInfo;
    }


    public boolean hasPermissionInfo() {

        return permissionInfo != null;
    }


    public TaskBuilder setOrder( final Serializable order ) {

        this.order = order;
        return this;
    }


    public TaskBuilder setContract( final PersistentContract contract ) {

        this.contract = contract;
        return this;
    }


    public TaskBuilder setPermissionInfo( final PermissionInfo permissionInfo ) {

        this.permissionInfo = permissionInfo;
        return this;
    }


    public void applyToTask( Task task )  {
        
        if( hasContract( ) )
            task.setContract( getContract() );
        else
            try {
                task.setContract( PersistentContract.acceptQuoteNow( DEFAULT_QUOTE.clone() ));
            } catch ( CloneNotSupportedException e ) {
                // this can not happen, seriously
            }

        if( hasOrder() )
            task.setORQ( getOrder() );

        if( hasPermissionInfo() )
            task.setPermissionInfo( getPermissionInfo() );
        else
            task.setPermissionInfo( new PermissionInfo( DEFAULT_PERMISSION_INFO ) );
    }
}


package de.zib.gndms.model.common;

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


import org.apache.openjpa.persistence.Externalizer;
import org.apache.openjpa.persistence.Factory;
import org.apache.openjpa.persistence.Persistent;
import org.joda.time.DateTime;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * GridResource + terminationTime
 **/
@Entity( name="TimedGridResources" )
@Table( name="TimedGridResources", schema="dspace" )
public class TimedGridResource extends GridResource implements TimedGridResourceItf {

    private DateTime terminationTime;

    protected TimedGridResource() {
        super();
    }

    @Persistent
    @Column(name="tod", nullable=false)
    @Temporal( TemporalType.TIMESTAMP)
    @Factory( "de.zib.gndms.model.util.JodaTimeForJPA.toDateTime" )
    @Externalizer( "de.zib.gndms.model.util.JodaTimeForJPA.fromDateTime" )
    public DateTime getTerminationTime() {
        return terminationTime;
    }


    public void setTerminationTime( DateTime terminationTime ) {
        this.terminationTime = terminationTime;
    }
}


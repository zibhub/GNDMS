package de.zib.gndms.taskflows.staging.client.tools;

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



import de.zib.gndms.common.model.gorfx.types.io.GORFXWriterBase;
import org.joda.time.DateTime;

/**
 * Builder interface for a time constraint.
 *
 * NOTE Cause of the few methods there is now converter class for the
 *      time constraint yet. This might change if the class gets more
 *      complex.
 * 
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 17.09.2008, Time: 14:54:27
 */
public interface TimeConstraintWriter extends GORFXWriterBase {

    public abstract void writeMinTime( DateTime dt );
    public abstract void writeMaxTime( DateTime dt );
    public abstract void writeAggregation( String aggregation );
}

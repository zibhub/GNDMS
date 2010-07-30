package de.zib.gndms.model.gorfx.types.io;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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
import org.joda.time.DateTime;

import java.util.Map;


/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 25.09.2008, Time: 13:17:43
 */
public interface ContractWriter extends GORFXWriterBase {
    void writeIfDecisionBefore( DateTime dat );
    void writeExecutionLikelyUntil( FutureTime dat );
    void writeExpectedSize( Long l );
    void writeResultValidUntil( FutureTime dat );
    void writeAdditionalNotes( Map<String, String> additionalNotes );
}

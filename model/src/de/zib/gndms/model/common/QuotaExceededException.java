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

package de.zib.gndms.model.common;

import java.util.Map;

/**
 * @date: 30.08.12
 * @time: 15:19
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class QuotaExceededException extends RuntimeException {

    private static final long serialVersionUID = 3340472372493006645L;
    private Map<String,String> context;


    public QuotaExceededException() {
    }


    public QuotaExceededException( String id ) {
        super( id );
    }


    public QuotaExceededException( String id, Map< String, String > context ) {
        super( id );
        this.context = context;
    }


    public Map<String, String> getContext() {
        return context;
    }


    public void setContext( Map< String, String > context ) {
        this.context = context;
    }
}

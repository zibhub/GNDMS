package de.zib.gndms.GORFX.service;
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

import java.util.Map;

/**
 * @author try ma ik jo rr a zib
 * @date 04.03.11  18:36
 * @brief If there is no such resource, this exception might be thrown.
 */
public class NoSuchResourceException extends RuntimeException {

    private static final long serialVersionUID = 8306742348320570202L;
    private Map<String,String> uriMap;


    public NoSuchResourceException() {
    }


    public NoSuchResourceException( String id ) {
        super( id );
    }


    public NoSuchResourceException( String id, Map<String, String> uriMap ) {
        super( id );
        this.uriMap = uriMap;
    }


    public Map<String, String> getUriMap() {
        return uriMap;
    }


    public void setUriMap( Map<String, String> uriMap ) {
        this.uriMap = uriMap;
    }
}

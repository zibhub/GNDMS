package de.zib.gndms.model.gorfx.types.io;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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



import java.util.Properties;

/**
 * Base class for property readers and writers.
 *
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 17.09.2008, Time: 15:17:00
 */
public abstract class AbstractPropertyIO implements GORFXWriterBase {

    private Properties properties;


    protected AbstractPropertyIO() {
    }


    protected AbstractPropertyIO( Properties properties ) {
        this.properties = properties;
    }


    public Properties getProperties() {
        return properties;
    }


    public void setProperties( Properties properties ) {
        this.properties = properties;
    }


    public void begin() {

         if( getProperties( ) == null )
             throw new IllegalStateException( "No property instance provided" );
     }


    protected String getMandatoryProperty( String key ) throws MandatoryPropertyMissingException {

        return PropertyReadWriteAux.getMandatoryProperty( properties, key );
    }


    protected String getPropertyPruneEmpty( String key ) {

        return PropertyReadWriteAux.pruneEmptyProperty( properties, key );
    }
}

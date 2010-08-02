package de.zib.gndmc.util;

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



import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author: try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 26.07.2010, Time: 13:31:14
 */
public abstract class LoadablePropertyBean {

    public abstract void setProperties( Properties prop );

    public abstract void createExampleProperties( Properties prop );

    public final void setProperties( String fn ) {

        // read property file
        InputStream f = null;
        RuntimeException exc = null;
        try {
            f = new FileInputStream( fn );
            Properties prop = new Properties( );
            prop.load( f );
            setProperties( prop );
        } catch ( FileNotFoundException e ) {
            exc =  new RuntimeException( "Failed to load properties file " + fn, e );
        } catch ( IOException e ) {
            exc =  new RuntimeException( "Failed to read properties from file " + fn, e );
        } finally {
            if( f != null )
                try {
                    f.close( );
                } catch ( IOException e ) {
                    RuntimeException re =
                        new RuntimeException( "Failed to close properties file " + fn, e );
                    if( exc != null )
                        re.initCause( exc );
                    throw re;
                }
            if( exc != null )
                throw exc;
        }
    }
}

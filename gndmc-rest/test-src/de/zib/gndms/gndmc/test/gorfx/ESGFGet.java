package de.zib.gndms.gndmc.test.gorfx;
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

import de.zib.gndms.common.kit.application.AbstractApplication;
import de.zib.gndms.gndmc.utils.DownloadResponseExtractor;
import de.zib.gndms.gndmc.utils.HTTPGetter;
import org.kohsuke.args4j.Option;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 13.03.12  15:51
 * @brief
 */
public class ESGFGet extends AbstractApplication {

    @Option( name="-passwd", required = true )
    protected String passwd;
    @Option( name="-url", required = true )
    protected String url;
    @Option( name="-out", required = true )
    protected String off;
    @Option( name="-cred", required = true )
    protected String keyStoreLocation;
    @Option( name="-trust", required = true )
    protected String trustStoreLocation;

    @Option( name="-dummy", required = false )
    protected String dummy;

    public static void main( String[] args ) throws Exception {

        ESGFGet cnt = new ESGFGet();
        cnt.run(args);
        System.exit(0);
        
    }


    @Override
    protected void run() throws Exception {

        HTTPGetter getter = new HTTPGetter();

        getter.setKeyStoreLocation( keyStoreLocation );
        getter.setTrustStoreLocation( trustStoreLocation );
        getter.setPassword( passwd );
        getter.setupSSL();

        getter.setExtractor( 200, new DownloadResponseExtractor( off ) );
        int statusCode = getter.get( url );
        
        System.out.println( "StatusCode: " + statusCode );
    }
}

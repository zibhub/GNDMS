package de.zib.gndmc;

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



import de.zib.gndms.kit.application.AbstractApplication;
import de.zib.gndms.dspace.client.DSpaceClient;
import de.zib.gndms.GORFX.client.GORFXClient;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.Argument;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author  try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 26.01.2009, Time: 13:26:02
 */
public class MaintenanceClient extends AbstractApplication {


    @Option( name="-uri", required=true, usage="URL of the Service-Endpoint", metaVar="URI" )
    protected String endpoint;
    @Argument
    private List<String> act = new ArrayList<String>( );


    public static void main(String[] args) throws Exception {
        MaintenanceClient cnt = new MaintenanceClient();
        cnt.run( args );
    }


    public void run() throws Exception {

        String srv = endpoint.substring( endpoint.lastIndexOf( '/' ) + 1, endpoint.length() );

        System.out.println( "Calling serivce: " + srv );

        StringWriter sw = new StringWriter( );
        for( String s : act )
            sw.write( s + " " );

        MaintenableClient cnt;
        if( srv.equals( "DSpace" ) )
            cnt = (MaintenableClient) MaintenableClientProxy.newInstance( new DSpaceClient( endpoint ) );
        else if ( srv.equals( "GORFX" ) )
            cnt = (MaintenableClient) MaintenableClientProxy.newInstance( new GORFXClient( endpoint ) );
        else
            throw new IllegalArgumentException( "Unsupported serivce: " + srv );


        System.out.println( "invoking action: " + sw.toString() );
        String s  =  (String) cnt.callMaintenanceAction( sw.toString(), null );

        System.out.println( "Result: " );
        System.out.println( s );
    }
}

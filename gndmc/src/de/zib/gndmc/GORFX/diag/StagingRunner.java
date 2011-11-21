package de.zib.gndmc.GORFX.diag;

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



import de.zib.gndms.GORFX.context.client.TaskClient;
import de.zib.gndms.dspace.slice.client.SliceClient;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQConverter;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQPropertyReader;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQStdoutWriter;
import de.zib.gndms.gritserv.typecon.types.ProviderStageInORQXSDTypeWriter;
import org.globus.wsrf.encoding.ObjectDeserializer;
import types.ProviderStageInResultT;
import types.SliceReference;

import java.util.Properties;

/**
 * @author  try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 05.02.2009, Time: 16:27:56
 */
public class StagingRunner extends RequestRunner {

    private static final ProviderStageInORQPropertyReader reader = new ProviderStageInORQPropertyReader( );
    private static final ProviderStageInORQStdoutWriter stdout = new ProviderStageInORQStdoutWriter( );
    private static final ProviderStageInORQConverter converter = new ProviderStageInORQConverter( );
    private static final ProviderStageInORQXSDTypeWriter xsdwrt = new ProviderStageInORQXSDTypeWriter( );


    public StagingRunner( ) {
        super();
        setConverter( converter );
    }

    public StagingRunner( Properties props, String uri ) {
        super( uri );
        readProps( props, reader );
        setConverter( converter );
    }


    public void prepare( ) {
        prepare( xsdwrt );
    }

    
    public synchronized void show( ) {
        show( stdout );
    }


    protected void showResult( TaskClient tcnt ) throws Exception {
        ProviderStageInResultT res  = tcnt.getExecutionResult( ProviderStageInResultT.class );
        SliceReference sr =  ( SliceReference ) ObjectDeserializer.toObject( res.get_any()[0], SliceReference.class ) ;
        if( sr != null ) {
            SliceClient sliceClient = new SliceClient( sr.getEndpointReference() );
            getLogger().info( "Created slice at: " );
            getLogger().info( sliceClient.getSliceLocation() );
        }
    }


    public void fromProps( Properties props ) {
        readProps( props, reader );
    }
}

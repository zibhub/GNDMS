package de.zib.gndmc.GORFX.tests;

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
 * @author Maik Jorra <jorra@zib.de>
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

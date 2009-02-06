package de.zib.gndms.GORFX.action.tests;

import org.jetbrains.annotations.NotNull;
import org.apache.axis.components.uuid.UUIDGen;
import org.apache.axis.components.uuid.UUIDGenFactory;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.log4j.*;
import org.globus.wsrf.encoding.ObjectDeserializer;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQStdoutWriter;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQConverter;
import de.zib.gndms.typecon.common.type.ProviderStageInORQXSDTypeWriter;
import de.zib.gndms.typecon.common.type.ContextXSDTypeWriter;

import java.util.Properties;
import java.util.HashMap;
import java.io.PrintWriter;

import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQPropertyReader;
import de.zib.gndms.GORFX.offer.client.OfferClient;
import de.zib.gndms.GORFX.ORQ.client.ORQClient;
import de.zib.gndms.GORFX.client.GORFXClient;
import de.zib.gndms.GORFX.context.client.TaskClient;
import de.zib.gndms.dspace.slice.client.SliceClient;
import types.*;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 05.02.2009, Time: 16:27:56
 */
public class StagingRunner implements Runnable {

    private static final UUIDGen uuidGen = UUIDGenFactory.getUUIDGen();
    private static final ProviderStageInORQPropertyReader reader = new ProviderStageInORQPropertyReader( );
    private static final ProviderStageInORQStdoutWriter stdout = new ProviderStageInORQStdoutWriter( );
    private static final ProviderStageInORQConverter converter = new ProviderStageInORQConverter( );
    private static final ProviderStageInORQXSDTypeWriter xsdwrt = new ProviderStageInORQXSDTypeWriter( );
    private static final Logger logger; 
    private static final int MILLIS = 2000;

    private String uuid;
    private ProviderStageInORQ orq;
    private ProviderStageInORQT orqt;
    private HashMap<String,String> ctx = new HashMap<String,String>() ;
    private ContextT ctxt;
    private OfferExecutionContractT con;
    private String gorfxUri;


    static {


        logger = Logger.getLogger( StagingRunner.class );
        ConsoleAppender app = new ConsoleAppender( );
        PatternLayout lay = new PatternLayout( );
        lay.setConversionPattern( "%d{ISO8601} %-5p %c{2} [%t,%M:%L] <%x> %m%n" );
        app.setLayout( lay );
        app.setWriter( new PrintWriter( System.out ) );
        app.setTarget( "System.out");
        logger.addAppender( app );
        logger.setLevel( Level.DEBUG );
    }


    public StagingRunner( Properties props, String uri ) {
        gorfxUri = uri;
        uuid = uuidGen.nextUUID();
        reader.setProperties( props );
        reader.performReading();
        orq = reader.getProduct();
        ctx.put( "Workflow.Id", uuid );
    }

    public void prepare ( ) {
        
        converter.setModel( orq );
        converter.setWriter( xsdwrt );
        converter.convert();
        orqt = xsdwrt.getProduct();
        ctxt = ContextXSDTypeWriter.writeContext( ctx );
        con = GORFXTestClientUtils.newContract();
    }

    public void run() {

        NDC.push( uuid );

        try{
            logger.debug( "Starting" );
    // create gorfx client and retrieve orq
            final GORFXClient gcnt = new GORFXClient( gorfxUri );
            EndpointReferenceType epr = gcnt.createOfferRequest( orqt, ctxt );

            // create orq client and request offer
            final ORQClient orqcnt = new ORQClient( epr );
            epr = orqcnt.getOfferAndDestroyRequest( con , ctxt );

            // create offer client and accept it
            final OfferClient ofcnt = new OfferClient( epr );
            epr = ofcnt.accept( );

            // create task resource an wait for completion
            final TaskClient tcnt = new TaskClient( epr );

            TaskStatusT state = TaskStatusT.unknown;
            boolean finished;
            boolean failed;

            do {
                state = tcnt.getTaskState();
                failed = state.equals( TaskStatusT.failed );
                finished = state.equals( TaskStatusT.finished );

                logger.debug("Waiting for staging to finish...");
                try {
                    Thread.sleep( MILLIS );
                }
                catch (InterruptedException e) {
                    // intentionally
                }
            }
            while (! (failed || finished ) );

            if( finished ) {
                ProviderStageInResultT res  = tcnt.getExecutionResult( ProviderStageInResultT.class );
                SliceReference sr =  ( SliceReference ) ObjectDeserializer.toObject( res.get_any()[0], SliceReference.class ) ;
                if( sr != null ) {
                    SliceClient sliceClient = new SliceClient( sr.getEndpointReference() );
                    logger.info( "Created slice at: " );
                    logger.info( sliceClient.getSliceLocation() );
                }
            } else {
                final TaskExecutionFailure fail = tcnt.getExecutionFailure();
                TaskExecutionFailureImplementationFault tefif = fail.getImplementationFault();
                logger.error( "message:       " + tefif.getMessage( ) );
                logger.error( "faultClass:    " + tefif.getFaultClass( ) );
                logger.error( "faultTrace:    " + tefif.getFaultTrace( ) );
                logger.error( "faultLocation: " + tefif.getFaultLocation( ) );
            }
        } catch (Exception e ) {
            logger.error( e );
        } finally{
            NDC.pop();
        }
    }


    public synchronized void show( ) {

        System.out.println( "ORQ id: " + uuid );
        converter.setModel( orq );
        converter.setWriter( stdout );
        converter.convert();
    }


    public String getGorfxUri() {
        return gorfxUri;
    }


    public void setGorfxUri( final String gorfxUri ) {
        this.gorfxUri = gorfxUri;
    }
}

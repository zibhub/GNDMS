package de.zib.gndms.GORFX.action.tests;

import de.zib.gndms.GORFX.ORQ.client.ORQClient;
import de.zib.gndms.GORFX.client.GORFXClient;
import de.zib.gndms.GORFX.context.client.TaskClient;
import de.zib.gndms.GORFX.offer.client.OfferClient;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
import de.zib.gndms.model.gorfx.types.io.ORQConverter;
import de.zib.gndms.model.gorfx.types.io.ORQPropertyReader;
import de.zib.gndms.model.gorfx.types.io.ORQWriter;
import de.zib.gndms.gritserv.typecon.types.AbstractXSDTypeWriter;
import de.zib.gndms.gritserv.typecon.types.ContextXSDTypeWriter;
import de.zib.gndms.gritserv.typecon.types.ContractXSDTypeWriter;
import org.apache.axis.components.uuid.UUIDGen;
import org.apache.axis.components.uuid.UUIDGenFactory;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.log4j.*;
import types.*;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Properties;

public abstract class RequestRunner implements Runnable{

    private static final UUIDGen uuidGen = UUIDGenFactory.getUUIDGen();
    private static final Logger logger;
    private static final int MILLIS = 2000;
    private ORQConverter converter;
    private String uuid;
    private DynamicOfferDataSeqT orqt;
    private HashMap<String, String> ctx = new HashMap<String,String>();
    private ContextT ctxt;
    private TransientContract con = null;
    private OfferExecutionContractT cont;
    private String gorfxUri;

    private AbstractORQ orq;
    

    static {
        logger = Logger.getLogger( StagingRunner.class );
        ConsoleAppender app = new ConsoleAppender( );
        PatternLayout lay = new PatternLayout( );
        lay.setConversionPattern( "%d{ISO8601} %-5p %c{2} [%t,%M:%L] <%x> %m%n" );
        app.setLayout( lay );
        app.setWriter( new PrintWriter( System.out ) );
        app.setTarget( "System.out");
        RequestRunner.logger.addAppender( app );
        RequestRunner.logger.setLevel( Level.DEBUG );
    }


    public RequestRunner( ) {

        uuid = uuidGen.nextUUID();
        ctx.put( "Workflow.Id", uuid );
    }
    

    public RequestRunner( String uri ) {

        setGorfxUri( uri );
        uuid = uuidGen.nextUUID();
        ctx.put( "Workflow.Id", uuid );
    }


    public abstract void show( );


    protected synchronized void show( ORQWriter w ) {

        System.out.println( "ORQ id: " + uuid );
        converter.setModel( getOrq() );
        converter.setWriter( w );
        converter.convert();
    }


    protected abstract void showResult( TaskClient tcnt ) throws Exception;


    public abstract void prepare( );


    protected <W extends AbstractXSDTypeWriter<? extends DynamicOfferDataSeqT> & ORQWriter>  void prepare( W w ) {

        converter.setModel( orq );
        converter.setWriter( w );
        converter.convert();
        orqt = w.getProduct();
        ctxt = ContextXSDTypeWriter.writeContext( ctx );
        if( con == null )
            cont = GORFXTestClientUtils.newContract();
        else
            cont = ContractXSDTypeWriter.write( con );
    }


    public abstract void fromProps( Properties props );


    protected void readProps( Properties props, ORQPropertyReader<?> r ) {

        r.setProperties( props );
        r.performReading();
        orq = r.getProduct();
    }
    

    public void run() {

        NDC.push( getUuid() );

        try{
            RequestRunner.logger.debug( "Starting" );
            // create gorfx client and retrieve orq
            final GORFXClient gcnt = new GORFXClient( gorfxUri );
            EndpointReferenceType epr = gcnt.createOfferRequest( orqt, ctxt );

            // create orq client and request offer
            final ORQClient orqcnt = new ORQClient( epr );
            epr = orqcnt.getOfferAndDestroyRequest( cont, ctxt );

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

                RequestRunner.logger.debug("Waiting for staging to finish...");
                try {
                    Thread.sleep( RequestRunner.MILLIS );
                }
                catch (InterruptedException e) {
                    // intentionally
                }
            }
            while (! (failed || finished ) );

            if( finished ) {
                showResult( tcnt );
            } else {
                final TaskExecutionFailure fail = tcnt.getExecutionFailure();
                TaskExecutionFailureImplementationFault tefif = fail.getImplementationFault();
                RequestRunner.logger.error( "message:       " + tefif.getMessage( ) );
                RequestRunner.logger.error( "faultClass:    " + tefif.getFaultClass( ) );
                RequestRunner.logger.error( "faultTrace:    " + tefif.getFaultTrace( ) );
                RequestRunner.logger.error( "faultLocation: " + tefif.getFaultLocation( ) );
            }
        } catch (Exception e ) {
            RequestRunner.logger.error( e );
        } finally{
            NDC.pop();
        }
    }


    public String getGorfxUri() {
        return gorfxUri;
    }


    public void setGorfxUri( final String gorfxUri ) {
        this.gorfxUri = gorfxUri;
    }


    public HashMap<String, String> getCtx() {
        return ctx;
    }


    public void setCtx( final HashMap<String, String> ctx ) {
        this.ctx = ctx;
    }


    public TransientContract getCon() {
        return con;
    }


    public void setCon( final TransientContract con ) {
        this.con = con;
    }


    public AbstractORQ getOrq() {
        return orq;
    }


    public void setOrq( final AbstractORQ orq ) {
        this.orq = orq;
    }


    public String getUuid() {
        return uuid;
    }


    public Logger getLogger() {
        return logger;
    }


    public ORQConverter getConverter() {
        return converter;
    }


    public void setConverter( final ORQConverter converter ) {
        this.converter = converter;
    }
}
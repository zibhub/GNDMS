package de.zib.gndms.GORFX.c3grid;

import de.zib.gndms.GORFX.ORQ.client.ORQClient;
import de.zib.gndms.GORFX.client.GORFXClient;
import de.zib.gndms.GORFX.context.client.TaskClient;
import de.zib.gndms.GORFX.context.common.TaskConstants;
import de.zib.gndms.GORFX.offer.client.OfferClient;
import de.zib.gndms.dspace.slice.client.SliceClient;
import de.zib.gndms.kit.application.AbstractApplication;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQConverter;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQPropertyReader;
import de.zib.gndms.model.gorfx.types.io.ContractPropertyReader;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.typecon.common.GORFXTools;
import de.zib.gndms.typecon.common.type.ProviderStageInORQXSDTypeWriter;
import de.zib.gndms.typecon.common.type.ContractXSDTypeWriter;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.encoding.DeserializationException;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.joda.time.DateTime;
import org.kohsuke.args4j.Option;
import org.oasis.wsrf.properties.GetResourcePropertyResponse;
import types.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;
import java.util.Calendar;
import java.util.GregorianCalendar;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 15.10.2008 Time: 13:32:25
 */
@SuppressWarnings({ "UseOfSystemOutOrSystemErr" })
public class ProviderStageInClient extends AbstractApplication {

    private static final long MILLIS = 2000L;

    // commandline args
    @Option( name="-uri", required=true, usage="URL of GORFX-Endpoint", metaVar="URI" )
    private String gorfxEpUrl;
    @Option( name="-props", required=true, usage="staging.properties" )
    private String sfrPropFile;
    @Option( name="-con-props", usage="contract.properties" )
    private String conPropFile;
    @Option( name="-dn", required=true, usage="DN" )
    private String dn;
    private TransientContract contract;

    @SuppressWarnings({ "UseOfSystemOutOrSystemErr" })
    public static void main(String[] args) throws Exception {

        ProviderStageInClient cnt = new ProviderStageInClient();
        cnt.run( args );

    }


    private ProviderStageInClient() {}


    public void run() throws Exception {

        // Create reusable context with pseudo DN
        final ContextT xsdContext = new ContextT();
        final ContextTEntry entry =
            GORFXTools.createContextEntry("Auth.DN", dn);
        xsdContext.setEntry(new ContextTEntry[] { entry });

        // Load SFR from Props and convert to XML objects
        final ProviderStageInORQT xsdArgs = loadSFRFromProps(sfrPropFile);

        if( conPropFile !=  null )
            contract = ContractPropertyReader.readFromFile( conPropFile );

        // Open GORFX and create ORQ
        final EndpointReferenceType orqEPR = createORQ(gorfxEpUrl, xsdContext, xsdArgs);

        // Negotiate with ORQ for Offer
        final EndpointReferenceType offerEpr = createOffer(xsdContext, orqEPR);

        // Accept offer and thus create Task
        final EndpointReferenceType taskClientEpr = acceptOfferAndCreateTask(offerEpr);

        waitForTaskToFinishOrFail(taskClientEpr);
    }
    

    @SuppressWarnings({ "FeatureEnvy" })
    private static void waitForTaskToFinishOrFail(final EndpointReferenceType taskClientEpr)
	      throws RemoteException, DeserializationException, URI.MalformedURIException {
        TaskExecutionState state;
        boolean finished = false;
        boolean failed = false ;
		TaskClient taskClient = null;

        do {
	        try {
		        if (taskClient == null)
		            taskClient = new TaskClient(taskClientEpr);
				final GetResourcePropertyResponse rpResponse= taskClient.getResourceProperty(
						TaskConstants.TASKEXECUTIONSTATE);
				state = (TaskExecutionState) ObjectDeserializer.toObject( rpResponse.get_any()[0], TaskExecutionState.class );
				failed = TaskStatusT.failed.equals(state.getStatus());
				finished = TaskStatusT.finished.equals(state.getStatus());
	        }
	        catch (NoSuchResourceException nre) {
		        failed = true;
	        }
	        catch (Exception re) {
		        re.printStackTrace(System.err);
		        taskClient = null;
	        }
	        System.out.println("Waiting for staging to finish or fail...");
            try {
	            if (! (failed || finished))
                    Thread.sleep(MILLIS);
            }
            catch (InterruptedException e) {
                // intentionally
            }
        }  while (! (failed || finished));


        // Write results to console
        if (finished) {
            final GetResourcePropertyResponse rpResponse= taskClient.getResourceProperty(TaskConstants.TASKEXECUTIONRESULTS);
            final ProviderStageInResultT result = (ProviderStageInResultT) ObjectDeserializer.toObject( rpResponse.get_any()[0], ProviderStageInResultT.class);
            System.out.println(result);
            SliceReference sr =  ( SliceReference ) ObjectDeserializer.toObject( result.get_any()[0], SliceReference.class ) ;
            if( sr != null ) {
                EndpointReferenceType epr = sr.getEndpointReference();
                System.out.println( epr );
                SliceClient sliceClient = new SliceClient(epr);
	            System.out.println("Collect your results at:");
                System.out.println(sliceClient.getSliceLocation());
            }
        }
        else {
            final GetResourcePropertyResponse rpResponse= taskClient.getResourceProperty(TaskConstants.TASKEXECUTIONFAILURE);
            final TaskExecutionFailure fail = (TaskExecutionFailure) ObjectDeserializer.toObject(rpResponse.get_any()[0], TaskExecutionFailure.class);
            TaskExecutionFailureImplementationFault tefif = fail.getImplementationFault();
            System.out.println( "message:       " + tefif.getMessage( ) );
            System.out.println( "faultClass:    " + tefif.getFaultClass( ) );
            System.out.println( "faultTrace:    " + tefif.getFaultTrace( ) );
            System.out.println( "faultLocation: " + tefif.getFaultLocation( ) );
            throw new RuntimeException(fail.toString());
        }
    }


    private static EndpointReferenceType acceptOfferAndCreateTask(final EndpointReferenceType offerEprParam)
            throws URI.MalformedURIException, RemoteException {
        final OfferClient offerClient = new OfferClient(offerEprParam);
        final EndpointReferenceType taskEpr = offerClient.accept();

        // Poll task till completion or failure
        return taskEpr;
    }


    private EndpointReferenceType createOffer(
            final ContextT xsdContextParam, final EndpointReferenceType orqEPRParam)
            throws URI.MalformedURIException, RemoteException {
        final ORQClient orqPort = new ORQClient(orqEPRParam);

        OfferExecutionContractT xsdOfferContract;

        if( contract == null )
            xsdOfferContract = createContract();
        else
            xsdOfferContract = ContractXSDTypeWriter.write( contract );


        return orqPort.getOfferAndDestroyRequest(xsdOfferContract, xsdContextParam);
    }


    private static EndpointReferenceType createORQ(
            final String gorfxEpUrlParam, final ContextT xsdContextParam,
            final ProviderStageInORQT xsdArgsParam)
            throws URI.MalformedURIException, RemoteException {
        GORFXClient gorfx = new GORFXClient(gorfxEpUrlParam);

        // Create ORQ via GORFX
        return gorfx.createOfferRequest(xsdArgsParam, xsdContextParam);
    }


    private static ProviderStageInORQT loadSFRFromProps(final String sfrPropFileParam)
            throws IOException {// Load SFR property file
        File propFile = new File(sfrPropFileParam);
        Properties sfrProps = new Properties();
        final FileInputStream stream = new FileInputStream(propFile);
        try {
            sfrProps.load(stream);
        }
        finally {
            stream.close();
        }

        // Create XSD ProviderStageInORQT from sfrPropFile
        final ProviderStageInORQPropertyReader reader = new ProviderStageInORQPropertyReader( sfrProps );
        reader.begin( );
        reader.read();
        ProviderStageInORQ sfrORQ = reader.getProduct();
        ProviderStageInORQXSDTypeWriter orqtWriter = new ProviderStageInORQXSDTypeWriter();
        ProviderStageInORQConverter orqConverter = new ProviderStageInORQConverter( orqtWriter, sfrORQ );
        orqConverter.convert( );
        return orqtWriter.getProduct();
    }

    private static OfferExecutionContractT createContract ( ) {

        final OfferExecutionContractT xsdOfferContract = new OfferExecutionContractT();

        xsdOfferContract.setIfDecisionBefore( new DateTime().plusHours(1).toGregorianCalendar() );

        final FutureTimeT execLikelyUntil = new FutureTimeT();

        long day = 24*60*60*1000;
        //long day = 5*1000;
        execLikelyUntil.setOffset( day );
        xsdOfferContract.setExecutionLikelyUntil( execLikelyUntil );

        final FutureTimeT resultValidity = new FutureTimeT();
        resultValidity.setOffset( 2*day );
        xsdOfferContract.setResultValidUntil( resultValidity );

        return xsdOfferContract;
    }

}
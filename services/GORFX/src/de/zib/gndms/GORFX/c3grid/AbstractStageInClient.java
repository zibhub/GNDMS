package de.zib.gndms.GORFX.c3grid;

import de.zib.gndms.GORFX.ORQ.client.ORQClient;
import de.zib.gndms.GORFX.context.client.TaskClient;
import de.zib.gndms.GORFX.context.common.TaskConstants;
import de.zib.gndms.GORFX.offer.client.OfferClient;
import de.zib.gndms.GORFX.offer.common.OfferConstants;
import de.zib.gndms.GORFX.client.GORFXClient;
import de.zib.gndms.dspace.slice.client.SliceClient;
import de.zib.gndms.kit.application.AbstractApplication;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.types.io.ContractConverter;
import de.zib.gndms.model.gorfx.types.io.ContractPropertyReader;
import de.zib.gndms.typecon.common.ContractStdoutWriter;
import de.zib.gndms.typecon.common.GORFXTools;
import de.zib.gndms.typecon.common.type.ContractXSDReader;
import de.zib.gndms.typecon.common.type.ContractXSDTypeWriter;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.message.MessageElement;
import org.apache.axis.types.URI;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.encoding.DeserializationException;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.joda.time.DateTime;
import org.kohsuke.args4j.Option;
import org.oasis.wsrf.properties.GetResourcePropertyResponse;
import org.oasis.wsrf.properties.ResourceUnknownFaultType;
import types.*;

import javax.xml.namespace.QName;
import java.rmi.RemoteException;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 17.12.2008 Time: 14:00:59
 */
@SuppressWarnings({ "UseOfSystemOutOrSystemErr", "MethodMayBeStatic" })
public abstract class AbstractStageInClient extends AbstractApplication {

	protected static final QName fileNameQName = new QName("http://gndms.zib.de/c3grid/types", "DataFile");
	protected static final QName metaFileNameQName = new QName("http://gndms.zib.de/c3grid/types", "MetadataFile");

	protected static final long MILLIS = 2000L;

	protected static final long DAY_LONG = 24L*60L*60L*1000L;

	// commandline args

	@Option( name="-uri", required=true, usage="URL of GORFX-Endpoint", metaVar="URI" )
	protected String gorfxEpUrl;
	@Option( name="-props", required=true, usage="staging.properties" )
	protected String sfrPropFile;
	@Option( name="-con-props", usage="contract.properties" )
	protected String conPropFile;
	@Option( name="-dn", required=true, usage="DN" )
	protected String dn;

	protected TransientContract contract;
	protected String dataFile;
	protected String metaDataFile;


	protected EndpointReferenceType createOffer(
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

	protected EndpointReferenceType acceptOfferAndCreateTask(final EndpointReferenceType offerEprParam)
		  throws URI.MalformedURIException, RemoteException, DeserializationException {
        final OfferClient offerClient = new OfferClient(offerEprParam);

		final GetResourcePropertyResponse rpResponse= offerClient.getResourceProperty(
				OfferConstants.OFFEREXECUTIONCONTRACT);
		final OfferExecutionContractT contractT = (OfferExecutionContractT) ObjectDeserializer.toObject( rpResponse.get_any()[0], OfferExecutionContractT.class );
		final TransientContract readContract = ContractXSDReader.readContract(contractT);

		final EndpointReferenceType taskEpr = offerClient.accept();

		System.out.println("# Accepted contract");
		ContractConverter contractConv = new ContractConverter(new ContractStdoutWriter(), readContract);
		contractConv.convert();

        return taskEpr;
    }


	protected static OfferExecutionContractT createContract ( ) {

        final OfferExecutionContractT xsdOfferContract = new OfferExecutionContractT();

        xsdOfferContract.setIfDecisionBefore( new DateTime().plusHours(1).toGregorianCalendar() );

        final FutureTimeT execLikelyUntil = new FutureTimeT();

        //long day = 5*1000;
        execLikelyUntil.setOffset( DAY_LONG );
        xsdOfferContract.setExecutionLikelyUntil( execLikelyUntil );

        final FutureTimeT resultValidity = new FutureTimeT();
        resultValidity.setOffset( 2L*DAY_LONG );
        xsdOfferContract.setResultValidUntil( resultValidity );

        return xsdOfferContract;
    }


	@SuppressWarnings({ "FeatureEnvy", "OverlyLongMethod" })
    protected void waitForTaskToFinishOrFail(final EndpointReferenceType taskClientEpr)
	      throws RemoteException, DeserializationException, URI.MalformedURIException {
        TaskExecutionState state = null;
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
            catch ( ResourceUnknownFaultType rfe ) {
                failed = true;
            }
	        catch (Exception re) {
		        re.printStackTrace(System.err);
		        taskClient = null;
	        }

	        final String outputStr = makeOutputStr(state);
	        System.err.println(outputStr);
            try {
	            if (! (failed || finished))
                    Thread.sleep(MILLIS);
            }
            catch (InterruptedException e) {
                // intentionally
            }
        }  while (! (failed || finished));


        // Write results to console
        if (finished)
	        handleFinish(taskClient);
        else
	        handleFailed(taskClient);
    }


	protected static void handleFailed(final TaskClient taskClientParam)
		  throws RemoteException, DeserializationException {
		final GetResourcePropertyResponse rpResponse= taskClientParam.getResourceProperty(
			  TaskConstants.TASKEXECUTIONFAILURE);
		final TaskExecutionFailure fail = (TaskExecutionFailure) ObjectDeserializer.toObject(rpResponse.get_any()[0], TaskExecutionFailure.class);
		TaskExecutionFailureImplementationFault tefif = fail.getImplementationFault();
		System.out.println( "message:       " + tefif.getMessage( ) );
		System.out.println( "faultClass:    " + tefif.getFaultClass( ) );
		System.out.println( "faultTrace:    " + tefif.getFaultTrace( ) );
		System.out.println( "faultLocation: " + tefif.getFaultLocation( ) );
		throw new RuntimeException(fail.toString());
	}


	@SuppressWarnings({ "HardcodedFileSeparator" })
	protected void handleFinish(final TaskClient taskClientParam)
		  throws RemoteException, DeserializationException, URI.MalformedURIException {
		final GetResourcePropertyResponse rpResponse= taskClientParam.getResourceProperty(
			  TaskConstants.TASKEXECUTIONRESULTS);
		final ProviderStageInResultT result = (ProviderStageInResultT) ObjectDeserializer.toObject( rpResponse.get_any()[0], ProviderStageInResultT.class);
		System.out.println(result);
		SliceReference sr =  ( SliceReference ) ObjectDeserializer.toObject( result.get_any()[0], SliceReference.class ) ;
		if( sr != null ) {
		    EndpointReferenceType epr = sr.getEndpointReference();
		    System.out.println( epr );
		    SliceClient sliceClient = new SliceClient(epr);
			System.out.println("Collect your results at:");
			final String sliceLoc = sliceClient.getSliceLocation();
			System.out.println(sliceLoc);
			System.out.println("");
			System.out.println("Collect your results by executing:");
			String home = System.getProperty("user.home", "/tmp");
			System.out.println("globus-url-copy '" + sliceLoc + '/' + dataFile + "' 'file://" + home + '/' + dataFile + '\'');
			System.out.println("globus-url-copy '" + sliceLoc + '/' + metaDataFile + "' 'file://" + home + '/' +  metaDataFile + '\'');
			System.out.println("");
		}
	}


	@SuppressWarnings({ "MethodWithMoreThanThreeNegations", "HardcodedFileSeparator" })
	protected static String makeOutputStr(final TaskExecutionState stateParam) {
		String stateStr = "(null)";
		int maxProgress = 100;
		int progress = 0;
		if (stateParam != null) {
			final TaskStatusT statusT = stateParam.getStatus();
			if (stateParam.getMaxProgress() != null)
			    maxProgress = Integer.parseInt(stateParam.getMaxProgress().toString());
			if (stateParam.getProgress() != null)
			    progress = Integer.parseInt(stateParam.getProgress().toString());
			if (statusT != null)
				stateStr = statusT.toString();
		}
		return "Waiting for staging to finish or fail... (state=" + stateStr +
			  ", progress=[" + Integer.toString(progress) + '/' + Integer.toString(maxProgress) + "])";
	}


	protected void extractFileNames(final MessageElement[] elems) throws Exception {
		for (MessageElement elem : elems) {
			if (elem != null) {
				final QName qName = elem.getQName();
					if (fileNameQName.equals(qName))
						dataFile = elem.getObjectValue().toString();
					else if (metaFileNameQName.equals(qName))
						metaDataFile = elem.getObjectValue().toString();
			}
		}
	}


	protected void loadAndPrintContract() throws IOException {
		if( conPropFile !=  null ) {
	        contract = ContractPropertyReader.readFromFile( conPropFile );

				// Print initial contract
				System.out.println("# Requested contract");
				ContractConverter contractConv = new ContractConverter(new ContractStdoutWriter(), contract);
				contractConv.convert();
		}
	}


	protected Properties loadSFRProps(final String sfrPropFileParam) throws IOException {// Load SFR property file
		File propFile = new File(sfrPropFileParam);
		Properties sfrProps = new Properties();
		final FileInputStream stream = new FileInputStream(propFile);
		try {
			sfrProps.load(stream);
		}
		finally {
			stream.close();
		}
		return sfrProps;
	}


	@Override
    public void run() throws Exception {

	    // Create reusable context with pseudo DN
	    final ContextT xsdContext = new ContextT();
	    final ContextTEntry entry =
	        GORFXTools.createContextEntry("Auth.DN", dn);
	    xsdContext.setEntry(new ContextTEntry[] { entry });

		final DynamicOfferDataSeqT xsdArgs = loadSFR();

		// Extract file names for later output generation
	    extractFileNames(xsdArgs.get_any());

		// Print SFR
		printSFR();

		loadAndPrintContract();

		// Open GORFX and create ORQ
	    final EndpointReferenceType orqEPR = createORQ(gorfxEpUrl, xsdContext, xsdArgs);

	    // Negotiate with ORQ for Offer
	    final EndpointReferenceType offerEpr = createOffer(xsdContext, orqEPR);

	    // Accept offer and thus create Task
	    final EndpointReferenceType taskClientEpr = acceptOfferAndCreateTask(offerEpr);

	    waitForTaskToFinishOrFail(taskClientEpr);
	}


	protected abstract DynamicOfferDataSeqT loadSFR() throws IOException;

	protected abstract void printSFR() throws IOException;


	protected EndpointReferenceType createORQ(
            final String gorfxEpUrlParam, final ContextT xsdContextParam,
            final DynamicOfferDataSeqT xsdArgsParam)
		  throws Exception, RemoteException {

        GORFXClient gorfx = new GORFXClient(gorfxEpUrlParam);

        // Create ORQ via GORFX
        return gorfx.createOfferRequest(xsdArgsParam, xsdContextParam);
    }
}

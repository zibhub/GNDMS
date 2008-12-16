package de.zib.gndms.GORFX.c3grid;

import de.zib.gndms.GORFX.ORQ.client.ORQClient;
import de.zib.gndms.GORFX.client.GORFXClient;
import de.zib.gndms.GORFX.context.client.TaskClient;
import de.zib.gndms.GORFX.context.common.TaskConstants;
import de.zib.gndms.GORFX.offer.client.OfferClient;
import de.zib.gndms.GORFX.offer.common.OfferConstants;
import de.zib.gndms.dspace.slice.client.SliceClient;
import de.zib.gndms.kit.application.AbstractApplication;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.gorfx.types.io.*;
import de.zib.gndms.typecon.common.ContractStdoutWriter;
import de.zib.gndms.typecon.common.GORFXTools;
import de.zib.gndms.typecon.common.type.ContractXSDReader;
import de.zib.gndms.typecon.common.type.ContractXSDTypeWriter;
import de.zib.gndms.typecon.common.type.ProviderStageInORQXSDTypeWriter;
import org.apache.axis.message.MessageElement;
import org.apache.axis.message.addressing.EndpointReferenceType;
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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Properties;


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
	private static final QName fileNameQName = new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "DataFile");
	private static final QName metaFileNameQName = new javax.xml.namespace.QName("http://gndms.zib.de/c3grid/types", "MetadataFile");

    private static final long MILLIS = 2000L;

	private static final long DAY_LONG = 24L*60L*60L*1000L;


    // commandline args
    @Option( name="-uri", required=true, usage="URL of GORFX-Endpoint", metaVar="URI" )
    private String gorfxEpUrl;
    @Option( name="-props", required=true, usage="staging.properties" )
    private String sfrPropFile;
    @Option( name="-con-props", usage="contract.properties" )
    private String conPropFile;
    @Option( name="-dn", required=true, usage="DN" )
    private String dn;
	@Option( name="-oidprefix", required=false, usage="oidPrefix to be stripped vom Object-Ids")
	private String oidPrefix = "";

    private TransientContract contract;
	private String dataFile;

	private String metaDataFile;

    @SuppressWarnings({ "UseOfSystemOutOrSystemErr" })
    public static void main(String[] args) throws Exception {

        ProviderStageInClient cnt = new ProviderStageInClient();
        cnt.run( args );

    }


    private ProviderStageInClient() {super();}

    @Override
    public void run() throws Exception {

        // Create reusable context with pseudo DN
        final ContextT xsdContext = new ContextT();
        final ContextTEntry entry =
            GORFXTools.createContextEntry("Auth.DN", dn);
        xsdContext.setEntry(new ContextTEntry[] { entry });

        // Load SFR from Props and convert to XML objects
	    final ProviderStageInORQXSDTypeWriter writer = new ProviderStageInORQXSDTypeWriter();
	    loadSFRFromProps(writer, sfrPropFile);
        final ProviderStageInORQT xsdArgs = writer.getProduct();

	    // Print SFR
	    System.out.println("# Staging request");
	    loadSFRFromProps(new ProviderStageInORQStdoutWriter(), sfrPropFile);

	    if( conPropFile !=  null ) {
            contract = ContractPropertyReader.readFromFile( conPropFile );

			// Print initial contract
			System.out.println("# Requested contract");
			ContractConverter contractConv = new ContractConverter(new ContractStdoutWriter(), contract);
			contractConv.convert();
	    }

	    extractFileNames(xsdArgs);

        // Open GORFX and create ORQ
        final EndpointReferenceType orqEPR = createORQ(gorfxEpUrl, xsdContext, xsdArgs);

        // Negotiate with ORQ for Offer
        final EndpointReferenceType offerEpr = createOffer(xsdContext, orqEPR);

        // Accept offer and thus create Task
        final EndpointReferenceType taskClientEpr = acceptOfferAndCreateTask(offerEpr);

        waitForTaskToFinishOrFail(taskClientEpr);
    }


	private void loadSFRFromProps(final  ProviderStageInORQWriter orqtWriter, final String sfrPropFileParam)
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
		fixOids(sfrORQ);
	    ProviderStageInORQConverter orqConverter = new ProviderStageInORQConverter( orqtWriter, sfrORQ );
	    orqConverter.convert( );
   }


	private void fixOids(final ProviderStageInORQ sfrORQParam) {
		String[] objs = sfrORQParam.getDataDescriptor().getObjectList();
		if (oidPrefix.length() > 0) {
				if (objs != null)
					for (int i = 0; i < objs.length; i++) {
						if (objs[i] != null && objs[i].startsWith(oidPrefix)) {
							final String obj = objs[i].trim();
							objs[i] = obj.substring(oidPrefix.length());
						}
					}
		}
	}


	private void extractFileNames(final ProviderStageInORQT xsdArgsParam) throws Exception {
		for (MessageElement elem : xsdArgsParam.get_any()) {
			if (elem != null) {
				final QName qName = elem.getQName();
					if (fileNameQName.equals(qName)) dataFile = elem.getAsString();
					else if (metaFileNameQName.equals(qName)) metaDataFile = elem.getAsString();
			}
		}
	}


    private static EndpointReferenceType createORQ(
            final String gorfxEpUrlParam, final ContextT xsdContextParam,
            final ProviderStageInORQT xsdArgsParam)
            throws URI.MalformedURIException, RemoteException {
        GORFXClient gorfx = new GORFXClient(gorfxEpUrlParam);

        // Create ORQ via GORFX
        return gorfx.createOfferRequest(xsdArgsParam, xsdContextParam);
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

	private static EndpointReferenceType acceptOfferAndCreateTask(final EndpointReferenceType offerEprParam)
		  throws URI.MalformedURIException, RemoteException, DeserializationException {
        final OfferClient offerClient = new OfferClient(offerEprParam);

		final GetResourcePropertyResponse rpResponse= offerClient.getResourceProperty(
				OfferConstants.OFFEREXECUTIONCONTRACT);
		final OfferExecutionContractT contractT = (OfferExecutionContractT) ObjectDeserializer.toObject( rpResponse.get_any()[0], OfferExecutionContractT.class );
		final TransientContract contract = ContractXSDReader.readContract(contractT);

		final EndpointReferenceType taskEpr = offerClient.accept();

		System.out.println("# Accepted contract");
		ContractConverter contractConv = new ContractConverter(new ContractStdoutWriter(), contract);
		contractConv.convert();

        return taskEpr;
    }


	private static OfferExecutionContractT createContract ( ) {

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
    private void waitForTaskToFinishOrFail(final EndpointReferenceType taskClientEpr)
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


	private static void handleFailed(final TaskClient taskClientParam)
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
	private void handleFinish(final TaskClient taskClientParam)
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
	private static String makeOutputStr(final TaskExecutionState stateParam) {
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
			  '[' + Integer.toString(progress) + '/' + Integer.toString(maxProgress) + "])";
	}

}
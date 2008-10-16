package de.zib.gndms.GORFX.c3grid;

import de.zib.gndms.GORFX.ORQ.stubs.*;
import de.zib.gndms.GORFX.ORQ.stubs.service.ORQServiceAddressingLocator;
import de.zib.gndms.GORFX.context.common.TaskConstants;
import de.zib.gndms.GORFX.context.stubs.TaskPortType;
import de.zib.gndms.GORFX.context.stubs.service.TaskServiceAddressingLocator;
import de.zib.gndms.GORFX.offer.stubs.AcceptRequest;
import de.zib.gndms.GORFX.offer.stubs.AcceptResponse;
import de.zib.gndms.GORFX.offer.stubs.OfferPortType;
import de.zib.gndms.GORFX.offer.stubs.service.OfferServiceAddressingLocator;
import de.zib.gndms.GORFX.stubs.*;
import de.zib.gndms.GORFX.stubs.service.GORFXServiceLocator;
import de.zib.gndms.GORFX.common.type.io.ProviderStageInORQXSDTypeWriter;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQPropertyReader;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQConverter;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import org.apache.axis.types.NormalizedString;
import org.apache.axis.types.Token;
import org.globus.wsrf.encoding.DeserializationException;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.joda.time.DateTime;
import org.oasis.wsrf.properties.GetResourcePropertyResponse;
import types.*;

import javax.xml.rpc.ServiceException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Properties;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 15.10.2008 Time: 13:32:25
 */
public class ProviderStageInClient {

    public static void main(String[] args)
            throws IOException, ServiceException, RemoteException,
            DeserializationException, FileNotFoundException {

        // Fetch Args
        final String gorfxEpUrl = args[0];
        final String sfrPropFile = args[1];

        // Load SFR property file
        File propFile = new File(sfrPropFile);
        Properties sfrProps = new Properties();
        sfrProps.load(new FileInputStream(propFile));

        // Open GORFX
        GORFXPortType gorfx = new GORFXServiceLocator().getGORFXPortTypePort(new URL(gorfxEpUrl));

        // Create reusable context with pseudo DN
        final ContextT xsdContext = new ContextT();
        final ContextTEntry entry = new ContextTEntry();
        entry.setKey(new Token("c3grid.StageFileRequest.Auth.DN"));
        entry.set_value(new NormalizedString("Mr. Wichtig"));
        xsdContext.setEntry(new ContextTEntry[] { entry });

        // Create XSD ProviderStageInORQT from sfrPropFile
        final ProviderStageInORQPropertyReader reader = new ProviderStageInORQPropertyReader( sfrProps );
        reader.begin( );
        reader.read();
        ProviderStageInORQ sfrORQ = reader.getProduct();
        ProviderStageInORQXSDTypeWriter orqtWriter = new ProviderStageInORQXSDTypeWriter();
        ProviderStageInORQConverter orqConverter = new ProviderStageInORQConverter( orqtWriter, sfrORQ );
        orqConverter.convert( );
        final ProviderStageInORQT xsdArgs = orqtWriter.getProduct( );


        // Create ORQ via GORFX
        final CreateOfferRequestRequestOfferRequestArguments createORQArgs = new CreateOfferRequestRequestOfferRequestArguments(xsdArgs);
        final CreateOfferRequestRequestContext createORQContext = new CreateOfferRequestRequestContext(xsdContext);
        final CreateOfferRequestRequest createORQRequest = new CreateOfferRequestRequest(createORQContext, createORQArgs);
        final CreateOfferRequestResponse createORQResponse = gorfx.createOfferRequest(createORQRequest);



        // Negotiate with ORQ for Offer
        final ORQServiceAddressingLocator orqLocator = new ORQServiceAddressingLocator();
        final ORQPortType orqPort = orqLocator.getORQPortTypePort(createORQResponse.getEndpointReference());

        final OfferExecutionContractT xsdOfferContract = new OfferExecutionContractT();
        xsdOfferContract.setExecutionLikelyUntil(new DateTime().plusDays(1).toGregorianCalendar());
        xsdOfferContract.setResultValidUntil(new DateTime().plusDays(2).toGregorianCalendar());
        xsdOfferContract.setIfDecisionBefore(new DateTime().plusHours(1).toGregorianCalendar());
        xsdOfferContract.setConstantExecutionTime(true);
        final GetOfferAndDestroyRequestRequestOfferExecutionContract offerContract;
        offerContract = new GetOfferAndDestroyRequestRequestOfferExecutionContract(xsdOfferContract);
        final GetOfferAndDestroyRequestRequestContext offerContext = new GetOfferAndDestroyRequestRequestContext(xsdContext);
        final GetOfferAndDestroyRequestRequest offerParameters = new GetOfferAndDestroyRequestRequest(offerContext, offerContract);
        final GetOfferAndDestroyRequestResponse offerResponse = orqPort.getOfferAndDestroyRequest(offerParameters);

        // Accept offer
        final OfferServiceAddressingLocator locator = new OfferServiceAddressingLocator();
        final OfferPortType offerPort = locator.getOfferPortTypePort(offerResponse.getEndpointReference());
        final AcceptRequest acceptParameters = new AcceptRequest();
        final AcceptResponse acceptResponse = offerPort.accept(acceptParameters);



        // Poll task till completion or failure
        final TaskServiceAddressingLocator taskLocator = new TaskServiceAddressingLocator();
        final TaskPortType taskPort = taskLocator.getTaskPortTypePort(acceptResponse.getEndpointReference());

        TaskExecutionState state;
        boolean finished;
        boolean failed;

        do {
            final GetResourcePropertyResponse rpResponse= taskPort.getResourceProperty(TaskConstants.TASKEXECUTIONSTATE);
            state = (TaskExecutionState) ObjectDeserializer.toObject( rpResponse.get_any()[0], TaskExecutionState.class );
            failed = TaskStatusT.failed.equals(state.getStatus());
            finished = TaskStatusT.finished.equals(state.getStatus());
            try {
                Thread.sleep(2000L);
            }
            catch (InterruptedException e) {
                // intentionally
            }
        } while (! (failed || finished));


        // Write results to console
        if (finished) {
            final GetResourcePropertyResponse rpResponse= taskPort.getResourceProperty(TaskConstants.TASKEXECUTIONRESULTS);
            final ProviderStageInResultT result = (ProviderStageInResultT) ObjectDeserializer.toObject( rpResponse.get_any()[0], ProviderStageInResultT.class);
            System.out.println(result);
        }
        else {
            final GetResourcePropertyResponse rpResponse= taskPort.getResourceProperty(TaskConstants.TASKEXECUTIONFAILURE);
            final TaskExecutionFailure fail = (TaskExecutionFailure) ObjectDeserializer.toObject(rpResponse.get_any()[0], TaskExecutionFailure.class);
            throw new RuntimeException(fail.toString());
        }
    }
}
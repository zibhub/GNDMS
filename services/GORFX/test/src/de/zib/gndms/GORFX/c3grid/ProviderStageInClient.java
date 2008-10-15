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
import org.globus.wsrf.encoding.DeserializationException;
import org.globus.wsrf.encoding.ObjectDeserializer;
import org.joda.time.DateTime;
import org.oasis.wsrf.properties.GetResourcePropertyResponse;
import types.*;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;


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
            throws MalformedURLException, ServiceException, RemoteException,
            DeserializationException {

        final String gorfxEpUrl = args[0];

        GORFXPortType gorfx = new GORFXServiceLocator().getGORFXPortTypePort(new URL(gorfxEpUrl));
        final ContextT xsdContext = new ContextT();
        final CreateOfferRequestRequestContext createORQContext = new CreateOfferRequestRequestContext(xsdContext);
        final ProviderStageInORQT xsdArgs = new ProviderStageInORQT();
        final CreateOfferRequestRequestOfferRequestArguments createORQArgs = new CreateOfferRequestRequestOfferRequestArguments(xsdArgs);
        final CreateOfferRequestRequest createORQRequest = new CreateOfferRequestRequest(createORQContext, createORQArgs);
        final CreateOfferRequestResponse createORQResponse = gorfx.createOfferRequest(createORQRequest);

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

        final OfferServiceAddressingLocator locator = new OfferServiceAddressingLocator();
        final OfferPortType offerPort = locator.getOfferPortTypePort(offerResponse.getEndpointReference());
        final AcceptRequest acceptParameters = new AcceptRequest();
        final AcceptResponse acceptResponse = offerPort.accept(acceptParameters);

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
        } while (! (failed || finished));

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
package de.zib.gndms.gndmc.gorfx;
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

import de.zib.gndms.common.model.gorfx.types.*;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.common.rest.Specifier;


import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;


import java.util.List;
import java.util.UUID;


import java.lang.reflect.*;
import java.lang.Math;

/**
 * @author try ma ik jo rr a zib
 * @date 14.03.11  11:38
 * @brief Performs all requests necessary for taskflow execution.
 *
 * To put the caller in control over the taskflow this class provides
 * handler methods for the result of imported calls. 
 *
 * \note The handler methods are only called if the preceding server response
 * was positive.
 */
public abstract class AbstractTaskFlowExecClient implements TaskStatusHandler {

    private GORFXClient gorfxClient; ///< A ready to uses instance of the gorfx client.
    public TaskFlowClient tfClient; ///< A ready to uses instance of the taskflow client.
    private TaskClient taskClient;   ///< A ready to uses instance of the task client.
    private int pollingDelay = 1000; ///< delay in ms to poll the task status, once the task is running.
    protected final Logger logger = LoggerFactory.getLogger( this.getClass() );

    /**
     * @brief Executes a complete task flow.
     *
     * @param order The order of the taskflow.
     * @param dn    The DN of the user calling the task flow.
     * \note here the workflow id is generated on the fly.
     */
    public void execTF( Order order, String dn ) {

        execTF( order, dn, true, null, UUID.randomUUID().toString() );
    }


    /**
     *
     * @brief Executes a complete task flow.
     *
     * This method is imported when you want to understand the
     * Taskflow protocol.
     *
     * @param order The order of the taskflow.
     * @param dn    The DN of the user calling the task flow.
     *
     * @param withQuote Activates co-scheduling
     * @param desiredQuote A quote holding desired time values for
     * the tasflow execution.
     *
     * \note for now the workflow id is generated on the fly.
     */
    public void execTF( Order order, String dn, boolean withQuote, final Quote desiredQuote ) {
        execTF( order, dn, withQuote, desiredQuote, UUID.randomUUID().toString() );
    }

    /**
     *
     * @brief Executes a complete task flow.
     *
     * This method is imported when you want to understand the
     * Taskflow protocol.
     *
     * @param order The order of the taskflow.
     * @param dn    The DN of the user calling the task flow.
     *
     * @param withQuote Activates co-scheduling
     * @param desiredQuote A quote holding desired time values for
     * the tasflow execution.
     * @param wid the workflow id
     */
	public void execTF(Order order, String dn, boolean withQuote,
			final Quote desiredQuote, final String wid) {
		GNDMSResponseHeader context = setupContext(new GNDMSResponseHeader());

		if (null == gorfxClient) {
			throw new IllegalStateException(
					"You need to set gorfxClient before executing a TaskFlow!");
		}

		/**
		 * \code this is important
		 */
		// ResponseEntity<Specifier<Facets>> res =gorfxClient.createTaskFlow(
		// order.getTaskFlowType(), order, dn, wid, context);

		// sends the order and creates the task flow

		Class clz0 = gorfxClient.getClass();
		Class[] argumentTypes0 = { order.getTaskFlowType().getClass(),
				Order.class, dn.getClass(), wid.getClass(), MultiValueMap.class };
		Object[] arguments0 = { order.getTaskFlowType(), order, dn, wid,
				context };

		ResponseEntity<Specifier<Facets>> res0 = (ResponseEntity<Specifier<Facets>>) call(
				clz0, gorfxClient, "createTaskFlow", argumentTypes0, arguments0, 3);

		if (res0 instanceof ResponseEntity) {
			logger.debug(res0.toString());
		}

		if (res0 == null)
			throw new RuntimeException("createTaskFlow failed");
		if (!HttpStatus.CREATED.equals(res0.getStatusCode())) {
			throw new RuntimeException("createTaskFlow failed "
					+ res0.getStatusCode().name() + " (" + res0.getStatusCode()
					+ ")" + " on URL " + gorfxClient.getServiceURL());
		}

		// the taskflow id is stored under "id" in the urlmap
		String tid = res0.getBody().getUriMap().get("id");

		Integer q = null;
		if (withQuote) {
			if (null == tfClient) {
				throw new IllegalStateException("No TaskFlowClient set.");
			}

			if (desiredQuote != null) {

				Class clz1 = tfClient.getClass();
				Class[] argumentTypes1 = { order.getTaskFlowType().getClass(),
						tid.getClass(), desiredQuote.getClass(), dn.getClass(),
						wid.getClass() };
				Object[] arguments1 = { order.getTaskFlowType(), tid,
						desiredQuote, dn, wid };
				logger.debug("calling set Quote");

				call(clz1, tfClient, "setQuote", argumentTypes1, arguments1, 3);

			}
			// queries the quotes for the task flow

			Class clz2 = tfClient.getClass();
			Class[] argumentTypes2 = { order.getTaskFlowType().getClass(),
					tid.getClass(), dn.getClass(), wid.getClass() };
			Object[] arguments2 = { order.getTaskFlowType(), tid, dn, wid };
			logger.debug("calling get Quotes");

			ResponseEntity<List<Specifier<Quote>>> res2 = (ResponseEntity<List<Specifier<Quote>>>) call(
					clz2, tfClient, "getQuotes", argumentTypes2, arguments2, 3);

			if (res2 instanceof ResponseEntity) {
				logger.debug(res2.toString());
			}

			if (res2 == null)
				throw new RuntimeException("getQuotes failed ");
			if (!HttpStatus.OK.equals(res2.getStatusCode()))
				throw new RuntimeException("getQuotes failed "
						+ res2.getStatusCode().name());

			// lets the implementors of this class choose a quote
			q = selectQuote(res2.getBody());
		}

		//
		// 'til here it is valid to change the order and request new quotes
		//

		// accepts quote q and triggers task creation
		// ResponseEntity<Specifier<Facets>> res3 = tfClient.createTask(
		// order.getTaskFlowType(), tid, q, dn,
		// wid );

		Class clz3 = tfClient.getClass();
		Class[] argumentTypes3 = { order.getTaskFlowType().getClass(),
				tid.getClass(), q.getClass(), dn.getClass(), wid.getClass() };
		Object[] arguments3 = { order.getTaskFlowType(), tid, q, dn, wid };
		
		logger.debug("calling createTask");

		ResponseEntity<Specifier<Facets>> res3 = (ResponseEntity<Specifier<Facets>>) call(
				clz3, tfClient, "createTask", argumentTypes3, arguments3, 3);
		if (res3 == null)
			throw new RuntimeException("createTask failed ");

		if (!HttpStatus.CREATED.equals(res3.getStatusCode()))
			throw new RuntimeException("createTask failed "
					+ res3.getStatusCode().name());

		final Specifier<Facets> taskSpecifier = res3.getBody();

		// let the implementor do smart things with the task specifier
		handleTaskSpecifier(taskSpecifier);

		// the task id is stored under "taskId" in the specifiers urlmap
		// waitForFinishOrFail( taskSpecifier, this, taskClient, pollingDelay,
		// dn, wid );

		Class[] argumentTypes = { taskSpecifier.getClass(),
				TaskStatusHandler.class, TaskClient.class, int.class,
				dn.getClass(), wid.getClass() };
		Object[] arguments = { taskSpecifier, this, taskClient, pollingDelay,
				dn, wid };
		logger.debug("calling status of the request");

		call(this.getClass(), this, "waitForFinishOrFail", argumentTypes,
				arguments, 3);
		/**
		 * \endcode
		 */
		
	}

	public Object call(Class clz, Object objectToCall, String methodToCall,
			Class[] argumentTypes, Object[] arguments, int retries) {
		Object expectedResult = null;
		try {

			expectedResult = callMethod(
					clz.getMethod(methodToCall, argumentTypes), objectToCall,
					retries, arguments);

		} catch (SecurityException e) {
			logger.debug("calling  " + methodToCall + " failed: " + e);
		} catch (NoSuchMethodException e) {
			logger.debug("calling  " + methodToCall + " failed: " + e);
		}

		return expectedResult;
	}
    
	public Object callMethod(Method m, Object o, int retries, Object... params) {
		int numberOfRetries = 1;
		Exception lastException = null;
		Object result = null;

		boolean done = false;
		while (!done && (numberOfRetries++ <= retries)) {

			try {
				result = m.invoke(o, params);
				done = true;
			} catch (ResourceAccessException e) {
				lastException = e;
				logger.debug("Rest-Attempt failed, retrying..." + e);
			} catch (IllegalArgumentException e) {
				logger.debug("calling method failed " + e);
				break;
			} catch (IllegalAccessException e) {
				logger.debug("calling method failed " + e);
				break;
			} catch (InvocationTargetException e) {
				logger.debug("calling method failed " + e);
				break;
			} catch (Exception e) {
				lastException = e;
			}
		}
		if (!done) {
			throw new RuntimeException(
					String.format(
							"Multiple Attempts of Method-calls %s failed",
							m.getName()), lastException);
		}
		return result;
	}
    /**
     * Polls a running task, until its either finished or failed.
     *
     * @param taskSpecifier The specifier of the task.
     * @param statusHandler The handler for the task status, can update some sort of UI.
     * @param taskClient  The task client, which should be used for polling.
     * @param pollingDelay The pollingDelay, its the delay between polling.
     * @param dn  The user DN.
     * @param wid The workflow id.
     *
     * @return The final task status, finished or failed.
     */
    public static TaskStatus waitForFinishOrFail( final Specifier<Facets> taskSpecifier,
                                                  final TaskStatusHandler statusHandler,
                                                  final TaskClient taskClient, 
                                                  final int pollingDelay,
                                                  final String dn,
                                                  final String wid )
    {

        TaskStatus ts;
        String taskId = taskSpecifier.getUriMap().get( "taskId" );

        ResponseEntity<TaskStatus> stat;
        boolean done = false;
        int numberOfPolls =0;
        do {
            // queries the status of the task execution
            stat = taskClient.getStatus( taskId, dn, wid );
            if(! HttpStatus.OK.equals( stat.getStatusCode() ) )
                throw new RuntimeException( "Task::getStatus failed " + stat.getStatusCode().name() );
            ts =  stat.getBody();

            // allows the implementor to do something with the task status
            statusHandler.handleStatus( ts );
            try {
                Thread.sleep( Math.min(pollingDelay+500*numberOfPolls, pollingDelay+150 ));
                numberOfPolls++;
            } catch ( InterruptedException e ) {
                throw new RuntimeException( e );
            }

            // finished without an error, good(?)
            if( finished( ts ) ) {
                // collect the result
                ResponseEntity<TaskResult> tr = null;
                try {
                    tr = taskClient.getResult( taskId, dn, wid );
                } catch( HttpClientErrorException e ) {
                    if( 404 == e.getStatusCode().value() )
                        continue;
                }

                if(! HttpStatus.OK.equals( tr.getStatusCode() ) )
                    throw new RuntimeException( "Failed to obtain task result " + tr.getStatusCode().name() );

                // do something with it
                statusHandler.handleResult( tr.getBody() );

                done = true;
            }
            else if( failed( ts ) ) { // must be failed, not so good
                // find out way
                ResponseEntity<TaskFailure> tf = taskClient.getErrors( taskId, dn, wid );
                if(! HttpStatus.OK.equals( tf.getStatusCode() ) )
                    throw new RuntimeException( "Failed to obtain task errors " + tf.getStatusCode().name() );

                // handle the failure
                statusHandler.handleFailure( tf.getBody() );

                done = true;
            }
        } while( ! done ); // run 'til the task hits a final state

        return ts;
    }


    /**
     * Same as the above method, but without a status handler.
     *
     * @param taskSpecifier The specifier of the task.
     * @param taskClient  The task client, which should be used for polling.
     * @param pollingDelay The pollingDelay, its the delay between polling.
     * @param dn  The user DN.
     * @param wid The workflow id.
     *
     * @return The final task status, finished or failed.
     */
    public static TaskStatus waitForFinishOrFail( final Specifier<Facets> taskSpecifier,
                                                  final TaskClient taskClient,
                                                  final int pollingDelay,
                                                  final String dn,
                                                  final String wid )
    {
       return waitForFinishOrFail( taskSpecifier, new LazyStatusHandler(), taskClient,
               pollingDelay,dn, wid );
    }


    /**
     * Offers implementing clients the possibility to add values to the request context.
     *
     * The request context is used to create taskflows and the right place to provide
     * myProxyTokens.
     *
     * @param context The create request context.
     * @return The augmented context
     */
    protected GNDMSResponseHeader setupContext( final GNDMSResponseHeader context ) {

        // example: context.addMyProxyToken( "c3grid", "foo", "bar" );
        return context;
    }


    /**
     * @brief Allows the caller to select a quote.
     * 
     * @param quotes All available quotes.
     * 
     * @return The index of the accepted quote. \c null will disable
     *         quote usage.
     */
    protected abstract Integer selectQuote( List<Specifier<Quote>> quotes );

    /** 
     * @brief Allows additional handling for the task specifier.
     * 
     * @param ts The task specifier, including all task facets as
     * payload.
     */
    protected abstract void handleTaskSpecifier( Specifier<Facets> ts );

    /** 
     * @brief Handler for the task result.
     *
     * Override this method to gain access to the task(flow) result an
     * send it to the user, post process it or store it for later
     * usage.
     *@param res The result object.
     */
    public abstract void handleResult( TaskResult res );

    /** 
     * @brief Handler for task failures.
     * 
     * Override this method to gain access to the task(flow) error
     * object, e.g. to send an error-report to someone who cares.
     *
     * @param fail The failure object.
     */
    public abstract void handleFailure( TaskFailure fail );


    /** 
     * @brief Checks if ts is FINISHED.
     * 
     * @param ts The current task state.
     * 
     * @return \c true if ts is FINISHED
     */
    private static boolean finished( TaskStatus ts ) {
        return TaskStatus.Status.FINISHED.equals( ts.getStatus() );
    }


    /** 
     * @brief Checks if ts is FINISHED.
     * 
     * @param ts The current task state.
     * 
     * @return \c true if ts is FINISHED
     */
    private static boolean failed( TaskStatus ts ) {
        return TaskStatus.Status.FAILED.equals( ts.getStatus() );
    }


    /**
     * @brief Delivers the value of ::gorfxClient.
     * 
     * @return The value of ::gorfxClient.
     */
    public GORFXClient getGorfxClient() {
        return gorfxClient;
    }


    /**
     * @brief Sets the value of ::gorfxClient to \e gorfxClient.
     * 
     * @param gorfxClient The new value of ::gorfxClient.
     */
    public void setGorfxClient( GORFXClient gorfxClient ) {
        this.gorfxClient = gorfxClient;
    }


    /**
     * @brief Delivers the value of ::tfClient.
     * 
     * @return The value of ::tfClient.
     */
    public TaskFlowClient getTfClient() {
        return tfClient;
    }


    /**
     * @brief Sets the value of ::tfClient to \e tfClient.
     * 
     * @param tfClient The new value of ::tfClient.
     */
    public void setTfClient( TaskFlowClient tfClient ) {
        this.tfClient = tfClient;
    }


    /**
     * @brief Delivers the value of ::taskClient.
     * 
     * @return The value of ::taskClient.
     */
    public TaskClient getTaskClient() {
        return taskClient;
    }


    /**
     * @brief Sets the value of ::taskClient to \e taskClient.
     * 
     * @param taskClient The new value of ::taskClient.
     */
    public void setTaskClient( TaskClient taskClient ) {
        this.taskClient = taskClient;
    }


    /**
     * @brief Delivers the value of ::pollingDelay.
     * 
     * @return The value of ::pollingDelay.
     */
    public long getPollingDelay() {
        return pollingDelay;
    }


    /**
     * @brief Sets the value of ::pollingDelay to \e pollingDelay.
     * 
     * @param pollingDelay The new value of ::pollingDelay.
     */
    public void setPollingDelay( int pollingDelay ) {
        this.pollingDelay = pollingDelay;
    }


    public static class LazyStatusHandler implements TaskStatusHandler {

        @Override
        public void handleStatus( final TaskStatus stat ) {
            // this handler is lazy, it does nothing
        }


        @Override
        public void handleResult( final TaskResult body ) {
            // not required here
        }


        @Override
        public void handleFailure( final TaskFailure body ) {
            // not required here
        }
    }
}

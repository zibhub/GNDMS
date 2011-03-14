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

import de.zib.gndms.model.gorfx.types.*;
import de.zib.gndms.rest.Facets;
import de.zib.gndms.rest.Specifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

/**
 * @author try ma ik jo rr a zib
 * @date 14.03.11  11:38
 * @brief
 */
public abstract class AbstractTaskFlowExecClient {

    private GORFXClient gorfxClient;
    private TaskFlowClient tfClient;
    private TaskClient taskClient;
    private long pollingDelay = 1000;


    public void execTF( AbstractTF order, String dn ) {

        String wid = UUID.randomUUID().toString();

        ResponseEntity<Specifier<Facets>> res = gorfxClient.createTaskFlow( order.getTaskFlowType(), order, dn, wid );

        if(! HttpStatus.CREATED.equals( res.getStatusCode() ) )
            throw new RuntimeException( "createTaskFlow failed " + res.getStatusCode().name() );

        String tid = res.getBody().getUrlMap().get( "id" );
        ResponseEntity<List<Specifier<Quote>>> res2 = tfClient.getQuotes( order.getTaskFlowType(), tid, dn, wid );

        if(! HttpStatus.OK.equals( res2.getStatusCode() ) )
            throw new RuntimeException( "getQuotes failed " + res2.getStatusCode().name() );

        Integer q = selectQuote( res2.getBody() );

        // 'til here it is valid to change the order and request new quotes
        ResponseEntity<Specifier<Facets>> res3 = tfClient.createTask( order.getTaskFlowType(), tid,
            q != null ? q.toString() : null, dn, wid );

        if(! HttpStatus.CREATED.equals( res3.getStatusCode() ) )
            throw new RuntimeException( "createTask failed " + res3.getStatusCode().name() );

        handleTaskSpecifier( res3.getBody() );
        String taskId = res3.getBody().getUrlMap().get( "taskId" );

        ResponseEntity<TaskStatus> stat;
        TaskStatus ts;
        do {
            stat = taskClient.getStatus( taskId, dn, wid );
            if(! HttpStatus.OK.equals( stat.getStatusCode() ) )
                throw new RuntimeException( "Task::getStatus failed " + stat.getStatusCode().name() );
            ts =  stat.getBody();
            handleStatus( ts );
            try {
                Thread.sleep( pollingDelay );
            } catch ( InterruptedException e ) {
                throw new RuntimeException( e );
            }
        } while( !(  finished( ts ) || failed( ts ) ) );

        if( finished( ts ) ) {
            ResponseEntity<TaskResult> tr = taskClient.getResult( taskId, dn, wid );
            if(! HttpStatus.OK.equals( tr.getStatusCode() ) )
                throw new RuntimeException( "Failed to obtain task result " + tr.getStatusCode().name() );

            handleResult(  tr.getBody() );
        } else  { // must be failed
            ResponseEntity<TaskFailure> tf = taskClient.getErrors( taskId, dn, wid );
            if(! HttpStatus.OK.equals( tf.getStatusCode() ) )
                throw new RuntimeException( "Failed to obtain task errors " + tf.getStatusCode().name() );

            handleFailure( tf.getBody() );
        }
    }


    protected abstract void handleFailure( TaskFailure fail );

    protected abstract void handleResult( TaskResult res );


    private boolean finished( TaskStatus ts ) {
        return TaskStatus.Status.FINISHED.equals( ts.getStatus() );
    }


    private boolean failed( TaskStatus ts ) {
        return TaskStatus.Status.FAILED.equals( ts.getStatus() );
    }


    protected abstract void handleStatus( TaskStatus stat );

    protected abstract void handleTaskSpecifier( Specifier<Facets> ts );

    protected abstract Integer selectQuote( List<Specifier<Quote>> quotes );


    public GORFXClient getGorfxClient() {
        return gorfxClient;
    }


    public void setGorfxClient( GORFXClient gorfxClient ) {
        this.gorfxClient = gorfxClient;
    }


    public TaskFlowClient getTfClient() {
        return tfClient;
    }


    public void setTfClient( TaskFlowClient tfClient ) {
        this.tfClient = tfClient;
    }


    public TaskClient getTaskClient() {
        return taskClient;
    }


    public void setTaskClient( TaskClient taskClient ) {
        this.taskClient = taskClient;
    }


    public long getPollingDelay() {
        return pollingDelay;
    }


    public void setPollingDelay( long pollingDelay ) {
        this.pollingDelay = pollingDelay;
    }
}

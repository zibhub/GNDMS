/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.zib.gndms.taskflows.esgfStaging.client;

import de.zib.gndms.common.model.gorfx.types.TaskResult;
import de.zib.gndms.gndmc.gorfx.AbstractTaskFlowExecClient;
import de.zib.gndms.gndmc.gorfx.ExampleTaskFlowExecClient;
import de.zib.gndms.gndmc.gorfx.GORFXTaskFlowExample;
import de.zib.gndms.taskflows.esgfStaging.client.model.ESGFStagingOrder;
import de.zib.gndms.taskflows.esgfStaging.client.model.ESGFStagingTaskFlowResult;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 07.11.11  14:59
 * @brief
 */
public class ESGFStagingTaskFlowExample extends GORFXTaskFlowExample {

    private AbstractTaskFlowExecClient etfc;

    public static void main(String[] args) throws Exception {

		GORFXTaskFlowExample cnt = new ESGFStagingTaskFlowExample();
		cnt.run(args);
		System.exit(0);
	}


    @Override
    protected AbstractTaskFlowExecClient provideTaskFlowClient() {
        etfc = new ExampleTaskFlowExecClient() {
            @Override
            protected void handleResult( TaskResult res ) {
                ESGFStagingTaskFlowResult dr = ESGFStagingTaskFlowResult.class.cast( res );
                System.out.println( "result: " + dr.getResult() );

            }
        };
        return etfc;
    }


    protected void normalRun()  {

        System.out.println( "Performing normal run!!" );
        // create an order instance...
        ESGFStagingOrder dft = new ESGFStagingOrder();
        etfc.execTF( dft, dn );
        System.out.println( "DONE\n" );
    }


    protected void failingRun()  {


        System.out.println( "Performing task which will fail!" );
        ESGFStagingOrder dft = new ESGFStagingOrder();

        etfc.execTF( dft, dn );
        System.out.println( "DONE\n" );
    }
}

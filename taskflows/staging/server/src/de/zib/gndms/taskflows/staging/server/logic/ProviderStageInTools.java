package de.zib.gndms.taskflows.staging.server.logic;

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



import de.zib.gndms.common.model.gorfx.types.io.SfrProperty;
import de.zib.gndms.kit.util.WidAux;
import de.zib.gndms.logic.action.ProcessBuilderAction;
import de.zib.gndms.taskflows.staging.client.model.ProviderStageInOrder;
import de.zib.gndms.taskflows.staging.client.tools.ProviderStageInOrderConverter;
import de.zib.gndms.taskflows.staging.client.tools.ProviderStageInOrderPropertyWriter;
import de.zib.gndms.taskflows.staging.client.tools.ProviderStageInOrderWriter;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 27.10.2008 Time: 11:08:44
 */
public final class ProviderStageInTools {
    private ProviderStageInTools() {}


    public static Properties getSFRProps(
            final @NotNull ProviderStageInOrder order, final Properties moreProps) {
        final Properties props = moreProps == null ? new Properties() : moreProps;
        final ProviderStageInOrderWriter writer = new ProviderStageInOrderPropertyWriter(props);
        final ProviderStageInOrderConverter converter = new ProviderStageInOrderConverter(writer, order );
        converter.convert();
        props.put( "c3grid.CommonRequest.Context.Workflow.Id", WidAux.getWid() );
        props.put( SfrProperty.GORFX_ID.key, WidAux.getGORFXid() );
        return props;
    }


    public static ProcessBuilderAction createPBAction(final @NotNull ProviderStageInOrder orderParam,
                                                      final Properties moreProps) {
        final Properties props = getSFRProps( orderParam, moreProps);
        return new ProcessBuilderAction() {
            protected @Override void writeProcessStdIn(final @NotNull BufferedOutputStream stream)
                    throws IOException {
                props.store(stream, "ProviderStageIn");
	            stream.flush();
            }
        };
    }


}

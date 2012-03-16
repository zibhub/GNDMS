package de.zib.gndms.kit.security;
/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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

import de.zib.gndms.model.common.types.GNDMSSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 16.03.12  16:59
 * @brief
 */
public class SpringSecurityContextInstaller implements GNDMSSecurityContextInstaller {

    @Override
    public void installSecurityContext( final GNDMSSecurityContextHolder contextHolder ) {

        if(  contextHolder == null || !contextHolder.hasContext() )
            return;

        if( SpringSecurityContextHolder.class.isInstance( contextHolder ) ) {
            SpringSecurityContextHolder holder =
                    SpringSecurityContextHolder.class.cast( contextHolder );

            SecurityContextHolder.setContext( holder.getSecurityContext() );
        }
    }
}

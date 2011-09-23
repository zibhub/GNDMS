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

package de.zib.gndms.logic.model.config;

import com.google.common.base.Function;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 19.07.11  14:53
 * @brief
 */
public class AvailableActionsAction extends HelpOverviewAction {

    public final static char SEPARATOR_CHAR = '#';

    @Override
    public String execute( @NotNull EntityManager em, @NotNull PrintWriter writer ) {

        Function<String,String> nameMapper = getNameMapper();

        for (Object obj : getConfigActions()) {
            Class<? extends ConfigAction<?>> action = (Class<? extends ConfigAction<?>>) obj;
            final String name = action.getCanonicalName();
            writer.print( nameMapper == null ? name : nameMapper.apply(name) );
            writer.print( SEPARATOR_CHAR );
        }

        return writer.toString();
    }
}

package de.zib.gndms.infra;

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



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 15.07.2008 Time: 14:22:20
 */
@SuppressWarnings({"OverloadedMethodsWithSameNumberOfParameters"})
public abstract class GridConfig {

    private static final Logger logger = LoggerFactory.getLogger(GridConfig.class);

	@NotNull
	public abstract String getGridName() throws Exception;

	@NotNull
	public abstract String getGridPath() throws Exception;

    @NotNull
    public abstract String getBaseUrl() throws Exception;
    
    @NotNull
    public abstract String getVoldUrl() throws Exception;

    /**
     * Returns whether to use debug mode or not.
     * Result is retrieved from the system environment variable 'GNDMS_DEBUG'.
     *
     * @return whether to use debug mode or not
     */
    @SuppressWarnings({ "MethodMayBeStatic" })
    public boolean isDebugMode() {
        return System.getenv("GNDMS_DEBUG") != null;
    }


    public String asString() {
        try {
            return '|' + getGridName() + '|' + getGridPath();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

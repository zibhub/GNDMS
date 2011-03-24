package de.zib.gndms.logic.model.config;

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



/**
 * An ConfigActionResult contains a String holding information about the result of an action.
 *
 * @see ConfigAction
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 23.10.2008 Time: 16:27:45
 */
public abstract class ConfigActionResult {
    private final String details;


    protected ConfigActionResult(final String detailsParam) {
        details = detailsParam;
    }


    protected ConfigActionResult() {
        this(null);
    }

    /**
     * Return the details about the result.
     *
     * @return the details about the result.
     */
    public String getDetails() {
        return details;
    }

    @Override
    public String toString() {
        final String details = getDetails();
        if (details == null)
            return getResultTypeNick()+"()";
        else
            return getResultTypeNick()+"(" + details +")";
    }


    protected abstract String getResultTypeNick();
}



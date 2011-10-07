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

package de.zib.firstrest.aspects;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 28.07.11  16:54
 * @brief
 */
public class NoSuchTaskFlowTypeException extends RuntimeException {

    private static final long serialVersionUID = -741878763343427342L;


    public NoSuchTaskFlowTypeException( String message ) {
        super( "no fid: " + message );
    }
}

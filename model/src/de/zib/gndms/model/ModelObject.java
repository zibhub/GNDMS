package de.zib.gndms.model;

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
 * Shared super class of all model objects.
 * 
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 * User: stepn Date: 30.07.2008 Time: 17:03:24
 *
 * NOTE: do not declare this class abstract!! ( gives compiler trouble )
 */

public class ModelObject {

    protected ModelObject( ) {

    }
    

    public static int hashCode0(Object obj) {
		return obj == null ? 0 : obj.hashCode();
	}

}


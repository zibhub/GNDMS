package de.zib.gndms.logic.model;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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
 * An Exception which will be thrown if an entity manager was expected, but not provided.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 12.08.2008, Time: 16:06:17
 */
public class NoEntityManagerException extends RuntimeException {
	private static final long serialVersionUID = -5819345104409948501L;


	public NoEntityManagerException( ) {
        super( "No entity manager provided");
    }
}

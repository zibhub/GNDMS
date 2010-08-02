package de.zib.gndms.model.common.types;

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



import de.zib.gndms.model.common.PersistentContract;


/**
 * @author: try ma ik jo rr a zib
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 05.12.2008, Time: 14:43:34
 */
public class InvalidContractException extends Exception {

    public InvalidContractException( ) {
        super( );
    }

    
    public InvalidContractException( String msg ) {
        super( msg );
    }


    public InvalidContractException( PersistentContract con ) {
        super( con.toString( ) );
    }


    public InvalidContractException(  PersistentContract con, String msg ) {
        super( con.toString( ) + " " + msg );
    }
}

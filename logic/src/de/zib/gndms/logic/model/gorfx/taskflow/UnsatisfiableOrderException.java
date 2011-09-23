package de.zib.gndms.logic.model.gorfx.taskflow;
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
 * @author try ma ik jo rr a zib
 * @date 14.02.11  16:48
 * @brief An exception which signals an order which can't be satisfied.
 *
 * @note This is only used on the server side.
 */
public class UnsatisfiableOrderException extends RuntimeException {

    private static final long serialVersionUID = -3285859503083289382L;


    public UnsatisfiableOrderException() {
    }


    public UnsatisfiableOrderException( String message ) {
        super( message );
    }


    public UnsatisfiableOrderException( String message, Throwable cause ) {
        super( message, cause );
    }


    public UnsatisfiableOrderException( Throwable cause ) {
        super( cause );
    }
}

package de.zib.gndms.common.kit.application;

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

import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;

/**
 * Abstract base class for an application with args4j support.
 *
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 06.11.2008, Time: 12:39:03
 */
public abstract class AbstractApplication {

    /**
     * When this method is called by an implementing subclass,
     * the fields of the class will be set as denoted by {@code args} and
     * {@link #run()} will be called.
     *
     * @see org.kohsuke.args4j.CmdLineParser#parseArgument(String[])
     * @param args a list of fields and their corresponding values, which will be set before {@code run()} is called
     *
     * @throws Exception if an error occurs, while parsing the input String list.
     */
    public final void run( final String[] args ) throws Exception {

        CmdLineParser pars = new CmdLineParser( this );
        try {
            pars.parseArgument( args );
            this.run();
        } catch ( CmdLineException e ) {
            System.err.println( e.getMessage( ) );
            pars.printUsage( System.err );
        }
    }

    /**
     * This method will be called after the fields of the class have been set.
     * @throws Exception 
     */
    protected abstract void run() throws Exception;
}

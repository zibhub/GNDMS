/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * \mainpage ADiS - Advanced Discovery Service
 *
 * \section Compiling
 *
 * To compile the project, run \verbatim buildr package \endverbatim
 *
 * All necessary dependencies will be downloaded from the internat automatically. The only dependency,
 * which cannot be downloaded is vold. The project structure assumes the vold project directory to be
 * in the same (root) directory as the adis project directory, i.e. there should be the two directories
 * projects/vold and projects/ADiS. If you like to change that, adjust the path to the vold directory
 * in the buildfile.
 *
 * \section CLI Command Line Interface
 *
 * To run the CLI tool, set the correct URL in the buildfile and run
 * \verbatim buildr run \endverbatim
 *
 * \section ClientLib Client Library
 *
 * After compiling, the jar can be found in the target directory. To see all dependencies, run the command
 * \verbatim buildr adis:deps \endverbatim
 *
 */

package de.zib.gndms.voldmodel;

import com.beust.jcommander.Parameter;

import de.zib.gndms.voldmodel.abi.ABI;
import de.zib.gndms.voldmodel.abi.ABIi;

public class Main extends ABI {
    @Parameter( names = { "--baseurl", "-b" }, description = "Base URL of VolD database", required = true )
    private String voldURL;

    @Parameter( names = { "--grid", "-g" }, description = "Grid name" )
    private String grid = "c3grid";

    @Parameter( names = { "--enc", "-e" }, description = "Encoding" )
    private String enc = "utf-8";

    private Main( ABIi iface, String[] args ) {
        super( iface, args );
    }

    public static void main( String[] args ) {
        Adis adis = new Adis();
        Main main = new Main( adis, args );

        adis.setVoldURL( main.voldURL );
        adis.setEnc( main.enc );
        adis.setGrid( main.grid );

        main.invoke();
    }
}

/*
 * Copyright 2008-2013 Zuse Institute Berlin (ZIB)
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
 * All necessary dependencies will be downloaded from the internet
 * automatically except for the VolD package.
 * The project structure assumes the VolD project directory
 * to be in the same (root) directory as the ADiS project directory,
 * i.e. there should be the two directories projects/VolD and
 * projects/ADiS. If you like to change that, adjust the path to the
 * VolD directory in the buildfile.
 *
 * \section CLI Command Line Interface
 *
 * To run the CLI tool, set the correct URL in the buildfile and run
 * \verbatim buildr run \endverbatim
 *
 * \section ClientLib Client Library
 *
 * After compiling, the jar can be found in the target directory. To
 * see all dependencies, run the command
 * \verbatim buildr adis:deps \endverbatim
 */

package de.zib.gndms.voldmodel;

import com.beust.jcommander.Parameter;

import de.zib.gndms.voldmodel.abi.ABI;
import de.zib.gndms.voldmodel.abi.ABIi;

/**
 * This class invokes ADiS with a set of arguments executing the
 * specified methods.
 *
 * @author Jšrg Bachmann
 */
public final class Main extends ABI {
    /**
     * The base url of the VolD database.
     */
    @Parameter(names = {"--baseurl", "-b" },
            description = "Base URL of VolD database",
            required = true)
    private String voldURL;

    /**
     * The grid name.
     */
    @Parameter(names = { "--grid", "-g" },
            description = "Grid name")
    private String grid = "c3grid";

    /**
     * The encoding.
     */
    @Parameter(names = { "--enc", "-e" },
            description = "Encoding")
    private String enc = "utf-8";

    /**
     * Constructs a new Main with the given interface and arguments.
     * @param iface The interface
     * @param args The arguments
     */
    private Main(final ABIi iface, final String[] args) {
        super(iface, args);
    }

    /**
     * Invokes ADiS with the given arguments.
     * @param args The arguments
     */
    public static void main(final String[] args) {
        Adis adis = new Adis();
        Main main = new Main(adis, args);

        adis.setVoldURL(main.voldURL);
        adis.setEnc(main.enc);
        adis.setGrid(main.grid);

        main.invoke();
    }
}

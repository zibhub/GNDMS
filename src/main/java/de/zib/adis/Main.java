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

package de.zib.adis;

import com.beust.jcommander.Parameter;

import de.zib.adis.abi.ABI;
import de.zib.adis.abi.ABIi;

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

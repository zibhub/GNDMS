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

package de.zib.gndms.voldmodel;

import de.zib.vold.client.VolDClient;

/**
 * The VolDReader connects to a given VolD and outputs its registered values.
 *
 * @author Ulrike Golas
 *
 */
public final class VolDReader {

	/**
	 * The private constructor.
	 */
	private VolDReader() {
	}

   /**
    * Prints all information registered at the given VolD using Adis.
    * @param args The VolD url
    */
    public static void main(final String[] args) {
		if (args.length != 1) {
			System.out.println("Usage: java VolDReader voldUrl");
			return;
		}

		// The voldUrl, something like "http://c3-r2d2.zib.de:8080/vold"
		String voldUrl = args[0];

		VolDClient voldi;
        voldi = new VolDClient();
        voldi.setBaseURL(voldUrl);
        voldi.setEnc("utf-8");

        Adis adis = new Adis();
        adis.setVoldURL(voldUrl);

        adis.checkState();

        System.out.println("DMS :" + adis.getDMS());
        System.out.println("WSS :" + adis.getWSS());
        System.out.println("CPs :" + adis.listCPs());
        System.out.println("ESGF :" + adis.listESGFStagingSites());
        System.out.println("Import :" + adis.listImportSites());
        System.out.println("Export :" + adis.listExportSites());
        System.out.println("OAIs :" + adis.listOAIs());
        System.out.println("Publisher :" + adis.listPublishingSites());
        System.out.println("PublishingSites :" + adis.listPublishingSites());
        System.out.println("Workflows :" + adis.listWorkflows());
        System.out.println("OID Prefixes: " + adis.listGORFX());
	}
}

package de.zib.gndms.gndmstest;

import de.zib.gndms.voldmodel.Adis;
import de.zib.vold.client.VolDClient;
import de.zib.vold.common.Key;

public class AdisTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Adis adis = new Adis();
		adis.setVoldURL("http://c3-r2d2.zib.de:8080/vold");

		System.out.println(String.format("%s", adis.listESGFStagingSites()));

		VolDClient voldi;
		voldi = new VolDClient();
		voldi.setBaseURL("http://c3-r2d2.zib.de:8080/vold");
		System.out.println("************" + "\n");
		System.out.println("DMS :"
				+ voldi.lookup(new Key("c3grid", "Dms", "...")));
		System.out.println("ESGF :"
				+ voldi.lookup(new Key("c3grid", "ESGF", "...")));
		System.out.println("PUBLISHER :"
				+ voldi.lookup(new Key("c3grid", "PUBLISHER", "...")));

		adis.checkState();
		System.out.println("************" + "\n");
		System.out.println("DMS :" + adis.getDMS());
		System.out.println("WSS :" + adis.getWSS());
		System.out.println("CPs :" + adis.listCPs() + "\n");
		System.out.println("ESGF :" + adis.listESGFStagingSites());
		System.out.println("Import :" + adis.listImportSites());
		System.out.println("Export :" + adis.listExportSites());
		System.out.println("OAIs :" + adis.listOAIs());
		System.out.println("Publisher :" + adis.listPublishingSites());
		System.out.println("PublishingSites :" + adis.listPublishingSites());
		System.out.println("Workflows :" + adis.listWorkflows());
		adis.listGORFX();
		System.out.println("OID Prefixes: " + adis.listGORFX());
		System.out.println("OID Prefixes de.dkrz: "
				+ adis.listGORFXbyOID("de.dkrz"));
		// System.out.println("OID Prefixes: " + adis.listGORFXbyOID("de" +
		// "..."));
		System.out
				.println("OID Prefixes de.uni-koeln.fub.dmi_echam5_run4_20C_002: "
						+ adis.listGORFXbyOID("de.uni-koeln.fub.dmi_echam5_run4_20C_002"));
		System.out.println("OID Prefixes c3-po.zib.de: "
				+ adis.listGORFXbyOID("c3-po.zib.de"));
		System.out.println("OID Prefixes de.zib.c3-po: "
				+ adis.listGORFXbyOID("de.zib.c3-po"));
		System.out.println("CPs :" + adis.listCPs() + "\n");
		System.out.println("TransferSites :" + adis.listTransferSites() + "\n");

	}

}

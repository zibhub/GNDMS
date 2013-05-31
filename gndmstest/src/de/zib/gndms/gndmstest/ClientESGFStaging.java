package de.zib.gndms.gndmstest;

import java.util.List;
import java.util.Properties;

import de.zib.gndms.common.dspace.SliceConfiguration;
import de.zib.gndms.common.model.gorfx.types.Order;
import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.common.rest.CertificatePurpose;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.gndmc.gorfx.TaskFlowClient;
import de.zib.gndms.taskflows.esgfStaging.client.model.ESGFStagingOrder;

public class ClientESGFStaging extends AbstractStagingClient {
	
	static Properties p;
	public static Order order;
	
	public static void main(String[] args) throws Exception {
		ClientESGFStaging c = new ClientESGFStaging();
		order = updateOrder();
		String dn = p.getProperty("nutzerDN");
		c.execTF(order, dn);
	}
	
	public ClientESGFStaging() throws Exception {

		super("https://c3-po.zib.de:8443/gndms/c3grid");

		p = PropertyFileLoader.loadPropertyFile("web/META-INF/config.conf");
	}

	protected static Order updateOrder() {
		ESGFStagingOrder order = new ESGFStagingOrder();
//		order.addLink(
//				"http://bmbf-ipcc-ar5.dkrz.de/thredds/fileServer/cmip5/output1/MPI-M/MPI-ESM-LR/decadal1960/6hr/atmos/6hrPlev/r10i1p1/v20111122/psl/psl_6hrPlev_MPI-ESM-LR_decadal1960_r10i1p1_1962010100-1962123118.nc",
//				"e8e6834e45f0d35459c06cad08420a62");
		order.addLink(
				"http://bmbf-ipcc-ar5.dkrz.de/thredds/fileServer/cmip5/output1/NCC/NorESM1-M/rcp45/3hr/atmos/3hr/r1i1p1/v20110912/clt/clt_3hr_NorESM1-M_rcp45_r1i1p1_202601010130-203512312230.nc", 
				"bd6090395568077613e25292556ab858");
		return order;
}
	
	@Override
	protected Integer selectQuote(List<Specifier<Quote>> specifiers) {
		Quote q = specifiers.get(0).getPayload();
    	long size = q.getExpectedSize();
    	if (size != 0) {
	    	String taskID = specifiers.get(0).getUriMap().get(UriFactory.TASKFLOW_ID);
	    	SliceConfiguration conf = new SliceConfiguration();
	    	conf.setSize(size);
	    	System.out.println("expected slice size "+size);
	    	((ESGFStagingOrder) order).setSliceConfiguration(conf);
	    	TaskFlowClient tfClient = getTfClient();
	    	tfClient.setOrder(order.getTaskFlowType(), taskID, order, p.getProperty("nutzerDN"), 
	    			"testworkflow");
    	}
		return super.selectQuote(specifiers);
	}
	
	@Override
	protected GNDMSResponseHeader setupContext(GNDMSResponseHeader context) {
		String name = p.getProperty("myproxyLogin");
		String pw = p.getProperty("myproxyPassword");
		System.out.println(String.format("setzt das MyProxyToken %s %s", name,
				pw));
		context.addMyProxyToken(CertificatePurpose.ESGF.toString(), name, pw);
		return context;
	}

}

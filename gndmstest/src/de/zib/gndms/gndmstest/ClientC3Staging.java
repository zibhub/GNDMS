package de.zib.gndms.gndmstest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.jruby.RubyProcess.Sys;

import de.zib.gndms.common.dspace.SliceConfiguration;
import de.zib.gndms.common.model.gorfx.types.MinMaxPair;
import de.zib.gndms.common.model.gorfx.types.Order;
import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.common.rest.CertificatePurpose;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.gndmc.gorfx.TaskFlowClient;
import de.zib.gndms.taskflows.dmsstaging.client.model.DmsStageInOrder;
import de.zib.gndms.taskflows.staging.client.model.DataConstraints;
import de.zib.gndms.taskflows.staging.client.model.DataDescriptor;
import de.zib.gndms.taskflows.staging.client.model.ProviderStageInOrder;
import de.zib.gndms.taskflows.staging.client.model.SpaceConstraint;

public class ClientC3Staging extends AbstractStagingClient {
	
	public static Properties p;
	public static DmsStageInOrder order;
	public static ClientC3Staging c;
	
	public static void main(String[] args) throws Exception {
		c = new ClientC3Staging();
		order = (DmsStageInOrder)updateOrder();
		String dn = p.getProperty("nutzerDN");
		System.out.println(order.getTaskFlowType());

		c.execTF(order, dn);
//		c.getTfClient().getTask("ProviderStageIn", "e027e1f5-0995-4206-84f9-74b71d8b23fe", dn, "");
//	c.handleTaskSpecifier(c.getTaskClient().getTaskFacets(id, dn))
//		c.getTfClient().deleteTaskflow(order.getTaskFlowType(), ClientC3Staging.order.getActId(), dn, c.getTaskClient().)
//		c.getTaskClient().deleteTask("e027e1f5-0995-4206-84f9-74b71d8b23fe", dn, "");
		
	}
	
	public ClientC3Staging() throws Exception {
		super("https://c3-po.zib.de:8443/gndms/c3grid");
		p = PropertyFileLoader.loadPropertyFile("web/META-INF/config.conf");
	}

	protected static ProviderStageInOrder updateOrder() {
        order = new DmsStageInOrder();
        
        // Contains the object ids (oids) of the desired data objects. (required)
        List<String> objectList = new ArrayList<String>();
        // // Staging from c3-4lom

        objectList.add("de.zib.c3-po");
        // Staging from c3-r2d2
//        objectList.add("de.zib.test");
//        objectList.add("c3-po.zib.de");

        
        // Space constraint, e.g latitude (required), longitude (required),
        // altitude (height-level) (optional) and other constraints (areaCRS, verticalCRS) (optional).
        SpaceConstraint spaceConstraint = new SpaceConstraint();
        // latitude (required)
        spaceConstraint.setLatitude(new MinMaxPair(-55.0, 20.0));
        // longitude (required)
        spaceConstraint.setLongitude(new MinMaxPair(90.0, 180.0));
        // altitude (height-level) (optional)
        spaceConstraint.setAltitude(new MinMaxPair(300.0, 925.0));
        // other area constraints (areaCRS) (optional)
        spaceConstraint.setAreaCRS(null);
        // other vertical constraints (verticalCRS) (optional)
        spaceConstraint.setVerticalCRS("http://www.c3grid.de/files/schemas/C3grid-crs-dict.xml#xpointer(//*[@gml:id='vertCRS.standardPressureLevel'])");
        
        // Time span for the data (optional)
        de.zib.gndms.taskflows.staging.client.model.TimeConstraint timeConstraint = new de.zib.gndms.taskflows.staging.client.model.TimeConstraint();
        timeConstraint.setMinTime("1982-04-01T00:00:00.000Z");
        timeConstraint.setMaxTime("1982-08-31T23:59:00.000Z");
        
        // Some data flags - data variables to be extracted (required)
        List<String> cfList = new ArrayList<String>();
        cfList.add("relative_humidity");
        cfList.add("air_temperature");
        
        // Additional constraints (required)
        Map<String,String> constraintList = new HashMap<String, String>();
        
        // Wrap above constraint in DataConstraints
        DataConstraints dataConstraints = new DataConstraints();
        dataConstraints.setCFList(cfList);
        dataConstraints.setConstraintList(constraintList);
        dataConstraints.setSpaceConstraint(spaceConstraint);
        dataConstraints.setTimeConstraint(timeConstraint);
        
        // Create whole data description with above DataConstraints
        DataDescriptor dataDescriptor = new DataDescriptor();
        dataDescriptor.setConstraints(dataConstraints);
        dataDescriptor.setObjectList(objectList);
        
        // Set the file formats
        // Format of the staged data file, e.g cdo, ncf (required)
        dataDescriptor.setDataFormat("nc");
        // Format of the staged metadata file, e.g xml (required)
        dataDescriptor.setMetaDataFormat("xml");
        // Format of the archive for data files, e.g tgz, zip (optional)
        dataDescriptor.setDataArchiveFormat(null);
        // Format of the archive for metadata files, e.g tgz, zip (optional)
        dataDescriptor.setMetaDataArchiveFormat(null);
        
        // Set names of stages files
        order.setActDataFile("27292407-2694-4338-ae68-5c150f2ce4e9.nc");
        order.setActMetadataFile("27292407-2694-4338-ae68-5c150f2ce4e9.xml");
        
        // Wrap above DateDescription in ProviderStageInOrder
        order.setDataDescriptor(dataDescriptor);
        
        order.setGridSite("");
        
        return order;
}
	
	@Override
	protected GNDMSResponseHeader setupContext(GNDMSResponseHeader context) {
		String name = p.getProperty("myproxyLogin");
		String pw = p.getProperty("myproxyPassword");
		System.out.println(String.format("setzt das MyProxyToken %s %s", name,
				pw));
		context.addMyProxyToken(CertificatePurpose.C3GRID.toString(), name, pw);
		return context;
	}

}

package de.zib.gndms.gndmstest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import de.zib.gndms.common.dspace.SliceConfiguration;
import de.zib.gndms.common.model.gorfx.types.MinMaxPair;
import de.zib.gndms.common.model.gorfx.types.Order;
import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.common.model.gorfx.types.TaskFailure;
import de.zib.gndms.common.model.gorfx.types.TaskResult;
import de.zib.gndms.common.model.gorfx.types.TaskStatus;
import de.zib.gndms.common.rest.CertificatePurpose;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.GNDMSResponseHeader;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.gndmc.dspace.SliceClient;
import de.zib.gndms.gndmc.dspace.SliceKindClient;
import de.zib.gndms.gndmc.dspace.SubspaceClient;
import de.zib.gndms.gndmc.gorfx.AbstractTaskFlowExecClient;
import de.zib.gndms.gndmc.gorfx.FullGORFXClient;
import de.zib.gndms.gndmc.gorfx.TaskClient;
import de.zib.gndms.gndmc.gorfx.TaskFlowClient;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.taskflows.dmsstaging.client.model.DmsStageInOrder;
import de.zib.gndms.taskflows.esgfStaging.client.model.ESGFStagingOrder;
import de.zib.gndms.taskflows.staging.client.model.DataConstraints;
import de.zib.gndms.taskflows.staging.client.model.DataDescriptor;
import de.zib.gndms.taskflows.staging.client.model.ProviderStageInOrder;
import de.zib.gndms.taskflows.staging.client.model.SpaceConstraint;

public class Client extends AbstractTaskFlowExecClient {

	ApplicationContext context;
	String gorfxEpUrl = "https://c3-4lom.zib.de:8443/gndms/c3grid";
    protected EntityManagerFactory emf;
	private static ESGFStagingOrder order;
//	private static ProviderStageInOrder order;
	private static Properties p;
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Client c = new Client();
		c.setupAll();

		p = new Properties();
		p.load(new FileInputStream(new File("web/META-INF/config.conf")));
		String dn = p.getProperty("nutzerDN");
				
//		System.out.println(c.createSlice(c.gorfxEpUrl,dn));

       	//SliceClient ssc = c.createClientObject(SliceClient.class);
       	//ssc.setServiceURL(c.gorfxEpUrl);
        //System.out.println(ssc.getSliceInformation("ProviderStaging", "ProviderKind", "f3e9e23f-37e0-4bc0-9ea4-5b23f885b269", dn));

		System.out.println(c.deleteSlice(c.gorfxEpUrl,"ProviderStaging", "ProviderKind", "909d7bd5-ad57-46a6-bdd9-8d3c0d0bae0d",dn));
		
		
//		order = new ESGFStagingOrder();
//		order.addLink(
//				"http://bmbf-ipcc-ar5.dkrz.de/thredds/fileServer/cmip5/output1/NCC/NorESM1-M/rcp45/3hr/atmos/3hr/r1i1p1/v20110912/clt/clt_3hr_NorESM1-M_rcp45_r1i1p1_202601010130-203512312230.nc",
//				"bd6090395568077613e25292556ab858");

//         order = createOrder();

//    	c.execTF(order, dn);

	}

	@SuppressWarnings("unchecked")
	private <T> T createClientObject(Class<T> clazz) {
		return (T) context.getAutowireCapableBeanFactory().createBean(clazz,
               AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
	}

	protected static ProviderStageInOrder createOrder() {
		DmsStageInOrder order = new DmsStageInOrder();
		
		// Contains the object ids (oids) of the desired data objects. (required)
		List<String> objectList = new ArrayList<String>() {{
			add("de.zib.test");
		}};
		
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
		spaceConstraint.setAreaCRS("");
		// other vertical constraints (verticalCRS) (optional)
		spaceConstraint.setVerticalCRS("http://www.c3grid.de/files/schemas/C3grid-crs-dict.xml#xpointer(//*[@gml:id='vertCRS.standardPressureLevel'])");
		
		// Time span for the data (optional)
		de.zib.gndms.taskflows.staging.client.model.TimeConstraint timeConstraint = new de.zib.gndms.taskflows.staging.client.model.TimeConstraint();
		timeConstraint.setMinTime("1982-04-01T00:00:00.000Z");
		timeConstraint.setMaxTime("1982-08-31T23:59:00.000Z");
		
		// Some data flags - data variables to be extracted (required)
		List<String> cfList = new ArrayList<String>() {{
			add("relative_humidity");
			add("air_temperature");
		}};
		
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
		dataDescriptor.setDataArchiveFormat("");
		// Format of the archive for metadata files, e.g tgz, zip (optional)
		dataDescriptor.setMetaDataArchiveFormat("");
		
		// Set names of stages files
		order.setActDataFile("27292407-2694-4338-ae68-5c150f2ce4e9.nc");
		order.setActMetadataFile("27292407-2694-4338-ae68-5c150f2ce4e9.xml");
		
		// Wrap above DateDescription in ProviderStageInOrder
		order.setDataDescriptor(dataDescriptor);
		
		order.setGridSite("");
		
		return order;
	}

	
	private String deleteSlice(String serviceURL, String subspace, String slicekind, String slice, String dn) {
		SubspaceClient sc = createClientObject(SubspaceClient.class);
       	sc.setServiceURL(serviceURL);
		
       	SliceClient ssc = createClientObject(SliceClient.class);
       	ssc.setServiceURL(serviceURL);
       	       	
		ResponseEntity<Specifier<Facets>> response = 
				ssc.deleteSlice(subspace, slicekind, slice, dn);;
			Specifier<Facets> spec = response.getBody();
			String url = spec.getUrl();
			return url;
	}	
	
	private String createSlice(String serviceURL, String dn) {
		SubspaceClient sc = createClientObject(SubspaceClient.class);
       	sc.setServiceURL(serviceURL);
		
		//ResponseEntity<Specifier<Void>> response = 
//				sc.createSlice("ProviderStaging", "ProviderKind", "", dn);
		ResponseEntity<Specifier<Void>> response = 
				sc.createSlice("ProviderStaging", "ProviderKind", "terminationTime=100", dn);
		if (HttpStatus.CREATED.equals(response.getStatusCode())) {
			Specifier<Void> spec = response.getBody();
			String url = spec.getUrl();
			return url;
		} else
			System.out.println("unable to create slice");
			return null;
	}

	private void setupAll() throws Exception {
		context = new ClassPathXmlApplicationContext(
				"classpath:META-INF/spring/bundle-context.xml");
		FullGORFXClient fgx = createBean(FullGORFXClient.class);
		fgx.setServiceURL(gorfxEpUrl);
		setGorfxClient(fgx);
		
		TaskFlowClient tfc = createBean(TaskFlowClient.class);
		tfc.setServiceURL(gorfxEpUrl);
		setTfClient(tfc);

		TaskClient tc = createBean(TaskClient.class);
		tc.setServiceURL(gorfxEpUrl);
		setTaskClient(tc);
	}

	public <T> T createBean(final Class<T> beanClass) {
		return createBean(beanClass,
				AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE);
	}

	public <T> T createBean(final Class<T> beanClass, int autowireMode) {
		return (T) context.getAutowireCapableBeanFactory().createBean(
				beanClass, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true);
	}

	@Override
	protected GNDMSResponseHeader setupContext(GNDMSResponseHeader context) {
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(new File("web/META-INF/config.conf")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String name = p.getProperty("myproxyLogin");
		String pw = p.getProperty("myproxyPassword");
		System.out.println(String.format("setzt das MyProxyToken %s %s", name,
				pw));
//		context.addMyProxyToken(CertificatePurpose.ESGF.toString(), name, pw,true);
		context.addMyProxyToken(CertificatePurpose.C3GRID.toString(), name, pw, true);
		return super.setupContext(context);
	}

	public void handleStatus(TaskStatus stat) {
		System.out.println("status "+stat.getStatus().toString());
	}

//	@Override
//	protected Integer selectQuote(List<Specifier<Quote>> specifiers) {
//		System.out.println("wähle quote");
//		return 0;
//	}

//	@Override
//	protected Integer selectQuote(List<Specifier<Quote>> specifiers) {
//		Quote q = specifiers.get(0).getPayload();
//    	long size = q.getExpectedSize();
//    	if (size != 0) {
//	    	String taskID = specifiers.get(0).getUriMap().get(UriFactory.TASKFLOW_ID);
//	    	SliceConfiguration conf = new SliceConfiguration();
//	    	conf.setSize(size);
//	    	((ESGFStagingOrder) order).setSliceConfiguration(conf);
//	    	TaskFlowClient tfClient = getTfClient();
//	    	tfClient.setOrder(order.getTaskFlowType(), taskID, order, p.getProperty("nutzerDN"), 
//	    			"testworkflow");
//    	}
    	
    	@Override
    	protected Integer selectQuote(List<Specifier<Quote>> specifiers) {
    		Quote q = specifiers.get(0).getPayload();
        	long size = q.getExpectedSize();
        	if (size != 0) {
    	    	String taskID = specifiers.get(0).getUriMap().get(UriFactory.TASKFLOW_ID);
    	    	SliceConfiguration conf = new SliceConfiguration();
    	    	conf.setSize(size);
    	    	(( ESGFStagingOrder) order).setSliceConfiguration(conf);
    	    	TaskFlowClient tfClient = getTfClient();
    	    	tfClient.setOrder(order.getTaskFlowType(), taskID, order, p.getProperty("nutzerDN"), 
    	    			"testworkflow");
        	}
    	
    	ArrayList<String> l = new ArrayList<String>();
		for (Specifier<Quote> s : specifiers) {
			l.add(s.getPayload().getSite());
		}
		System.out.println(String.format("wähle quote %s", l));
		return 0;
	}
	
	@Override
	protected void handleTaskSpecifier(Specifier<Facets> ts) {
		System.out.println("gehandelt");
	}

	@Override
	public void handleResult(TaskResult res) {
		System.out.println("ergebnis da "+res.getResult().toString());
	}

	@Override
	public void handleFailure(TaskFailure fail) {
		System.out.println("fehler: "+fail.getMessage());
	}
    public EntityManagerFactory getEmf( ) {
        return emf;
    }


    @Inject
    public void setEmf( EntityManagerFactory emf ) {
        this.emf = emf;
    }


}

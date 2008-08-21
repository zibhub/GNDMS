package de.zib.gndms.dspace.service;

import de.zib.gndms.dspace.service.globus.resource.ExtDSpaceResourceHome;
import de.zib.gndms.dspace.subspace.service.globus.resource.ExtSubspaceResourceHome;
import de.zib.gndms.dspace.subspace.service.globus.resource.SubspaceResource;
import de.zib.gndms.dspace.subspace.stubs.types.SubspaceReference;
import de.zib.gndms.dspace.stubs.types.UnknownSubspace;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.common.ImmutableScopedName;
import org.apache.log4j.Logger;
import org.apache.axis.types.URI;
import org.globus.wsrf.ResourceKey;

import javax.xml.namespace.QName;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;

/** 
 * TODO:I am the service side implementation class.  IMPLEMENT AND DOCUMENT ME
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class DSpaceImpl extends DSpaceImplBase {

	private static final Logger logger;
	private final GNDMSystem system;

    // todo: maybe encapsulate complete queries in actions
    // query strings:
    private static final String getSubspaceQuery =
            "SELECT x FROM Subspaces x WHERE x.metaSubspace = :uriParam";
    private static final String listPublicSubspacesQuery =
            "SELECT x FROM Subspaces x WHERE x.systemId = :sysParam";
    private static final String listSupportedSchemasQuery =
            "SELECT DISTINCT x.scopedName.scopeName FROM MetaSubspaces x";

    static {
		logger = Logger.getLogger(DSpaceImpl.class);
	}

	@SuppressWarnings({"FeatureEnvy"})
	public DSpaceImpl() throws RemoteException {
		super();
		try {
			final ExtDSpaceResourceHome home = getResourceHome();
			system = home.getSystem();
			system.addHome(home);
            system.addHome(getSubspaceResourceHome());
            // TODO
            // system.addHome(getSliceResourceHome());
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public ExtDSpaceResourceHome getResourceHome() throws Exception {
		return (ExtDSpaceResourceHome) super.getResourceHome();    // Overridden method
	}

    public SubspaceReference[] listPublicSubspaces(org.apache.axis.types.URI schemaURI) throws RemoteException {

        EntityManager em = system.getEntityManagerFactory().createEntityManager(  );
        Query q = em.createQuery( listPublicSubspacesQuery );
        q.setParameter( "uriParam",  schemaURI.toString() );
        q.setParameter( "sysParam",  system.getSystemName( ) );
        List<Subspace> rl = (List<Subspace>) q.getResultList( );

        ArrayList<SubspaceReference> al = new ArrayList<SubspaceReference>( rl.size( ) );

        try{
            ExtSubspaceResourceHome srh  = (ExtSubspaceResourceHome) getSubspaceResourceHome();
            for( Subspace sp: rl )
                al.add( srh.getReferenceForSubspace( sp ) );
            return al.toArray( new SubspaceReference[0] );
        } catch ( Exception e ) {
            throw new RemoteException( "Following exception occured while creating SubspaceReferenc: " + e.getMessage() );
        }
    }

    public org.apache.axis.types.URI[] listSupportedSchemas() throws RemoteException {

        EntityManager em = system.getEntityManagerFactory().createEntityManager(  );
        Query q = em.createQuery( listSupportedSchemasQuery );
        q.setParameter( "sysParam",  system.getSystemName( ) );
        List<String> rl = (List<String>) q.getResultList( );

        ArrayList<URI> al = new ArrayList<URI>( rl.size( ) );

        try {
            for( String s: rl )
                al.add( new URI( s ) );
        } catch ( URI.MalformedURIException e ) {
            throw new RemoteException( "Maleformed exception URI occurred: " +e.getMessage( ) );
        }

        return al.toArray( new URI[0] );
    }

    
  public SubspaceReference getSubspace( QName subspaceSpecifier) throws RemoteException, UnknownSubspace {

      EntityManager em = system.getEntityManagerFactory().createEntityManager(  );
      Query q = em.createQuery( getSubspaceQuery );
      q.setParameter( "uriParam",  new ImmutableScopedName( subspaceSpecifier ) );
      q.setParameter( "sysParam",  system.getSystemName( ) );
      Subspace sp = ( Subspace) q.getSingleResult( );
      if( sp == null )
        throw new UnknownSubspace( );

      try {
          ExtSubspaceResourceHome srh  = (ExtSubspaceResourceHome) getSubspaceResourceHome();
          return srh.getReferenceForSubspace( sp );
      } catch ( Exception e ) {
          throw new RemoteException( "Following exception occured while creating SubspaceReferenc: " + e.getMessage() );
      }
  }


    public de.zib.gndms.dspace.slice.stubs.types.SliceReference createSliceInSubspace( QName subspaceSpecifier,types.SliceCreationSpecifier sliceCreationSpecifier,types.ContextT context) throws RemoteException, de.zib.gndms.dspace.subspace.stubs.types.OutOfSpace, de.zib.gndms.dspace.subspace.stubs.types.UnknownOrInvalidSliceKind, UnknownSubspace, de.zib.gndms.dspace.stubs.types.InternalFailure {
		org.apache.axis.message.addressing.EndpointReferenceType epr = new org.apache.axis.message.addressing.EndpointReferenceType();
		de.zib.gndms.dspace.slice.service.globus.resource.SliceResourceHome home = null;
		org.globus.wsrf.ResourceKey resourceKey = null;
		org.apache.axis.MessageContext ctx = org.apache.axis.MessageContext.getCurrentContext();
		String servicePath = ctx.getTargetService();
		String homeName = org.globus.wsrf.Constants.JNDI_SERVICES_BASE_NAME + servicePath + "/" + "sliceHome";

		try {
			javax.naming.Context initialContext = new javax.naming.InitialContext();
			home = (de.zib.gndms.dspace.slice.service.globus.resource.SliceResourceHome) initialContext.lookup(homeName);
			resourceKey = home.createResource();
			
			//  Grab the newly created resource
			de.zib.gndms.dspace.slice.service.globus.resource.SliceResource thisResource = (de.zib.gndms.dspace.slice.service.globus.resource.SliceResource)home.find(resourceKey);
			
			//  This is where the creator of this resource type can set whatever needs
			//  to be set on the resource so that it can function appropriatly  for instance
			//  if you want the resouce to only have the query string then there is where you would
			//  give it the query string.
			
			
			// sample of setting creator only security.  This will only allow the caller that created
			// this resource to be able to use it.
			//thisResource.setSecurityDescriptor(gov.nih.nci.cagrid.introduce.servicetools.security.SecurityUtils.createCreatorOnlyResourceSecurityDescriptor());
			
			

			String transportURL = (String) ctx.getProperty(org.apache.axis.MessageContext.TRANS_URL);
			transportURL = transportURL.substring(0,transportURL.lastIndexOf('/') +1 );
			transportURL += "Slice";
			epr = org.globus.wsrf.utils.AddressingUtils.createEndpointReference(transportURL,resourceKey);
		} catch (Exception e) {
			throw new RemoteException("Error looking up Slice home:" + e.getMessage(), e);
		}

		//return the typed EPR
		de.zib.gndms.dspace.slice.stubs.types.SliceReference ref = new de.zib.gndms.dspace.slice.stubs.types.SliceReference();
		ref.setEndpointReference(epr);

		return ref;
  }


    @Override
    public ExtSubspaceResourceHome getSubspaceResourceHome() throws Exception {
        return (ExtSubspaceResourceHome) super.getSubspaceResourceHome();    // Overridden method
    }
}


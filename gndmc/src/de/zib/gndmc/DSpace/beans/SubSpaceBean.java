package de.zib.gndmc.DSpace.beans;

import de.zib.gndms.model.common.ImmutableScopedName;

import java.util.Properties;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 26.07.2010, Time: 13:13:49
 */
public class SubSpaceBean extends DSpaceBean {

    //public final static String SUBSPACE_QNAME_KEY = "DSpace.subSpace.qname";
    public final static String SUBSPACE_LOCAL_KEY = "DSpace.subSpace.local";
    public final static String SUBSPACE_SCOPE_KEY = "DSpace.subSpace.scope";
    private ImmutableScopedName scopedName;


    public ImmutableScopedName getSubSpaceScopedName() {
        return scopedName;
    }


    public void setSubSpaceName( String scope, String local ) {
        this.scopedName = new ImmutableScopedName( scope, local );
    }


    @Override
    public void setProperties( Properties prop ) {
        super.setProperties( prop );
        String s = prop.getProperty( SUBSPACE_SCOPE_KEY );
        String l = prop.getProperty( SUBSPACE_LOCAL_KEY );
        scopedName = new ImmutableScopedName( s, l );

        System.out.println( "s: " + scopedName.getNameScope() + "| l: " + scopedName.getLocalName() ); 
    }


    @Override
    public void createExampleProperties( Properties prop ) {
        super.createExampleProperties( prop );
        //prop.setProperty(  SUBSPACE_QNAME_KEY , "<the-name-of-the-targeted-subspace>" );
        prop.setProperty(  SUBSPACE_LOCAL_KEY , "<the-LOCAL-name-part-of-the-targeted-subspace>" );
        prop.setProperty(  SUBSPACE_SCOPE_KEY , "<the-SCOPE-name-part-of-the-targeted-subspace>" );
    }
}

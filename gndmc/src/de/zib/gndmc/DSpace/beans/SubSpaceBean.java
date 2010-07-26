package de.zib.gndmc.DSpace.beans;

import java.util.Properties;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 26.07.2010, Time: 13:13:49
 */
public class SubSpaceBean extends DSpaceBean {

    public final static String SUBSPACE_QNAME_KEY = "DSpace.subSpace.qname";
    private String subSpaceQName;


    public String getSubSpaceQName() {
        return subSpaceQName;
    }


    public void setSubSpaceQName( String subSpaceQName ) {
        this.subSpaceQName = subSpaceQName;
    }


    @Override
    public void setProperties( Properties prop ) {
        super.setProperties( prop );
        subSpaceQName = prop.getProperty( SUBSPACE_QNAME_KEY );
    }


    @Override
    public void createExampleProperties( Properties prop ) {
        super.createExampleProperties( prop );
        prop.setProperty(  SUBSPACE_QNAME_KEY , "<the-name-of-the-targeted-subspace>" );
    }
}

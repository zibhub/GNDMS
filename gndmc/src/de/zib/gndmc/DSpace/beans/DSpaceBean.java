package de.zib.gndmc.DSpace.beans;

import de.zib.gndmc.util.LoadablePropertyBean;

import java.util.Properties;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 26.07.2010, Time: 13:09:12
 */
public class DSpaceBean extends LoadablePropertyBean {


    public final static String DSPACE_URI_KEY = "DSpace.serviceURI";
    private String dspaceURI;


    public String getDspaceURI() {
        return dspaceURI;
    }


    public void setDspaceURI( String dspaceURI ) {
        this.dspaceURI = dspaceURI;
    }


    public void setProperties( Properties prop )  {

       dspaceURI = prop.getProperty( DSPACE_URI_KEY );
    }


    public void createExampleProperties( Properties prop ) {

        prop.setProperty(  DSPACE_URI_KEY, "<URI of the dspace service>" );
    }
}

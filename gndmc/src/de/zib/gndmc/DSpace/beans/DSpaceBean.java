package de.zib.gndmc.DSpace.beans;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import de.zib.gndmc.util.LoadablePropertyBean;

import java.util.Properties;

/**
 * @author: try ma ik jo rr a zib
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

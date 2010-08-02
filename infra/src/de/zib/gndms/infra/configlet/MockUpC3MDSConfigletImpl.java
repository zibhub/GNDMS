package de.zib.gndms.infra.configlet;

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



import de.zib.gndms.c3resource.C3ResourceReader;
import de.zib.gndms.c3resource.jaxb.Site;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Iterator;

/**
 * @author: try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 16.01.2009, Time: 14:12:56
 */
public class MockUpC3MDSConfigletImpl extends C3MDSConfiglet {

    private C3Catalog myCatalog;

    @Override
    protected void threadRun() {
        try {
            getLog().info("Refreshing (MockUp)C3MDSCatalog...");
            final C3ResourceReader reader = new C3ResourceReader();
            final String curRequiredPrefix = getRequiredPrefix();
            InputStream is = new FileInputStream( getMdsUrl() );
            Iterator<Site> sites = reader.readXmlSites( curRequiredPrefix, is );
            C3Catalog newCatalog = new C3Catalog(curRequiredPrefix, sites);
            setMyCatalog(newCatalog);
            getLog().debug("Finished Refreshing (MockUp)C3MDSCatalog.");
        }
        catch (RuntimeException e) {
            getLog().warn(e);
        }
        catch (
            IOException e) {
            getLog().warn(e);
        }
    }


    @Override
    public synchronized C3Catalog getCatalog() {
        return getMyCatalog( );
    }


    public synchronized C3Catalog getMyCatalog() {
        
        while (myCatalog == null)
            try {
                wait();
            }
            catch (InterruptedException e) {
                /* ignored */
            }
        return myCatalog;
    }


    protected synchronized void setMyCatalog( final C3Catalog myCatalog ) {
        this.myCatalog = myCatalog;
        notifyAll();
    }
}

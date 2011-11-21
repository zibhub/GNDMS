package de.zib.gndmc.gridftp;
/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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

import de.zib.gndmc.util.GridFTPClientProvider;
import de.zib.gndms.kit.application.AbstractApplication;
import org.kohsuke.args4j.Option;

/**
 * @author try ma ik jo rr a zib
 * @date 07.06.11  17:24
 * @brief
 */
public class TestBase extends AbstractApplication {

    @Option( name="-uri", required=true, usage="URL of gridftp-server to use", metaVar="URI" )
    protected String uri;
    @Option( name="-path", required=true, usage="path to the source files", metaVar="PATH" )
    protected String path;
    private GridFTPClientProvider gridFTPClientProvider;


    @Override
    public void run() throws Exception {

        gridFTPClientProvider = new GridFTPClientProvider();
        gridFTPClientProvider.setPath( path );
        gridFTPClientProvider.setSourceURI( uri );
    }


    public GridFTPClientProvider getGridFTPClientProvider() {
        return gridFTPClientProvider;
    }


    public void setGridFTPClientProvider( GridFTPClientProvider gridFTPClientProvider ) {
        this.gridFTPClientProvider = gridFTPClientProvider;
    }
}

/**
 * Copyright 2008-2012 Zuse Institute Berlin (ZIB)
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
 *
 */

package de.zib.gndms.gndmc.utils;

import de.zib.gndms.stuff.devel.StreamCopyNIO;
import org.springframework.http.client.ClientHttpResponse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * @date: 15.03.12
 * @time: 09:27
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class DownloadResponseExtractor extends DefaultResponseExtractor {
    String outputFile;
    
    DownloadResponseExtractor( ) {
        outputFile = "/tmp/GNDMS.t" + Thread.currentThread().getId() + "." + UUID.randomUUID().toString();
    }
    
    DownloadResponseExtractor( final String outputFile ) {
        this.outputFile = outputFile;
    }

    @Override
    public void extractData( final String url, final ClientHttpResponse response ) throws IOException {
        String downloadID = UUID.randomUUID().toString();

        logger.debug( "(DID: " + downloadID + ") Downloading " + url + " to file " + getOutputFile() );

        super.extractData( url, response );

        InputStream in = getBody();
        OutputStream out = new FileOutputStream( outputFile );
        StreamCopyNIO.copyStream( in, out );

        logger.debug( "(DID: " + downloadID + ") Done." );
    }

    public void setOutputFile( String outputFile ) {
        this.outputFile = outputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }
}

package de.zib.gndms.gndmc.dspace.test;

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

import de.zib.gndms.common.model.FileStats;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.gndmc.dspace.SliceClient;
import de.zib.gndms.gndmc.dspace.SliceKindClient;
import de.zib.gndms.gndmc.dspace.SubspaceClient;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Tests the SliceClient
 * 
 * @author bachmann@zib.de
 */

public class SliceClientTest {

    final ApplicationContext context;

    private SubspaceClient subspaceClient;
    private SliceKindClient sliceKindClient;
    private SliceClient sliceClient;

    final private String serviceUrl;

    final static String subspaceConfig = "size: 6000; path: /tmp/gndms/sub; gsiFtpPath: undefined";
    final static String subspaceId = "testsub";

    final static String sliceKindId = "testkind";
    final static String sliceKindConfig = "sliceKindMode:700; uniqueDirName:kind";
    
    final static String sliceConfig = "terminationTime:2011-12-16; sliceSize:1024";
    final static String sliceFileName = "testfile";
    final static String sliceFile = "/tmp/test.file";
    final static String sliceFileContent = "Hallo Welt";

    String sliceId;

    final private String admindn;


    @Parameters( { "serviceUrl", "admindn" } )
    public SliceClientTest( final String serviceUrl, @Optional("root") final String admindn ) {
        this.serviceUrl = serviceUrl;
        this.admindn = admindn;

        this.context = new ClassPathXmlApplicationContext( "classpath:META-INF/client-context.xml" );
    }


    @BeforeClass( groups = { "subspaceServiceTest" } )
    public void init() {
        subspaceClient = ( SubspaceClient )context.getAutowireCapableBeanFactory().createBean(
                SubspaceClient.class,
                AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true );
        subspaceClient.setServiceURL( serviceUrl );
        
        sliceKindClient = ( SliceKindClient )context.getAutowireCapableBeanFactory().createBean(
                SliceKindClient.class,
                AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true );
        sliceKindClient.setServiceURL( serviceUrl );
        
        sliceClient = ( SliceClient )context.getAutowireCapableBeanFactory().createBean(
                SliceClient.class,
                AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true );
        sliceClient.setServiceURL( serviceUrl );
    }


    @Test( groups = { "subspaceServiceTest" } )
    public void testCreateSubspace() {
        final String mode = "CREATE";

        ResponseEntity<Facets> subspace = null;
        try {
            subspace = subspaceClient.createSubspace( subspaceId, subspaceConfig, admindn );

            Assert.assertNotNull(subspace);
            Assert.assertEquals( subspace.getStatusCode(), HttpStatus.CREATED );
        }
        catch( HttpClientErrorException e ) {
            if( ! e.getStatusCode().equals( HttpStatus.UNAUTHORIZED ) )
                throw e;
        }

        final ResponseEntity< Facets > res = subspaceClient.listAvailableFacets( subspaceId, admindn );
        Assert.assertNotNull( res );
        Assert.assertEquals(res.getStatusCode(), HttpStatus.OK);
    }


    @Test(
            groups = { "subspaceServiceTest" },
            dependsOnMethods = { "testCreateSubspace" }
    )
    public void testCreateSliceKind() {
        try {
            final ResponseEntity<List< Specifier< Void > >> sliceKind =
                    subspaceClient.createSliceKind( subspaceId, sliceKindId, sliceKindConfig, admindn );
            Assert.assertNotNull( sliceKind );
            Assert.assertEquals( sliceKind.getStatusCode(), HttpStatus.CREATED );
        }
        catch( HttpClientErrorException e ) {
            if( ! e.getStatusCode().equals( HttpStatus.BAD_REQUEST ) )
                throw e;
        }

        final ResponseEntity< List< Specifier< Void > > > listResponseEntity
                = subspaceClient.listSliceKinds( subspaceId, admindn );
        final List< Specifier< Void > > specifierList = listResponseEntity.getBody();

        for( Specifier< Void > s: specifierList ) {
            if( ! s.getUriMap().containsKey( UriFactory.SLICE_KIND ) )
                continue;
            if( s.getUriMap().get( UriFactory.SLICE_KIND ).equals( sliceKindId ) )
                return;
        }

        throw new IllegalStateException( "The created SliceKind " + sliceKindId
                + " could not be found in SliceKindListing" );
    }


    @Test(
            groups = { "subspaceServiceTest" },
            dependsOnMethods = { "testCreateSliceKind" }
    )
    public void testCreateSlice() {
        final ResponseEntity<Specifier<Void>> slice =
                subspaceClient.createSlice(subspaceId, sliceKindId, sliceConfig, admindn);
        Assert.assertNotNull( slice );
        Assert.assertEquals( slice.getStatusCode(), HttpStatus.CREATED );
        
        sliceId = slice.getBody().getUriMap().get( UriFactory.SLICE );
    }


    @Test(
            groups = { "subspaceServiceTest" },
            dependsOnMethods = { "testCreateSlice" }
    )
    public void testFileTransfer() {
        // TODO: test for nonexistance of sliceFile as initial constraint

        // upload file
        {
            final ResponseEntity<Void> responseEntity = sliceClient.setFileContent(
                    subspaceId,
                    sliceKindId,
                    sliceId,
                    sliceFileName,
                    new MultipartFile() {
                        @Override
                        public String getName() {
                            return sliceFileName;
                        }

                        @Override
                        public String getOriginalFilename() {
                            return sliceFile;
                        }

                        @Override
                        public String getContentType() {
                            return null;
                        }

                        @Override
                        public boolean isEmpty() {
                            return false;
                        }

                        @Override
                        public long getSize() {
                            return 0;
                        }

                        @Override
                        public byte[] getBytes() throws IOException {
                            return null;
                        }

                        @Override
                        public InputStream getInputStream() throws IOException {
                            return null;
                        }

                        @Override
                        public void transferTo(File dest) throws IOException, IllegalStateException {
                        }
                    },
                    admindn );

            Assert.assertNotNull( responseEntity );
            Assert.assertEquals( responseEntity.getStatusCode(), HttpStatus.OK );
        }

        // try to find uploaded file
        {
            if( !findFile() )
                throw new IllegalStateException( "Uploaded file " + sliceFileName + " could not be listed. Upload failed?" );
        }

        // download file and compare with uploaded file
        {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            final ResponseEntity<Void> responseEntity = sliceClient.listFileContent(
                    subspaceId,
                    sliceKindId,
                    sliceId,
                    sliceFileName,
                    new LinkedList<String>(),
                    admindn,
                    byteArrayOutputStream );

            Assert.assertNotNull( responseEntity );
            Assert.assertEquals( responseEntity.getStatusCode(), HttpStatus.OK );

            Assert.assertEquals( sliceFileContent.getBytes(), byteArrayOutputStream.toByteArray() );
        }

        // delete uploaded file
        {
            final ResponseEntity< Void > responseEntity =
                    sliceClient.deleteFile(subspaceId, sliceKindId, sliceId, sliceFileName, admindn);

            Assert.assertNotNull( responseEntity );
            Assert.assertEquals( responseEntity.getStatusCode(), HttpStatus.OK );
        }

        // try to not! find deleted file
        {
            if( findFile() )
                throw new IllegalStateException( "Still found deleted file " + sliceFileName );
        }
    }


    @Test(
            groups = { "subspaceServiceTest" },
            dependsOnMethods = { "testFileTransfer" }
    )
    public void testDeleteSlice() {
        {
            final ResponseEntity< Specifier< Facets > > responseEntity =
                    sliceClient.deleteSlice( subspaceId, sliceKindId, sliceId, admindn );

            Assert.assertNotNull( responseEntity );
            Assert.assertEquals( responseEntity.getStatusCode(), HttpStatus.OK );
        }
        
        // check for nonexistance of slice
        {
            final ResponseEntity< Facets > responseEntity =
                    sliceClient.listSliceFacets( subspaceId, sliceId, sliceId, admindn );
            
            Assert.assertNotNull( responseEntity );
            Assert.assertEquals( responseEntity.getStatusCode(), HttpStatus.NOT_FOUND );
        }
    }


    private boolean findFile( ) {
        final ResponseEntity< List< FileStats > > listResponseEntity =
                sliceClient.listFiles(subspaceId, sliceKindId, sliceId, admindn);

        Assert.assertNotNull( listResponseEntity );
        Assert.assertEquals( listResponseEntity.getStatusCode(), HttpStatus.OK );

        for( FileStats fileStats: listResponseEntity.getBody() ) {
            if( fileStats.path.equals( "/" + sliceFileName ) )
                return true;
        }

        return false;
    }
}

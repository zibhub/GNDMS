package de.zib.gndms.gndmc.dspace.Test;

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
import de.zib.gndms.common.model.gorfx.types.TaskStatus;
import de.zib.gndms.common.rest.Facets;
import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.common.rest.UriFactory;
import de.zib.gndms.gndmc.dspace.SliceClient;
import de.zib.gndms.gndmc.dspace.SliceKindClient;
import de.zib.gndms.gndmc.dspace.SubspaceClient;
import de.zib.gndms.gndmc.gorfx.AbstractTaskFlowExecClient;
import de.zib.gndms.gndmc.gorfx.TaskClient;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
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
    private TaskClient taskClient;

    final private String serviceUrl;
    private RestTemplate restTemplate;

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


    @BeforeClass( groups = { "sliceServiceTest" } )
    public void init() {
        subspaceClient = ( SubspaceClient )context.getAutowireCapableBeanFactory().createBean(
                SubspaceClient.class,
                AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true );
        subspaceClient.setServiceURL( serviceUrl );
        
        sliceKindClient = ( SliceKindClient )context.getAutowireCapableBeanFactory().createBean(
                SliceKindClient.class,
                AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true );
        sliceKindClient.setServiceURL( serviceUrl );

        sliceClient = ( SliceClient )context.getAutowireCapableBeanFactory().createBean(
                SliceClient.class,
                AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true );
        sliceClient.setServiceURL(serviceUrl);

        taskClient = ( TaskClient )context.getAutowireCapableBeanFactory().createBean(
                TaskClient.class,
                AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true );
        taskClient.setServiceURL( serviceUrl );

        restTemplate = ( RestTemplate )context.getAutowireCapableBeanFactory().getBean( "restTemplate" );
    }


    @Test( groups = { "sliceServiceTest" } )
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
            groups = { "sliceServiceTest" },
            dependsOnMethods = { "testCreateSubspace" }
    )
    public void testCreateSliceKind() {
        try {
            final ResponseEntity< Specifier< Void > > sliceKind =
                    subspaceClient.createSliceKind( subspaceId, sliceKindId, sliceKindConfig, admindn );
            Assert.assertNotNull( sliceKind );
            Assert.assertEquals( sliceKind.getStatusCode(), HttpStatus.CREATED );
        }
        catch( HttpClientErrorException e ) {
            if( ! e.getStatusCode().equals( HttpStatus.PRECONDITION_FAILED ) ) // already exists from last test?
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
            groups = { "sliceServiceTest" },
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
            groups = { "sliceServiceTest" },
            dependsOnMethods = { "testCreateSlice" }
    )
    public void testFileTransfer() throws IOException, NoSuchAlgorithmException, KeyManagementException {
        // TODO: test for nonexistance of sliceFile as initial constraint
        // create tmp testfile
        {
            FileOutputStream testfile = new FileOutputStream( sliceFile );
            ByteArrayInputStream in = new ByteArrayInputStream( sliceFileContent.getBytes() );
            FileCopyUtils.copy( in, testfile );
            testfile.flush();
            testfile.close();
        }

        // upload file
        {
            final ResponseEntity<Integer> responseEntity = sliceClient.setFileContent(
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
                            // This is where he gets the content from.
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
                            // don't need this
                            return null;
                        }

                        @Override
                        public InputStream getInputStream() throws IOException {
                            // don't need this
                            return null;
                        }

                        @Override
                        public void transferTo(File dest) throws IOException, IllegalStateException {
                            // don't need this
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
            final ResponseEntity<Integer> responseEntity = sliceClient.listFileContent(
                    subspaceId,
                    sliceKindId,
                    sliceId,
                    sliceFileName,
                    new LinkedList<String>(),
                    admindn,
                    byteArrayOutputStream );

            Assert.assertNotNull( responseEntity );
            Assert.assertEquals( responseEntity.getStatusCode(), HttpStatus.OK );

            InputStream stream = new FileInputStream( sliceFile );
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            FileCopyUtils.copy( stream, out );
            Assert.assertEquals( byteArrayOutputStream.toByteArray(), out.toByteArray() );
        }

        // delete uploaded file
        {
            final ResponseEntity< Integer > responseEntity =
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
            groups = { "sliceServiceTest" },
            dependsOnMethods = { "testFileTransfer" }
    )
    public void testDeleteSlice() {
        {
            final ResponseEntity< Specifier< Facets > > responseEntity =
                    sliceClient.deleteSlice( subspaceId, sliceKindId, sliceId, admindn );

            Assert.assertNotNull( responseEntity );
            Assert.assertEquals( responseEntity.getStatusCode(), HttpStatus.OK );

            // wait for task to finish
            AbstractTaskFlowExecClient.waitForFinishOrFail(
                    responseEntity.getBody(),
                    taskClient,
                    500,
                    admindn,
                    "DELETESLICEWID" );
        }
        
        // check for nonexistance of slice
        {
            try {
                final ResponseEntity< Facets > responseEntity =
                        sliceClient.listSliceFacets( subspaceId, sliceKindId, sliceId, admindn );

                Assert.assertNotNull( responseEntity );
                Assert.assertEquals( responseEntity.getStatusCode(), HttpStatus.NOT_FOUND );
            }
            catch( HttpClientErrorException e ) {
                Assert.assertEquals( e.getStatusCode(), HttpStatus.NOT_FOUND );
            }
        }
    }


    @Test(
            groups = { "sliceServiceTest" },
            dependsOnMethods = { "testDeleteSlice" }
    )
    public void testDeleteSubspace() {
        final ResponseEntity< Specifier< Facets > > responseEntity = subspaceClient.deleteSubspace( subspaceId, admindn );
        Assert.assertNotNull( responseEntity );
        Assert.assertEquals( responseEntity.getStatusCode(), HttpStatus.OK );

        final TaskClient client = new TaskClient( serviceUrl );
        client.setRestTemplate( restTemplate );

        // wait for task to finish
        TaskStatus taskStatus = AbstractTaskFlowExecClient.waitForFinishOrFail(
                responseEntity.getBody(),
                client,
                100,
                admindn,
                "DELETESUBSPACEWID");
        Assert.assertNotNull( taskStatus );
        Assert.assertEquals( taskStatus.getStatus(), TaskStatus.Status.FINISHED );
        Assert.assertEquals( taskStatus.getMaxProgress(), taskStatus.getProgress() );
        Assert.assertEquals( taskStatus.getStatus(), TaskStatus.Status.FINISHED );
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

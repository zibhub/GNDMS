package de.zib.gndms.common.rest;

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

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.testng.AssertJUnit;
import org.testng.annotations.Test;

/**
 * Tests the gndms response header.
 * 
 * @author Ulrike Golas
 */

public class GNDMSResponseHeaderTest {

	/**
	 * Tests the  getter and setter.
	 */
	@Test
    public final void testGetAndSet() {
		GNDMSResponseHeader respHeader = new GNDMSResponseHeader();

		AssertJUnit.assertNull(respHeader.getResourceURL());
		String resourceUrl = "http://www.zib.de/gndms/grid000";
		String resourceUrl2 = "http://www.zib.de/gndms/grid111";
		respHeader.setResourceURL(resourceUrl);
		List<String> list = respHeader.getResourceURL();
		AssertJUnit.assertTrue(list.contains(resourceUrl));
		AssertJUnit.assertEquals(1, respHeader.getResourceURL().size());
		respHeader.setResourceURL(resourceUrl2);
		AssertJUnit.assertEquals(1, respHeader.getResourceURL().size());
		list = respHeader.getResourceURL();
		AssertJUnit.assertTrue(list.contains(resourceUrl2));
		
		AssertJUnit.assertNull(respHeader.getParentURL());
		String parentUrl = "http://www.zib.de/gndms";
		String parentUrl2 = "http://www.zib.de/gndms2";
		respHeader.setParentURL(parentUrl);
		AssertJUnit.assertEquals(1, respHeader.getParentURL().size());
		list = respHeader.getParentURL();
		AssertJUnit.assertTrue(list.contains(parentUrl));
		respHeader.setParentURL(parentUrl2);
		AssertJUnit.assertEquals(1, respHeader.getParentURL().size());
		list = respHeader.getParentURL();
		AssertJUnit.assertTrue(list.contains(parentUrl2));

		AssertJUnit.assertNull(respHeader.getFacetURL());
		String facetUrl = "http://www.zib.de/gndms/grid000/facet1";
		String facetUrl2 = "http://www.zib.de/gndms/grid000/facet2";
		respHeader.setFacetURL(facetUrl);
		AssertJUnit.assertEquals(1, respHeader.getFacetURL().size());
		list = respHeader.getFacetURL();
		AssertJUnit.assertTrue(list.contains(facetUrl));
		respHeader.setFacetURL(facetUrl2);
		AssertJUnit.assertEquals(1, respHeader.getFacetURL().size());
		list = respHeader.getFacetURL();
		AssertJUnit.assertTrue(list.contains(facetUrl2));

		AssertJUnit.assertNull(respHeader.getDN());
		String dn = "me";
		String dn2 = "me2";
		respHeader.setDN(dn);
		AssertJUnit.assertEquals(1, respHeader.getDN().size());
		list = respHeader.getDN();
		AssertJUnit.assertTrue(list.contains(dn));
		respHeader.setDN(dn2);
		AssertJUnit.assertEquals(1, respHeader.getDN().size());
		list = respHeader.getDN();
		AssertJUnit.assertTrue(list.contains(dn2));

		AssertJUnit.assertNull(respHeader.getWId());
		String wid = "wid1";
		String wid2 = "wid2";
		respHeader.setWId(wid);
		AssertJUnit.assertEquals(1, respHeader.getWId().size());
		list = respHeader.getWId();
		AssertJUnit.assertTrue(list.contains(wid));
		respHeader.setWId(wid2);
		AssertJUnit.assertEquals(1, respHeader.getWId().size());
		list = respHeader.getWId();
		AssertJUnit.assertTrue(list.contains(wid2));
    }


     @Test
    public final void testNormalMyProxyToken() {

         GNDMSResponseHeader head = new GNDMSResponseHeader(  );
         head.addMyProxyToken( "bar", "gibson", "lesPaul" );
         head.addMyProxyToken( "foo", "hello", "world" );
         head.addMyProxyToken( "baz", "ibanez", null );
         head.addMyProxyToken( "foo", "fender", "strat" );

         Map<String, List<String>> myProxyToken = head.getMyProxyToken();
         AssertJUnit.assertEquals( myProxyToken.size(), 3 );

         AssertJUnit.assertTrue( myProxyToken.containsKey( "bar" ) );
         List<String> barVal = myProxyToken.get( "bar" );
         AssertJUnit.assertEquals( barVal.size(), 2 );
         AssertJUnit.assertEquals( barVal.get(0), "gibson" );
         AssertJUnit.assertEquals( barVal.get(1), "lesPaul" );

         AssertJUnit.assertTrue( myProxyToken.containsKey( "baz" ) );
         List<String> bazVal = myProxyToken.get( "baz" );
         AssertJUnit.assertEquals( bazVal.size(), 1 );
         AssertJUnit.assertEquals( bazVal.get(0), "ibanez" );

         AssertJUnit.assertTrue( myProxyToken.containsKey( "foo" ) );
         List<String> fooVal = myProxyToken.get( "foo" );
         AssertJUnit.assertEquals( fooVal.size(), 2 );
         AssertJUnit.assertEquals( fooVal.get(0), "fender" );
         AssertJUnit.assertEquals( fooVal.get(1), "strat" );
    }


    @Test
    public final void testEmptyMyProxyToken() {
        GNDMSResponseHeader head = new GNDMSResponseHeader(  );
        Map<String, List<String>> myProxyToken = head.getMyProxyToken();
        AssertJUnit.assertEquals( myProxyToken.size(), 0 );
    }

    @Test( expectedExceptions = IllegalArgumentException.class )
    public final void testExcMyProxyToken() {
        GNDMSResponseHeader head = new GNDMSResponseHeader(  );
        head.addMyProxyToken( "bar", "   ", "" );
    }

	/**
	 * Tests the constructors.
	 */
	@Test
	public final void testConstructors() {
		GNDMSResponseHeader respHeader = new GNDMSResponseHeader();
		GNDMSResponseHeader respHeader2 = new GNDMSResponseHeader(new HttpHeaders());
		AssertJUnit.assertEquals(respHeader2, respHeader);
		
		String resourceUrl = "";
		String parentUrl = "";
		String facetUrl = "";
		String dn = "me";
		String wid = "wid";
		respHeader = new GNDMSResponseHeader(resourceUrl, facetUrl, parentUrl, dn, wid);
		AssertJUnit.assertEquals(1, respHeader.getResourceURL().size());
		AssertJUnit.assertTrue(respHeader.getResourceURL().contains(resourceUrl));
		AssertJUnit.assertEquals(1, respHeader.getParentURL().size());
		AssertJUnit.assertTrue(respHeader.getParentURL().contains(parentUrl));
		AssertJUnit.assertEquals(1, respHeader.getFacetURL().size());
		AssertJUnit.assertTrue(respHeader.getFacetURL().contains(facetUrl));
		AssertJUnit.assertEquals(1, respHeader.getDN().size());
		AssertJUnit.assertTrue(respHeader.getDN().contains(dn));
		AssertJUnit.assertEquals(1, respHeader.getWId().size());
		AssertJUnit.assertTrue(respHeader.getWId().contains(wid));
		
	}
}

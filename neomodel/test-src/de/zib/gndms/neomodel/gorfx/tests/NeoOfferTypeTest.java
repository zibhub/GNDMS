package de.zib.gndms.neomodel.gorfx.tests;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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

import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.neomodel.common.NeoDao;
import de.zib.gndms.neomodel.common.NeoSession;
import de.zib.gndms.neomodel.gorfx.NeoOfferType;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

import java.util.HashMap;

/**
 * NeoOfferTypeTest
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 * User: stepn Date: 05.09.2008 Time: 14:48:36
 */
public class NeoOfferTypeTest extends NeoTest {

    @Test( groups = { "neo" } )
    public void createOfferType() {
        NeoOfferType ot = session.createOfferType();

        ot.setId("offerTypeNr1");
        ot.setCalculatorFactoryClassName("cfn");
        ot.setTaskActionFactoryClassName("tfn");
        ot.setConfigMapData(new HashMap<String,String>());
        ot.setOfferArgumentType(new ImmutableScopedName("a", "b"));
        ot.setOfferResultType(new ImmutableScopedName("x", "z"));

        session.success();
    }

    @Test( groups = { "neo" }, dependsOnMethods = { "createOfferType" } )
    public void findOfferType() {
        NeoOfferType ot = session.findOfferType("offerTypeNr1");

        assertEquals(ot.getId(), "offerTypeNr1");
        assertEquals(ot.getCalculatorFactoryClassName(), "cfn");
        assertEquals(ot.getTaskActionFactoryClassName(), "tfn");
        assertEquals(ot.getConfigMapData(), new HashMap<String, String>());
        assertTrue(ot.getOfferArgumentType().equalFields(new ImmutableScopedName("a", "b")));
        assertTrue(ot.getOfferResultType().equalFields(new ImmutableScopedName("x", "z")));

        ot.delete(session);

        session.success();
    }
}

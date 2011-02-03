package de.zib.gndms.neomodel.gorfx.tests;

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
 * Created by IntelliJ IDEA.
 * User: stepn
 * Date: 03.02.11
 * Time: 17:07
 * To change this template use File | Settings | File Templates.
 */
public class NeoOfferTypeTest {

    @Test( groups = { "neo" } )
    public void createOfferType() {
        GraphDatabaseService gdb = new EmbeddedGraphDatabase("/tmp/neo");
        NeoDao dao = new NeoDao("gndms", gdb);
        NeoSession session = dao.beginSession();
        try {
            NeoOfferType ot = session.createOfferType();

            ot.setId("offerTypeNr1");
            ot.setCalculatorFactoryClassName("cfn");
            ot.setTaskActionFactoryClassName("tfn");
            ot.setConfigMapData(new HashMap<String,String>());
            ot.setOfferArgumentType(new ImmutableScopedName("a", "b"));
            ot.setOfferResultType(new ImmutableScopedName("x", "z"));

            session.success();
        }
        finally {
            session.finish();
            gdb.shutdown();
        }
    }

    @Test( groups = { "neo" }, dependsOnMethods = { "createOfferType" } )
    public void findOfferType() {
        GraphDatabaseService gdb = new EmbeddedGraphDatabase("/tmp/neo");
        NeoDao dao = new NeoDao("gndms", gdb);
        NeoSession session = dao.beginSession();
        try {
            NeoOfferType ot = session.findOfferType("offerTypeNr1");

            assertEquals(ot.getId(), "offerTypeNr1");
            assertEquals(ot.getCalculatorFactoryClassName(), "cfn");
            assertEquals(ot.getTaskActionFactoryClassName(), "tfn");
            assertEquals(ot.getConfigMapData(), new HashMap<String, String>());
            assertTrue(ot.getOfferArgumentType().equalFields(new ImmutableScopedName("a", "b")));
            assertTrue(ot.getOfferResultType().equalFields(new ImmutableScopedName("x", "z")));

            ot.delete();
            session.success();
        }
        finally {
            session.finish();
            gdb.shutdown();
        }
    }
}

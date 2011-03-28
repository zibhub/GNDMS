package de.zib.gndms.kit.config;

import de.zib.gndms.stuff.confuror.ConfigEditor;
import de.zib.gndms.stuff.confuror.ConfigHolder;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;

/**
 * TreeConfigTest
 *
 * @see TreeConfig
 *
 * @author try ste fan pla nti kow zib
 *         <p/>
 *         User stepn Date: 28.03.11 TIME: 14:41
 */
public class TreeConfigTest {
    private volatile TreeConfig cfg;

    private final ObjectMapper mapper = new ObjectMapper();
    private final JsonFactory factory = mapper.getJsonFactory();

    @Test
    public void testEmpty() throws IOException, ConfigEditor.UpdateRejectedException {
        cfg = buildTreeConfig("{ }");
        Assert.assertFalse(cfg.iterator().hasNext());
        cfg = buildTreeConfig("[ ]");
        final Iterator<String> iterator = cfg.iterator();
        Assert.assertEquals(iterator.next(), "count");
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testSingleObject() throws IOException, ConfigEditor.UpdateRejectedException {
        cfg = buildTreeConfig("{ 'a': 7 }");
        final Iterator<String> iterator = cfg.iterator();
        Assert.assertEquals(iterator.next(), "a");
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testSingleArray() throws IOException, ConfigEditor.UpdateRejectedException {
        cfg = buildTreeConfig("[ 12, 13 ]");
        final Iterator<String> iterator = cfg.iterator();
        Assert.assertEquals(iterator.next(), "count");
        Assert.assertEquals(iterator.next(), "0");
        Assert.assertEquals(iterator.next(), "1");
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testNestedObject() throws IOException, ConfigEditor.UpdateRejectedException {
        cfg = buildTreeConfig("{ 'a': 7, 'b' : { 'a': 4, 'c': [1, 2, 3] } }");
        final Iterator<String> iterator = cfg.iterator();
        Assert.assertEquals(iterator.next(), "a");
        Assert.assertEquals(iterator.next(), "b.a");
        Assert.assertEquals(iterator.next(), "b.c.count");
        Assert.assertEquals(iterator.next(), "b.c.0");
        Assert.assertEquals(iterator.next(), "b.c.1");
        Assert.assertEquals(iterator.next(), "b.c.2");
        Assert.assertFalse(iterator.hasNext());
        Assert.assertEquals(cfg.getConcreteNonMandatoryOption("a"), "7");
        Assert.assertEquals(cfg.getConcreteNonMandatoryOption("b.a"), "4");
        Assert.assertEquals(cfg.getConcreteNonMandatoryOption("b.c.count"), "3");
        Assert.assertEquals(cfg.getConcreteNonMandatoryOption("b.c.0"), "1");
        Assert.assertEquals(cfg.getConcreteNonMandatoryOption("b.c.1"), "2");
        Assert.assertEquals(cfg.getConcreteNonMandatoryOption("b.c.2"), "3");
    }


    @Test
    public void testNestedArray() throws IOException, ConfigEditor.UpdateRejectedException {
        cfg = buildTreeConfig("[ 'a', 7, 'b', { 'a': 4, 'c': [1, 2, 3] } ]");
        final Iterator<String> iterator = cfg.iterator();
        Assert.assertEquals(iterator.next(), "count");
        Assert.assertEquals(iterator.next(), "0");
        Assert.assertEquals(iterator.next(), "1");
        Assert.assertEquals(iterator.next(), "2");
        Assert.assertEquals(iterator.next(), "3.a");
        Assert.assertEquals(iterator.next(), "3.c.count");
        Assert.assertEquals(iterator.next(), "3.c.0");
        Assert.assertEquals(iterator.next(), "3.c.1");
        Assert.assertEquals(iterator.next(), "3.c.2");
        Assert.assertFalse(iterator.hasNext());
        Assert.assertEquals(cfg.getConcreteNonMandatoryOption("count"), "4");
        Assert.assertEquals(cfg.getConcreteNonMandatoryOption("0"), "a");
        Assert.assertEquals(cfg.getConcreteNonMandatoryOption("1"), "7");
        Assert.assertEquals(cfg.getConcreteNonMandatoryOption("2"), "b");
        Assert.assertEquals(cfg.getConcreteNonMandatoryOption("3.a"), "4");
        Assert.assertEquals(cfg.getConcreteNonMandatoryOption("3.c.count"), "3");
        Assert.assertEquals(cfg.getConcreteNonMandatoryOption("3.c.0"), "1");
        Assert.assertEquals(cfg.getConcreteNonMandatoryOption("3.c.1"), "2");
        Assert.assertEquals(cfg.getConcreteNonMandatoryOption("3.c.2"), "3");
    }

    @Test
    public void testValues() throws IOException, ConfigEditor.UpdateRejectedException, MandatoryOptionMissingException {
        cfg = buildTreeConfig("[ 'true', true, null, '%{HOME}' ]");
        Assert.assertEquals(cfg.isBooleanOptionSet("0"), true);
        Assert.assertEquals(cfg.isBooleanOptionSet("1"), true);
        Assert.assertEquals(cfg.hasOption("2"), false);
        Assert.assertEquals(cfg.hasOption("10"), false);
        Assert.assertEquals(cfg.hasOption("-3"), false);
        Assert.assertTrue(cfg.getOption("3").length() > 0);

        boolean throwsUp;
        try {
            cfg.getIntOption("2");
            throwsUp = false;
        }
        catch (MandatoryOptionMissingException mom) {
            throwsUp = true;
        }
        Assert.assertTrue(throwsUp);
    }

    @Test
    public void testDynArray() throws IOException, ConfigEditor.UpdateRejectedException, MandatoryOptionMissingException {
        cfg = buildTreeConfig("[ 'true', true, null, '%{HOME}' ]");
        Assert.assertEquals(cfg.dynArraySize(), 4);
        final Iterator<String> iterator = cfg.dynArrayKeys();
        Assert.assertEquals(iterator.next(), "0");
        Assert.assertEquals(iterator.next(), "1");
        Assert.assertEquals(iterator.next(), "2");
        Assert.assertEquals(iterator.next(), "3");
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testNestedDynArray()
            throws IOException, ConfigEditor.UpdateRejectedException, MandatoryOptionMissingException, ParseException {
        cfg = buildTreeConfig("{ 'a': '12', 'b': [ 'true', true, null, '%{HOME}' ], 'c': 4  }");
        final ConfigProvider cfgProvider = cfg.getDynArrayOption("b");
        Assert.assertEquals(cfgProvider.dynArraySize(), 4);
        final Iterator<String> iterator = cfgProvider.dynArrayKeys();
        Assert.assertEquals(iterator.next(), "0");
        Assert.assertEquals(iterator.next(), "1");
        Assert.assertEquals(iterator.next(), "2");
        Assert.assertEquals(iterator.next(), "3");
        Assert.assertFalse(iterator.hasNext());
    }

    private @NotNull TreeConfig buildTreeConfig(@NotNull String json)
            throws ConfigEditor.UpdateRejectedException, IOException {
        final ConfigHolder holder = new ConfigHolder(mapper, ConfigHolder.parseSingle(factory, json));
        final JsonNode snapshot = holder.getSnapshotAsNode();
        return new TreeConfig(snapshot);
    }
}

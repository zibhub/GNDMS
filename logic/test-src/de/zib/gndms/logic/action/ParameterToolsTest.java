package de.zib.gndms.logic.action;

import static de.zib.gndms.logic.model.config.ParameterTools.*;
import org.testng.Assert;
import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for ConfigurationAction.ParameterTools
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 14.08.2008 Time: 12:50:50
 */
@SuppressWarnings({
        "FeatureEnvy", "OverlyLongMethod", "InstanceMethodNamingConvention",
        "HardcodedFileSeparator" })
public class ParameterToolsTest {
    Map<String, String> paraMap = new HashMap<String, String>(8);

    @Test(groups = {"factory"})
    public void toStrTest() {
        paraMap.clear();
        assertEquals(asString(paraMap, null), "");

        paraMap.put("test1", "val1");
        assertEquals(asString(paraMap, null), "test1: 'val1'");

        paraMap.put("test2", "val2");
        final String strVal = asString(paraMap, null);
        Assert.assertTrue(
                "test1: 'val1'; test2: 'val2'".equals(strVal)
                          || "test2: 'val2'; test1: 'val1'".equals(strVal));
    }


    @Test(groups = {"factory"})
    public void positiveParseTest() throws ParameterParseException {
        paraMap.clear();

        parseParameters(paraMap, "", null);

        paraMap.clear();
        parseParameters(paraMap, "foo: bar", null);
        assertEquals(paraMap.get("foo"), "bar");

        paraMap.clear();
        parseParameters(paraMap, " foo:bar", null);
        assertEquals(paraMap.get("foo"), "bar");

        paraMap.clear();
        parseParameters(paraMap, "foo: bar;", null);
        assertEquals(paraMap.get("foo"), "bar");

        paraMap.clear();
        parseParameters(paraMap, "  foo: bar  ", null);
        assertEquals(paraMap.get("foo"), "bar");

        paraMap.clear();
        parseParameters(paraMap, "foo: bar ;", null);
        assertEquals(paraMap.get("foo"), "bar");

        paraMap.clear();
        parseParameters(paraMap, "  foo: bar ; ", null);
        assertEquals(paraMap.get("foo"), "bar");

        paraMap.clear();
        parseParameters(paraMap, "  foo: 'bar'", null);
        assertEquals(paraMap.get("foo"), "bar");

        paraMap.clear();
        parseParameters(paraMap, "blorp: blurp;  foo: bar ; ", null);
        assertEquals(paraMap.get("foo"), "bar");
        assertEquals(paraMap.get("blorp"), "blurp");

        paraMap.clear();
        parseParameters(paraMap, "blorp: blurp;  foo: bar", null);
        assertEquals(paraMap.get("foo"), "bar");
        assertEquals(paraMap.get("blorp"), "blurp");

        paraMap.clear();
        parseParameters(paraMap, "blorp:'blurp ' ;  foo: bar", null);
        assertEquals(paraMap.get("foo"), "bar");
        assertEquals(paraMap.get("blorp"), "blurp ");

        paraMap.clear();
        parseParameters(paraMap, "blorp:blurp;  foo:' bar';", null);
        assertEquals(paraMap.get("foo"), " bar");
        assertEquals(paraMap.get("blorp"), "blurp");
    }


    @SuppressWarnings({ "HardcodedFileSeparator" })
    @Test(groups = {"factory"})
    public void positiveParseEscapeTest() throws ParameterParseException {
        paraMap.clear();
        parseParameters(paraMap, "", null);
        assert paraMap.isEmpty();

        paraMap.clear();
        parseParameters(paraMap, "foo: ''", null);
        assertEquals(paraMap.get("foo"), "");

        paraMap.clear();
        parseParameters(paraMap, "foo= ':'", null);
        assertEquals(paraMap.get("foo"), ":");

        paraMap.clear();
        parseParameters(paraMap, "foo:", null);
        assertEquals(paraMap.get("foo"), "");

        paraMap.clear();
        parseParameters(paraMap, "foo: 'bar'", null);
        assertEquals(paraMap.get("foo"), "bar");

        paraMap.clear();
        parseParameters(paraMap, "foo: \"bar\"", null);
        assertEquals(paraMap.get("foo"), "bar");

        paraMap.clear();
        parseParameters(paraMap, "foo: b\\ar", null);
        assertEquals(paraMap.get("foo"), "bar");

        paraMap.clear();
        parseParameters(paraMap, "foo: b\\ar\\;;", null);
        assertEquals(paraMap.get("foo"), "bar;");

        paraMap.clear();
        parseParameters(paraMap, "foo:'b\\ar';", null);
        assertEquals(paraMap.get("foo"), "bar");

        paraMap.clear();
        parseParameters(paraMap, "foo:'b\\;r';", null);
        assertEquals(paraMap.get("foo"), "b;r");

        paraMap.clear();
        parseParameters(paraMap, "foo:'b\\;r\\'';", null);
        assertEquals(paraMap.get("foo"), "b;r'");


        paraMap.clear();
        parseParameters(paraMap, "foo:'bar\\\\';", null);
        assertEquals(paraMap.get("foo"), "bar\\");

        paraMap.clear();
        parseParameters(paraMap, "foo; bar: baz;", null);
        assertEquals(paraMap.get("foo"), "true");

        paraMap.clear();
        parseParameters(paraMap, "!foo; bar: baz;", null);
        assertEquals(paraMap.get("foo"), "false");

        paraMap.clear();
        parseParameters(paraMap, "foo", null);
        assertEquals(paraMap.get("foo"), "true");

        paraMap.clear();
        parseParameters(paraMap, "!foo", null);
        assertEquals(paraMap.get("foo"), "false", null);

        paraMap.clear();
        parseParameters(paraMap, "-foo", null);
        assertEquals(paraMap.get("foo"), "false", null);

        paraMap.clear();
        parseParameters(paraMap, "+foo", null);
        assertEquals(paraMap.get("foo"), "true", null);
    }

    @Test(groups = {"factory"},
          expectedExceptions = { ParameterParseException.class})
    public void negativeWsInKeyTest1() throws ParameterParseException {
        paraMap.clear();
        parseParameters(paraMap, "foo : ", null);
    }

    @Test(groups = {"factory"},
          expectedExceptions = { ParameterParseException.class})
    public void negativeWsInKeyTest2() throws ParameterParseException {
        paraMap.clear();
        parseParameters(paraMap, "fo o:", null);
    }

    @Test(groups = {"factory"},
          expectedExceptions = { ParameterParseException.class})
    public void negativeUncompleteTest1() throws ParameterParseException {
        paraMap.clear();
        parseParameters(paraMap, "foo: 'bar", null);
    }

    @Test(groups = {"factory"},
          expectedExceptions = { ParameterParseException.class})
    public void negativeEmptyKeyTest1() throws ParameterParseException {
        paraMap.clear();
        parseParameters(paraMap, ":", null);
    }

    @Test(groups = {"factory"},
          expectedExceptions = { ParameterParseException.class})
    public void negativeEmptyKeyTest2() throws ParameterParseException {
        paraMap.clear();
        parseParameters(paraMap, ":''", null);
    }

    @Test(groups = {"factory"},
          expectedExceptions = { ParameterParseException.class})
    public void negativeUnfinishedEscapeTest1() throws ParameterParseException {
        paraMap.clear();
        parseParameters(paraMap, ":'\\", null);
    }

    @Test(groups = {"factory"},
          expectedExceptions = { ParameterParseException.class})
    public void negativeUnfinishedEscapeTest2() throws ParameterParseException {
        paraMap.clear();
        parseParameters(paraMap, ": \\", null);
    }


    @Test(groups = {"factory"},
          expectedExceptions = { ParameterParseException.class})
    public void negativeDuplicateKey() throws ParameterParseException {
        paraMap.clear();
        parseParameters(paraMap, "foo:bar; foo:", null);
    }
}

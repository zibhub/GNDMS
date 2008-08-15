package de.zib.gndms.logic.action;

import static de.zib.gndms.logic.action.CommandAction.ParameterTools.*;
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

    @Test(groups = {"util"})
    public void toStrTest() {
        paraMap.clear();
        assertEquals(asString(paraMap), "");

        paraMap.put("test1", "val1");
        assertEquals(asString(paraMap), "test1: 'val1'");

        paraMap.put("test2", "val2");
        final String strVal = asString(paraMap);
        Assert.assertTrue(
                "test1: 'val1'; test2: 'val2'".equals(strVal)
                          || "test2: 'val2'; test1: 'val1'".equals(strVal));
    }

    @Test(groups = {"util"})
    public void positiveParseTest() throws ParameterParseException {
        paraMap.clear();

        parseParameters(paraMap, "");

        paraMap.clear();
        parseParameters(paraMap, "foo: bar");
        assertEquals(paraMap.get("foo"), "bar");

        paraMap.clear();
        parseParameters(paraMap, " foo:bar");
        assertEquals(paraMap.get("foo"), "bar");

        paraMap.clear();
        parseParameters(paraMap, "foo: bar;");
        assertEquals(paraMap.get("foo"), "bar");

        paraMap.clear();
        parseParameters(paraMap, "  foo: bar  ");
        assertEquals(paraMap.get("foo"), "bar");

        paraMap.clear();
        parseParameters(paraMap, "foo: bar ;");
        assertEquals(paraMap.get("foo"), "bar");

        paraMap.clear();
        parseParameters(paraMap, "  foo: bar ; ");
        assertEquals(paraMap.get("foo"), "bar");

        paraMap.clear();
        parseParameters(paraMap, "  foo: 'bar'");
        assertEquals(paraMap.get("foo"), "bar");

        paraMap.clear();
        parseParameters(paraMap, "blorp: blurp;  foo: bar ; ");
        assertEquals(paraMap.get("foo"), "bar");
        assertEquals(paraMap.get("blorp"), "blurp");

        paraMap.clear();
        parseParameters(paraMap, "blorp: blurp;  foo: bar");
        assertEquals(paraMap.get("foo"), "bar");
        assertEquals(paraMap.get("blorp"), "blurp");

        paraMap.clear();
        parseParameters(paraMap, "blorp:'blurp ' ;  foo: bar");
        assertEquals(paraMap.get("foo"), "bar");
        assertEquals(paraMap.get("blorp"), "blurp ");

        paraMap.clear();
        parseParameters(paraMap, "blorp:blurp;  foo:' bar';");
        assertEquals(paraMap.get("foo"), " bar");
        assertEquals(paraMap.get("blorp"), "blurp");
    }


    @SuppressWarnings({ "HardcodedFileSeparator" })
    @Test(groups = {"util"})
    public void positiveParseEscapeTest() throws ParameterParseException {
        paraMap.clear();
        parseParameters(paraMap, "");
        assert paraMap.isEmpty();

        paraMap.clear();
        parseParameters(paraMap, "foo: ''");
        assertEquals(paraMap.get("foo"), "");

        paraMap.clear();
        parseParameters(paraMap, "foo= ':'");
        assertEquals(paraMap.get("foo"), ":");

        paraMap.clear();
        parseParameters(paraMap, "foo:");
        assertEquals(paraMap.get("foo"), "");

        paraMap.clear();
        parseParameters(paraMap, "foo: b\\ar");
        assertEquals(paraMap.get("foo"), "bar");

        paraMap.clear();
        parseParameters(paraMap, "foo: b\\ar\\;;");
        assertEquals(paraMap.get("foo"), "bar;");

        paraMap.clear();
        parseParameters(paraMap, "foo:'b\\ar';");
        assertEquals(paraMap.get("foo"), "bar");

        paraMap.clear();
        parseParameters(paraMap, "foo:'b\\;r';");
        assertEquals(paraMap.get("foo"), "b;r");

        paraMap.clear();
        parseParameters(paraMap, "foo:'b\\;r\\'';");
        assertEquals(paraMap.get("foo"), "b;r'");


        paraMap.clear();
        parseParameters(paraMap, "foo:'bar\\\\';");
        assertEquals(paraMap.get("foo"), "bar\\");
    }

    @Test(groups = {"util"},
          expectedExceptions = { ParameterParseException.class})
    public void negativeWsInKeyTest1() throws ParameterParseException {
        paraMap.clear();
        parseParameters(paraMap, "foo : ");
    }

    @Test(groups = {"util"},
          expectedExceptions = { ParameterParseException.class})
    public void negativeWsInKeyTest2() throws ParameterParseException {
        paraMap.clear();
        parseParameters(paraMap, "fo o:");
    }

    @Test(groups = {"util"},
          expectedExceptions = { ParameterParseException.class})
    public void negativeUncompleteTest1() throws ParameterParseException {
        paraMap.clear();
        parseParameters(paraMap, "foo: 'bar");
    }

    @Test(groups = {"util"},
          expectedExceptions = { ParameterParseException.class})
    public void negativeUncompleteTest2() throws ParameterParseException {
        paraMap.clear();
        parseParameters(paraMap, "foo: bar; baz");
    }

    @Test(groups = {"util"},
          expectedExceptions = { ParameterParseException.class})
    public void negativeEmptyKeyTest1() throws ParameterParseException {
        paraMap.clear();
        parseParameters(paraMap, ":");
    }

    @Test(groups = {"util"},
          expectedExceptions = { ParameterParseException.class})
    public void negativeEmptyKeyTest2() throws ParameterParseException {
        paraMap.clear();
        parseParameters(paraMap, ":''");
    }

    @Test(groups = {"util"},
          expectedExceptions = { ParameterParseException.class})
    public void negativeUnfinishedEscapeTest1() throws ParameterParseException {
        paraMap.clear();
        parseParameters(paraMap, ":'\\");
    }

    @Test(groups = {"util"},
          expectedExceptions = { ParameterParseException.class})
    public void negativeUnfinishedEscapeTest2() throws ParameterParseException {
        paraMap.clear();
        parseParameters(paraMap, ": \\");
    }


    @Test(groups = {"util"},
          expectedExceptions = { ParameterParseException.class})
    public void negativeDuplicateKey() throws ParameterParseException {
        paraMap.clear();
        parseParameters(paraMap, "foo:bar; foo:");
    }
    @Test(groups = {"util"},
          expectedExceptions = { ParameterParseException.class})
    public void negativeProblem() throws ParameterParseException {
        paraMap.clear();
        parseParameters(paraMap, "scope:'http://www.c3grid.de/G2';name:Staging;size:10;path:/tmp");
    }
}

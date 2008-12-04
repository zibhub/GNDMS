package de.zib.gndms.kit.util;

import org.apache.log4j.MDC;
import org.apache.log4j.NDC;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 22.10.2008 Time: 14:31:50
 */
public class WidAux {
    private WidAux() {}


    public static void removeWid() {
        MDC.remove("c3wid");
        NDC.pop();
    }


    public static String getWid() {
        return (String) MDC.get("c3wid");
    }


    public static void initWid(final String cachedWid) {
        if (cachedWid == null)
            return;
        else {
            MDC.put("c3wid", cachedWid);
            NDC.push("c3wid:" + cachedWid);
        }
    }
}

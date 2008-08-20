package de.zib.gndms.logic.util;

import de.zib.gndms.model.common.ModelUUIDGen;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


/**
 * ModelUUIDGen that uses java's UUID class.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 18.08.2008 Time: 12:21:34
 */
public class SimpleModelUUIDGen implements ModelUUIDGen {
    private static final ModelUUIDGen gen = new SimpleModelUUIDGen();

    private SimpleModelUUIDGen() { super(); }

    public static ModelUUIDGen getInstance() {
        return gen;
    }

    @NotNull
    public String nextUUID() {
        return UUID.randomUUID().toString();
    }
}

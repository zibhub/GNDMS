package de.zib.gndms.infra.util;

import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.model.gorfx.OfferType;
import de.zib.gndms.model.gorfx.types.AbstractORQCalculator;
import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 05.09.2008 Time: 18:00:40
 */
public class InstanceFactory<T extends AbstractORQCalculator<?>> extends AbstractFactory<T> {
    private final Class<T> clazz;


    public InstanceFactory(final GNDMSystem sysParam, final OfferType ot) {
        super(sysParam, ot);
        try {
            clazz = (Class<T>) Class.forName(ot.getCalculatorClass());
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public Class<T> getClazz() {
        return clazz;
    }


    public @NotNull T getInstance() throws IllegalAccessException, InstantiationException {
        final T instance = clazz.newInstance();
        instance.setFactory(this);
        return instance;
    }
}

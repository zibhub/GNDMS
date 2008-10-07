package de.zib.gndms.kit.factory;

import de.zib.gndms.model.gorfx.OfferType;
import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 05.09.2008 Time: 18:00:40
 */
public class InstanceFactory<T extends FactoryInstance<T>> extends AbstractFactory<T> {
    private final Class<T> clazz;


    public InstanceFactory(final OfferType ot) {
        super(ot);
        try {
            clazz = (Class<T>) Class.forName(ot.getCalculatorClassName());
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


    public void setup() {
    }
}

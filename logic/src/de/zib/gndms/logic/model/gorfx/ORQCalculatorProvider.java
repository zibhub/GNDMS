package de.zib.gndms.logic.model.gorfx;

import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManagerFactory;
import java.lang.reflect.InvocationTargetException;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 14.11.2008, Time: 11:08:56
 */
public interface ORQCalculatorProvider {

    AbstractORQCalculator<?,?> getORQCalculator(
        @NotNull EntityManagerFactory emf,
        @NotNull String offerTypeKey)
        throws ClassNotFoundException, IllegalAccessException, InstantiationException,
        NoSuchMethodException, InvocationTargetException;
}

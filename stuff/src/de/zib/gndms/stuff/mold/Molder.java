package de.zib.gndms.stuff.mold;

import org.jetbrains.annotations.NotNull;


/**
 * A Molder can be used to copy parts of an instance, meaning just until a specific subclass.
 *<p>
 * For example we have a class {@code B} extending {@code A} and an object {@code obj}, belonging to class B, to be copied.<br>
 * If we create a {@code Molder<A> molder} out of {@code obj} (see {@link Molding}),
 * and a new instance {@code A inst=new A();}<br>
 * then invoking {@code molder.mold(ins)} delivers {@code ins.equals((A)obj)} being true.
 * </p>
 * @author Stefan Plantikow<plantikow@zib.de>
 *
 * @version $Id$
 *
 *          User: stepn Date: 01.12.2008 Time: 11:24:18
 */
public interface Molder<V> {

    /**
     * Molds an instance, see description above.
     * @param molded the instance to be 'molded'
     */
    void mold(@NotNull V molded);
}

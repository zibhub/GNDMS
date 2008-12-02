package de.zib.gndms.stuff.copy;

import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 01.12.2008 Time: 11:11:48
 */
public class Bar extends Foo {
	@SuppressWarnings({"MethodOverloadsMethodOfSuperclass" })
	public void mold(final @NotNull Bar instance) {
		super.mold(instance);
		System.out.println("Bar-Mold!");
	}
}

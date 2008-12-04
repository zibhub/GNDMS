package de.zib.gndms.stuff.copy;

import org.jetbrains.annotations.NotNull;
import de.zib.gndms.stuff.mold.Molding;
import de.zib.gndms.stuff.mold.Molder;
import de.zib.gndms.stuff.mold.Mold;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 01.12.2008 Time: 11:11:43
 */
public class Foo implements Molding {
	@SuppressWarnings({ "MethodMayBeStatic" })
	public void mold(Foo instance) {
		System.out.println("Foo-Mold!");
	}

	@SuppressWarnings({ "unchecked", "RawUseOfParameterizedType" })
	public <D> Molder<D> molder(@NotNull final Class<D> moldedClazz) {
		return Mold.newMolderProxy( (Class) getClass(), this, moldedClazz);
	}

	public static void main(String[] args) {
		Bar bar1 = new Bar();
		Bar bar2 = new Bar();
		Foo foo = new Foo();

		Molder<Bar> molder = bar2.molder(Bar.class);
		molder.mold(bar1);
		bar2.molder(Foo.class).mold(foo);
		bar2.molder(Foo.class).mold(bar1);
	}
}

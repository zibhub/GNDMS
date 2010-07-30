package de.zib.gndms.stuff.copy;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



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

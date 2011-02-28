package de.zib.gndms.stuff.mold;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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


/**
 * A Molder can be used to copy parts of an instance, meaning just until a specific subclass.
 *<p>
 * For example we have a class {@code B} extending {@code A} and an object {@code obj}, belonging to class B, to be copied.<br>
 * If we create a {@code Molder<A> molder} out of {@code obj} (see {@link Molding}),
 * and a new instance {@code A inst=new A();}<br>
 * then invoking {@code molder.mold(ins)} delivers {@code ins.equals((A)obj)} being true.
 * </p>
 * @author  try ste fan pla nti kow zib
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

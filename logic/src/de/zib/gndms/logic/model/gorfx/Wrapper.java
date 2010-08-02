package de.zib.gndms.logic.model.gorfx;

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



/**
 * An Interface for an Wrapper.
 *
 * @author: try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 17.11.2008 Time: 16:36:32
 */
public interface Wrapper<T> {
    /**
     * Wraps {@code wrapped} to an instance of class {@code Y}.
     *
     * @param wrapClass
     * @param wrapped
     * @param <X>
     * @param <Y>
     * @return
     */
	<X extends T, Y extends T> Y wrap(Class<Y> wrapClass, X wrapped);
}

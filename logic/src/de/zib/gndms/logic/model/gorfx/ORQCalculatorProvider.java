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



import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManagerFactory;
import java.lang.reflect.InvocationTargetException;

/**
 * @author: try ma ik jo rr a zib
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 14.11.2008, Time: 11:08:56
 */
public interface ORQCalculatorProvider {

    @SuppressWarnings({ "MethodWithTooExceptionsDeclared" })
    @NotNull AbstractORQCalculator<?,?> newORQCalculator(
        @NotNull EntityManagerFactory emf,
        @NotNull String offerTypeKey)
        throws ClassNotFoundException, IllegalAccessException, InstantiationException,
        NoSuchMethodException, InvocationTargetException;
}

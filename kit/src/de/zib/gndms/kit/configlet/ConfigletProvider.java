package de.zib.gndms.kit.configlet;

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
 *
 * An Interface to provide Configlets
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 11.11.2008, Time: 18:24:02
 */
public interface ConfigletProvider {
    /**
     * Returns a specific {@code Configlet}
     *
     * @param clazz the class the Configlet belongs to
     * @param name the name of the Configlet
     * @return
     */
    <T extends Configlet> T getConfiglet(@NotNull Class<T> clazz, @NotNull String name);
}

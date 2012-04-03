/**
 * Copyright 2008-2012 Zuse Institute Berlin (ZIB)
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
 *
 */

package de.zib.gndms.gndmc.dspace.test;

import javax.persistence.MappedSuperclass;

/**
 * @date: 30.01.12
 * @time: 19:22
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
//@Entity
//@Table( name = "B", schema = "dspace" )
@MappedSuperclass
public abstract class B extends A {
    public int y;

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}

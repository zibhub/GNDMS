/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package de.zib.gndms.gndmc.dspace.test;

import javax.persistence.*;

/**
* Created by IntelliJ IDEA.
* User: explicit
* Date: 02.12.11
* Time: 17:36
* To change this template use File | Settings | File Templates.
*/
@Entity( name = "ENTITY" )
@Table( name = "TESTTABLE", schema = "dspace" )
public class TestTable {
    private String a;
    private String b;

    private int version;

    @Id @Column( length=10, columnDefinition="CHAR" )
    public void setA(String a) {
        this.a = a;
    }

    public String getA( ) {
        return a;
    }

    public void setVersion( int v ) {
        version = v;
    }

    @Version
    public int getVersion( ) {
        return version;
    }

}

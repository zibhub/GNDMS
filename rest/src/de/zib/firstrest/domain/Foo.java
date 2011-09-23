package de.zib.firstrest.domain;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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
 * @author try ma ik jo rr a zib
 * @version $Id$
 *          <p/>
 *          Date: 21.12.2010, Time: 11:05:16
 */
@XStreamAlias( "foo" )
public class Foo {

    String id;
    String value;


    public Foo() {
    }


    public Foo( String id, String value ) {
        this.id = id;
        this.value = value;
    }


    public String getId() {
        return id;
    }


    public void setId( String id ) {
        this.id = id;
    }


    public String getValue() {
        return value;
    }


    public void setValue( String value ) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "Foo{" +
            "id='" + id + '\'' +
            ", value='" + value + '\'' +
            '}';
    }
}

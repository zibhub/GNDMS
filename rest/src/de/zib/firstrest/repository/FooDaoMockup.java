package de.zib.firstrest.repository;

import de.zib.firstrest.domain.Foo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 *          Date: 21.12.2010, Time: 11:39:56
 */
public class FooDaoMockup implements FooDao {

    private HashMap<String, Foo> muchFoo;


    public Foo getFoo( String id ) {
        return muchFoo.get( id );
    }


    public void putFoo( Foo foo ) {
        muchFoo.put( foo.getId(), foo );
    }

    public List<Foo> getAllFoo() {
        return new ArrayList<Foo>( muchFoo.values() );
    }


    public void setMuchFoo( HashMap<String, Foo> muchFoo ) {
        this.muchFoo = muchFoo;
    }
}

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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

package de.zib.firstrest.service;

import de.zib.firstrest.domain.Foo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.List;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 26.07.11  15:22
 * @brief
 */
public interface FooService {

    ResponseEntity<Foo> findFoo( String fid );

    ResponseEntity<List<Foo>> allFoo();

    View savePerson( Foo foo );
}

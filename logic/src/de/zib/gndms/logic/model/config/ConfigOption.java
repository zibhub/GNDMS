package de.zib.gndms.logic.model.config;

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



import java.lang.annotation.*;


/**
 * This annotation provides a description and an alternative name for object fields.
 *
 * @author: try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 18.08.2008 Time: 16:41:05
 */
@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD)
@Documented
public @interface ConfigOption {    
   String descr() default "";
   String altName() default "";
}

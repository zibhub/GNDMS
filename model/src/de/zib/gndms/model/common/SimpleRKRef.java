package de.zib.gndms.model.common;

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



import javax.persistence.Embeddable;
import javax.xml.namespace.QName;

/**
 * VEPRefs for grid resources that use a SimpleResourceKey with a UUID string value
 * (i.e. resource instances from introduce-generated services)
 * 
 * @author  try ste fan pla nti kow zib
 * @version $Id$ 
 *
 * User: stepn Date: 30.07.2008 Time: 15:03:31
 */
@Embeddable
public abstract class SimpleRKRef extends VEPRef {

    public abstract QName getResourceKeyName();

    public abstract String getResourceKeyValue();
    public abstract void setResourceKeyValue(final String newValue);
}


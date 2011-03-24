package de.zib.gndms.infra.system;

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
 * A simple box that holds a GNDMSystem instance
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 25.07.2008 Time: 13:01:27
 */
@SuppressWarnings({"ClassReferencesSubclass"})
public interface SystemHolder {
	@NotNull
	GNDMSystem getSystem() throws IllegalStateException;

	void setSystem(@NotNull GNDMSystem system) throws IllegalStateException;
}

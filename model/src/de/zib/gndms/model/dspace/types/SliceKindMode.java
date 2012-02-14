package de.zib.gndms.model.dspace.types;

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



/**
 * Slicekind modes
 * 
 * @author  try ste fan pla nti kow zib
 * @version $Id$ 
 *
 * User: stepn Date: 01.08.2008 Time: 16:46:03
 */
enum SliceKindMode {
    /* 8 Letters at most */
    RO(true), RW(false);

    SliceKindMode (boolean isReadOnly) {
        readOnly = isReadOnly;
    }

    private final boolean readOnly;


    public boolean isReadOnly() {

        return readOnly;
    }
}

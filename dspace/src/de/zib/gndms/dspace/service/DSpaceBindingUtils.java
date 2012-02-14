package de.zib.gndms.dspace.service;

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


/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 17.08.11  14:57
 * @brief
 */
public final class DSpaceBindingUtils {

	/**
	 * The private constructur (utility classes should not be instantiated).
	 */
	private DSpaceBindingUtils() {
	}
	
    /**
     * Returns the gridftp path for a slice. 
     * @param sourceSlice A reference for a slice.
     * @return The ftp path.
     */
	public static String getFtpPathForSlice( final String sourceSlice ) {
        // TODO use a slice client to acquire the path
        return null;  // Implement Me. Pretty Please!!!
    }

	/**
	 * Deletes a slice including all its content.
	 * @param destinationSlice The reference to the slice to be destroyed.
	 */
    public static void destroySlice( final String destinationSlice ) {
        // TODO Implement Me. Pretty Please!!!
    }
}

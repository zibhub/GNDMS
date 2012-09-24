package de.zib.gndms.logic.model.dspace;

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

import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.neomodel.gorfx.Taskling;

import java.util.List;

/**
 * Provides a mapping of subspaces to slice kind ids and slice kinds.
 * 
 * @author Ulrike Golas, Jörg Bachmann
 */

public interface SliceKindProvider {
    /**
     * Checks whether a given slice kind id exists for the subspace.
     * @param subspace The considered subspace.
     * @param sliceKind The slice kind id.
     * @return true, if this slice kind exists, otherwise false.
     */
    boolean exists( String subspace, String sliceKind );


    /**
     * Returns a list containing all existing slice kind ids for a subspace.
     * @param subspace The considered subspace.
     * @return The list.
     * @throws NoSuchElementException if the subspace does not exist.
     */
    List< String > list( String subspace ) throws NoSuchElementException;


    /**
     * Returns the slice kind for a given slice kind id in a subspace.
     * @param subspace The considered subspace.
     * @param sliceKind The requested slice kind id.
     * @return The corresponding slice kind.
     * @throws NoSuchElementException if the subspace does not exist.
     */
    SliceKind get( String subspace, String sliceKind ) throws NoSuchElementException;


    void create( String slicekindId, String config );
    

    Taskling delete( String sliceKindId );
    
    
    void invalidate( String sliceKindId );
}

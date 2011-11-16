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



import de.zib.gndms.common.model.common.AccessMask;
import de.zib.gndms.logic.model.AbstractModelEntityAction;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;

import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.util.Set;

/**
 * Action to create a new slice kind object from scratch.
 *
 * The object will be created when this action is executed.
 *
 * @see SliceKind
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 *
 * User: mjorra, Date: 18.08.2008, Time: 13:44:55
 */
public class CreateSliceKindAction extends AbstractModelEntityAction<SliceKind, SliceKind> {

    private String  URI; // must be unique
    private AccessMask permission; // must not be null
    private Set<Subspace> subspaces; // can be null


    public CreateSliceKindAction( ) {
    }

    public CreateSliceKindAction( @NotNull String URI, @NotNull AccessMask perm ) {
        this.URI = URI;
        this.permission = perm;
        this.subspaces = null;
    }

    public CreateSliceKindAction( @NotNull String URI, @NotNull AccessMask perm, Set<Subspace> subspaces ) {
        this.URI = URI;
        this.permission = perm;
        this.subspaces = subspaces;
    }


    public void inititalize( ) {
        super.initialize();
        requireParameter( "URI", URI );
        requireParameter( "permission", permission );
    }

    /**
     * Creates a new SliceKind object using the fields of this class.
     * The object will be made managed and persistent by the given EntityManager
     * 
     * @param em the EntityManager  being executed on its persistence context.
     * @return the new slice kind object
     */
    public SliceKind execute( @NotNull EntityManager em ) {

        SliceKind sl = new SliceKind( );
        sl.setURI( URI );
        sl.setPermission( permission );
        sl.setSubspaces( subspaces );

        em.persist( sl );
        return sl;
    }
    

    public String getURI() {
        return URI;
    }

    public void setURI( String URI ) {
        this.URI = URI;
    }

    public AccessMask getMode() {
        return permission;
    }

    public void setMode( AccessMask permission ) {
        this.permission = permission;
    }

    public Set<Subspace> getSubspaces() {
        return subspaces;
    }

    public void setSubspaces( Set<Subspace> subspaces ) {
        this.subspaces = subspaces;
    }


}

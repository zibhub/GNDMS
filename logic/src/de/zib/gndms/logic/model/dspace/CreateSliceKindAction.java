package de.zib.gndms.logic.model.dspace;

import de.zib.gndms.logic.model.AbstractModelAction;
import de.zib.gndms.model.dspace.MetaSubspace;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.common.AccessMask;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.util.Set;

/**
 * Action to create a new slice kind object from scratch.
 *
 * The object will be created when this action is executed.
 *
 * @see SliceKind
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: 18.08.2008, Time: 13:44:55
 */
public class CreateSliceKindAction extends AbstractModelAction<SliceKind, SliceKind> {

    private String  URI; // must be unique
    private AccessMask permission; // must not be null
    private Set<MetaSubspace> metaSubspaces; // can be null


    public CreateSliceKindAction( ) {
    }

    public CreateSliceKindAction( @NotNull String URI, @NotNull AccessMask perm ) {
        this.URI = URI;
        this.permission = perm;
        this.metaSubspaces = null;
    }

    public CreateSliceKindAction( @NotNull String URI, @NotNull AccessMask perm, Set<MetaSubspace> metaSubspaces ) {
        this.URI = URI;
        this.permission = permission;
        this.metaSubspaces = metaSubspaces;
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
        sl.setMetaSubspaces( metaSubspaces );

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

    public Set<MetaSubspace> getMetaSubspaces() {
        return metaSubspaces;
    }

    public void setMetaSubspaces( Set<MetaSubspace> metaSubspaces ) {
        this.metaSubspaces = metaSubspaces;
    }


}

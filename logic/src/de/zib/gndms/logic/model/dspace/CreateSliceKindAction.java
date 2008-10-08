package de.zib.gndms.logic.model.dspace;

import de.zib.gndms.logic.model.AbstractModelAction;
import de.zib.gndms.model.dspace.MetaSubspace;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.types.SliceKindMode;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.util.Set;

/**
 * Action to create a new slice kind object from scratch.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: 18.08.2008, Time: 13:44:55
 */
public class CreateSliceKindAction extends AbstractModelAction<SliceKind, SliceKind> {

    private String  URI; // must be unique
    private SliceKindMode mode; // must not be null
    private Set<MetaSubspace> metaSubspaces; // can be null


    public CreateSliceKindAction( ) {
    }

    public CreateSliceKindAction( @NotNull String URI, @NotNull SliceKindMode mode ) {
        this.URI = URI;
        this.mode = mode;
        this.metaSubspaces = null;
    }

    public CreateSliceKindAction( @NotNull String URI, @NotNull SliceKindMode mode, Set<MetaSubspace> metaSubspaces ) {
        this.URI = URI;
        this.mode = mode;
        this.metaSubspaces = metaSubspaces;
    }


    public void inititalize( ) {
        super.initialize();
        requireParameter( "URI", URI );
        requireParameter( "mode", mode );
    }


    public SliceKind execute( @NotNull EntityManager em ) {

        SliceKind sl = new SliceKind( );
        sl.setURI( URI );
        sl.setMode( mode );
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

    public SliceKindMode getMode() {
        return mode;
    }

    public void setMode( SliceKindMode mode ) {
        this.mode = mode;
    }

    public Set<MetaSubspace> getMetaSubspaces() {
        return metaSubspaces;
    }

    public void setMetaSubspaces( Set<MetaSubspace> metaSubspaces ) {
        this.metaSubspaces = metaSubspaces;
    }


}

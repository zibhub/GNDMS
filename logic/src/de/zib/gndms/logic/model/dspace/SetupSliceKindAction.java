package de.zib.gndms.logic.model.dspace;

import de.zib.gndms.logic.action.MandatoryOptionMissingException;
import de.zib.gndms.logic.model.config.ConfigActionHelp;
import de.zib.gndms.logic.model.config.ConfigOption;
import de.zib.gndms.logic.model.config.SetupAction;
import de.zib.gndms.model.dspace.MetaSubspace;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.types.SliceKindMode;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;
import java.util.Set;

/**
 * Action to create a new slice kind object from scratch.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: 18.08.2008, Time: 13:44:55
 *
 * todo changing the mode of a slice isn't a tivial action, cause it requires a change of the file permissions of all
 * todo slice us using this slice kind. Is this desiered?
 *
 * todo test it
 */
@ConfigActionHelp(shortHelp = "Setup a slice kind", longHelp = "A slice kind descripes the permissions of a slice. " +
        "With this action, the kind can be defined or changed. It requires an URI and the mode which can either be RW or RO")
public class SetupSliceKindAction extends SetupAction<Void> {

    @ConfigOption(descr="The URI of the slice kind (the PK for the db record)")
    private String uri; // must be unique

    @ConfigOption(descr="The write mode of the slice kind (its a required attribute)")
    private SliceKindMode sliceKindMode; // must not be null

    private Set<MetaSubspace> metaSubspaces; // can be null
    
    private SliceKind sliceKind;


    public SetupSliceKindAction( ) {
    }

    public SetupSliceKindAction( @NotNull String URI, @NotNull SliceKindMode sliceKindMode ) {
        this.uri = URI;
        this.sliceKindMode = sliceKindMode;
        this.metaSubspaces = null;
    }

    public SetupSliceKindAction( @NotNull String URI, @NotNull SliceKindMode sliceKindMode, Set<MetaSubspace> metaSubspaces ) {
        this.uri = URI;
        this.sliceKindMode = sliceKindMode;
        this.metaSubspaces = metaSubspaces;
    }


    @Override
    public void initialize( ) {

        try{
            if( uri == null && (isCreating() || hasOption("uri") ))
                setUri(getOption("uri"));
            if( sliceKindMode == null && (isCreating() || hasOption("sliceKindMode")))
                setSliceKindMode( Enum.valueOf( SliceKindMode.class , getOption("sliceKindMode")) );
        } catch ( MandatoryOptionMissingException e) {
            throw new IllegalStateException(e);
        }
        //catch ( ParseException e) {
        //    throw new IllegalStateException(e);
        //}

        super.initialize();

        requireParameter( "uri", uri);
        requireParameter( "sliceKindMode", sliceKindMode );
    }


    public Void execute( @NotNull EntityManager em, @NotNull PrintWriter writer ) {

        SliceKind r;
        if( isCreating() ) {
            r = new SliceKind( );
            r.setURI( getUri( ) );
            r.setMode( getSliceKindMode() ); 
            em.persist( r );
        } else {
            r = em.find( SliceKind.class, getUri( ) );
            if ( r == null ) {
                writer.println( "No slice kind with given found (uri: " + uri +")" );
                return null;
            }
            if( r.getMode( ).equals( getSliceKindMode( ) ) ) {
                writer.println( "Given sliceKindMode is equal to the slice kinds current sliceKindMode. Nothing changed." );
                return null;
            }

            r.setMode( getSliceKindMode( ) );
        }


        return null;
    }


    public String getUri() {
        return uri;
    }

    public void setUri( String URI ) {
        this.uri = URI;
    }

    public SliceKindMode getSliceKindMode() {
        return sliceKindMode;
    }

    public void setSliceKindMode( SliceKindMode sliceKindMode ) {
        this.sliceKindMode = sliceKindMode;
    }

    public Set<MetaSubspace> getMetaSubspaces() {
        return metaSubspaces;
    }

    public void setMetaSubspaces( Set<MetaSubspace> metaSubspaces ) {
        this.metaSubspaces = metaSubspaces;
    }


}

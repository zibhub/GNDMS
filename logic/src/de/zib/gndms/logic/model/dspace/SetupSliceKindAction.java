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
    private String sliceKind; // must be unique

    @ConfigOption(descr="The write mode of the slice kind (its a required attribute)")
    private SliceKindMode theSliceKindMode; // must not be null

    private Set<MetaSubspace> metaSubspaces; // can be null
    
    private SliceKind theSliceKind;


    public SetupSliceKindAction( ) {
    }

    public SetupSliceKindAction( @NotNull String URI, @NotNull SliceKindMode sliceKindMode ) {
        this.sliceKind = URI;
        this.theSliceKindMode = sliceKindMode;
        this.metaSubspaces = null;
    }

    public SetupSliceKindAction( @NotNull String URI, @NotNull SliceKindMode sliceKindMode, Set<MetaSubspace> metaSubspaces ) {
        this.sliceKind = URI;
        this.theSliceKindMode = sliceKindMode;
        this.metaSubspaces = metaSubspaces;
    }


    @Override
    public void initialize( ) {

        try{
            if( sliceKind == null && (isCreating() || hasOption("sliceKind") ))
                setSliceKind(getOption("sliceKind"));
            if( theSliceKindMode == null && (isCreating() || hasOption("sliceKindMode")))
                setTheSliceKindMode( Enum.valueOf( SliceKindMode.class , getOption("sliceKindMode")) );
        } catch ( MandatoryOptionMissingException e) {
            throw new IllegalStateException(e);
        }
        //catch ( ParseException e) {
        //    throw new IllegalStateException(e);
        //}

        super.initialize();

        requireParameter( "sliceKind", sliceKind);
        requireParameter( "sliceKindMode", theSliceKindMode);
    }


    public Void execute( @NotNull EntityManager em, @NotNull PrintWriter writer ) {

        SliceKind r;
        if( isCreating() ) {
            r = new SliceKind( );
            r.setURI( getSliceKind( ) );
            r.setMode( getTheSliceKindMode() );
            em.persist( r );
        } else {
            r = em.find( SliceKind.class, getSliceKind( ) );
            if ( r == null ) {
                writer.println( "No slice kind with given found (uri: " + sliceKind +")" );
                return null;
            }
            if( r.getMode( ).equals( getTheSliceKindMode( ) ) ) {
                writer.println( "Given sliceKindMode is equal to the slice kinds current sliceKindMode. Nothing changed." );
                return null;
            }

            r.setMode( getTheSliceKindMode( ) );
        }


        return null;
    }


    public String getSliceKind() {
        return sliceKind;
    }

    public void setSliceKind( String URI ) {
        this.sliceKind = URI;
    }

    public SliceKindMode getTheSliceKindMode() {
        return theSliceKindMode;
    }

    public void setTheSliceKindMode( SliceKindMode sliceKindMode ) {
        this.theSliceKindMode = sliceKindMode;
    }

    public Set<MetaSubspace> getMetaSubspaces() {
        return metaSubspaces;
    }

    public void setMetaSubspaces( Set<MetaSubspace> metaSubspaces ) {
        this.metaSubspaces = metaSubspaces;
    }


}

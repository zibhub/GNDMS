package de.zib.gndms.logic.model.dspace;

import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.logic.model.config.ConfigActionHelp;
import de.zib.gndms.logic.model.config.ConfigActionResult;
import de.zib.gndms.logic.model.config.ConfigOption;
import de.zib.gndms.logic.model.config.SetupAction;
import de.zib.gndms.model.dspace.MetaSubspace;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.common.AccessMask;
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
 * todo changing the mode of a slice isn't a trivial action, cause it requires a change of the file permissions of all
 * todo slice must using this slice kind. Is thisdesiredd?
 *
 * todo test it
 */
@ConfigActionHelp(shortHelp = "Setup a slice kind", longHelp = "A slice kind descripes the permissions of a slice. " +
        "With this action, the kind can be defined or changed. It requires an URI and the mode which is a posix like permission string, (e.g. 750)")
public class SetupSliceKindAction extends SetupAction<ConfigActionResult> {

    @ConfigOption(descr="The URI of the slice kind (the PK for the db record)")
    private String sliceKind; // must be unique

    @ConfigOption(descr="The write mode of the slice kind (its a required attribute)")
    private String theSliceKindMode; // must not be null

    @ConfigOption(descr="The directory to store the slices belonging to this kind (its a required attribute)")
    private String uniqueDirName;

    private Set<MetaSubspace> metaSubspaces; // can be null
    
    private SliceKind theSliceKind;


    public SetupSliceKindAction( ) {
    }

    public SetupSliceKindAction( @NotNull String URI, @NotNull String sliceKindMode ) {
        this.sliceKind = URI;
        this.theSliceKindMode = sliceKindMode;
        this.metaSubspaces = null;
    }

    public SetupSliceKindAction( @NotNull String URI, @NotNull String sliceKindMode, Set<MetaSubspace> metaSubspaces ) {
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
                setTheSliceKindMode( getOption("sliceKindMode") );
            if( uniqueDirName == null && (isCreating() || hasOption("uniqueDirName")))
                setUniqueDirName( getOption("uniqueDirName") );
        } catch ( MandatoryOptionMissingException e) {
            throw new IllegalStateException(e);
        }
        //catch ( ParseException e) {
        //    throw new IllegalStateException(e);
        //}

        super.initialize();

        requireParameter( "sliceKind", sliceKind);
        requireParameter( "sliceKindMode", theSliceKindMode);
        requireParameter( "uniqueDirName", uniqueDirName );
    }


    public ConfigActionResult execute( @NotNull EntityManager em, @NotNull PrintWriter writer ) {

        SliceKind r;
        AccessMask msk = AccessMask.fromString( getTheSliceKindMode() );
        if( isCreating() ) {
            r = new SliceKind( );
            r.setURI( getSliceKind( ) );
            r.setPermission( msk );
            r.setSliceDirectory( uniqueDirName );
            em.persist( r );
        } else {
            r = em.find( SliceKind.class, getSliceKind( ) );
            if ( r == null ) {
                return failed( "No slice kind with given found (uri: " + sliceKind +")" );
            }
            if( r.getPermission( ).equals( msk ) && r.getSliceDirectory( ).equals( uniqueDirName )  ) {
                return failed( "Nothing to changed." );
            }

            r.setPermission( msk );
            r.setSliceDirectory( uniqueDirName );
        }

        return ok();
    }


	@Override
	public boolean isSupportedMode(final SetupMode modeParam) {
		if (SetupMode.CREATE.equals(modeParam)) return true;
		if (SetupMode.UPDATE.equals(modeParam)) return true;
		return false;
	}


	public String getSliceKind() {
        return sliceKind;
    }

    public void setSliceKind( String URI ) {
        this.sliceKind = URI;
    }

    public String getTheSliceKindMode() {
        return theSliceKindMode;
    }

    public void setTheSliceKindMode( String sliceKindMode ) {
        this.theSliceKindMode = sliceKindMode;
    }

    public Set<MetaSubspace> getMetaSubspaces() {
        return metaSubspaces;
    }

    public void setMetaSubspaces( Set<MetaSubspace> metaSubspaces ) {
        this.metaSubspaces = metaSubspaces;
    }


    public String getUniqueDirName() {
        return uniqueDirName;
    }


    public void setUniqueDirName( String uniqueDirName ) {
        this.uniqueDirName = uniqueDirName;
    }
}

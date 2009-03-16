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
 *
 * An Action to create a new slice kind object from scratch.
 *
 * <p>An instance of this class manages entities, being an instance of {@code SliceKind}, which will be retrieved from the
 * configuration map during the initialization.
 *
 *
 * <p>When this action is started, the configuration map must have the options 'sliceKind', 'sliceKindMode' and 'uniqueDirName' set.
 * Otherwise an <tt>IllegalStateException</tt> will be thrown.
 *
 * <p>If SetupMode is set to
 * <ul>
 *  <li>
 *      create, a new SliceKind instance will be created and its fields are set 
 *      using the corrisponding getter methods of this class.
 *  </li>
 *  <li>
 *      update, the SliceKind instance will be retrieved from the EntityManager, using <tt>getSliceKind()</tt>
     * as primary key and {@code SliceKind.class} as entityclass. Then the permissions and slice directory will be
     * updated.
    </li>
 *  </ul>
 *
 * <p> Note other setup modes are not supported.
 *
 * <p>An instance of this class returns a {@code ConfigActionResult} informing about the success of its execution, when
 * the <tt>execute()</tt> method is called.
/**
 *
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



  /**
    * Calls <tt>super.initialize()</tt> and retrieves several field values from the configuration map,
    * if SetupMode is <tt>create</tt>.
    * The options 'sliceKind', 'sliceKindMode' and 'uniqueDirName' must be set anyway.
    */
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


    /**
     *
     * If SetupMode is <tt>create</tt>, this method creates a new <tt>SliceKind</tt> instance and its fields are set 
     * using the corrisponding getter methods of this class.
     *
     * <p>On <tt>update</tt> the SliceKind instance will be retrieved from the EntityManager, using <tt>getSliceKind()</tt>
     * as primary key and {@code SliceKind.class} as entityclass. Then the permissions and slice directory will be
     * updated.
     *
     * @param em an EntityManager managing SliceKind entities.
     * @param writer
     * @return An {@code OKResult} instance, if no problem occurred. Otherwise a {@code FailedResult} instance.
     */
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

    /**
     * Returns true if {@code modeParam} is either <tt>create</tt> or <tt>update</tt>. Otherwise returns false.
     *
     * @param modeParam 
     * @return true if {@code modeParam} is either <tt>create</tt> or <tt>update</tt>. Otherwise returns false.
     */
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

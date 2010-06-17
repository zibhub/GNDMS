package de.zib.gndms.logic.model.dspace;

import de.zib.gndms.kit.util.DirectoryAux;
import de.zib.gndms.logic.model.CreateTimedGridResourceAction;
import de.zib.gndms.model.common.ModelUUIDGen;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.File;
import java.util.Calendar;

/**
 * An action which creates a new slice in a given workspace
 *
 * It takes care of the hierarchic structur between {@link Subspace},{@link SliceKind} and {@link Slice}.
 * The new slice instance will be registered on the corresponding subspace object and the system will be notified about
 * the model change by calling {@code addChangedModel()}. See {@link #execute(javax.persistence.EntityManager)}
 *
 * @see de.zib.gndms.model.dspace.DSpace
 * @see de.zib.gndms.model.dspace.Subspace
 * @see Slice
 *
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: 13.08.2008, Time: 10:21:36
 */
public class CreateSliceAction extends CreateTimedGridResourceAction<Subspace, Slice> {

    private SliceKind sliceKind;     // kind of the slice to create
    private long storageSize;
    private DirectoryAux directoryAux;

    
    public CreateSliceAction( ) {
         directoryAux = DirectoryAux.getDirectoryAux();
    }


    /**
     * Creates a new {@code CreateSliceAction} action and sets its fields as given by the signature.
     * It creates and returns a new slice object, when this action is executed.
     *
     *
     * @param uuid a uuid identifying the grid resource of the new slice instance
     * @param ttm the termination time of the slice object
     * @param gen an uuid generator for the directory id of the new slice object
     * @param kind the sliceKind instance for the slice. (See {@link Slice}).
     * @param ssize total storage size for the slice instance
     */
    public CreateSliceAction( String uuid, Calendar ttm, ModelUUIDGen gen, SliceKind kind, long ssize ) {

        super( uuid, ttm );
        setUUIDGen(gen);
        this.sliceKind = kind;
        this.storageSize = ssize;
        directoryAux = DirectoryAux.getDirectoryAux();

    }

        /**
     * Creates a new {@code CreateSliceAction} action and sets its fields as given by the signature.
     * It creates and returns a new slice object, when this action is executed.
     *
     *
     * @param uuid a uuid identifying the grid resource of the new slice instance
     * @param ttm the termination time of the slice object
     * @param gen an uuid generator for the directory id of the new slice object
     * @param kind the sliceKind instance for the slice. (See {@link Slice}).
     * @param ssize total storage size for the slice instance
     * @param da an helper object for directory access 
     */
    public CreateSliceAction( String uuid, Calendar ttm, ModelUUIDGen gen, SliceKind kind, long ssize, DirectoryAux da ) {
        
        super( uuid, ttm );
        setUUIDGen(gen);
        this.sliceKind = kind;
        this.storageSize = ssize;
        this.directoryAux = da;
    }

    /**
     * Checks if a {@link de.zib.gndms.model.dspace.SliceKind} instance has been denoted and throws an exception if not given.
     * Calls super.initialize()
     *
     * @see CreateTimedGridResourceAction#initialize()
     */
    @Override
    public void initialize() {

        if( sliceKind == null )
            throw new IllegalThreadStateException( "No slice kind provided" );

        super.initialize();
    }

    /**
     * Creates and returns a new slice instance.
     * It will be created using the fields given with the constructor of this class and the subspace retrieved from
     * {@code getModel()}.
     *
     * It arranges the hierarchic structor between {@link Subspace},{@link SliceKind} and {@link Slice}.
     * At the moment this is done using subfolders. The permissions of the folder corresponding to the slice instance
     * are set as denoted in {@link #sliceKind}.
     * The new Slice instance will be registered on the corresponding Subspace.
     * Therefore {@link #addChangedModel(de.zib.gndms.model.common.GridResource)} is called with the new slice instance
     * and the changed subspace instance.
     * 
     * @param em the EntityManager  being executed on its persistence context.
     * @return the new created slice instance
     */
    @Override
    public Slice execute( @NotNull EntityManager em ) {

        Subspace sp = getModel( );

        if( ! sp.getMetaSubspace( ).getCreatableSliceKinds( ).contains( sliceKind ) )
            throw new IllegalStateException("SliceKind not assigned to Subspace");

        String lp = sp.getPath( ) +  File.separator + sliceKind.getSliceDirectory() + File.separator;
        File f = null;
        String did = new String( );

        while ( !( f != null && !f.exists() ) ) {
            did = nextUUID();
            f = new File( lp + did );
        }

        try {
            // this also creats the dir for the namespace if it
            // doesn't exist yet.
            f.mkdirs( );
        } catch ( SecurityException e ) {
            throw new RuntimeException(e);
        }

        // fix permissions
        directoryAux.setPermissions( sliceKind.getPermission(), f.getAbsolutePath( ) );

        Slice sl = new Slice( did, sliceKind, sp );
        sl.setId( getId( ) );
        sl.setTerminationTime( getTerminationTime( ) );
        sl.setTotalStorageSize( storageSize );
        sl.setOwner( sp );
        sp.addSlice( sl );

        // todo maybe persist slice first
//        em.persist( sl );

//        em.merge( sp );

        addChangedModel( sl );

        // maybe this isn't of interesst
        addChangedModel( sp );
        
        return  sl;
    }


    public SliceKind getSliceKind( ) {

        return sliceKind;
    }

    
    public void setSliceKind( SliceKind knd ) {

        sliceKind = knd;
    }


    public DirectoryAux getDirectoryAux( ) {
        return directoryAux;
    }

    /**
     * Sets the DirectoryAux instance used to set the permissions of the newly created slice directory.
     *
     * @param da
     */
    public void setDirectoryAux( DirectoryAux da ) {
        directoryAux = da;
    }
    
}

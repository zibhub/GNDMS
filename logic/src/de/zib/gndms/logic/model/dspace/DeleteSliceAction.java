package de.zib.gndms.logic.model.dspace;

import de.zib.gndms.kit.util.DirectoryAux;
import de.zib.gndms.logic.model.AbstractModelAction;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.Subspace;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;

/**
 * An action which deletes a slice.
 *
 * When the slice is deleted, its associed folder is also removed.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: Mi 23. Jun 17:40:11 CEST 2010
 */
public class DeleteSliceAction extends AbstractModelAction<Subspace, Slice> {

    private Slice slice;
    private DirectoryAux directoryAux;

    
    public DeleteSliceAction( ) {
    }


    /**
     * Creates a new {@code DeleteSliceAction} action and sets its fields as given by the signature.
     *
     * @param slice The slice to delete.
     */
    public DeleteSliceAction( Slice slice ) {
        this.slice = slice;
    }

    /**
     * Creates a new {@code DeleteSliceAction} action and sets its fields as given by the signature.
     *
     * @param slice The slice to delete.
     * @param da an helper object for directory access 
     */
    public DeleteSliceAction( Slice slice, DirectoryAux da ) {
        this.slice = slice;
        directoryAux = da;

    }

    /**
     * Checks if a {@link de.zib.gndms.model.dspace.Slice} instance has been denoted and throws an exception if not given.
     * Calls super.initialize()
     *
     */
    @Override
    public void initialize() {

        if( slice == null )
            throw new IllegalThreadStateException( "No slice provided" );

        setModel( slice.getSubspace() );
        super.initialize();
    }

    /**
     * Deletes and returns null;
     * 
     * @param em the EntityManager  being executed on its persistence context.
     * @return null
     */
    @Override
    public Slice execute( @NotNull EntityManager em ) {

        if( directoryAux == null )
            directoryAux = getInjector().getInstance( DirectoryAux.class );

        Subspace sp = getModel( );

        directoryAux.deleteDirectory( slice.getOwner(), sp.getPathForSlice( slice ) );

        sp.removeSlice( slice );
        getEntityManager().remove( slice );

        addChangedModel( sp );
        addChangedModel( slice );
        return  null;
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


    /**
     * One-shot execution of this action.
     * Creates initilizes and calls a new DeleteSliceAction.
     *
     * @param sl The slice which should be deleted.
     * @param par The parent action.
     */
    public static DeleteSliceAction deleteSlice( @NotNull Slice sl, @NotNull AbstractModelAction par ) {
        
        DeleteSliceAction dla = new DeleteSliceAction( sl );
        dla.setParent( par );
        dla.setClosingEntityManagerOnCleanup( false );
        dla.call();

        return dla;
    }
}

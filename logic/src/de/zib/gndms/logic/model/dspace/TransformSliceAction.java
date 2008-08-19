package de.zib.gndms.logic.model.dspace;

import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import de.zib.gndms.model.dspace.types.SliceKindMode;
import de.zib.gndms.model.common.ModelUUIDGen;
import de.zib.gndms.logic.model.aux.DirectoryAux;
import de.zib.gndms.logic.model.AbstractModelAction;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.File;
import java.util.Calendar;

/**
 * Converts a slice to another slice kind and addes it to
 * another subspace.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: 13.08.2008, Time: 13:20:25
 *
 * todo In this version the out-dated slices remains in the db and its directory on disk, maybe change this later
 */
public class TransformSliceAction extends AbstractModelAction<Slice, Slice> {


    private CreateSliceAction createSliceAction;

    
    public TransformSliceAction( String uuid, Calendar ttm, SliceKind kind, Subspace tgt, ModelUUIDGen uuidgen ) {

        createSliceAction = new CreateSliceAction( uuid, ttm, uuidgen, kind );
        createSliceAction.setModel( tgt );
    }


    public TransformSliceAction( String uuid, Calendar ttm, SliceKind kind, Subspace tgt, ModelUUIDGen uuidgen, DirectoryAux da ) {

        createSliceAction = new CreateSliceAction( uuid, ttm, uuidgen, kind, da );
        createSliceAction.setModel( tgt );
    }


    @Override
    public void initialize() {
        createSliceAction.setParent( this );
        super.initialize();
    }
    

    @Override
    public Slice execute(@NotNull EntityManager em) {

        Slice sl = getModel( );
        Subspace sp = sl.getOwner( );

        if(! sp.getMetaSubspace( ).getCreatableSliceKinds( ).contains( createSliceAction.getSliceKind( ) ) )
            return null;

        // create an new slice of the given kind
        Slice nsl = createSliceAction.call( );
        if ( nsl == null )
             throw new RuntimeException( "Can't create slice" );


        // copy contents of old slice to the new one
        String src_pth = sp.getPathForSlice( sl );
        String tgt_pth = createSliceAction.getModel( ).getPathForSlice( nsl );

        String[] ls = sl.getFileListing( );

        SliceKindMode sm = createSliceAction.getSliceKind( ).getMode( );
        // make slice path writable to copy content
        if ( sm.equals( SliceKindMode.RO ) )
            createSliceAction.getDirectoryAux().setDirectoryReadWrite( tgt_pth );


        boolean suc = true;
        for( int i=0; i < ls.length; ++i ) {
            suc = suc &&
                    createSliceAction.getDirectoryAux().copyFile(
                            src_pth + File.separator + ls[i], tgt_pth + File.separator + ls[i]
                    );
        }

        // restore slice path settings
        if ( sm.equals( SliceKindMode.RO ) )
            createSliceAction.getDirectoryAux().setDirectoryReadOnly( tgt_pth );

        // sth went wrong destroy created slice
        if( ! suc ) {
            createSliceAction.getModel().destroySlice( nsl );
            throw new RuntimeException( "Can't copy slice content" );
        }

        // no entries in BatchUpdateAction required, allready done by the create action

        return nsl;
    }

}

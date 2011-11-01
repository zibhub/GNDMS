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
import de.zib.gndms.kit.util.DirectoryAux;
import de.zib.gndms.logic.model.AbstractModelEntityAction;
import de.zib.gndms.model.common.ModelUUIDGen;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.util.Calendar;

/**
 * A TransformSliceAction converts its slice to a new created slice (using {@link de.zib.gndms.logic.model.dspace.CreateSliceAction})
 * and adds it to the subspace corresponding to new slice object.
 *
 * The new slice instance is specified and will be created by the constructor of this class.
 *
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 *
 * User: mjorra, Date: 13.08.2008, Time: 13:20:25
 *
 * In this version the out-dated slices remains in the db and its directory on disk, the caller must take care of
 * removing it with the appropriate action.
 *
 * todo make uses of UUIdGen from AbstractEntityAction
 */
public class TransformSliceAction extends AbstractModelEntityAction<Slice, Slice> {


    private final CreateSliceAction createSliceAction;
    private DirectoryAux directoryAux;


    /**
     * Creates a new {@link CreateSliceAction} instances with the parameters given
     * in the signature and sets the model of {@link #createSliceAction} to {@code tgt}.
     *
     * @param uuid a uuid identifying the grid resource of the new slice instance
     * @param uid name of the owner of the new slice, can be null, then the current user will become the owner.
     * @param ttm the termination time of the slice object
     * @param kind the sliceKind instance for the slice. (See {@link Slice}).
     * @param tgt a subspace the new created slice will be registered on
     * @param ssize total storage size for the slice instance
     * @param uuidgen an uuid generator for the directory id of the new slice object
     */
    public TransformSliceAction( String uuid, String uid, Calendar ttm, SliceKind kind, Subspace tgt, long ssize, ModelUUIDGen uuidgen ) {

        createSliceAction = new CreateSliceAction( uuid, uid, ttm, uuidgen, kind, ssize );
        createSliceAction.setModel( tgt );
    }


   /**
     * Creates a new {@link CreateSliceAction} instances with the parameters given
     * in the signature and sets the model of {@link #createSliceAction} to {@code tgt}.
     *
     * @param uuid a uuid identifying the grid resource of the new slice instance
     * @param uid name of the owner of the new slice, can be null, then the current user will become the owner.
     * @param ttm the termination time of the slice object
     * @param kind the sliceKind instance for the slice. (See {@link Slice}).
     * @param tgt a subspace the new created slice will be registered on
     * @param ssize total storage size for the slice instance
     * @param uuidgen an uuid generator for the directory id of the new slice object
     * @param da an helper object for directory access
     */
   public TransformSliceAction( String uuid, String uid, Calendar ttm, SliceKind kind, Subspace tgt, long ssize, ModelUUIDGen uuidgen, DirectoryAux da ) {

       directoryAux = da;
       createSliceAction = new CreateSliceAction( uuid, uid, ttm, uuidgen, kind, ssize, da );
       createSliceAction.setModel( tgt );
   }


    @Override
    public void initialize() {
        createSliceAction.setParent( this );
        createSliceAction.setClosingEntityManagerOnCleanup( false );
        super.initialize();
    }
    

    /**
     * Executes the slice transformation.
     *
     * NOTE: Since this method uses DirectoryAux.copyDir the obeys the restriction of this method.
     */
    @Override
    public Slice execute(@NotNull EntityManager em) {

        Slice sl = getModel( );
        Subspace sp = sl.getSubspace( );

        if( directoryAux == null ) {
            directoryAux = getInjector().getInstance( DirectoryAux.class );
        }

        if(! sp.getMetaSubspace( ).getCreatableSliceKinds( ).contains( createSliceAction.getSliceKind( ) ) )
            return null;

        // create an new slice of the given kind
        Slice nsl = createSliceAction.execute( em );
        if ( nsl == null )
             throw new RuntimeException( "Can't create slice" );


        // copy contents of old slice to the new one
        String src_pth = sp.getPathForSlice( sl );
        String tgt_pth = createSliceAction.getModel( ).getPathForSlice( nsl );

        AccessMask msk = createSliceAction.getSliceKind( ).getPermission( );
        boolean ro = msk.queryFlagsOff( AccessMask.Ugo.USER, AccessMask.AccessFlags.WRITABLE );

        // make slice path writable to copy content
        if ( ro ) {
            msk.addFlag( AccessMask.Ugo.USER, AccessMask.AccessFlags.WRITABLE );
            directoryAux.setPermissions( nsl.getOwner(), msk, tgt_pth );
        }


        directoryAux.copyDir( nsl.getOwner(), src_pth, tgt_pth );

        // restore slice path settings
        if ( ro ) {
            msk.removeFlag( AccessMask.Ugo.USER, AccessMask.AccessFlags.WRITABLE );
            directoryAux.setPermissions( nsl.getOwner(), msk, tgt_pth );
        }

        /*
        // sth went wrong destroy created slice
        if( ! suc ) {
            DeleteSliceAction.deleteSlice( sl, this );
            throw new RuntimeException( "Can't copy slice content" );
        }
        */

        // no entries in BatchUpdateAction required, already done by the create action

        return nsl;
    }



}

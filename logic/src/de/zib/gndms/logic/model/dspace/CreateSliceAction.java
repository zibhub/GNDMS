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
import de.zib.gndms.logic.model.CreateTimedGridResourceAction;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.dspace.Subspace;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.File;
import java.util.Calendar;
import java.util.UUID;

/**
 * An action which creates a new slice in a given workspace
 *
 * It takes care of the hierarchic structure between {@link Subspace},{@link SliceKind} and {@link Slice}.
 * The new slice instance will be registered on the corresponding subspace object and the system will be notified about
 * the model change by calling {@code addChangedModel()}. See {@link #execute(javax.persistence.EntityManager)}
 *
 * @see de.zib.gndms.model.dspace.DSpace
 * @see de.zib.gndms.model.dspace.Subspace
 * @see Slice
 *
 *
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 *
 * User: mjorra, Date: 13.08.2008, Time: 10:21:36
 */
public class CreateSliceAction extends CreateTimedGridResourceAction<Subspace, Slice> {

    private SliceKind sliceKind;     // kind of the slice to create
    private long storageSize;
    private DirectoryAux directoryAux;
    private String uid = System.getProperty( "user.name" );

    
    public CreateSliceAction( ) {
    }


    /**
     * Creates a new {@code CreateSliceAction} action and sets its fields as given by the signature.
     * It creates and returns a new slice object, when this action is executed.
     *
     *
     * @param uid name of the owner of the new slice, can be null, then the current user will become the owner.
     * @param ttm the termination time of the slice object
     * @param kind the sliceKind instance for the slice. (See {@link Slice}).
     * @param ssize total storage size for the slice instance
     */
    public CreateSliceAction( String uid, Calendar ttm, SliceKind kind, long ssize ) {

        super( UUID.randomUUID().toString(), ttm );
        if( uid != null )
            this.uid = uid;
        this.sliceKind = kind;
        this.storageSize = ssize;

    }

        /**
     * Creates a new {@code CreateSliceAction} action and sets its fields as given by the signature.
     * It creates and returns a new slice object, when this action is executed.
     *
     *
     * @param uid name of the owner of the new slice, can be null, then the current user will become the owner.
     * @param ttm the termination time of the slice object
     * @param kind the sliceKind instance for the slice. (See {@link Slice}).
     * @param ssize total storage size for the slice instance
     * @param da an helper object for directory access 
     */
    public CreateSliceAction( String uid, Calendar ttm, SliceKind kind, long ssize, DirectoryAux da ) {
        
        super( UUID.randomUUID().toString(), ttm );
        if( uid != null )
            this.uid = uid;
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
     * It arranges the hierarchic structure between {@link Subspace},{@link SliceKind} and {@link
     * Slice}.
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

        if( directoryAux == null )
            directoryAux = getInjector().getInstance( DirectoryAux.class );

        // get nondetached objects
        final Subspace sp = em.find( Subspace.class, getModel().getId() );
        sliceKind = em.find( SliceKind.class, sliceKind.getId() );

        if( ! sp.getCreatableSliceKinds( ).contains( sliceKind ) )
            throw new IllegalStateException("SliceKind not assigned to Subspace");

        String lp = sp.getPath( ) +  File.separator + sliceKind.getSliceDirectory() + File.separator;

        File f;
        String did;
        do {
            did = nextUUID();
            f = new File( lp + did );
        } while ( !( f != null && !f.exists() ) );

        try {
            // check if slice kind dir exists
            File p = f.getParentFile();
            if(! p.exists() )
                directoryAux.mkdir( System.getProperty( "user.name" ),
                    p.getAbsolutePath(), AccessMask.fromString( "1777" ) );
            
            directoryAux.mkdir( uid, f.getAbsolutePath(), sliceKind.getPermission() );
        } catch ( SecurityException e ) {
            throw new RuntimeException(e);
        }
        
        /*
        // fix permissions
        directoryAux.setPermissions( uid, sliceKind.getPermission(), f.getAbsolutePath( ) );
        if(! uid.equals( System.getProperty( "user.name" ) ) )
            directoryAux.changeOwner( uid, f.getAbsolutePath() );
            */

        Slice sl = new Slice( did, sliceKind, sp, uid );
        sl.setId( getId( ) );
        sl.setTerminationTime( getTerminationTime( ) );
        sl.setTotalStorageSize( storageSize );

        addChangedModel( sl );

        // maybe this isn't of interest
        addChangedModel( sp );

        em.persist( sl );

        return  sl;
    }


    public SliceKind getSliceKind( ) {

        return sliceKind;
    }

    
    public void setSliceKind( SliceKind knd ) {

        sliceKind = knd;
    }


    public String getUid() {
        return uid;
    }


    public void setUid( String uid ) {
        this.uid = uid;
    }


    public long getStorageSize() {
        return storageSize;
    }


    public void setStorageSize( long storageSize ) {
        this.storageSize = storageSize;
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

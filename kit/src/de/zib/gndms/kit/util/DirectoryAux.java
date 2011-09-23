package de.zib.gndms.kit.util;

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

import java.io.File;

/**
 * This abstract class provides usefull methods concerning directory access.
 *
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 *
 * User: mjorra, Date: 08.08.2008, Time: 10:44:04
 */
public interface DirectoryAux {

    /**
     * Sets the owner-writable flag for the directory.
     *
     * @note Rights of group and other will be removed.
     *
     * @param uid The owner of pth.
     * @param pth The path of the directory.
     *
     * @return true if the operation was successful
     */
    public boolean setDirectoryReadWrite( String uid, String pth );

    /**
     * Removes the owner-writable flag for the directory.
     *
     * @note Rights of group and other will be removed.
     *
     * @param uid The owner of pth.
     * @param pth The path of the directory.
     *
     * @return true if the operation was successful
     */
    public boolean setDirectoryReadOnly( String uid, String pth );

    /**
     * Sets the permissions for a dspace subspace.
     *
     * Removes the owner-readable flag for the directory.
     * Making it write and executable.
     *
     * @note Rights of group and other will be removed.
     *
     * @param uid The owner of pth.
     * @param pth The path of the directory.
     *
     * @return true if the operation was successful
     */
    public boolean setSubspacePermissions( String uid, String pth );

    /**
     * Sets the premission of the directory to perm
     *
     * @param uid The owner of pth.
     * @param perm The new permissions, overwriting all current permissions.
     * @param pth The path of the directory.
     *
     * @return true if the operation was successful
     */
    public boolean setPermissions( String uid, AccessMask perm, String pth );


    /**
     * Changes the owner of a file or directory.
     *
     * @param uid the new uid.
     * @param path to the object.
     */
    public boolean changeOwner( String uid, String path );

    /**
     * Deletes the given directory.
     *
     * @param owner The owner of the directory
     * @param pth The Path.
     */
    public boolean deleteDirectory( String owner, String pth );

    /**
     * Creates a directory.
     *
     * @param uid The owner of the new directory.
     * @param pth The Path.
     * @param perm The permission of the target
     */
    public boolean mkdir( String uid, String pth, AccessMask perm );

    /**
     * Creates the contents of a directory...
     *
     * @param src_pth
     * @param tgt_pth
     */
    public boolean copyDir( String uid, String src_pth, String tgt_pth );


    public static class Utils {


        /**
         * Little helper which delets a direcotry and its contents.
         *
         * @param pth The complete Path to the directory.
         * @return The success of the operation.
         */
        public static boolean genericDeleteDirectory( String pth ) {

            File f = new File( pth );

            if( ! ( f.exists( ) && f.isDirectory( ) ) )
                return false;

            try{
                String[] fl = f.list( );
                // for( i in 0..<fl.length )  {
                for( int i=0; i < fl.length; ++i )  {
                    File cf = new File( fl[i] );
                    cf.delete( );
                }
                return f.delete( );
            } catch (SecurityException e) {
                return false;
            }
        }

    }
}


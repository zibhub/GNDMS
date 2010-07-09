package de.zib.gndms.kit.util;

import de.zib.gndms.model.common.AccessMask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * This abstract class provides usefull methods concerning directory access.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
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
    public boolean setDirectoryReadWrite( String pth );

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


    public static class Utils {
        /**
         *
         * Copies file src to tgt
         *
         * @return true if everything went fine.
         */
        public static boolean copyFile( String src, String tgt )  {

            File sf = new File( src );
            File tf = new File( tgt );

            FileChannel inc = null;
            FileChannel outc = null;
            try {
                inc = new FileInputStream( sf ).getChannel( );
                outc = new FileOutputStream( tf ).getChannel( );
                inc.transferTo( 0, inc.size(), outc );
            } catch ( IOException e) {
                return false;
            } finally {
                if ( inc != null )
                    try {
                        inc.close( );
                    } catch ( IOException e ) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                if ( outc != null )
                    try {
                        outc.close( );
                    } catch ( IOException e ) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
            }

            return true;
        }



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

        /*
        public boolean createSubspaceDirectory( String pth ) {

            File f = new File( pth );

            try {
                // this also creats the dir for the subspace if it
                // doesn't exist yet.
                f.mkdirs( );
                setSubspacePermissions( f.getAbsolutePath( ) );
            } catch (SecurityException e) {
                return false;
            }

            return true;
        }
        */
    };
}


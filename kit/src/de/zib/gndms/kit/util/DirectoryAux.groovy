package de.zib.gndms.kit.util

import java.nio.channels.FileChannel
import de.zib.gndms.model.common.AccessMask
import de.zib.gndms.model.common.AccessMask

/**
 * This abstract class provides usefull methods concerning directory access.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: 08.08.2008, Time: 10:44:04
 */
abstract class DirectoryAux {

    private static final DirectoryAux DIR_AUX = new LinuxDirectoryAux();

    public static final DirectoryAux getDirectoryAux() {
        return DIR_AUX;
    }

    /**
     * Sets the owner-writable flag for the directory. 
     * 
     * @note Rights of group and other will be removed.
     *
     * @param pth The path of the directory.
     * 
     * @return true if the operation was successful
     */
    public abstract boolean setDirectoryReadWrite( String pth )

    /** 
     * Removes the owner-writable flag for the directory. 
     * 
     * @note Rights of group and other will be removed.
     *
     * @param pth The path of the directory.
     * 
     * @return true if the operation was successful
     */
    public abstract boolean setDirectoryReadOnly( String pth )

    /** 
     * Sets the permissions for a dspace subspace.
     *
     * Removes the owner-readable flag for the directory. 
     * Making it write and executable.
     * 
     * @note Rights of group and other will be removed.
     *
     * @param pth The path of the directory.
     * 
     * @return true if the operation was successful
     */
    public abstract boolean setSubspacePermissions( String pth )

    /**
     * Sets the premission of the directory to perm
     * @perm The new permissions, overwriting all current permissions.
     * @perm pth The path of the directory.
     * 
     * @return true if the operation was successful
     */
    public abstract boolean setPermissions( AccessMask perm, String pth );

    /** 
     *  
     * Copies file src to tgt
     *
     * @return true if everything went fine.
     */
    public static boolean copyFile( String src, String tgt )  {

        File sf = new File( src )
        File tf = new File( tgt )

        FileChannel inc;
        FileChannel outc;
        try {
            inc = new FileInputStream( sf ).getChannel( )
            outc = new FileOutputStream( tf ).getChannel( )
            inc.transferTo( 0, inc.size(), outc )
        } catch (IOException e) {
            return false
        } finally {
            if ( inc != null )
                inc.close( )
            if ( outc != null )
                outc.close( )
        }

        return true
    }


    /**
     * Little helper which delets a direcotry and its contents.
     *
     * @param pth The complete Path to the directory.
     * @return The success of the operation.
     */
    public static boolean deleteDirectory( String pth ) {

        File f = new File( pth )

        if( ! ( f.exists( ) && f.isDirectory( ) ) )
            return false

        try{
            String[] fl = f.list( )
            for( i in 0..<fl.length )  {
                File cf = new File( fl[i] )
                cf.delete( )
            }

            return f.delete( )

        } catch (SecurityException e) {
            return false
        }

        return false
    }


    public boolean createSubspaceDirectory( String pth ) {
        
        File f = new File( pth )

        try {
            // this also creats the dir for the subspace if it
            // doesn't exist yet.
            f.mkdirs( )
            setSubspacePermissions( f.getAbsolutePath( ) )
        } catch (SecurityException e) {
            return false
        }

        return true
    }
}


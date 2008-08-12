package de.zib.gndms.model.dspace.aux

/**
 * Interface for some helpers for directory access.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: 08.08.2008, Time: 10:44:04
 */
interface DirectoryAux {

    /**
     * Sets the owner-writable flag for the directory. 
     * 
     * @note Rights of group and other will be removed.
     *
     * @param pth The path of the directory.
     * 
     * @return true if the operation was successful
     */
    public boolean setDirectoryReadWrite( String pth )

    /** 
     * Removes the owner-writable flag for the directory. 
     * 
     * @note Rights of group and other will be removed.
     *
     * @param pth The path of the directory.
     * 
     * @return true if the operation was successful
     */
    public boolean setDirectoryReadOnly( String pth )

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
    public boolean setSubspacePermissions( String pth )
}

/** 
* Implementation of the directory helpers for a linux system.
*
* This implementation uses the system-call "chmod"
*/
class LinuxDirectoryAux implements DirectoryAux {

    final static String WX = "300"
    final static String RO = "400"
    final static String RW = "600"

    public boolean setDirectoryReadWrite( String pth ) {
        return setMode( RW, pth )
    }


    public boolean setDirectoryReadOnly( String pth ) {
        return setMode( RO, pth )
    }

    public boolean setSubspacePermissions( String pth ) {
        return setMode( WX, pth )
    }

    private static boolean setMode( String md, String pth ) {

        Process p = Runtime.getRuntime().exec( "chmod " +md+ " " + pth )
        return p.exitStatus( ) == 0 
    }
}

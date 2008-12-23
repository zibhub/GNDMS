package de.zib.gndms.kit.util

import de.zib.gndms.model.common.AccessMask
import de.zib.gndms.model.common.AccessMask
import de.zib.gndms.model.common.AccessMask

/**
 * Implementation of the directory helpers for a linux system.
 *
 * This implementation uses the system-call "chmod"
 */
class LinuxDirectoryAux extends DirectoryAux {

    final static String WX = "300"
    final static String RO = "500"
    final static String RW = "700"

    public boolean setDirectoryReadWrite( String pth ) {
        return setMode( RW, pth )
    }


    public boolean setDirectoryReadOnly( String pth ) {
        return setMode( RO, pth )
    }


    public boolean setSubspacePermissions( String pth ) {
        return setMode( WX, pth )
    }


    public boolean setPermissions( AccessMask perm, String pth ) {

        setMode( perm.toString(), pth )
    }

    private static boolean setMode( String md, String pth ) {

        Process p = Runtime.getRuntime().exec( "chmod " +md+ " " + pth )
            return p.waitFor( ) == 0
    }
}

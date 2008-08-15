package de.zib.gndms.logic.model.aux

/**
 * Implementation of the directory helpers for a linux system.
 *
 * This implementation uses the system-call "chmod"
 */
class LinuxDirectoryAux extends DirectoryAux {

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
            return p.waitFor( ) == 0
    }
}

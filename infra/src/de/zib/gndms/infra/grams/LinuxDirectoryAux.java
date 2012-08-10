package de.zib.gndms.infra.grams;

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


import com.sun.jna.Library;
import com.sun.jna.Native;
import de.zib.gndms.common.model.common.AccessMask;

import java.io.File;
import java.io.IOException;

/**
 * Implementation of the directory helpers for a linux system.
 *
 * This implementation uses the system call "chmod"
 */
public class LinuxDirectoryAux extends AbstractDirectoryAux {

    final static String EXECUTABLE = "executable";
    final static String ARGS = "arguments";
    private static CLibrary libc = (CLibrary) Native.loadLibrary("c", CLibrary.class);



    public int chmod( int mask, File file ) {
        try {
            return libc.chmod( file.getCanonicalPath(), mask);
        } catch ( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    public int mkdir( int mask, File file ) {
        try {
            return libc.mkdir( file.getCanonicalPath(), mask );
        }
        catch( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    public int rmdir( File file ) {
        try {
            return libc.rmdir( file.getCanonicalPath() );
        }
        catch( IOException e ) {
            throw new RuntimeException( e );
        }
    }

    public boolean mkdir( String uid, String path, AccessMask perm ) {
        int ret = 1;
        do  {
            logger.debug( ret + ". attempt to create slice dir" + path );
            mkdir( perm.getIntValue(), new File( path ) );
            File f = new File( path );
            if( f.exists() ) {
                logger.debug( "creation successful" );
                return true;
            } else {
                logger.debug( "failed retrying after " + DELAY + "ms" );
                try {
                    Thread.sleep( DELAY );
                } catch ( InterruptedException e ) {
                    logger.debug( "retry delay interrupted" );
                }
                ++ret;
            }
        } while ( ret <= NUM_RETRYS );
        throw new RuntimeException( "failed to create slice dir " + path + " for user " +uid+ " with " + perm );
    }

    interface CLibrary extends Library {
        public int chmod( String path, int mode );
        public int mkdir( String path, int mode );
        public int rmdir( String path );
        public int rename( String old_path, String new_path );
    }


    public boolean changeOwner( String dn, String path) {
        logger.debug( "changing owner of Slice " + path +" to " + dn );
        return false;
    }


    public boolean move( String src_path, String target_path ) {
        return 0 != libc.rename( src_path, target_path );
    }

    public boolean copyDir( String uid, String src_pth, String tgt_pth ) {
        throw new UnsupportedOperationException( "Please do that right now!" );
    }

}

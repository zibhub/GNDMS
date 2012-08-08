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
import de.zib.gndms.common.model.FileStats;
import de.zib.gndms.common.model.common.AccessMask;
import de.zib.gndms.kit.util.DirectoryAux;
import de.zib.gndms.model.common.NoSuchResourceException;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Implementation of the directory helpers for a linux system.
 *
 * This implementation uses the system call "chmod"
 */
public class LinuxDirectoryAux implements DirectoryAux {

    protected final Logger logger = LoggerFactory.getLogger( this.getClass() );

    final static String WX = "300";
    final static String RO = "500";
    final static String RW = "700";
    final static String EXECUTABLE = "executable";
    final static String ARGS = "arguments";
    final static int DELAY = 10000;
    private static final int NUM_RETRYS = 5;
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

    public int rename( File from, File to ) {
        try {
            return libc.rename( from.getCanonicalPath(), to.getCanonicalPath() );
        }
        catch( IOException e ) {
            throw new RuntimeException( e );
        }
    }
    
    public List< String > listContent( String path ) {
        final File dir = new File( path );
        return Arrays.asList( dir.list() );
    }
    
    public FileStats stat( File file ) {
        final FileStats fileStats = new FileStats();
        try {
            fileStats.path = file.getCanonicalPath();
            fileStats.size = new Long( file.length() );
            fileStats.mtime = ISODateTimeFormat.basicDateTime().print( file.lastModified() );
        }
        catch (IOException e) {
            logger.warn( "Could not get file stats of file " + file, e );
            return null;
        }

        return fileStats;
    }

    interface CLibrary extends Library {
        public int chmod( String path, int mode );
        public int mkdir( String path, int mode );
        public int rmdir( String path );
        public int rename( String old_path, String new_path );
    }

    public boolean setDirectoryReadWrite( String uid, String pth ) {
        return setMode( uid, RW, pth );
    }


    public boolean setDirectoryReadOnly( String uid, String pth ) {
        return setMode( uid, RO, pth );
    }


    public boolean setSubspacePermissions( String uid, String pth ) {
        return setMode( uid, WX, pth );
    }


    public boolean setPermissions( String uid, AccessMask perm, String pth ) {
        return setMode( uid, perm.toString(), pth );
    }


    private boolean setMode( String uid, String mode, String path ) {
        AccessMask mask = AccessMask.fromString( mode );
        int m = mask.getIntValue();
        return ( 0 != chmod( m, new File( path ) ) );
    }

    public boolean changeOwner( String dn, String path) {
        logger.debug( "changing owner of Slice " + path +" to " + dn );
        return false;
    }


    public boolean deleteDirectory( String uid, String pth ) {

        File f = new File( pth );
        if(! f.exists() )
            throw new NoSuchResourceException( "failed to delete dir " + pth + ": doesn't exists" );

        DirectoryAux.Utils.recursiveDelete(pth);

        return true;
    }

    public boolean deleteFiles( String uid, String pth, String filter ) {
        File dir = new File( pth );
        if( !dir.exists() )
            throw new NoSuchResourceException( "failed to delete files " + pth + File.separatorChar + filter + ": directory does not exist" );
        if( !dir.isDirectory() )
            throw new NoSuchResourceException( "failed to delete files " + pth + File.separatorChar + filter + ": " + pth + " is no directory" );

        final Pattern regex = Pattern.compile( filter );

        File[] files = dir.listFiles( new FilenameFilter() {
            @Override
            public boolean accept( File dir, String name ) {
                Matcher matcher = regex.matcher( name );

                return matcher.matches();
            }
        });

        boolean success = true;
        for( File f: files ) {
            if( !f.delete() )
                success = false;
        }
        
        return success;
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

    public boolean move( String src_path, String target_path ) {
        return 0 != rename( new File( src_path ), new File( target_path ) );
    }

    public boolean copyDir( String uid, String src_pth, String tgt_pth ) {
        throw new UnsupportedOperationException( "Please do that right now!" );
    }
}

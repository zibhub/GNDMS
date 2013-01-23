/*
 * Copyright 2008-2012 Zuse Institute Berlin (ZIB)
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
 *
 */

package de.zib.gndms.infra.grams;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @date: 10.08.12
 * @time: 13:58
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public abstract class AbstractDirectoryAux implements DirectoryAux {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    final static String WX = "300";
    final static String RO = "500";
    final static String RW = "700";

    final static int DELAY = 10000;
    final static int NUM_RETRYS = 5;


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


    public List< String > listContent( String path ) {
        final File dir = new File( path );
        if (dir == null || dir.list() == null) {
        	return new ArrayList<String>();
        }
        return Arrays.asList(dir.list());
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


    public boolean move( File from, File to ) {
        try {
            return move( from.getCanonicalPath(), to.getCanonicalPath() );
        } catch (IOException e) {
            throw new RuntimeException( e );
        }
    }

    public long diskUsage( String uid, String path ) {
        File file = new File( path );

        if( file.isDirectory() ) {
            long size = 0;

            final List<String> content = listContent(path);
            for( String c: content ) {
                size += diskUsage( uid, path + File.separatorChar + c );
            }

            return size;
        }
        else if ( file.isFile() ) {
            return file.length();
        }
        else
            return 0;
    }
}

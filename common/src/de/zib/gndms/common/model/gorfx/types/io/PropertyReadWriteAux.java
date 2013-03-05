package de.zib.gndms.common.model.gorfx.types.io;

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



import de.zib.gndms.common.model.gorfx.types.FutureTime;
import de.zib.gndms.common.model.gorfx.types.MinMaxPair;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.ISODateTimeFormat;

import java.util.*;

/**
 * Auxiliary methods for conversion into a property.
 * 
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 17.09.2008, Time: 15:39:59
 */
public class PropertyReadWriteAux {

    public static void writeList( @NotNull Properties prop, @NotNull String key, char sep, @NotNull Iterable it  ) {

        StringBuilder out = new StringBuilder();
        for( Object o : it )
            out.append( o.toString( ) ).append( sep );

        out.deleteCharAt( out.length() - 1 );

        prop.setProperty( key, out.toString( ) );
    }


    public static List<String> readList( @NotNull Properties prop, @NotNull String key, char sep ) {

        String s = prop.getProperty ( key );
        if( s != null )
            return Arrays.asList( s.split( ""+sep ) );

        return null;
    }

    
    public static void writeListMultiLine( @NotNull Properties prop, @NotNull String key, @NotNull Iterable it  ) {

        int i = 0;
		key = key + '.';
		for ( Object item : it )
			prop.setProperty( key + i++, item.toString() );
		prop.setProperty( key + "Count", Integer.toString( i ) );
    }


    public static List<String> readListMultiLine( @NotNull Properties prop, @NotNull String key ) {

        String s = prop.getProperty( key +".Count" );
        if( s == null )
            return null;

        int l = Integer.parseInt( s );
        ArrayList<String> al = new ArrayList<String>( l );
        key = key + '.';
        for ( int i=0 ; i < l; ++i )
            al.add( prop.getProperty(  key + i ) );

        return al;
    }


    public static <T,U> void writeMap( @NotNull Properties prop, @NotNull String key, @NotNull Map<T, U> om ) {

        Set<T> os = om.keySet();
        String dkey = key + '.';
        for( T o: os ) {
            if( om.get( o ) != null )
                prop.setProperty( dkey + o.toString( ), om.get( o ).toString( ) );
        }
        writeList( prop, key, ' ', os );
    }


    public static HashMap<String, String> readMap( @NotNull Properties prop, @NotNull String key ) {

        List<String> keys = readList( prop, key, ' ' );
        if( key.trim().equals( "" ) )
            return null;

        HashMap<String, String> hm = new HashMap<String, String>( );
        key = key + ".";
        for( String k : keys ) {
            String s = prop.getProperty( key + k );
       //     if( s == null )
       //         throw new IllegalStateException( "Missing property entry for " + key + k );
       // for some situations its desired that s can be null
            hm.put( k, s );
        }

        return hm;
    }

    public static HashMap<String, String> readMapFullKey( @NotNull Properties prop, @NotNull String key ) {

        List<String> keys = readList( prop, key, ' ' );
        if( key.trim().equals( "" ) )
            return null;

        HashMap<String, String> hm = new HashMap<String, String>( );
        key = key + ".";
        for( String k : keys ) {
            String s = prop.getProperty( key + k );
       //     if( s == null )
       //         throw new IllegalStateException( "Missing property entry for " + key + k );
       // for some situations its desired that s can be null
            hm.put( key + k, s );
        }

        return hm;
    }


    public static MinMaxPair readMinMaxPair( @NotNull Properties prop, @NotNull String minkey, @NotNull String maxkey  ) {

        return new MinMaxPair(
                readMandatoryDouble( prop, minkey ),
                readMandatoryDouble( prop, maxkey )
            );
    }


    public static double readMandatoryDouble( @NotNull Properties prop, @NotNull String key ) {

        String s = prop.getProperty( key );
        if( s == null )
            throw new MandatoryPropertyMissingException( "Missing \"double\" entry for " + key );

        return Double.parseDouble( s );
    }


    public static void writeISODateTime( @NotNull Properties prop, @NotNull String key, @NotNull DateTime tm ) {
        prop.setProperty( key, ISODateTimeFormat.dateTime( ).print( tm ) );
    }


    public static DateTime readISODateTime( @NotNull Properties prop, @NotNull String key ) {
        String s = pruneEmptyProperty( prop, key );
        if( s == null )
            return null;

        return new DateTime( s );
    }


    public static String dateToString( Calendar c ) {
        DateTime dt = new DateTime( c );
        return ISODateTimeFormat.dateTime( ).print( dt  );
    }


    public static Calendar dateToString( String s ) {

        return new DateTime( s ).toGregorianCalendar();
    }


	public static void writeFutureTime( @NotNull Properties prop, @NotNull String key, @NotNull FutureTime fm ) {
	    prop.setProperty( key, fm.toString() );
	}

	public static FutureTime readFutureTime( @NotNull Properties prop, @NotNull String key ) {
        String s = pruneEmptyProperty( prop, key );
        if( s == null )
            return null;

		try {
			long offsetInMs = Long.parseLong(s.trim());
			return FutureTime.atOffset(new Duration(offsetInMs));
		}
		catch (NumberFormatException nfe) {
            return FutureTime.atTime( new DateTime( s ) );
		}
    }


    public static String getMandatoryProperty( @NotNull Properties prop, @NotNull String key ) throws MandatoryPropertyMissingException {

        String s = pruneEmptyProperty( prop, key );
            prop.getProperty( key );
        if ( s == null )
            throw new MandatoryPropertyMissingException( key );

        return s;
    }


    public static String pruneEmptyProperty( @NotNull Properties prop, @NotNull String key ) {

        String s = prop.getProperty( key );
        if( s != null )
            return s.trim().length() > 0 ? s : null;

        return null;
    }
}

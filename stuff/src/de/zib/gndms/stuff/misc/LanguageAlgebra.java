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

package de.zib.gndms.stuff.misc;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.Map;

/**
 * @date: 19.06.12
 * @time: 11:22
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class LanguageAlgebra {
    /**
     * Get the largest prefix shared by a set of words.
     *
     * @param words The set of words to get the largest prefix from.
     * @return The greatest common prefix.
     */
    public static String getGreatestCommonPrefix( Collection< String > words ) {
        if( null == words || 0 == words.size() ) {
            throw new IllegalArgumentException( "Cannot build the greatest common prefix out of an empty set of words!" );
        }

        String commonprefix = words.iterator().next();

        for( String w: words ) {
            commonprefix = getCommonPrefix( commonprefix, w );
        }

        return commonprefix;
    }


    /**
     * Get the greatest common prefix of two strings.
     *
     * @param a A string.
     * @param b A string.
     * @return The greatest common prefix of both strings.
     */
    public static String getCommonPrefix( String a, String b ) {
        for( int i = 1; i < b.length(); ++i ) {
            if( a.length() < i ) {
                return a;
            }

            if( ! a.substring( i-1, i ).equals( b.substring( i-1, i ) ) ) {
                return a.substring( 0, i-1 );
            }
        }

        return b;
    }


    public static MultiValueMap< String,String > getMultiValueMapFromMap( Map< String, String > map ) {
        MultiValueMap< String, String > multiMap = new LinkedMultiValueMap< String, String >( map.size() );

        for( String k: map.keySet() ) {
            multiMap.add( k, map.get( k ) );
        }

        return multiMap;
    }
}

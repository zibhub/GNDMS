package de.zib.gndms.stuff.misc;
/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import java.io.PrintWriter;
import java.util.Collection;

/**
 * An immutable key with documentation
 *
 * This is undeliverable useful when defining some properties, option or the like,
 * which should be presented to some users.
 *
 * Instance of this class should be defined as singletons.
 *
 * @see DocumentedKey::createAndRegisterKey for an example
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 23.02.12  15:59
 */
public class DocumentedKey {
    
    public final String key;
    public final String doc;


    /**
     * @brief Creates an documented key
     *
     * @param key the key
     * @param doc the documentation, multiple lines are allowed \c '\\n' is the newline char.
     */
    public DocumentedKey( final String key, final String doc ) {

        this.key = key;
        this.doc = doc;
    }


    /**
     * @brief Writes the this key to an output writer
     *
     * Performs some primitive formatting suitable for properties or ini-files.
     *
     * Output will look like:
     * @verbatim
     * <commentary> <doc>
     * <key><assign>
     * @endverbatim
     *
     * @param out The output writer
     * @param commentary A symbol which marks lines as comments the output
     * @param assign A symbol which is used as assignment
     */
    public void asTemplate( PrintWriter out, String commentary, String assign  ) {
        
        String lines[] = doc.split( "\n" );
        for ( String line : lines )
            out.append( commentary ).append( ' ' ).append( line ).append( '\n' );

        out.append( key ).append( assign ).append( '\n' );
    }


    /**
     * @brief Writes the this key to an output writer as property
     *
     * Output will look like:
     * @verbatim
     * # <doc>
     * <key>=
     * @endverbatim
     *
     * @param out The output writer
     */
    public void asPropertiesTemplate( PrintWriter out ) {
        asTemplate( out, "#", "=" );
    }


    /**
     * Creates a documented key and adds it to a collection
     *
     * Often key are grouped together by a scope. This is represented by \e keys.
     *
     * With this in your class you can do something like:
     * \code
        public static final DocumentedKey KEY1;
        public static final DocumentedKey KEY2;
        public static final List<DocumentedKey> IMMUTABLE_KEYS;

        static {
            List<DocumentedKey> keys = new ArrayList<DocumentedKey>( 2 );
            KEY1 = createAndRegisterKey( keys, "key1", "this key is important" );
            KEY2 = createAndRegisterKey( keys, "key2", "that key is even more important" );
            IMMUTABLE_KEYS = Collections.unmodifiableList( keys );
        }
     * \endcode
     *
     * @param keys The collection
     * @param key The key
     * @param doc The documentation of the key
     *
     * @return The created key
     */
    public static DocumentedKey createAndRegisterKey( final Collection<DocumentedKey> keys,
                                                      final String key, final String doc )
    {
        DocumentedKey docKey = new DocumentedKey( key, doc );
        keys.add( docKey );
        return docKey;
    }

}

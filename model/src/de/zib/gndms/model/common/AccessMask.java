package de.zib.gndms.model.common;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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



import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.persistence.Column;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * An AccessMask is used to handle access rights for the access types user, group and other.
 *
 * Read, write and execute rights can be set or unset by using a bitmask.
 *
 *
 *
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 * @see AccessFlags for the representation of a bit mask.
 * User: mjorra, Date: 22.12.2008, Time: 13:13:43
 */
@Embeddable
public class AccessMask {

    // todo add serialVersionUId

    /**
     * AccessFlags is an enum for the different access types.
     *
     * Its value can be chosen by bit flags.
     * In detail an int will be interpreted as follows:
     *
     * <pre>
     * If we have an int x = x1 x2 x3  , in binary representation with x element of [0;7]
     * Then if set to 1 the meaning of the flags are
     *        x1 = Executable
     *        x2 = Writable
     *        x3 = Readable
     * </pre>
     *
     *
     *
     */
    public enum AccessFlags {
        READABLE( 0x4 ),
        WRITABLE( 0x2 ),
        EXECUTABLE( 0x1 ),
        READWRITE( 0x6 ),
        READEXECUT( 0X5 ),
        WRITEEXEC( 0X3 ),
        ALL( 0X7 ),
        NONE( 0x0 );

        /**
         * A bit mask containg the different access flags
         */
        private final int mask;

        /**
         * Maps a bit mask to its correspondig access flags.
         */
        private final static Map<Integer, AccessFlags> valueForFlag = new HashMap<Integer, AccessFlags>( );

        /**
         * If true, the {@code valueForFlag} Map will be initialized with all enum values
         */
        private static boolean uninitialized = true;


        /**
         * Sets the access flags according to the flags encoded by {@code msk}.
         * 
         * @param msk a value, encoding the access flags.
         */
        AccessFlags( int msk ) {
            mask = msk;
        }


        /**
         * Returns the access mask, containg the flags for the different access types.
         * 
         * @return the access mask, containg the flags for the different access types.
         */
        @Transient
        public int getMask( ) {
            return mask;    
        }


        /**
         * Returns a String representation of the mask.
         * @return a String representation of the mask.
         */
        public String toString( ) {
            return maskToString( this );
        }


        /**
         * Returns a String representation of {@code accessFlag}'s mask.
         * 
         * @param accessFlag an instance of AccessFlags.
         * @return a String represantation of {@code accessFlag}'s mask.
         */
        public String maskToString( AccessFlags accessFlag ) {

            return maskToString( accessFlag.getMask() );
        }


        /**
         * Returns the value of access mask, in decimal base, as String.
         * 
         * @param accessMask an access mask as int value.
         * @return  a String representation  of the access mask.
         */
        public String maskToString( int accessMask ) {
            return Integer.toString( accessMask );
        }

        /**
         * Returns an {@code AccessFlags} with the flags set as intended by the bit {@code mask}.
         * 
         * @param mask the bitmask containg the settings for the access configuration.
         * @return an {@code AccessFlags} with the flags set as intended by the bit {@code mask}.
         */
        public static AccessFlags flagsForMask( int mask ) {

            if( mask > 7 || mask < 0 )
                throw new IllegalArgumentException( "Numeric mask must be between 0x0 and 0x7. Received: " + mask );

            if( uninitialized ) {
                for ( AccessFlags f: AccessFlags.values() )
                    valueForFlag.put( f.getMask(), f );
                uninitialized = false;
            }

            return valueForFlag.get( mask );
        }


        /**
         * {@code s} must be a single character, matching an int in [0;7]
         * 
         * @see AccessFlags#fromChar(char) 
         */
        public static AccessFlags fromString( String s ) {
            return flagsForMask( Integer.valueOf( s ) );
        }



        /**
         * An {@code AccessFlags} type will be returned, set as intended by the bit mask of the
         * numerical value of the char.
         *
         * @param c a char, mathing an int in [0;7].
         * @return an {@code AccessFlags}, set as intended by the bit mask of the numerical value of the char.
         */
        public static AccessFlags fromChar( char c ) {
            return fromString( Character.toString( c ) );
        }
    }

    /**
     * An enum to distinguish rights for a {@code user}, a {@code group} and {@code other}.
     * The enum provides an index for all three entries, starting from 0 with user and incrementing in the order,
     * given above. 
     */
    public enum Ugo{
        USER( 0 ),
        GROUP( 1 ),
        OTHER( 2 );

        private final int index;

        Ugo( int idx ) {
            index = idx;
        }
    }

    /**
     * This array holds the access-rights for the different groups.
     * @see Ugo
     */
    private AccessFlags access[] = new AccessFlags[3];

    /**
     * Special attribute of the access masc.
     *
     * Can be s.th. like stickybit etc.
     */
    private Integer special = null;

    /**
     * Returns the access rights for a user.
     *
     * @return the access rights for a user.
     */
    @Transient
    public AccessFlags getUserAccess() {
        return getAccess()[ Ugo.USER.index ];
    }

    /**
     * Sets the access rights for a user.
     * @param userAccess the access rights for a user.
     */
    public void setUserAccess( AccessFlags userAccess ) {
        getAccess()[ Ugo.USER.index ] = userAccess;
    }


    public void setUserAccess( int userAccess ) {
        setUserAccess( AccessFlags.flagsForMask( userAccess ) );
    }


    @Transient
    public AccessFlags getGroupAccess() {
        return getAccess()[ Ugo.GROUP.index ];
    }


    public void setGroupAccess( AccessFlags groupAccess ) {
        getAccess()[ Ugo.GROUP.index ] = groupAccess;
    }

    public void setGroupAccess( int groupAccess ) {
        setGroupAccess( AccessFlags.flagsForMask( groupAccess ) );
    }


    @Transient
    public AccessFlags getOtherAccess() {
        return getAccess()[ Ugo.OTHER.index ];
    }


    public void setOtherAccess( AccessFlags otherAccess ) {
        getAccess()[ Ugo.OTHER.index ] = otherAccess;
    }


    public void setOtherAccess( int otherAccess ) {
        setOtherAccess( AccessFlags.flagsForMask( otherAccess ) );
    }


    public boolean queryFlagsOn( Ugo ugo, AccessFlags flag ) {

        return ( getAccess()[ ugo.index ].getMask() & flag.getMask() ) != 0;
    }


    public boolean queryFlagsOff( Ugo ugo, AccessFlags flag ) {
        return ! queryFlagsOn( ugo, flag );
    }

    
    public void addFlag( Ugo ugo, AccessFlags flag ) {

        getAccess()[ ugo.index ] = AccessFlags.flagsForMask(
            getAccess()[ ugo.index ].getMask( ) | flag.getMask()
        );
    }


    public void removeFlag( Ugo ugo, AccessFlags flag ) {

        getAccess()[ ugo.index ] = AccessFlags.flagsForMask(
            getAccess()[ ugo.index ].getMask( ) & ( flag.getMask() ^ 0x7 )
        );
    }

    /**
     * Returns the access masks' int values for user,group and other as String.
     * @return the access masks' int values for user,group and other as String
     */
    public String toString( ) {
        return
            (special != null ? String.valueOf( special ) : "" ) +
            getUserAccess().toString() + getGroupAccess().toString() + getOtherAccess().toString();
    }


    /**
     * Returns an {@code AccessMask} from a String containing the bitmasks for
     * all three access groups.
     *
     * The String must consist of exactly three characters, each one representing a number in [0;7].
     * The first character defines the access mask of {@code user}
     * The second character defines the access mask of {@code group}
     * The third character defines the access mask of {@code other}
     *
     * The string might be prefixed with the spacial byte (@see special).
     *
     * @param msk a String representing the access masks of the different access groups.
     * @return an {@code AccessMask} containing the bitmasks for
     * all three access groups.
     * @see AccessFlags
     */
    public static AccessMask fromString( String msk ) {
        AccessMask am = new AccessMask( );
        am.setAsString( msk );
        return am;
    }


    private void setSpecial( Integer i ) {
        special = i;
    }


    @Transient
    public Integer getSpecial() {
        return special;
    }


    /**
     * Returns the access flags belonging to user, group and other.
     * @return the access flags belonging to user, group and other.
     */
    @Transient
    public AccessFlags[] getAccess() {
        return access;
    }


    public void setAccess( AccessFlags[] access ) {
        this.access = access;
    }


    @Column( name="mask", nullable=false)
    public String getAsString( ) {
        return toString();
    }


    /**
     *
     * @see AccessFlags#fromString(String) 
     */
    public void setAsString( String msk ) {

        int l = msk.length( );

        if( l < 3 || l > 4 )
            throw new IllegalArgumentException( "msk must have the length 3 or 4. Argument is: " + msk );

        // AccessMask am = new AccessMask();
        int i = 0;
        if( l == 4 )
            setSpecial (  Integer.valueOf( "" + msk.charAt( i++ ) ) );

        setUserAccess( AccessFlags.fromChar( msk.charAt( i ) ) );
        setGroupAccess( AccessFlags.fromChar( msk.charAt( ++i ) ) );
        setOtherAccess( AccessFlags.fromChar( msk.charAt( ++i ) ) );
    }

}

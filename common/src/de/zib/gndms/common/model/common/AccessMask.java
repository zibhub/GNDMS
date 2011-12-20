package de.zib.gndms.common.model.common;

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

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;

/**
 * An AccessMask is used to handle access rights for the access types user, group and other.
 *
 * Read, write and execute rights can be set or unset by using a bit mask.
 *
 * @author  try ma ik jo rr a zib
 * @version $Id$
 * @see AccessFlags for the representation of a bit mask.
 * User: mjorra, Date: 22.12.2008, Time: 13:13:43
 */
public class AccessMask implements Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 3846269515013219026L;

	/**
     * AccessFlags is an enumeration for the different access types.
     *
     * Its value can be chosen by bit flags.
     * In detail an int value will be interpreted as follows:
     *
     * <pre>
     * If we have an int x = x1 x2 x3 in binary representation with x element of [0;7]
     * then if set to 1 the meaning of the flags are
     *        x3 = Executable
     *        x2 = Writable
     *        x1 = Readable
     * </pre>
     *
     */
    public enum AccessFlags {
    	/**
    	 * Read access.
    	 */
    	READABLE( 0x4 ),
    	/**
    	 * Write access.
    	 */
        WRITABLE( 0x2 ),
        /**
         * Execution access.
         */
        EXECUTABLE( 0x1 ),
        /**
         * Read and write access.
         */
        READWRITE( 0x6 ),
        /**
         * Read and execution access.
         */
        READEXECUT( 0X5 ),
        /**
         * write and execution access.
         */
        WRITEEXEC( 0X3 ),
        /**
         * Unrestricted access.
         */
        ALL( 0X7 ),
        /**
         * No access.
         */
        NONE( 0x0 );

    	/**
    	 * The maximal number for a flag.
    	 */
    	private static final int MAX = 7;
    	
       /**
         * A bit mask containing the different access flags.
         */
        private final int mask;

        /**
         * Maps a bit mask to its corresponding access flags.
         */
        private static final Map<Integer, AccessFlags> VALUEFORFLAG = new HashMap<Integer, AccessFlags>();

        /**
         * If true, the {@code valueForFlag} Map will be initialized with all enum values.
         */
        private static boolean uninitialized = true;

        /**
         * Sets the access flags according to the flags encoded by {@code msk}.
         * 
         * @param msk a value, encoding the access flags.
         */
        AccessFlags( final int msk ) {
            mask = msk;
        }

        /**
         * Returns the access mask, containing the flags for the different access types.
         * 
         * @return The access mask.
         */
        public int getMask( ) {
            return mask;    
        }

        /**
         * Returns a String representation of the mask.
         * @return The String.
         */
        @Override
		public String toString( ) {
            return maskToString( this );
        }

        /**
         * Returns a String representation of {@code accessFlag}'s mask.
         * 
         * @param accessFlag An instance of AccessFlags.
         * @return The String representation .
         */
        public String maskToString( final AccessFlags accessFlag ) {
            return maskToString( accessFlag.getMask() );
        }

        /**
         * Returns the value of access mask, in decimal base, as String.
         * 
         * @param accessMask an access mask as int value.
         * @return  a String representation  of the access mask.
         */
        public String maskToString( final int accessMask ) {
            return Integer.toString( accessMask );
        }

        /**
         * Returns an {@code AccessFlags} with the flags set as intended by the bit {@code mask}.
         * 
         * @param mask the bit mask containing the settings for the access configuration.
         * @return an {@code AccessFlags} with the flags set as intended by the bit {@code mask}.
         */
        public static AccessFlags flagsForMask( final int mask ) {
            if ( mask > MAX || mask < 0 ) {
                throw new IllegalArgumentException( "Numeric mask must be between 0x0 and 0x7. Received: " + mask );
            }
            if ( uninitialized ) {
                for ( AccessFlags f : AccessFlags.values() ) {
                    VALUEFORFLAG.put( f.getMask(), f );                    
                }
                uninitialized = false;
            }

            return VALUEFORFLAG.get( mask );
        }

        /**
         * {@code s} must be a single character, matching an int in [0;7].
         * 
         * @param s The character.
         * @return The corresponding access flags.
         * 
         * @see AccessFlags#fromChar(char) 
         */
        public static AccessFlags fromString( final String s ) {
            return flagsForMask( Integer.valueOf( s ) );
        }

        /**
         * An {@code AccessFlags} type will be returned, set as intended by the bit mask of the
         * numerical value of the char.
         *
         * @param c a char, matching an int in [0;7].
         * @return an {@code AccessFlags}, set as intended by the bit mask of the numerical value of the char.
         */
        public static AccessFlags fromChar( final char c ) {
            return fromString( Character.toString( c ) );
        }
    }

    /**
     * An enumeration to distinguish rights for a {@code user}, a {@code group} and {@code other}.
     * It provides an index for all three entries, starting from 0 with user and incrementing in the order
     * given above. 
     */
    public enum Ugo {
        /**
         * The user.
         */
    	USER( 0 ),
    	/**
    	 * The group.
    	 */
        GROUP( 1 ),
        /**
         * Others.
         */
        OTHER( 2 );

    	/**
    	 * The maximal number of different rights.
    	 */
    	private static final int MAXRIGHTS = 3;

        /**
         * The index.
         */
    	private final int index;

    	/**
    	 * Creates a new Ugo with the given index.
    	 * @param idx The index value.
    	 */
    	Ugo( final int idx ) {
            index = idx;
        }
    }
    
    /**
     * This array holds the access-rights for the different groups.
     * @see Ugo
     */
    private AccessFlags[] access = new AccessFlags[Ugo.MAXRIGHTS];

    /**
     * Special attribute of the access mask.
     *
     * Can be s.th. like sticky bit etc.
     */
    private Integer special = null;

    /**
     * Returns the access rights for a user.
     *
     * @return The access rights.
     */
    public final AccessFlags getUserAccess() {
        return getAccess()[ Ugo.USER.index ];
    }

    /**
     * Sets the access rights for a user.
     * @param userAccess The access rights.
     */
    public final void setUserAccess( final AccessFlags userAccess ) {
        getAccess()[ Ugo.USER.index ] = userAccess;
    }

    /**
     * Sets the access rights for a user.
     * @param userAccess The access rights as int.
     */ 
    public final void setUserAccess( final int userAccess ) {
        setUserAccess( AccessFlags.flagsForMask( userAccess ) );
    }

    /**
     * Returns the access rights for a group.
     *
     * @return The access rights..
     */
    public final AccessFlags getGroupAccess() {
        return getAccess()[ Ugo.GROUP.index ];
    }

   /**
    * Sets the access rights for a group.
    * @param groupAccess The access rights.
    */ 
    public final void setGroupAccess( final AccessFlags groupAccess ) {
        getAccess()[ Ugo.GROUP.index ] = groupAccess;
    }

    /**
     * Sets the access rights for a group.
     * @param groupAccess The access rights as int.
     */ 
    public final void setGroupAccess( final int groupAccess ) {
        setGroupAccess( AccessFlags.flagsForMask( groupAccess ) );
    }

    /**
     * Returns the access rights for others.
     *
     * @return The access rights.
     */
    public final AccessFlags getOtherAccess() {
        return getAccess()[ Ugo.OTHER.index ];
    }

    /**
     * Sets the access rights for others.
     * @param otherAccess The access rights.
     */ 
    public final void setOtherAccess( final AccessFlags otherAccess ) {
        getAccess()[ Ugo.OTHER.index ] = otherAccess;
    }
    
    /**
     * Sets the access rights for others.
     * @param otherAccess The access rights as int.
     */ 
    public final void setOtherAccess( final int otherAccess ) {
        setOtherAccess( AccessFlags.flagsForMask( otherAccess ) );
    }

    /**
     * Checks whether at least one of the given access values is set by the corresponding ugo in the access mask.
     * This method is especially useful for access flags read, write, or execute, then it returns true if the 
     * corresponding access is granted by the flag.
     * 
     * @param ugo The ugo rights.
     * @param flag The access flag.
     * @return true, if at least one access right of the flag is granted, otherwise false.
     */
    public final boolean queryFlagsOn( final Ugo ugo, final AccessFlags flag ) {

        return ( getAccess()[ ugo.index ].getMask() & flag.getMask() ) != 0;
    }

    /**
    * Checks whether none of the given access values is set by the corresponding ugo in the access mask.
     * @see queryFlagsOn
     *
     * @param ugo The ugo rights.
     * @param flag The access flag.
     * @return true, if no access right of the flag is granted, otherwise false.
     */
    public final boolean queryFlagsOff( final Ugo ugo, final AccessFlags flag ) {
        return !queryFlagsOn( ugo, flag );
    }

    /**
     * Adds the access rights of the given flag to the corresponding ugo of the access mask.
     * 
     * @param ugo The ugo rights.
     * @param flag The flag.
     */
    public final void addFlag( final Ugo ugo, final AccessFlags flag ) {

        getAccess()[ ugo.index ] = AccessFlags.flagsForMask(
            getAccess()[ ugo.index ].getMask( ) | flag.getMask()
        );
    }

    /**
     * Deletes the access rights of the given flag to the corresponding ugo of the access mask.
     * 
     * @param ugo The ugo rights.
     * @param flag The flag.
     */
    public final void removeFlag( final Ugo ugo, final AccessFlags flag ) {

        getAccess()[ ugo.index ] = AccessFlags.flagsForMask(
            getAccess()[ ugo.index ].getMask( ) & ( flag.getMask() ^ AccessFlags.ALL.getMask() )
        );
    }

    /**
     * Returns the access masks' int values for user, group and other as String.
     * @return The String value.
     */
    @Override
	public final String toString( ) {
    	String s;
    	if (special != null) {
    		s = String.valueOf( special );
    	} else {
    		s = "";
    	}
    	return s + getUserAccess().toString() + getGroupAccess().toString() + getOtherAccess().toString();
    }

    public final Integer getIntValue( ) {
        return (
                ( getSpecial() != null ?
                        getSpecial() : 0 )  * 512
                + getUserAccess().getMask() * 64
                + getGroupAccess().getMask()* 8
                + getOtherAccess().getMask()
                );
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
    public static AccessMask fromString( final String msk ) {
        AccessMask am = new AccessMask( );
        am.setAsString( msk );
        return am;
    }

    /**
     * Sets the special attribute.
     * @param i The special value.
     */
    private void setSpecial( final Integer i ) {
        special = i;
    }


   /**
    * Returns the special attribute.
    * @return The special value.
    */
    public final Integer getSpecial() {
        return special;
    }


    /**
     * Returns the access flags belonging to user, group and other.
     * @return The access flags.
     */
    public final AccessFlags[] getAccess() {
        return access;
    }

    /**
     * Sets the access flags.
     * @param access The access flags.
     */
    public final void setAccess( final AccessFlags[] access ) {
        this.access = access;
    }

    /**
     * Returns the access mask as String.
     * @return The String value.
     */
    public final  String getAsString( ) {
        return toString();
    }

    /**
     * Sets the access flags from a String.
     * @param msk The given String.
     * @see AccessFlags#fromString(String) 
     */
    public final  void setAsString( final String msk ) {

        int l = msk.length( );

        if ( l < Ugo.MAXRIGHTS || l > Ugo.MAXRIGHTS + 1 ) {
            throw new IllegalArgumentException( "msk must have the length 3 or 4. Argument is: " + msk );
        }
        // AccessMask am = new AccessMask();
        int i = 0;
        if ( l == Ugo.MAXRIGHTS + 1 ) {
            setSpecial( Integer.valueOf( "" + msk.charAt( i++ ) ) );
        }
        setUserAccess( AccessFlags.fromChar( msk.charAt( i ) ) );
        setGroupAccess( AccessFlags.fromChar( msk.charAt( ++i ) ) );
        setOtherAccess( AccessFlags.fromChar( msk.charAt( ++i ) ) );
    }
    
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() == getClass()) {
			AccessMask mask = (AccessMask) obj;
			return mask.getAsString().equals(getAsString());
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		final int start = 18;
		final int multi = 23;
		int hashCode = start;
		hashCode = hashCode * multi + getAsString().hashCode();
		return hashCode;

	}
}

package de.zib.gndms.model.common;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.persistence.Column;
import java.util.Map;
import java.util.HashMap;
import java.io.Serializable;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 * <p/>
 * User: mjorra, Date: 22.12.2008, Time: 13:13:43
 */

@Embeddable
public class AccessMask implements Serializable {
    // todo add serialVersionUId

    public enum AccessFlags {
        READABLE( 0x4 ),
        WRITABLE( 0x2 ),
        EXECUTABLE( 0x1 ),
        READWRITE( 0x6 ),
        READEXECUT( 0X5 ),
        WRITEEXEC( 0X3 ),
        ALL( 0X7 ),
        NONE( 0x0 );

        private final int mask;
        private final static Map<Integer, AccessFlags> valueForFlag = new HashMap<Integer, AccessFlags>( );

        static{
            for ( AccessFlags f: AccessFlags.values() )
                valueForFlag.put( f.getMask(), f );
        }



        AccessFlags( int msk ) {
            mask = msk;
        }


        @Transient
        public int getMask( ) {
            return mask;    
        }
        

        public String toString( ) {
            return maskToString( this );
        }


        public String maskToString( AccessFlags accessFlag ) {

            return maskToString( accessFlag.getMask() );
        }


        public String maskToString( int accessFlag ) {
            return Integer.toString( accessFlag );
        }


        public static AccessFlags flagsForMask( int mask ) {

            if( mask > 7 || mask < 0 )
                throw new IllegalArgumentException( "Numeric mask must be between 0x0 and 0x7. Received: " + mask );

            return valueForFlag.get( mask );
        }


        // s must only contain a single char which matches an int in [0;7]
        public static AccessFlags fromString( String s ) {
            return flagsForMask( Integer.valueOf( s ) );
        }

        public static AccessFlags fromChar( char c ) {
            return fromString( Character.toString( c ) );
        }
    }


    public enum Ugo{
        USER( 0 ),
        GROUP( 1 ),
        OTHER( 2 );

        private final int index;

        Ugo( int idx ) {
            index = idx;
        }
    }


    public AccessFlags access[] = new AccessFlags[3];

    @Transient
    public AccessFlags getUserAccess() {
        return getAccess()[ Ugo.USER.index ];
    }


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


    public String toString( ) {
        return getUserAccess().toString() + getGroupAccess().toString() + getOtherAccess().toString();
    }


    public static AccessMask fromString( String msk ) {

        if( msk.length( ) != 3 )
            throw new IllegalArgumentException( "msk must have the length 3. Argument is: " + msk );

        AccessMask am = new AccessMask();
        am.setUserAccess( AccessFlags.fromChar( msk.charAt( 0 ) ) );
        am.setGroupAccess( AccessFlags.fromChar( msk.charAt( 1 ) ) );
        am.setOtherAccess( AccessFlags.fromChar( msk.charAt( 2 ) ) );

        return am;
    }

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

    public void setAsString( String msk ) {
        setUserAccess( AccessFlags.fromChar( msk.charAt( 0 ) ) );
        setGroupAccess( AccessFlags.fromChar( msk.charAt( 1 ) ) );
        setOtherAccess( AccessFlags.fromChar( msk.charAt( 2 ) ) );
    }
}

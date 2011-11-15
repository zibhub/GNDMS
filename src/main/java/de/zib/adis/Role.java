package de.zib.adis;

public enum Role
{
        DMS,
        WSS;

        @Override
        public String toString( )
        {
                String s = super.toString();
                return s.substring( 0, 1 )+s.substring( 1 ).toLowerCase();
        }
}


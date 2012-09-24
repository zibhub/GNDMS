package de.zib.gndms.common.dspace;

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


import de.zib.gndms.common.logic.config.Configuration;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.io.Serializable;

/**
 * The slice configuration checks and accesses a ConfigHolder for a slice, which
 * has to consist (at least) of the following fields: <br>
 * size - the maximal size of the slice<br>
 * termination - the termination time of the slice as number representing
 * standard base time.
 * 
 * @author Ulrike Golas
 * 
 */

public class SliceConfiguration implements Configuration, Serializable {

    private static final long serialVersionUID = 7556343794310284647L;

	/**
	 * The key for the slice's size.
	 */
    public final static String SLICE_SIZE = "sliceSize";

	/**
	 * The key for the slice's DEADLINE time.
	 */
    public final static String TERMINATION_TIME = "terminationTime";

    /**
	 * The maximum size of the slice.
	 */
	private Long size;
	/**
	 * The termination time of the slice.
	 */
	private DateTime terminationTime;

    protected String group;


    /**
     * The standard constuructor.
     *
     * To construct standard instances
     */
    public SliceConfiguration() {
    }


    /**
	 * Constructs a SliceConfiguration.
	 * 
	 * @param size
	 *            The maximum size.
	 * @param termination
	 *            The termination time.
	 */
	public SliceConfiguration( final Long size, final Long termination ) {
		setSize( size );
		setTerminationTime( termination );
	}

	/**
	 * Constructs a SliceConfiguration.
	 * 
	 * @param size
	 *            The maximum size.
	 * @param termination
	 *            The termination time.
	 */
	public SliceConfiguration( final Long size, final DateTime termination ) {
		setSize( size );
		setTerminationTime( termination );
	}


    public boolean isValid() {
        if( size != null && size < 0 )
            return false;
        return true;
    }


	/**
	 * Returns the size of a slice configuration.
	 * 
	 * @return The size.
	 */
	public final Long getSize() {
		return size;
	}

	/**
	 * Sets the size of a slice configuration.
	 * 
	 * @param size
	 *            The size.
	 */
	public final void setSize( final Long size ) {
		this.size = size;
	}

	/**
	 * Returns the termination time of a slice configuration as DateTime.
	 * 
	 * @return The termination time.
	 */
	public final DateTime getTerminationTime() {
		return terminationTime;
	}

	/**
	 * Returns the termination time of a slice configuration as long value.
	 * 
	 * @return The termination time.
	 */
	public final Long getTerminationTimeAsLong() {
        if( null == terminationTime )
            return null;
		return terminationTime.getMillis();
	}

	/**
	 * Sets the termination time of a slice configuration as DateTime.
	 * 
	 * @param terminationTime
	 *            The termination time.
	 */
	public final void setTerminationTime( final DateTime terminationTime ) {
		this.terminationTime = terminationTime;
	}

	/**
	 * Sets the termination time of a slice configuration as long value.
	 * 
	 * @param termination
	 *            The termination time.
	 */
	public final void setTerminationTime(final long termination) {
		terminationTime = new DateTime().plus( termination );
	}

	@Override
	public final String getStringRepresentation() {
        StringBuilder s = new StringBuilder();
        if( null != getSize() )
            s.append( SLICE_SIZE ).append( ": '" ).append( size ).append( "'; " );
        if( null != getTerminationTime() )
            s.append( TERMINATION_TIME ).append( ": '" ).append(
                ISODateTimeFormat.dateTime().print( terminationTime ) ).append( "'; "
        );
		return s.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return getStringRepresentation();
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
			SliceConfiguration config = (SliceConfiguration) obj;

            if( ( config.getSize() == null ) != ( getSize() == null ) )
                return false;
            if( ( config.getTerminationTime() == null ) != ( getTerminationTime() == null ) )
                return false;
            
            if( config.getSize() != null && !config.getSize().equals( getSize() ) )
                return false;
            if( config.getTerminationTime() != null && !config.getTerminationTime().equals( getTerminationTime() ) )
                return false;

            return true;
        }
        else {
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
		final int start = 28;
		final int multi = 29;
		int hashCode = start;
		final int length = 32;
		hashCode = hashCode * multi + (int) (size ^ (size >>> length));
		hashCode = hashCode * multi + terminationTime.hashCode();
		return hashCode;

	}


    public String getGroup() {
        return group;
    }


    public void setGroup( String group ) {
        this.group = group;
    }
}

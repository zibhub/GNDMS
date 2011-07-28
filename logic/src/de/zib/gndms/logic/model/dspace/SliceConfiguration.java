package de.zib.gndms.logic.model.dspace;

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

import java.util.Calendar;
import java.util.GregorianCalendar;

import de.zib.gndms.common.logic.config.Configuration;
import de.zib.gndms.model.dspace.Slice;

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

public class SliceConfiguration implements Configuration {
	/**
	 * The key for the slice's size.
	 */
	public static final String SIZE = "size";
	/**
	 * The key for the slice's termination time.
	 */
	public static final String TERMINATION = "termination";

	/**
	 * The maximum size of the slice.
	 */
	private long size;
	/**
	 * The termination time of the slice.
	 */
	private Calendar terminationTime;

	/**
	 * Constructs a SliceConfiguration.
	 * 
	 * @param size
	 *            The maximum size.
	 * @param termination
	 *            The termination time.
	 */
	public SliceConfiguration(final long size,
			final long termination) {
		this.size = size;
		setTerminationTime(termination);
	}

	/**
	 * Constructs a SliceConfiguration.
	 * 
	 * @param size
	 *            The maximum size.
	 * @param termination
	 *            The termination time.
	 */
	public SliceConfiguration(final long size,
			final Calendar termination) {
		this.size = size;
		this.terminationTime = termination;
	}

	@Override
	public final boolean isValid() {
		return (size >= 0 && terminationTime != null);
	}

	/**
	 * Returns the size of a slice configuration.
	 * 
	 * @return The size.
	 */
	public final long getSize() {
		return size;
	}

	/**
	 * Sets the size of a slice configuration.
	 * 
	 * @param size
	 *            The size.
	 */
	public final void setSize(final long size) {
		this.size = size;
	}

	/**
	 * Returns the termination time of a slice configuration as Calendar.
	 * 
	 * @return The termination time.
	 */
	public final Calendar getTerminationTime() {
		return terminationTime;
	}

	/**
	 * Returns the termination time of a slice configuration as long value.
	 * 
	 * @return The termination time.
	 */
	public final long getTerminationTimeAsLong() {
		return terminationTime.getTimeInMillis();
	}

	/**
	 * Sets the termination time of a slice configuration as Calendar.
	 * 
	 * @param terminationTime
	 *            The termination time.
	 */
	public final void setTerminationTime(final Calendar terminationTime) {
		this.terminationTime = terminationTime;
	}

	/**
	 * Sets the termination time of a slice configuration as long value.
	 * 
	 * @param termination
	 *            The termination time.
	 */
	public final void setTerminationTime(final long termination) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTimeInMillis(termination);
		this.terminationTime = cal;
	}

	@Override
	public final String getStringRepresentation() {
		StringBuffer s = new StringBuffer();
		s.append(SIZE + " : '" + size + "'; ");
		s.append(TERMINATION + " : '" + terminationTime.getTimeInMillis()
				+ "'; ");
		return s.toString();
	}

	/**
	 * Returns the slice configuration of a given slice.
	 * 
	 * @param slice
	 *            The slice.
	 * @return The slice configuration.
	 */
	public static final SliceConfiguration getSliceConfiguration(Slice slice) {
		return new SliceConfiguration(slice.getTotalStorageSize(), slice.getTerminationTime().getTimeInMillis());
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
			return (config.getSize() == size
					&& config.getTerminationTime().equals(terminationTime));
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
		final int start = 28;
		final int multi = 29;
		int hashCode = start;
		final int length = 32;
		hashCode = hashCode * multi + (int) (size ^ (size >>> length));
		hashCode = hashCode * multi + terminationTime.hashCode();
		return hashCode;

	}
}

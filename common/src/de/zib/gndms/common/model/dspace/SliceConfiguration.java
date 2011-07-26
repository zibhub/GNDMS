package de.zib.gndms.common.model.dspace;

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

/**
 * The slice configuration checks and accesses a ConfigHolder for a slice, which
 * has to consist (at least) of the following fields: <br>
 * directory - the (relative) path of the slice as text<br>
 * owner - the owner of the slice as text<br>
 * termination - the termination time of the slice as number representing
 * standard base time.
 * 
 * @author Ulrike Golas
 * 
 */

public class SliceConfiguration implements Configuration {
	/**
	 * The key for the slice's directory.
	 */
	public static final String DIRECTORY = "directory";
	/**
	 * The key for the slice's owner.
	 */
	public static final String OWNER = "owner";
	/**
	 * The key for the slice's termination time.
	 */
	public static final String TERMINATION = "termination";

	/**
	 * The directory of the slice.
	 */
	private String directory;
	/**
	 * The owner of the slice.
	 */
	private String owner;
	/**
	 * The termination time of the slice.
	 */
	private Calendar terminationTime;

	/**
	 * Constructs a SliceConfiguration.
	 * 
	 * @param directory
	 *            The directory.
	 * @param owner
	 *            The owner.
	 * @param termination
	 *            The termination time.
	 */
	public SliceConfiguration(final String directory, final String owner,
			final long termination) {
		this.directory = directory;
		this.owner = owner;
		setTerminationTime(termination);
	}

	/**
	 * Constructs a SliceConfiguration.
	 * 
	 * @param directory
	 *            The directory.
	 * @param owner
	 *            The owner.
	 * @param termination
	 *            The termination time.
	 */
	public SliceConfiguration(final String directory, final String owner,
			final Calendar termination) {
		this.directory = directory;
		this.owner = owner;
		this.terminationTime = termination;
	}

	@Override
	public final boolean isValid() {
		return (directory != null && owner != null && terminationTime != null);
	}

	/**
	 * Returns the directory of a slice configuration.
	 * 
	 * @return The directory.
	 */
	public final String getDirectory() {
		return directory;
	}

	/**
	 * Sets the directory of a slice configuration.
	 * 
	 * @param directory
	 *            The directory.
	 */
	public final void setDirectory(final String directory) {
		this.directory = directory;
	}

	/**
	 * Returns the owner of a slice configuration.
	 * 
	 * @return The owner.
	 */
	public final String getOwner() {
		return owner;
	}

	/**
	 * Sets the owner of a slice configuration.
	 * 
	 * @param owner
	 *            The owner.
	 */
	public final void setOwner(final String owner) {
		this.owner = owner;
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
	public final String displayConfiguration() {
		String s = "";
		s = s.concat(DIRECTORY + " : '" + directory + "'; ");
		s = s.concat(OWNER + " : '" + owner + "'; ");
		s = s.concat(TERMINATION + " : '" + terminationTime.getTimeInMillis()
				+ "'; ");
		return s;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public final String toString() {
		return displayConfiguration();
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
			return (config.getDirectory().equals(directory)
					&& config.getOwner().equals(owner) 
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
		hashCode = hashCode * multi + directory.hashCode();
		hashCode = hashCode * multi + owner.hashCode();
		hashCode = hashCode * multi + terminationTime.hashCode();
		return hashCode;

	}
}

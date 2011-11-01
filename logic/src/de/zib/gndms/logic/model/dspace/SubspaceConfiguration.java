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

import de.zib.gndms.common.logic.config.Configuration;
import de.zib.gndms.common.logic.config.SetupMode;
import de.zib.gndms.common.logic.config.WrongConfigurationException;
import de.zib.gndms.model.dspace.Subspace;

/**
 * The subspace configuration checks and accesses a ConfigHolder for a subspace,
 * which has to consist (at least) of the following fields: <br> 
 * path - the path of the subspace as text <br>
 * gsiFtpPath - the path for gsiFtp access as text <br>
 * visible - whether the subspaces meta subspace is visible as boolean<br>
 * size - the maximal size of the subspace as number<br>
 * mode - an entry in {@link SetupMode}.
 * 
 * @author Ulrike Golas
 * 
 */
public class SubspaceConfiguration implements Configuration {

	/**
	 * The key for the subspace's path.
	 */
	public static final String PATH = "path";
	/**
	 * The key for the subspace's gsiftp-path.
	 */
	public static final String GSIFTPPATH = "gsiFtpPath";
	/**
	 * The key for the subspace's visibility.
	 */
	public static final String VISIBLE = "visible";
	/**
	 * The key for the subspace's size.
	 */
	public static final String SIZE = "size";
	/**
	 * The key for the subspace's mode.
	 */
	public static final String MODE = "mode";

	/**
	 * The path of the subspace.
	 */
	private String path;
	/**
	 * The gsi ftp path of the subspace.
	 */
	private String gsiFtpPath;
	/**
	 * The visibility of the subspace.
	 */
	private boolean visible;
	/**
	 * The size of the subspace.
	 */
	private long size;
	/**
	 * The setup mode of the subspace.
	 */
	private SetupMode mode;
	
	/**
	 * Constructs a SubspaceConfiguration.
	 * @param path The path.
	 * @param gsiFtpPath The gsi ftp path.
	 * @param visible The visibility.
	 * @param size the size.
	 * @param mode The setup mode.
	 */
	public SubspaceConfiguration(final String path, final String gsiFtpPath, 
			final boolean visible, final long size, final SetupMode mode) {
		this.path = path;
		this.gsiFtpPath = gsiFtpPath;
		this.visible = visible;
		this.size = size;
		this.mode = mode;
	}

	/**
	 * Constructs a SubspaceConfiguration.
	 * @param path The path.
	 * @param gsiFtpPath The gsi ftp path.
	 * @param visible The visibility.
	 * @param size the size.
	 * @param mode The setup mode.
	 */
	public SubspaceConfiguration(final String path, final String gsiFtpPath, 
			final boolean visible, final long size, final String mode) {
		this.path = path;
		this.gsiFtpPath = gsiFtpPath;
		this.visible = visible;
		this.size = size;
		setMode(mode);
	}

	/**
	 * Converts a Configuration into a SubspaceConfiguration, if possible, and
	 * returns it, if valid.
	 * 
	 * @param config
	 *            The given configuration.
	 * @return The valid SubspaceConfiguration.
	 */
	public static SubspaceConfiguration checkSubspaceConfig(final Configuration config) {
		try {
			SubspaceConfiguration subspaceConfig = (SubspaceConfiguration) config;
			if (subspaceConfig.isValid()) {
				return subspaceConfig;
			} else {
				throw new WrongConfigurationException(
						"Wrong subspace configuration");
			}
		} catch (ClassCastException e) {
			throw new WrongConfigurationException(
					"Wrong subspace configuration");
		}
	}

	@Override
	public final boolean isValid() {
		return (path != null && gsiFtpPath != null && size >= 0);
	}

	@Override
	public final String getStringRepresentation() {
		StringBuilder s = new StringBuilder();
		s.append(PATH + " : '" + path + "'; ");
		s.append(GSIFTPPATH + " : '" + gsiFtpPath + "'; ");
		s.append(VISIBLE + " : '" + visible + "'; ");
		s.append(SIZE + " : '" + size + "'; ");
		s.append(MODE + " : '" + mode + "'; ");
		return s.toString();
	}

	/**
	 * Returns the path of a subspace configuration.
	 * @return the path
	 */
	public final String getPath() {
		return path;
	}

	/**
	 * Sets the path of a subspace configuration.
	 * @param path the path to set
	 */
	public final void setPath(final String path) {
		this.path = path;
	}

	/**
	 * Returns the gsi ftp path of a subspace configuration.
	 * @return the gsiFtpPath
	 */
	public final String getGsiFtpPath() {
		return gsiFtpPath;
	}

	/**
	 * Sets the gsi ftp path of a subspace configuration.
	 * @param gsiFtpPath the gsiFtpPath to set
	 */
	public final void setGsiFtpPath(final String gsiFtpPath) {
		this.gsiFtpPath = gsiFtpPath;
	}

	/**
	 * Returns the visibility of a subspace configuration.
	 * @return the visible
	 */
	public final boolean isVisible() {
		return visible;
	}

	/**
	 * Sets the visibility of a subspace configuration.
	 * @param visible the visible to set
	 */
	public final void setVisible(final boolean visible) {
		this.visible = visible;
	}

	/**
	 * Returns the size of a subspace configuration.
	 * @return the size
	 */
	public final long getSize() {
		return size;
	}

	/**
	 * Sets the size of a subspace configuration.
	 * @param size the size to set
	 */
	public final void setSize(final long size) {
		this.size = size;
	}

	/**
	 * Returns the mode of a subspace configuration.
	 * @return the mode
	 */
	public final SetupMode getMode() {
		return mode;
	}

	/**
	 * Sets the mode of a subspace configuration.
	 * @param mode the mode to set
	 */
	public final void setMode(final SetupMode mode) {
		this.mode = mode;
	}

	/**
	 * Sets the mode of a subspace configuration as String.
	 * @param mode the mode to set
	 */
	public final void setMode(final String mode) {
		try {
			this.mode = SetupMode.valueOf(mode);
		} catch (IllegalArgumentException e) {
			throw new WrongConfigurationException(mode + " is no valid setup mode.");
		} catch (NullPointerException e) {
			throw new WrongConfigurationException("Mode is null.");
		}
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

	/**
	 * Constructs the subspace configuration of a subspace.
	 * 
	 * @param subspace The subspace.
	 * @return The configuration.
	 */
    public static final SubspaceConfiguration getSubspaceConfiguration(Subspace subspace) {
		return new SubspaceConfiguration(subspace.getPath(), subspace.getGsiFtpPath(), subspace.getMetaSubspace().isVisibleToPublic(), subspace.getAvailableSize(), "READ");
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
			SubspaceConfiguration config = (SubspaceConfiguration) obj;
			return (config.getPath().equals(path)
					&& config.getGsiFtpPath().equals(gsiFtpPath) 
					&& config.isVisible() == visible 
					&& config.getSize() == size 
					&& config.getMode().equals(mode));
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
		final int start = 15;
		final int multi = 23;
		int hashCode = start;
		hashCode = hashCode * multi + path.hashCode();
		hashCode = hashCode * multi + gsiFtpPath.hashCode();
		if (visible) {
			hashCode = hashCode * multi + 1;
		}
		final int length = 32;
		hashCode = hashCode * multi + (int) (size ^ (size >>> length));
		hashCode = hashCode * multi + mode.hashCode();
		return hashCode;
	}


}

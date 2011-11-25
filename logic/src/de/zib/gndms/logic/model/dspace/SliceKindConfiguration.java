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
import de.zib.gndms.common.logic.config.WrongConfigurationException;
import de.zib.gndms.common.model.common.AccessMask;
import de.zib.gndms.model.dspace.SliceKind;

/**
 * The slice kind configuration checks and accesses a ConfigHolder for a slice kind,
 * which has to consist (at least) of the following fields: <br> 
 * uri - the uri for the slice kind as text<br>
 * permission - the permissions of the slice kind as text according to an {@link AccessMask}<br>
 * and may also contain: <br>
 * metasubspaces - the meta-subspaces of the slice kind as text.
 * 
 * @author Ulrike Golas
 * 
 */
// TODO does that make sense, how to get the meta-subspaces from the string?

public class SliceKindConfiguration implements Configuration {

	/**
	 * The key for the slice kind's uri.
	 */
	public static final String URI = "uri";
	/**
	 * The key for the slice kind's permission.
	 */
	public static final String PERMISSION = "permission";
	/**
	 * The key for the slice kind's subspaces.
	 */
	public static final String SUBSPACES = "subspaces";

	/**
	 * The uri of the slice kind.
	 */
	private String uri;
	/**
	 * The permission of the slice kind.
	 */
	private AccessMask permission;
	/**
	 * The subspaces of the slice kind.
	 */
	private String subspaces;

	/**
	 * Constructs a SliceKindConfiguration.
	 * @param uri The uri.
	 * @param permission The permission as long value.
	 * @param subspaces The subspaces.
	 */
	public SliceKindConfiguration(final String uri, final String permission, final String subspaces) {
		this.uri = uri;
		setPermission(permission);
		this.subspaces = subspaces;
	}

	/**
	 * Constructs a SliceKindConfiguration.
	 * @param uri The uri.
	 * @param permission The permission as long value.
	 * @param subspaces The subspaces.
	 */
	public SliceKindConfiguration(final String uri, final AccessMask permission, final String subspaces) {
		this.uri = uri;
		this.permission = permission;
		this.subspaces = subspaces;
	}

	/**
	 * Converts a Configuration into a SliceKindConfiguration, if possible, and
	 * returns it, if valid.
	 * 
	 * @param config
	 *            The given configuration.
	 * @return The valid SliceKindConfiguration.
	 */
	public static SliceKindConfiguration checkSliceKindConfig(final Configuration config) {
		try {
			SliceKindConfiguration sliceKindConfig = (SliceKindConfiguration) config;
			if (sliceKindConfig.isValid()) {
				return sliceKindConfig;
			} else {
				throw new WrongConfigurationException(
						"Wrong slice kind configuration");
			}
		} catch (ClassCastException e) {
			throw new WrongConfigurationException(
					"Wrong slice kind configuration");
		}
	}

	@Override
	public final boolean isValid() {
		return (uri != null && permission != null);
	}


	@Override
	public final String getStringRepresentation() {
        StringBuilder s = new StringBuilder();
        s.append( URI + " : '" ).append( uri ).append( "'; " );
        s.append( PERMISSION + " : '" ).append( permission.getAsString() ).append( "'; " );
		if (subspaces != null) {
            s.append( SUBSPACES + " : '" ).append( subspaces ).append( "'; " );
		}
		return s.toString();
	}

	/**
	 * Returns the uri of a slice kind configuration.
	 * @return the uri
	 */
	public final String getUri() {
		return uri;
	}

	/**
	 * Sets the uri of a slice kind configuration.
	 * @param uri the uri to set
	 */
	public final void setUri(final String uri) {
		this.uri = uri;
	}

	/**
	 * Returns the permission of a slice kind configuration.
	 * @return the permission
	 */
	public final AccessMask getPermission() {
		return permission;
	}

	/**
	 * Sets the permission of a slice kind configuration.
	 * @param permission the permission to set
	 */
	public final void setPermission(final AccessMask permission) {
		this.permission = permission;
	}

	/**
	 * Returns the permission of a slice kind configuration.
	 * @return the permission
	 */
	public final String getPermissionAsString() {
		return permission.getAsString();
	}

	/**
	 * Sets the permission of a slice kind configuration.
	 * @param permission the permission to set
	 */
	public final void setPermission(final String permission) {
		try {
			this.permission = AccessMask.fromString(permission);
		} catch (IllegalArgumentException e) {
			throw new WrongConfigurationException(permission + " is no valid acces mask value.");
		} catch (NullPointerException e) {
		throw new WrongConfigurationException("Permission is null.");
	}
	}
	
	/**
	 * Returns the subspaces of a slice kind configuration.
	 * @return the subspaces
	 */
	public final String getSubspaces() {
		return subspaces;
	}

	/**
	 * Sets the subspaces of a slice kind configuration.
	 * @param subspaces the subspaces to set
	 */
	public final void setSubspaces(final String subspaces) {
		this.subspaces = subspaces;
	}

	/**
	 * Constructs the slice kind configuration of a subspace.
	 * 
	 * @param slicekind The subspace.
	 * @return The configuration.
	 */
    public static final SliceKindConfiguration getSliceKindConfiguration(SliceKind slicekind) {
		return new SliceKindConfiguration(slicekind.getURI(), slicekind.getPermission(), slicekind.getSubspaces().toString());
	}

	/* (non-Javadoc)
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
			SliceKindConfiguration config = (SliceKindConfiguration) obj;
			return (config.getUri().equals(uri)
					&& config.getPermission().equals(permission) 
					&& config.getSubspaces().equals(subspaces));
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
		final int start = 35;
		final int multi = 17;
		int hashCode = start;
		hashCode = hashCode * multi + uri.hashCode();
		hashCode = hashCode * multi + permission.hashCode();
		hashCode = hashCode * multi + subspaces.hashCode();
		return hashCode;

	}
}

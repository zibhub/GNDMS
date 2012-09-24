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
import de.zib.gndms.common.logic.config.WrongConfigurationException;
import de.zib.gndms.common.model.common.AccessMask;

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
	public static final String SUBSPACE = "subspace";

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
	private String subspace;

	/**
	 * Constructs a SliceKindConfiguration.
	 * @param uri The uri.
	 * @param permission The permission as long value.
	 * @param subspaces The subspaces.
	 */
	public SliceKindConfiguration(final String uri, final String permission, final String subspaces) {
		this.uri = uri;
		setPermission(permission);
		this.subspace = subspace;
	}

	/**
	 * Constructs a SliceKindConfiguration.
	 * @param uri The uri.
	 * @param permission The permission as long value.
	 * @param subspace The subspace.
	 */
	public SliceKindConfiguration(final String uri, final AccessMask permission, final String subspace) {
		this.uri = uri;
		this.permission = permission;
		this.subspace = subspace;
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
		if (subspace != null) {
            s.append( SUBSPACE + " : '" ).append( subspace ).append( "'; " );
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
	 * Returns the subspace of a slice kind configuration.
	 * @return the subspace
	 */
	public final String getSubspace() {
		return subspace;
	}

	/**
	 * Sets the subspace of a slice kind configuration.
	 * @param subspace the subspace to set
	 */
	public final void setSubspace(final String subspace) {
		this.subspace = subspace;
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
					&& config.getSubspace().equals(subspace));
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
		hashCode = hashCode * multi + subspace.hashCode();
		return hashCode;

	}
}

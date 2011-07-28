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
	 * The key for the slice kind's meta-subspaces.
	 */
	public static final String METASUBSPACES = "metaSubspaces";

	/**
	 * The uri of the slice kind.
	 */
	private String uri;
	/**
	 * The permission of the slice kind.
	 */
	private AccessMask permission;
	/**
	 * The meta subspace of the slice kind.
	 */
	private String metasubspaces;

	/**
	 * Constructs a SliceKindConfiguration.
	 * @param uri The uri.
	 * @param permission The permission as long value.
	 * @param metasubspaces The metasubspaces.
	 */
	public SliceKindConfiguration(final String uri, final String permission, final String metasubspaces) {
		this.uri = uri;
		setPermission(permission);
		this.metasubspaces = metasubspaces;
	}

	/**
	 * Constructs a SliceKindConfiguration.
	 * @param uri The uri.
	 * @param permission The permission as long value.
	 * @param metasubspaces The metasubspaces.
	 */
	public SliceKindConfiguration(final String uri, final AccessMask permission, final String metasubspaces) {
		this.uri = uri;
		this.permission = permission;
		this.metasubspaces = metasubspaces;
	}

	@Override
	public final boolean isValid() {
		return (uri != null && permission != null);
	}


	@Override
	public final String getStringRepresentation() {
		StringBuffer s = new StringBuffer();
		s.append(URI + " : '" + uri + "'; ");
		s.append(PERMISSION + " : '" + permission.getAsString() + "'; ");
		if (metasubspaces != null) {		
			s.append(METASUBSPACES + " : '" + metasubspaces + "'; ");
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
	 * Returns the meta subspaces of a slice kind configuration.
	 * @return the metasubspaces
	 */
	public final String getMetasubspaces() {
		return metasubspaces;
	}

	/**
	 * Sets the meta subspaces of a slice kind configuration.
	 * @param metasubspaces the metasubspaces to set
	 */
	public final void setMetasubspaces(final String metasubspaces) {
		this.metasubspaces = metasubspaces;
	}

	/**
	 * Constructs the slice kind configuration of a subspace.
	 * 
	 * @param slicekind The subspace.
	 * @return The configuration.
	 */
    public static final SliceKindConfiguration getSliceKindConfiguration(SliceKind slicekind) {
		return new SliceKindConfiguration(slicekind.getURI(), slicekind.getPermission(), slicekind.getMetaSubspaces().toString());
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
					&& config.getMetasubspaces().equals(metasubspaces));
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
		hashCode = hashCode * multi + metasubspaces.hashCode();
		return hashCode;

	}
}

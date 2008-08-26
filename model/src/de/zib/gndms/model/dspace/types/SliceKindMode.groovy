package de.zib.gndms.model.dspace.types

import javax.persistence.Embeddable

/**
 * Slicekind modes
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 01.08.2008 Time: 16:46:03
 */
enum SliceKindMode {
	/* 8 Letters at most */
	RO(true), RW(false)

	def SliceKindMode(boolean isReadOnly) {
		readOnly = isReadOnly
	}

	final boolean readOnly
}

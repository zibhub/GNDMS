package de.zib.gndms.model.common;

import org.globus.wsrf.ResourceKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.Column;
import javax.persistence.Embeddable
import javax.persistence.Basic;

/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 28.07.2008 Time: 14:34:05
 */
@SuppressWarnings(["ClassNamingConvention"])
@Embeddable
public class EPRT {
	@Column(name="site", nullable=true) @Nullable
	String site;

	@Basic @Column(name="rk", nullable=false) @NotNull
	Serializable rk;
}

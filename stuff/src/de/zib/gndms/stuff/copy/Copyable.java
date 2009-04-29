package de.zib.gndms.stuff.copy;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.*;


/**
 * This annotation is used to choose a copymode
 *
 * @see Copier
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 27.11.2008 Time: 17:20:28
 */
@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE) @Inherited 
public @interface Copyable {
	@NotNull CopyMode value();
}

package de.zib.gndms.logic.model.config;

import java.lang.annotation.*;


/**
 * An anntotaion giving a description about a {@link ConfigAction}
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 18.08.2008 Time: 17:25:59
 */
@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.TYPE)
@Documented
public @interface ConfigActionHelp {
    String shortHelp() default "";
    String longHelp() default "";
}

package de.zib.gndms.logic.model.config;

import java.lang.annotation.*;


/**
 * This annotation provides a description and an alternative name for object fields.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 18.08.2008 Time: 16:41:05
 */
@Retention(RetentionPolicy.RUNTIME) @Target(ElementType.FIELD)
@Documented
public @interface ConfigOption {    
   String descr() default "";
   String altName() default "";
}

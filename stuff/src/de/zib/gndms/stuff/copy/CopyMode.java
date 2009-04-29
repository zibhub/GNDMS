package de.zib.gndms.stuff.copy;

/**
 * An enum holding all implemented possibilities to copy an instance.
 *
 * @see Copier
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 27.11.2008 Time: 17:21:01
 */
public enum CopyMode {
	/** copies an instance using its clone method */ CLONE,
    /** copies an instance using its mold method */ MOLD,
    /** copies an instance using plain java (de)serialization */ SERIALIZE,
    /** copies an instance using its class' constructor*/ CONSTRUCT,
    /** instance must not be copied */ DONT
}

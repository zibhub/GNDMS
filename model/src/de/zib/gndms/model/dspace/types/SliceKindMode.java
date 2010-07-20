package de.zib.gndms.model.dspace.types;

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
    RO(true), RW(false);

    SliceKindMode (boolean isReadOnly) {
        readOnly = isReadOnly;
    }

    private final boolean readOnly;
}

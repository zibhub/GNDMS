package de.zib.gndms.infra.action;

import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.infra.system.SystemHolder;
import de.zib.gndms.logic.model.config.ConfigAction;
import org.jetbrains.annotations.NotNull;


/**
 * A SystemAction is a ConfigAction with a <tt>GNDMSystem</tt> instance.
 * 
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 22.08.2008 Time: 16:33:53
 */
public abstract class SystemAction<R> extends ConfigAction<R> implements SystemHolder {
    private GNDMSystem system;

    protected SystemAction() {
    }


    @Override
    public void initialize() {
        super.initialize();    // Overridden method
        if (system == null)
            throw new IllegalStateException("Missing system");
    }


    @NotNull
    public GNDMSystem getSystem() {
        return system;
    }

    public void setSystem(@NotNull final GNDMSystem systemParam) {
        system = systemParam;
    }
}

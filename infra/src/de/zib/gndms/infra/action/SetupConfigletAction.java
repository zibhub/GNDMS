package de.zib.gndms.infra.action;

import de.zib.gndms.logic.model.config.ConfigActionResult;
import de.zib.gndms.logic.model.config.SetupAction;
import de.zib.gndms.logic.model.config.ConfigOption;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.model.common.ConfigletState;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 09.01.2009, Time: 16:29:43
 */
public abstract class SetupConfigletAction extends SetupAction<ConfigActionResult> {

    @ConfigOption(descr = "Name of the configlet")
    String name;

    @ConfigOption(descr = "Name of the configlet's class; cant be changed after creation!")
    String className;

    @Override
    public void initialize() {
        super.initialize();    // Overridden method
        try {
            if (name == null && hasOption("name"))
                setName(getOption("name"));
            if (className == null && hasOption("className"))
                setClassName(getOption("className"));
        }
        catch ( MandatoryOptionMissingException e) {
            throw new IllegalStateException(e);
        }
    }


    @Override
    public final ConfigActionResult execute(
        final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
        switch (getMode()) {
            case CREATE: return create(em, writer);
            case READ: return read(em, writer);
            case UPDATE: return update(em, writer);
            case DELETE: return delete(em, writer);
            default:
                throw new IllegalStateException("Unexpected SetupMode");
        }
    }

    
    protected abstract ConfigActionResult read( final ConfigletState state, final EntityManager emParam, final PrintWriter writerParam );

    protected ConfigActionResult read(final EntityManager emParam, final PrintWriter writerParam) {
        final ConfigletState state = emParam.find(ConfigletState.class, getName());
        if (state == null)
            return failed("Configlet not found");
        else
            return read( state, emParam, writerParam );
    }


    protected abstract ConfigActionResult create( ConfigletState state, final EntityManager emParam, final PrintWriter writerParam );
    

    protected ConfigActionResult create(final EntityManager emParam, final PrintWriter writerParam) {
        if (emParam.find(ConfigletState.class, getName()) != null) return failed("Already exists");
        final ConfigletState state = new ConfigletState();
        state.setName(getName());
        state.setClassName(getClassName());
        create( state, emParam, writerParam );
        emParam.persist(state);
        return ok();
    }


    protected abstract ConfigActionResult update( ConfigletState state, final EntityManager emParam, final PrintWriter writerParam );


    protected ConfigActionResult update(final EntityManager emParam, final PrintWriter writerParam) {
        ConfigletState state = emParam.find(ConfigletState.class, getName());
        if (state == null) return failed("Configlet not found");
        update( state, emParam, writerParam );
        return ok();
    }


    protected void preDelete(  ConfigletState state, final EntityManager emParam, final PrintWriter writerParam ) { }

    protected ConfigActionResult delete(final EntityManager emParam, final PrintWriter writerParam) {
        ConfigletState state = emParam.find(ConfigletState.class, getName());
        if (state == null) return failed("Configlet not found");
        preDelete(  state, emParam, writerParam );
        emParam.remove(state);
        return ok();
    }


    @Override
    public boolean isSupportedMode(final SetupMode modeParam) {
        return true;
    }


    public String getName() {
        return name;
    }


    public void setName(final String nameParam) {
        name = nameParam;
    }


    public String getClassName() {
        return className;
    }


    public void setClassName(final String classNameParam) {
        className = classNameParam;
    }
}

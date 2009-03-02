package de.zib.gndms.infra.action;

import de.zib.gndms.logic.model.config.ConfigActionResult;
import de.zib.gndms.logic.model.config.SetupAction;
import de.zib.gndms.logic.model.config.ConfigAction;
import de.zib.gndms.logic.model.config.ConfigOption;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.model.common.ConfigletState;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;

/**
 *
 * 
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

    /**
     * Depending on the setup mode, it will invoke the create,read,update or delete method with the
     * EntityManager and the PrintWriter.
     * 
     * Implements {@link de.zib.gndms.logic.model.config.ConfigAction#execute(javax.persistence.EntityManager, java.io.PrintWriter)}
     * @param em the EntityManager, a state will be modified.
     * @param writer the PrintWriter a state will be written to.
     * @return  An {@code OKResult} instance, if no problem occurred. Otherwise a {@code FailedResult} instance.
     */
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

    /**
     * Reads the state into the printwriter.
     *
     * @param emParam
     * @param writerParam the printwriter the state will be written to.
     * @return  An {@code OKResult} instance, if no problem occurred
     */
    protected abstract ConfigActionResult read( final ConfigletState state, final EntityManager emParam, final PrintWriter writerParam );

    /**
     * Reads the state into the printwriter.
     *
     * @param emParam
     * @param writerParam the printwriter the state will be written to.
     * @return  An {@code OKResult} instance, if no problem occurred
     */
    protected ConfigActionResult read(final EntityManager emParam, final PrintWriter writerParam) {
        final ConfigletState state = emParam.find(ConfigletState.class, getName());
        if (state == null)
            return failed("Configlet not found");
        else
            return read( state, emParam, writerParam );
    }


    /**
     * Sets the state of a newly created {@code ConfigletState}.
     *
     * @param state a newly created {@code ConfigletState} to be configured.
     * @param emParam
     * @param writerParam
     * @return An {@code OKResult} instance, if no problem occurred. Otherwise a {@code FailedResult} instance.
     */
    protected abstract ConfigActionResult create( ConfigletState state, final EntityManager emParam, final PrintWriter writerParam );

    /**
     * Creates a new {@code ConfigletState} by setting its {@code name} and {@code classname} to the values set for {@code this}.
     * Its state will be set by {@link de.zib.gndms.infra.action.SetupConfigletAction#create(de.zib.gndms.model.common.ConfigletState, javax.persistence.EntityManager, java.io.PrintWriter)}.
     * The created {@code ConfigletState} will then be persistent and managed by the EntityManager {@code emParam}.
     *
     * @param emParam the EntityManager, the new created {@code ConfigletState} will be managed by.
     * @param writerParam
     * @return An {@code OKResult} instance, if no problem occurred. Otherwise a {@code FailedResult} instance.
     */
    protected ConfigActionResult create(final EntityManager emParam, final PrintWriter writerParam) {
        if (emParam.find(ConfigletState.class, getName()) != null) return failed("Already exists");
        final ConfigletState state = new ConfigletState();
        state.setName(getName());
        state.setClassName(getClassName());
        create( state, emParam, writerParam );
        emParam.persist(state);
        return ok();
    }

    
    /**
     * Sets the state of {@code state} to the state of {@code this}
     * 
     * @param state the ConfigletState to be changed
     * @param emParam the EnityManager, containing a entity instance for the entityClass {@code ConfigletState} and
     *      the primary key {@code getName()}
     * @param writerParam
     * @return An {@code OKResult} instance, if no problem occurred. Otherwise a {@code FailedResult} instance.
     */
    protected abstract ConfigActionResult update( ConfigletState state, final EntityManager emParam, final PrintWriter writerParam );

    /**
     * Updates the state of the entity instance managed by {@code emParam}, with the entityClass {@code ConfigletState}
     * and the primary key {@code getName()}, with the state of {@code this}.
     *  
     * @param emParam the EnityManager, containing a entity instance for the entityClass {@code ConfigletState} and
     *      the primary key {@code getName()}
     *
     *
     * @param writerParam
     * @return An {@code OKResult} instance, if no problem occurred. Otherwise a {@code FailedResult} instance.
     */
    protected ConfigActionResult update(final EntityManager emParam, final PrintWriter writerParam) {
        ConfigletState state = emParam.find(ConfigletState.class, getName());
        if (state == null) return failed("Configlet not found");
        update( state, emParam, writerParam );
        return ok();
    }

    /**
     * Does nothing by default.
     * 
     * @param state
     * @param emParam
     * @param writerParam
     */
    protected void preDelete(  ConfigletState state, final EntityManager emParam, final PrintWriter writerParam ) { }

    /**
     *  Removes the entity with with the entityClass {@code ConfigletState} and the primary key {@code getName()}
     *  from the EntityManager {@code emParam} and invokes {@link de.zib.gndms.infra.action.SetupConfigletAction#preDelete(de.zib.gndms.model.common.ConfigletState, javax.persistence.EntityManager, java.io.PrintWriter)} .
     *
     * @param emParam
     * @param writerParam
     * @return An {@code OKResult} instance, if no problem occurred. Otherwise a {@code FailedResult} instance.
     */
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

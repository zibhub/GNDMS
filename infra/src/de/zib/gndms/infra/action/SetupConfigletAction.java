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
 * This class provides a default implementation of {@code SetupAction} for database manipulation of entities,
 * being a {@code ConfigletState}.
 *
 *
 * <p>Depending on the setup mode, it will either create, read, update or delete an entity, when this action is executed.
 * It returns a {@code ConfigActionResult} informing about the success of its execution.
 *
 * <p>When {@code initialize()} is invoked, it will try to retrieve the {@code name} and {@code className}.
 * If no value has been set yet, it looks up the option 'name' and 'className' from the configuration map.
 * If nothing denoted, it tries to retrieve the setup mode from its parent chain.
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

    /**
     * Tries to retrieve the fields {@code name}, {@code className} and calls {@code super.initialize()}
     */
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
     * Depending on setup mode, it will invoke a create,read,update or delete method with the
     * EntityManager and the PrintWriter, handling a {@code ConfigletState} entity.
     * 
     * @param em the EntityManager, managing a {@code ConfigletState} entity.
     * @param writer the PrintWriter a state may be written to.
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
     * @param emParam the EnityManager, containing the entity instance {@code state}.
     * @param writerParam the printwriter the state will be written to.
     * @return  An {@code OKResult} instance, if no problem occurred
     */
    protected abstract ConfigActionResult read( final ConfigletState state, final EntityManager emParam, final PrintWriter writerParam );

    /**
     * calls {@link de.zib.gndms.infra.action.SetupConfigletAction#read(de.zib.gndms.model.common.ConfigletState, javax.persistence.EntityManager, java.io.PrintWriter)}
     * to read the state into the printwriter.
     *
     * @param emParam the EnityManager, containing an entity instance for the entityClass {@code ConfigletState} and
     *      the primary key {@code getName()}
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
     * Will be called by {@link de.zib.gndms.infra.action.SetupConfigletAction#create(javax.persistence.EntityManager, java.io.PrintWriter)} 
     */
    protected abstract ConfigActionResult create( ConfigletState state, final EntityManager emParam, final PrintWriter writerParam );

    /**
     * Creates a new entity instance ({@code ConfigletState}) by setting its {@code name} and {@code classname} to the values
     * {@code getName()} and {@code getClassName()}.
     * Makes the entity managed and persistent.
     * Calls {@link de.zib.gndms.infra.action.SetupConfigletAction#create(de.zib.gndms.model.common.ConfigletState, javax.persistence.EntityManager, java.io.PrintWriter)}
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
     * Will be called by {@link de.zib.gndms.infra.action.SetupConfigletAction#update(javax.persistence.EntityManager, java.io.PrintWriter)}.
     * Updates an entity.
     * 
     * @param state the ConfigletState to be changed
     * @param emParam the EnityManager, containing the entity instance {@code state}.
     * @param writerParam
     * @return An {@code OKResult} instance, if no problem occurred. Otherwise a {@code FailedResult} instance.
     */
    protected abstract ConfigActionResult update( ConfigletState state, final EntityManager emParam, final PrintWriter writerParam );

    /**
     * Retrieves the entity instance with the primary key {@code getName()} from the entityclass {@code ConfigletState.class}
     * and calls {@link de.zib.gndms.infra.action.SetupConfigletAction#update(de.zib.gndms.model.common.ConfigletState, javax.persistence.EntityManager, java.io.PrintWriter)}
     * to update the entity.
     *  
     * @param emParam the EnityManager, containing an entity instance for the entityClass {@code ConfigletState} and
     *      the primary key {@code getName()}
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
     * Will be called by {@link de.zib.gndms.infra.action.SetupConfigletAction#delete(javax.persistence.EntityManager, java.io.PrintWriter)}.
     * Does nothing by default.
     * 
     * @param state the entity to be deleted.
     * @param emParam the EnityManager, containing the entity instance {@code state}.
     * @param writerParam
     */
    protected void preDelete(  ConfigletState state, final EntityManager emParam, final PrintWriter writerParam ) { }

    /**
     *  Invokes {@link de.zib.gndms.infra.action.SetupConfigletAction#preDelete(de.zib.gndms.model.common.ConfigletState, javax.persistence.EntityManager, java.io.PrintWriter)}
     *  and removes the entity with with the entityClass {@code ConfigletState} and the primary key {@code getName()}
     *  from the EntityManager {@code emParam}. 
     *
     * @param emParam the EnityManager, containing a entity instance for the entityClass {@code ConfigletState} and
     *      the primary key {@code getName()}
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


    /**
     * Returns true for all setup modi.
     * 
     * @param modeParam
     * @return
     */
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

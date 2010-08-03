package de.zib.gndms.infra.action;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import de.zib.gndms.logic.model.config.ConfigActionResult;
import de.zib.gndms.logic.model.config.SetupAction;
import de.zib.gndms.logic.model.config.ConfigAction;
import de.zib.gndms.logic.model.config.ConfigOption;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.model.common.ConfigletState;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;
import java.lang.annotation.Documented;

/**
 *
 * This abstract class provides a default implementation of {@code SetupAction} for database manipulation of entities,
 * being a {@link ConfigletState}.
 *
 * <p>All values of {@link SetupMode} are supported.
 *
 * <p>Depending on the chosen value, it will either setup a new Configlet and store its associated state in the database,
 * update a configlet's <tt>state</tt>, write it to a PrintWriter or remove it from the database,
 * when this action is executed. A {@code ConfigActionResult} will be returned, informing about the success of the execution.
 *
 * <p>When {@code initialize()} is invoked, it checks if all necessary fields have been set yet.
 * If not denoted, it looks up the options {@link #name 'name'} and {@link #className 'className'} in the current configuration map,
 * if necessary also from an instance of the parent chain. 
 *
 * 
 * @author  try ma ik jo rr a zib
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
     * Calls {@code super.initialize()} and tries to retrieve the fields {@code name}, {@code className}.
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
     * Retrieves the entity instance with the primary key {@code getName()} from the entityclass {@code ConfigletState.class}
     * and calls {@link de.zib.gndms.infra.action.SetupConfigletAction#read(de.zib.gndms.model.common.ConfigletState, javax.persistence.EntityManager, java.io.PrintWriter)}
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
     * Will be called by {@link de.zib.gndms.infra.action.SetupConfigletAction#create(javax.persistence.EntityManager, java.io.PrintWriter)} to
     * set the state of the entity. 
     */
    protected abstract ConfigActionResult create( ConfigletState state, final EntityManager emParam, final PrintWriter writerParam );

    /**
     * Creates a new entity instance (a {@code ConfigletState}) by setting its {@code name} and {@code classname} to the values
     * {@code getName()} and {@code getClassName()}.
     * Makes the entity managed and persistent by the <tt>EntityManager</tt>.
     * Calls {@link de.zib.gndms.infra.action.SetupConfigletAction#create(de.zib.gndms.model.common.ConfigletState, javax.persistence.EntityManager, java.io.PrintWriter)}
     * to set its <tt>state</tt>.
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
     * Will be called by {@link de.zib.gndms.infra.action.SetupConfigletAction#update(javax.persistence.EntityManager, java.io.PrintWriter)} to update the state
     * of an <tt>ConfigletState</tt> entity.
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
     * to update the <tt>state</tt> of the entity.
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
     *  and removes the entity with the entityClass {@code ConfigletState} and the primary key {@code getName()}
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

package de.zib.gndms.logic.model.dspace;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
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



import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.logic.model.ModelChangedAction;
import de.zib.gndms.logic.model.config.ConfigActionHelp;
import de.zib.gndms.logic.model.config.ConfigActionResult;
import de.zib.gndms.logic.model.config.ConfigOption;
import de.zib.gndms.logic.model.config.SetupAction;
import de.zib.gndms.model.dspace.Subspace;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;


/**
 * An Action to delete a Subspace with the corresponding MetaSubspace in the database.
 * *
 * <p>Note: only <tt>delete</tt> mode is not supported.
 *
 * <p>An instance of this class returns a {@code ConfigActionResult} informing about the success of its execution, when
 * the <tt>execute()</tt> method is called.
 *

 * @see MetaSubspace
 * @see Subspace
 * @author  Ulrike Golas
 */
@ConfigActionHelp(shortHelp = "Delete a subspace", longHelp = "Used to prepare the database schema for GNDMS by deleting subspaces")
public class DeleteSubspaceAction extends SetupAction<ConfigActionResult> {
    @ConfigOption(descr="The name/key of the subspace")
    private String subspace;

    @ConfigOption(descr="Local filesystem root path for all slices stored in this subspace")
    private String path;

/**
    * Calls <tt>super.initialize()</tt> and retrieves several field values from the configuration map,
    * if SetupMode is <tt>create</tt>.
    * The option 'subspace' must be set anyway.
    * 
    */
    @Override
    public void initialize() {
        super.initialize();    // Overridden method
        try {
            if (path == null && hasOption("path")) {
                setPath(getOption("path"));
            }
        }
        catch ( MandatoryOptionMissingException e) {
            throw new IllegalStateException(e);
        }

        requireParameter("subspace", subspace);
    }


    /**
     *  Tries to retrieve the MetaSubspace entity with the primary key <tt>getSubspace()</tt> from the entityclass {@code MetaSubspace.class}
     *  managed by {@code em} and a corresponding <tt>Subspace</tt> instance.
     *
     *  <p>If <tt>SetupMode</tt> is set to <tt>create</tt> and if necessary,
     *  it creates a new Subspace or MetaSubspcae instance and link them.
     *  The fields of the entity and the corresponding Subspace instance
     *  are then (re)set using the corresponding getter methods of this class.
     *  Makes both instances managed and persistent by the EntityManager, if not already done.
     *
     *  <p> Removes both instances from the EntityManager, if SetupMode is <tt>delete</tt>
     *
     *  <p> Adds a new <tt>ModelChangedAction(subspace)</tt> to the postponedActions of this action instance. 
     * 
     * @param em an EntityManager managing MetaSubspace and Subspace entities.
     * @param writer
     * @return An {@code OKResult} instance, if no problem occurred. Otherwise a {@code FailedResult} instance.
     */
    @SuppressWarnings({ "FeatureEnvy", "MethodWithMoreThanThreeNegations" })
    @Override
    public ConfigActionResult execute(final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
        Subspace space = prepareSubspace(em, subspace);

        try {

                em.remove(space);
        } catch ( Exception e ) {
           throw new RuntimeException( e ); 
        }
        // Register resources that require refreshing
        getPostponedEntityActions().addAction(new ModelChangedAction(space));

        return ok();
    }

    /**
     * Tries to retrieve the entity instance with the primary key {@code pkParam} from the entityclass {@code Subspace.class}.
     * If not <tt>null</tt> it will be returned. Otherwise a new <tt>Subspace</tt> instance is created,
     * with <tt>pkParam</tt> as its ScopedName.
     *
     * @param em
     * @param pkParam
     * @return
     */
    private Subspace prepareSubspace(final EntityManager em, final String pkParam) {
        Subspace subspace = em.find(Subspace.class, pkParam);
        if (subspace == null) {
            if (! isCreating())
                throw new IllegalStateException("No matching subspace found for update");
            subspace = new Subspace();
            subspace.setId(pkParam);
        }
        return subspace;
    }

    public String getSubspace() {
        return subspace;
    }


    public void setSubspace(final String subspaceParam) {
        subspace = subspaceParam;
    }


    public String getPath() {
        return path;
    }

    public void setPath(final String pathParam) {
        path = pathParam;
    }
}

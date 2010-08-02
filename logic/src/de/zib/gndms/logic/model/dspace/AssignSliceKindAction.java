package de.zib.gndms.logic.model.dspace;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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
import de.zib.gndms.logic.model.config.ConfigAction;
import de.zib.gndms.logic.model.config.ConfigActionHelp;
import de.zib.gndms.logic.model.config.ConfigActionResult;
import de.zib.gndms.logic.model.config.ConfigOption;
import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.model.dspace.MetaSubspace;
import de.zib.gndms.model.dspace.SliceKind;
import org.jetbrains.annotations.NotNull;
import javax.persistence.EntityManager;
import java.io.PrintWriter;
import java.util.Set;


    /**
     * An Action assigning or removing a connection between a SliceKind and a MetaSubspace.
     *
     * <p>When this action is started, the configuration map must have the options 'subspace', 'sliceKind' set.
     * Otherwise an <tt>IllegalStateException</tt> will be thrown.
     * Furthermore it retrieves the value for the enum {@code Mode} if denoted in the current configuration.
     * Otherwise <tt>Mode.ADD</tt> will be used.
     *
     * <p>An instance of this class returns a {@code ConfigActionResult} informing about the success of its execution, when
     * the <tt>execute()</tt> method is called.
     *
     * @see de.zib.gndms.model.dspace.SliceKind
     * @see de.zib.gndms.model.dspace.MetaSubspace
     * @author: try ste fan pla nti kow zib
     * @version $Id$
     *
     *          User: stepn Date: 08.10.2008 Time: 12:53:58
     */
@ConfigActionHelp(shortHelp = "Assign SliceKind to MetaSubspace", longHelp = "-")
public class AssignSliceKindAction extends ConfigAction<ConfigActionResult> {


    @SuppressWarnings({ "EnumeratedClassNamingConvention" })
    enum Mode { ADD, REMOVE }

    @ConfigOption(descr="Subspace QNname")
    ImmutableScopedName subspace;

    @ConfigOption(descr="URI of the SliceKind")
    String sliceKind;

    @ConfigOption(descr="One of ADD (DEFAULT) or REMOVE")
    Mode mode;

   /**
    * Calls {@code super.initialize()} and tries to retrieve the fields {@code subspace}, {@code sliceKind},
    * and {@code mode}.
    */
    @Override
    public void initialize() {
        super.initialize();
        try {
            subspace = getISNOption("subspace");
            sliceKind = getOption("sliceKind");
            mode = getEnumOption(Mode.class, "mode", true, Mode.ADD);
        }
        catch (MandatoryOptionMissingException e) {
            throw new RuntimeException(e);
        }
    }


   /**
     *
     * Retrieves the MetaSubspace entity with the entityClass {@code MetaSubspace.class} and the primary key {@code subspace} and
     * a SliceKind entity with the entityClass {@code SliceKind.class} and the primary key {@code sliceKind}.
     *
     * Depending on the chosen <tt>Mode</tt> value, the sliceKind entity and the MetaSubspace entity are assigned to or removed from each other.
     *
     * <p>The change on the model will be registerd to the postponed actions.
    * 
     * @param em an EntityManager managing SliceKind and MetaSubspace entities.
     * @param writer
     * @return An {@code OKResult} instance, if no problem occurred. Otherwise a {@code FailedResult} instance.
     */
    @Override
    public ConfigActionResult execute(final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
        final MetaSubspace space = em.find(MetaSubspace.class, subspace);
	    if (space == null)
	        throw new IllegalArgumentException("Space not found");
	    final @NotNull Set<SliceKind> sliceKindSet = space.getCreatableSliceKinds();

        final SliceKind sk = em.find(SliceKind.class, sliceKind);
	    if (sk == null)
	        throw new IllegalArgumentException("SliceKind not found");
	    final @NotNull Set<MetaSubspace> metas =  sk.getMetaSubspaces();

        switch (mode) {
            case ADD:
	            metas.add(space);
                sliceKindSet.add(sk);
                break;

            case REMOVE:
	            metas.remove(space);
                sliceKindSet.remove(sk);
                break;

            default:
                throw new IllegalArgumentException("Unsupported mode encountered");
        }

        // Register resources that require refreshing
        getPostponedActions().addAction(new ModelChangedAction(space.getInstance()));

        return ok();
    }
}

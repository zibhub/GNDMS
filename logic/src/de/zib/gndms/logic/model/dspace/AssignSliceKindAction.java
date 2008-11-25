package de.zib.gndms.logic.model.dspace;

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
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
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


    @Override
    public ConfigActionResult execute(final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
        final @NotNull MetaSubspace space = em.find(MetaSubspace.class, subspace);
        final @NotNull SliceKind sk = em.find(SliceKind.class, sliceKind);
        final @NotNull Set<SliceKind> sliceKindSet = space.getCreatableSliceKinds();

        switch (mode) {
            case ADD:
                sliceKindSet.add(sk);
                break;

            case REMOVE:
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

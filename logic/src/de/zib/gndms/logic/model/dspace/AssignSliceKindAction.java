package de.zib.gndms.logic.model.dspace;

import de.zib.gndms.logic.action.MandatoryOptionMissingException;
import de.zib.gndms.logic.model.ModelChangedAction;
import de.zib.gndms.logic.model.config.ConfigAction;
import de.zib.gndms.logic.model.config.ConfigActionHelp;
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
public class AssignSliceKindAction extends ConfigAction<Void> {
    @SuppressWarnings({ "EnumeratedClassNamingConvention" })
    enum Mode { ADD, REMOVE }

    @ConfigOption(descr="Scope of the MetaSubspace")
    String metaScope;

    @ConfigOption(descr="Name of the MetaSubspace")
    String metaName;

    @ConfigOption(descr="URI of the SliceKind")
    String sliceKindUri;

    @ConfigOption(descr="One of ADD (DEFAULT) or REMOVE")
    Mode mode;

    @Override
    public void initialize() {
        super.initialize();
        try {
            metaScope = getOption("metaScope");
            metaName = getOption("metaName");
            sliceKindUri = getOption("sliceKindUri");
            mode = getEnumOption(Mode.class, "mode", true, Mode.ADD);
        }
        catch (MandatoryOptionMissingException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Void execute(final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
        final ImmutableScopedName name = new ImmutableScopedName(metaScope, metaName);
        final @NotNull MetaSubspace space = em.find(MetaSubspace.class, name);
        final @NotNull SliceKind sk = em.find(SliceKind.class, sliceKindUri);
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

        return null;
    }
}

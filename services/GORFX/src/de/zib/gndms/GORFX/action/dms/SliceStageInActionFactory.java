package de.zib.gndms.GORFX.action.dms;

import com.google.inject.Inject;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.infra.system.SystemHolder;
import de.zib.gndms.logic.model.gorfx.ORQTaskAction;
import de.zib.gndms.model.common.types.factory.InjectingRecursiveKeyFactory;
import de.zib.gndms.model.gorfx.OfferType;
import org.jetbrains.annotations.NotNull;


/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 17:44:10
 */
public class SliceStageInActionFactory
	  extends InjectingRecursiveKeyFactory<OfferType, ORQTaskAction<?>>
	implements SystemHolder {

	private GNDMSystem system;


	@NotNull
	public GNDMSystem getSystem() {
		return system;
	}


	@Inject
	public void setSystem(@NotNull final GNDMSystem systemParam) {
		system = systemParam;
	}


	@Override
    public ORQTaskAction<?> newInstance(final OfferType keyParam)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
		final SliceStageInTaskAction stageInTaskAction = new SliceStageInTaskAction();
		injectMembers(stageInTaskAction);
		return stageInTaskAction;
    }

}

package de.zib.gndms.GORFX.action.dms;

import com.google.inject.Inject;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.infra.system.SystemHolder;
import de.zib.gndms.logic.model.gorfx.AbstractORQCalculator;
import de.zib.gndms.model.common.types.factory.InjectingRecursiveKeyFactory;
import de.zib.gndms.model.gorfx.OfferType;
import org.jetbrains.annotations.NotNull;


/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 17:37:55
 */
public class SliceStageInORQFactory
	  extends InjectingRecursiveKeyFactory<OfferType, AbstractORQCalculator<?,?>>
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
    public AbstractORQCalculator<?, ?> newInstance(final OfferType keyParam)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
	    final SliceStageInORQCalculator orqCalculator = new SliceStageInORQCalculator();
	    injectMembers(orqCalculator);
	    return orqCalculator;
    }
}

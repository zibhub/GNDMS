package de.zib.gndms.GORFX.action.dms;

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



import javax.inject.Inject;
import de.zib.gndms.infra.system.GNDMSystem;
import de.zib.gndms.infra.system.SystemHolder;
import de.zib.gndms.logic.model.gorfx.AbstractORQCalculator;
import de.zib.gndms.model.common.types.factory.InjectingRecursiveKeyFactory;
import de.zib.gndms.model.gorfx.OfferType;
import org.jetbrains.annotations.NotNull;


/**
 * @author  try ma ik jo rr a zib
 * @version  $Id$
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

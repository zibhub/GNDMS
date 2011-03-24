package de.zib.gndms.logic.model.gorfx.c3grid;

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



import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.logic.model.gorfx.AbstractORQCalculator;
import de.zib.gndms.model.common.types.factory.InjectingRecursiveKeyFactory;
import de.zib.gndms.model.gorfx.OfferType;
import org.jetbrains.annotations.NotNull;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 08.10.2008 Time: 13:54:07
 */
public class ProviderStageInORQFactory
	  extends InjectingRecursiveKeyFactory<OfferType, AbstractORQCalculator<?,?>> {

    @Override
    @NotNull
    public AbstractProviderStageInORQCalculator newInstance(@NotNull final OfferType keyParam)
            throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        final @NotNull MapConfig config = new MapConfig(keyParam.getConfigMap());
	    final Class<? extends AbstractProviderStageInORQCalculator> orqCalculatorClass =
		      config.getClassOption(
				    AbstractProviderStageInORQCalculator.class, "estimationClass",
				    ExternalProviderStageInORQCalculator.class);
	    final AbstractProviderStageInORQCalculator instance = orqCalculatorClass.newInstance();
	    injectMembers(instance);
	    return instance;
    }


}

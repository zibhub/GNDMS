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



import de.zib.gndms.logic.model.gorfx.AbstractQuoteCalculator;
import de.zib.gndms.model.gorfx.types.ProviderStageInOrder;


/**
 * Example class of provider state in calculator.
 *
 * @author  try ma ik jo rr a zib
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 4:12:20 PM
 *
 */
public abstract class AbstractProviderStageInQuoteCalculator
        extends AbstractQuoteCalculator<ProviderStageInOrder> {
    public AbstractProviderStageInQuoteCalculator() {
        super( );
    }
}

package de.zib.gndms.common.mockup.logic.config;
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

import de.zib.gndms.common.mockup.logic.action.Action;

/**
 * @author try ma ik jo rr a zib
 * @date 16.02.11  10:59
 * @brief Marks a special type of action, the config action.
 *
 * This type of action will be used to configure the GNDMS system through the rest interface.
 * @deprecated
 */
public interface ConfigAction extends Action<String> {

}
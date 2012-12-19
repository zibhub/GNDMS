/*
 * Copyright 2008-2013 Zuse Institute Berlin (ZIB)
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.zib.gndms.voldmodel;

/**
 * An enumeration to define available types in C3-Grid.
 *
 * At the moment, this are the types:
 * - OAI for OAI servers,
 * - IMPORT for import sites,
 * - EXPORT for export sites,
 * - PUBLISHER for publishing sites,
 * - ESGF for ESGF staging sites,
 * - WORKFLOW for workflows,
 * - OID for data providers,
 * - CPID_GRAM for compute providers,
 * - GRAM for compute providers gram end points.
 *
 * @author Jšrg Bachmann
 */
public enum Type {
    /**
     * The type for OAI servers.
     */
    OAI,
    /**
     * The type for import sites.
     */
    IMPORT,
    /**
     * The type for export sites.
     */
    EXPORT,
    /**
     * The type for publishing sites.
     */
    PUBLISHER,
    /**
     * The type for ESGF staging sites.
     */
    ESGF,
    /**
     * The type for workflows.
     */
    WORKFLOW,
    /**
     * The type for data providers.
     */
    OID,
    /**
     * The type for compute providers.
     */
    CPID_GRAM,
    /**
     * The type for compute provider gram endpoints.
     */
    GRAM
}


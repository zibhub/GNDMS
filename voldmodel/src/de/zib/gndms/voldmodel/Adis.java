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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.zib.gndms.voldmodel.abi.ABIi;
import de.zib.vold.client.VolDClient;
import de.zib.vold.common.Key;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;

/**
 * ADiS is the VolD client for C3-Grid.
 *
 * VolD will be used as the MDS replacement in the new C3-Grid. This class is
 * the client used to store and get data from the VolD storage specific to the
 * needs of C3-Grid.
 *
 * @author Joerg Bachmann, Ulrike Golas
 */
public class Adis extends ABIi {
    /**
     * The logger.
     */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * The internal VolD client.
     */
    private VolDClient voldi;

    /**
     * The grid, which is set to "c3grid".
    */
    private String grid;

    /**
     * The constructor, setting up a VolD client with the given context,
     * utf-8 encoding and the grid name "c3grid".
     * @param context The context containing the voldRestTemplate.
     */
    public Adis(final BeanFactory context) {
        this.voldi = new VolDClient(context);
        this.voldi.setEnc("utf-8");
        this.grid = "c3grid";
    }

    /**
     * The constructor, setting up a VolD client with utf-8 encoding, the
     * grid name "c3grid" and the context given in
     * "META-INF/vold-client-context.xml".
     */
    public Adis() {
        this.voldi = new VolDClient();
        this.voldi.setEnc("utf-8");
        this.grid = "c3grid";
    }

    /**
     * Sets the URL of the VolD client to the VolD service.
     * @param voldURL This string should look like
     *                  "http://ip.address.de/VolD/master"
     */
    public final void setVoldURL(final String voldURL) {
        voldi.setBaseURL(voldURL);
    }

    /**
     * Sets the encoding used to store data.
     * @param enc The encoing, "utf-8" is the standard.
     */
    public final void setEnc(final String enc) {
        voldi.setEnc(enc);
    }

    /**
     * Sets the name of the grid in which the GNDMS is used.
     * @param grid For C3-Grid this should be c3grid
     */
    public final void setGrid(final String grid) {
        this.grid = grid;
    }

    /**
     * Checks that ADiS is properly initialized. This means that
     * a grid name has to be set and the VolD client is properly
     * set up.
     */
    public final void checkState() {
        voldi.checkState();

        if (grid == null) {
            throw new IllegalStateException("Tried to operate on "
                    + "ADiS while it had not been initialized yet. "
                    + "Set proper gridname.");
        }
    }

    /**
     * Checks if the given String actually describes a role as
     * defined in {@link de.zib.gndms.voldmodel.Role}.
     * @param role The role to be checked
     * @return true, if this role is valid; otherwise false
     */
    private boolean checkRole(final String role) {
        for (Role r : Role.values()) {
            if (r.toString().equals(role)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the given String actually describes a type as
     * defined in {@link de.zib.gndms.voldmodel.Type}.
     * @param type The type to be checked
     * @return true, if this type is valid; otherwise false
     */
    private boolean checkType(final String type) {
        for (Type t : Type.values()) {
            if (t.name().equals(type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the value corresponding to the given role in the
     * VolD registry.
     * Note that if multiple values are registered for this role,
     * only the first is returned.
     * @param role The role
     * @return The corresponding value
     */
    private String getRole(final String role) {
        // guard
        checkState();
        if (!checkRole(role)) {
            return null;
        }

        Map<Key, Set<String>> result =
                voldi.lookup(new Key(grid, role, "..."));

        if (result == null) {
            return null;
        }

        // should be exactly one entry!
        if (result.size() > 1) {
            logger.warn("More than one " + role + " endpoint registered!");
        }

        // search for first valid entry
        for (Map.Entry<Key, Set<String>> entry : result.entrySet()) {
            if (entry.getValue().size() == 0) {
                continue;
            }
            if (entry.getValue().size() > 1) {
                logger.warn("More than one " + role + " endpoint registered!");
            }
            // first valid entry is returned
            return entry.getValue().iterator().next();
        }
        return null;
    }

    /**
     * Returns all names and their (first) values corresponding to the
     * given type in the VolD registry.
     * @param type The type
     * @return The corresponding names and values
     */
    private Map<String, String>
        listValuesByNameAndType(final String type) {
        // guard
        checkState();
        if (!checkType(type)) {
            return null;
        }

        Map<Key, Set<String>> result = voldi.lookup(new Key(grid, type, "..."));

        if (result == null) {
            return null;
        } else {
            return flatmapFirst(result);
        }
    }

    /**
     * Returns all values corresponding to the given type in the
     * VolD registry.
     * @param type The type
     * @return The corresponding values
     */
    private Collection<String> listValuesByType(final String type) {
        // guard
        checkState();
        if (!checkType(type)) {
            return null;
        }

        Map<Key, Set<String>> result = voldi.lookup(new Key(grid, type, "..."));

        if (result == null) {
            return null;
        } else {
            return flatten(result.values());
        }
    }

    /**
     * Returns all key names corresponding to the given type in the
     * VolD registry.
     * @param type The type
     * @return The corresponding key names
     */
    private Collection<String> listKeysByType(final String type) {
        // guard
        checkState();
        if (!checkType(type)) {
            return null;
        }

        Map<Key, Set<String>> result = voldi.lookup(new Key(grid, type, "..."));

        if (result == null) {
            return null;
        } else {
            return listKeyNames(result.keySet());
        }
    }

    /**
     * Returns the URL of the central data management system.
     * @return The end point to the DMS
     */
    public final String getDMS() {
        return getRole(Role.DMS.toString());
    }

    /**
     * Returns the URL of the workflow scheduler system.
     * @return The end point to the WSS
     */
    public final String getWSS() {
        return getRole(Role.WSS.toString());
    }

    /**
     * Lists all available compute providers.
     * @return All registered compute provider ids
     */
    public final Map<String, String> listCPs() {
        return listValuesByNameAndType(Type.CPID_GRAM.toString());
    }

    /**
     * Lists all available Harvester URLs.
     * This method can be used by the portal to get all harvesters.
     * @return The harvester end points
     */
    public final Collection<String> listOAIs() {
        return listValuesByType(Type.OAI.toString());
    }

    /**
     * Lists all available import sites.
     * @return All import site URLs
     */
    public final Map<String, String> listImportSites() {
        return listValuesByNameAndType(Type.IMPORT.toString());
    }

    /**
     * Lists all available export sites.
     * @return All export site URLs
     */
    public final Map<String, String> listExportSites() {
        return listValuesByNameAndType(Type.EXPORT.toString());
    }

    /**
     * Lists all available transfer sites.
     * @return All transfer site URLs
     */
    public final Map<String, String> listTransferSites() {
        return listValuesByNameAndType(Type.TRANSFER.toString());
    }

    /**
     * Lists all available publishing sites.
     * @return All publishing site URLs
     */
    public final Map<String, String> listPublishingSites() {
        return listValuesByNameAndType(Type.PUBLISHER.toString());
    }

    /**
     * Lists all available ESGF data stagers.
     * @return All ESGF stager site URLs
     */
    public final Map<String, String> listESGFStagingSites() {
        return listValuesByNameAndType(Type.ESGF.toString());
    }

    /**
     * Lists all available workflows.
     * @return All available workflows
     */
    public final Collection<String> listWorkflows() {
        // TODO: is this method / key actually used?
        return listKeysByType(Type.WORKFLOW.toString());
    }

    /**
     * Lists all data provider URLs hosting data together with their OID prefix.
     * @return The OID prefixes and data provider URLs
     */
    public final Map<String, Set<String>> listGORFX() {
        // guard
        checkState();

        Map<Key, Set<String>> result =
               voldi.lookup(new Key(grid, Type.OID.toString(), "..."));

        if (result == null) {
            return null;
        } else {
            return flatmap(result);
        }
     }

    /**
     * Lists all data provider URLs hosting data with a given OID prefix.
     * @param oidprefix The OID prefix
     * @return The data provider URLs
     */
    public final Collection<String> listGORFXbyOID(final String oidprefix) {
        // guard
        checkState();

        Map<Key, Set<String>> result =
               voldi.lookup(new Key(grid, Type.OID.toString(), "..."));

        if (result == null) {
            return null;
        } else {
            Set<Key> keys = result.keySet();
            Map<Key, Set<String>> result2 = new HashMap<Key, Set<String>>();
            for (Key key : keys) {
                if (oidprefix.startsWith(key.get_keyname()))  {
                    result2.put(key, result.get(key));
                }
            }
            return flatten(result2.values());
        }
    }

    /**
     * Gets all GRAM end points supporting the given workflow.
     * @param workflow The workflow name
     * @return The end points
     */
    public final Map<String, Set<String>>
                  getEPbyWorkflow(final String workflow) {
        // TODO: is this method / key actually used?
        // Does this make sense? How about the workflow version?
        // Up to now, this is not put into VolD ...

        // guard
        checkState();

        Map<Key, Set<String>> result = voldi.lookup(new Key(grid,
                Type.WORKFLOW.toString(), workflow));

        if (result == null) {
            return null;
        }

        Map<String, Set<String>> newresult = new HashMap<String, Set<String>>();

        // should be exactly one entry
        for (Map.Entry<Key, Set<String>> entry : result.entrySet()) {
            for (String gram : entry.getValue()) {
                Map<Key, Set<String>> gramres = voldi.lookup(new Key(grid,
                       Type.GRAM.toString(), gram));

                if (gramres == null) {
                    logger.warn("Workflow " + workflow + " had a GRAM EndPoint "
                            + gram + " registered, which has neither a GridFTP "
                            + "nor a SubSpace endpoint.");
                    continue;
                }
                newresult.putAll(flatmap(gramres));
            }
        }
        return newresult;
    }

    /**
     * Inserts a new entry into the VolD using the given role as
     * {@link de.zib.vold.common.Key} and the given end point.
     * @param role The type
     * @param endpoint The end point
     * @return true on success
     */
    private boolean setRole(final String role, final String endpoint) {
        // guard
        checkState();
        if (!checkRole(role)) {
            return false;
        }

        voldi.insert(null, simplemap(new Key(grid,
               role.toString(), ""), endpoint));
        return true;
    }

    /**
     * Inserts a new entry into the VolD combining the given type and name to
     * a {@link de.zib.vold.common.Key} and the given value.
     * @param type The type
     * @param name The name
     * @param value The value
     * @return true on success
     */
    private boolean
           setType(final String type, final String name, final String value) {
        checkState();

        if (!checkType(type)) {
            return false;
        }
        String insertname = name;
        if (name == null) {
            insertname = "";
        }

        Map<Key, Set<String>> result =
               voldi.lookup(new Key(grid, type, insertname));
        if (result.size() != 0) {
            logger.warn("EndPoint " + insertname + " of type "
                    + type + " with URL "
                    + result.get(new Key(grid, type, insertname))
                    + "was overwritten.");
        }
        voldi.insert(null, simplemap(new Key(grid, type, insertname), value));
        return true;
    }

    /**
     * Sets the central data management system end point.
     * @param endpoint The end point
     * @return true on success
     */
    public final boolean setDMS(final String endpoint) {
        return setRole(Role.DMS.toString(), endpoint);
    }

    /**
     * Sets the workflow scheduler system end point.
     * @param endpoint The end point
     * @return true on success
     */
    public final boolean setWSS(final String endpoint) {
        return setRole(Role.WSS.toString(), endpoint);
    }

    /**
     * Registers an export site.
     *
     * This method should be called by a host running te export site.
     * @param name A human readable name
     * @param subspace The end point URL
     * @return true on success
     */
    public final boolean setExport(final String name, final String subspace) {
        return setType(Type.EXPORT.toString(), name, subspace);
    }

    /**
     * Registers an ESGF data stager.
     *
     * This method should be called by a host running the ESGF stager.
     * @param name A human readable name
     * @param gndms The end point URL
     * @return true on success
     */
    public final boolean setESGFStager(final String name, final String gndms) {
        return setType(Type.ESGF.toString(), name, gndms);
    }

    /**
     * Registers a publishing site.
     *
     * This method should be called by a host running the publishing site.
     * @param name A human readable name
     * @param subspace The end point URL
     * @return true on success
     */
    public final boolean
            setPublisher(final String name, final String subspace) {
        return setType(Type.PUBLISHER.toString(), name, subspace);
    }

    /**
     * Registers an import site.
     *
     * This method should be called by the host running the import site.
     * @param name A human readable name
     * @param subspace The end point URL
     * @return true on success
     */
    public final boolean setImport(final String name, final String subspace) {
        return setType(Type.IMPORT.toString(), name, subspace);
    }

    /**
     * Registers a transfer site.
     *
     * This method should be called by the host running the transfer site.
     * @param name A human readable name
     * @param subspace The end point URL
     * @return true on success
     */
    public final boolean setTransfer(final String name, final String subspace) {
        return setType(Type.TRANSFER.toString(), name, subspace);
    }

    /**
     * Registers a compute provider.
     *
     * This method should be called by the host running the compute provider.
     * @param name A human readable name
     * @param subspace The end point URL
     * @return true on success
     */
    public final boolean setCP(final String name, final String subspace) {
        return setType(Type.CPID_GRAM.toString(), name, subspace);
    }

    /**
     * Registers a workflow.
     *
     * This method should be called by all compute providers supporting
     * this workflow.
     * @param subspace The end point for all incoming and outgoing data
     * @param cpId The compute provider id
     * @param gram The gram end point
     *    (a triple of gram server, job manager and queue name).
     * @param workflows The set of all workflows supported by
     *     this compute provider
     * @return true on success
     */
    public final boolean setWorkflows(
            final String subspace,
            final String cpId,
            final String gram,
            final Collection<String> workflows
    ) {
        // TODO: is this method really used? Are workflows registered
        // at VolD?

        // guard
        checkState();

        final Map<Key, Set<String>> request = new HashMap<Key, Set<String>>();
        final Set<String> cpIdSet = simpleset(cpId);

        // add workflow |--> cpId
        for (String workflow : workflows) {
            request.put(new Key(grid, Type.WORKFLOW.toString(),
                   workflow), cpIdSet);
        }

        // add cpId |--> gram
        request.put(new Key(grid, Type.CPID_GRAM.toString(),
               cpId), simpleset(gram));

        // add cpId |--> subspace
        request.put(new Key(grid, Type.GRAM.toString(), cpId),
              simpleset(subspace));

        voldi.insert(null, request);
        return true;
    }

    /**
     * Registers a set of OID prefixes for a data provider.
     *
     * This method should be called by the data provider hosting
     * the OID prefixes.
     * @param gorfx The end point of the data provider
     * @param oidprefixes The OID prefixes of this data provider
     * @return true on success.
     */
    public final boolean
      setOIDPrefixe(final String gorfx, final Collection<String> oidprefixes) {
        // guard
        checkState();

        Map<Key, Set<String>> request = new HashMap<Key, Set<String>>();

        for (String oidprefix : oidprefixes) {
            request.put(new Key(grid,
                     Type.OID.toString(), oidprefix), simpleset(gorfx));
        }

        voldi.insert(null, request);
        return true;
    }

    /**
     * Registers an OAI harvester.
     *
     * This method should be called by the data provider or portal
     * running the harvester.     *
     * @param endpoint The new end point
     * @return true on success
     */
    public final boolean setOAI(final String endpoint) {
        return setType(Type.OAI.toString(), "", endpoint);
    }

    /**
     * Returns a map containing the given key and as its value a
     * set with the given String.
     * @param key The key
     * @param value The value
     * @return The constructed map
     */
    private static Map<Key, Set<String>>
                simplemap(final Key key, final String value) {
        Map<Key, Set<String>> result = new HashMap<Key, Set<String>>();

        result.put(key, simpleset(value));
        return result;
    }

    /**
     * Creates a new set containing the given string.
     * @param s The string
     * @return The set containing this string
     */
    private static Set<String> simpleset(final String s) {
        Set<String> set = new HashSet<String>();

        set.add(s);
        return set;
    }

    /**
     * Returns for a map of keys and sets a corresponding
     * map of key names and sets (see {@link de.zib.vold.common.Key}).
     * @param map The original map using keys
     * @return The new map using key names
     */
    private static Map<String, Set<String>>
               flatmap(final Map<Key, Set<String>> map) {
        Map<String, Set<String>> newMap = new HashMap<String, Set<String>>();

        for (Key key: map.keySet()) {
            newMap.put(key.get_keyname(), map.get(key));
        }

        return newMap;
    }

    /**
     * Returns for a map of keys and sets a corresponding
     * map of key names and sets (see {@link de.zib.vold.common.Key}).
     * @param map The original map using keys
     * @return The new map using key names
     */
    private Map<String, String>
               flatmapFirst(final Map<Key, Set<String>> map) {
        Map<String, String> newMap = new HashMap<String, String>();

        for (Key key: map.keySet()) {
            if (map.get(key).size() > 1) {
                logger.warn("More than one endpoint with name "
                + key.get_keyname() + " of type " + key.get_type()
                + "registered.");
            }

            newMap.put(key.get_keyname(), map.get(key).iterator().next());
        }

        return newMap;
    }

    /**
     * Flattens a collection of sets into one set.
     * @param coll The collection
     * @return The single set containing all collection elements
     */
    private static Set<String> flatten(final Collection<Set<String>> coll) {
        Set<String> result = new HashSet<String>();

        for (Set<String> set : coll) {
            result.addAll(set);
        }
        return result;
    }

    /**
     * Lists all names of a given set of keys
     * (see {@link de.zib.vold.common.Key}).
     * @param set The keys
     * @return The set of names
     */
    private static Set<String> listKeyNames(final Set<Key> set) {
        Set<String> result = new HashSet<String>();

        for (Key k : set) {
            result.add(k.get_keyname());
        }
        return result;
    }
}

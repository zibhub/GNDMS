package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.logic.model.config.ConfigAction;
import de.zib.gndms.logic.model.config.ConfigActionHelp;
import de.zib.gndms.logic.model.config.ConfigOption;
import de.zib.gndms.model.gorfx.OfferType;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;



/**
 * An Action to manage the configuration map of an <tt>OfferType</tt> entity.
 *
 * <p>Depending on the <tt>UpdateMode</tt> it will either delete, just update or completly overwrite
 * all keys and their corresponding values in the configuration map, being a valid config option (see {@link #isValidConfigOptionName(String)})
 * of an already existing <tt>OfferType</tt> entity.
 *
 * <p>Before this action is started,
 *  the following parameters must be set in the configuration map: <tt>'cfgOutFormat','cfgUpdateMode'</tt>.
 * If not already denoted, 'offerType' must also be set in the map.
 * Otherwise an <tt>IllegalStateException</tt> will be thrown.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 06.10.2008 Time: 11:50:03
 */
@ConfigActionHelp(shortHelp="Configure already setup OfferTypes", longHelp="Configure already-setup OfferTypes of this GNDMS installation or print their current configuration")
public class ConfigOfferTypeAction extends ConfigAction<String> {
    public enum OutFormat { PROPS, OPTS, NONE, PRINT_OK }

    public enum UpdateMode { UPDATE, OVERWRITE, DELKEYS }

    @ConfigOption(descr="Unique URI identifying this offerType")
    private String offerType;

    @ConfigOption(descr="How to update the config; one of UPDATE (default), OVERWRITE, or DELKEYS")
    private UpdateMode cfgUpdateMode;

    @ConfigOption(descr="Output format; one of PROPS, OPTS (default), NONE, or OK")
    private OutFormat cfgOutFormat;

    @Override
    public void initialize() {
        super.initialize();    // Overridden method
        try {
            if (offerType == null)
                setOfferType(getOption("offerType"));
            cfgOutFormat = getEnumOption(OutFormat.class, "cfgOutFormat", true, OutFormat.PROPS);
            cfgUpdateMode =
                    getEnumOption(UpdateMode.class, "cfgUpdateMode", true, UpdateMode.UPDATE);
        }
        catch ( MandatoryOptionMissingException e) {
            throw new IllegalArgumentException(offerType);
        }
    }



    /**
     * Retrieves the <tt>OfferType</tt> entity with the primary key <tt>getOfferType()</tt>
     * and the entityclass <tt>OfferType.class</tt>, which is managed by <tt>em</tt>.
     *
     * <p> Depending on the <tt>UpdateMode</tt> it will manipulate the entity's configuration map.
     *   If the mode is set to 'DELKEYS', all options from entity's configuration map are deleted, which have a valid config option name
     *   according to <tt>isValidConfigOpionName()</tt>.
     *
     *  Otherwise it will put all available options (<tt>getAllOptionName()</tt>) and their chosen values to the configuration map, if they are valid
     *      according to <tt>isValidConfigOpionName()</tt>.
     *
     * Calls {@link #genOutput(java.util.Map)}, with the modified configuration map, returning a result string.
     *
     * @param em
     * @param writer
     * @return {@link #genOutput(java.util.Map)} 
     */
    @Override
    public String execute(final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
        final @NotNull OfferType offerType_ = em.find(OfferType.class, getOfferType());
        final @NotNull Map<String, String> configMap;

        boolean update = false;
        switch (cfgUpdateMode) {
            case DELKEYS:
                configMap = offerType_.getConfigMap();
                for (String opt : getAllOptionNames())
                    if (isValidConfigOptionName(opt)) configMap.remove(opt);
                break;

            case UPDATE:
                configMap = offerType_.getConfigMap();
                update = true;
                break;
            default:
                configMap = new HashMap<String, String>(8);
                update = true;
        }

        if (update)
            for (String name : getAllOptionNames())
                try {
                    if (isValidConfigOptionName(name)) configMap.put(name, getOption(name));
                }
                catch (MandatoryOptionMissingException e) {
                    // shdouldnt happen
                    throw new RuntimeException(e);
                }

        return genOutput(configMap);
    }


    /**
     * Returns true if <tt>name</tt> does not start with 'cfg' and is not the String 'offerType'
     * 
     * @param name a name of an option
     * @return
     */
    @SuppressWarnings({ "MethodMayBeStatic" })
    private boolean isValidConfigOptionName(final String name) {
        return ! name.startsWith("cfg") && ! "offerType".equals(name);
    }

    /**
     * Depending on the selected <tt>OutFormat</tt>, another message is returned.
     *
     * If <tt>OutFormat</tt> is set to
     * <ul>
     *      <li>
     *          <tt>PRINT_OK</tt>, the String "OK()" is returned.
     *      </li>
     *      <li>
     *          <tt>NONE</tt>, null is returned
     *      </li>
     *      <li>
     *          <tt>PROPS</tt>, <tt>configMapParam</tt> is loaded as Properties and <tt>props.toString()</tt> will be returned.
     *      </li>
     *      <li>
     *          <tt>OPTS</tt> returns <tt>this.allOptionsToString(false)</tt>
     *      </li>
     * </ul>
     * @param configMapParam a Map containing the config map of an offer type instance.
     * @return
     */
    private String genOutput(final Map<String, String> configMapParam) {
        switch (cfgOutFormat) {
            case PRINT_OK:
                return "OK()";
            case NONE:
                return null;
            case PROPS:
                Properties props = new Properties();
                props.putAll(configMapParam);
                return props.toString();
            case OPTS:
            default:
                return allOptionsToString(false);
        }
    }


    public String getOfferType() {
        return offerType;
    }


    public void setOfferType(final String offerTypeParam) {
        offerType = offerTypeParam;
    }
}

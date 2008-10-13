package de.zib.gndms.logic.model.gorfx;

import de.zib.gndms.logic.action.MandatoryOptionMissingException;
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
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 06.10.2008 Time: 11:50:03
 */
@ConfigActionHelp(shortHelp="Configure already setup OfferTypes", longHelp="Configure already-setup OfferTypes of this GNDMS installation or print their current configuration")
public class ConfigOfferTypeAction extends ConfigAction<String> {
    public enum OutFormat { PROPS, OPTS }

    public enum UpdateMode { UPDATE, OVERWRITE, DELKEYS }

    @ConfigOption(descr="Unique URI identifying this offerType")
    private String offerType;

    @ConfigOption(descr="How to update the config; one of UPDATE (default), OVERWRITE, or DELKEYS")
    private UpdateMode cfgUpdateMode;

    @ConfigOption(descr="Output format; one of PROPS or OPTS (default)")
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
        catch (MandatoryOptionMissingException e) {
            throw new IllegalArgumentException(offerType);
        }
    }


    @Override
    public String execute(final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
        final @NotNull OfferType offerType = em.find(OfferType.class, getOfferType());
        final @NotNull Map<String, String> configMap;

        boolean update = false;
        switch (cfgUpdateMode) {
            case DELKEYS:
                configMap = offerType.getConfigMap();
                for (String opt : getAllOptionNames())
                    if (isValidConfigOptionName(opt)) configMap.remove(opt);
                break;

            case UPDATE:
                configMap = offerType.getConfigMap();
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


    @SuppressWarnings({ "MethodMayBeStatic" })
    private boolean isValidConfigOptionName(final String name) {
        return ! name.startsWith("cfg") && ! "offerType".equals(name);
    }


    private String genOutput(final Map<String, String> configMapParam) {
        switch (cfgOutFormat) {
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

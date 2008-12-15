package de.zib.gndms.model.gorfx.types;

import de.zib.gndms.stuff.copy.CopyMode;
import de.zib.gndms.stuff.copy.Copyable;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * An base class for model classes representing ORQ's.
 *
 * @author Maik Jorra <jorra@zib.de>
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 3:38:17 PM
 */
@Copyable(CopyMode.SERIALIZE)
public abstract class AbstractORQ implements Serializable {
    private static final long serialVersionUID = 5782532835559987893L;
    private String offerType;
	private transient boolean justEstimate;

    private String actId;
    private HashMap<String,String> actContext;
	private static final int INITIAL_STRING_BUILDER_CAPACITY = 256;


	protected AbstractORQ() {
    }


    protected AbstractORQ( String offerTypeParam ) {
	    offerType = offerTypeParam;
    }


    public String getOfferType() {
        return offerType;
    }


    protected void setOfferType( String URI ) {
	    offerType = URI;
    }


    public String getActId() {
        return actId;
    }


    public void setActId( String id ) {
	    actId = id;
    }


    public boolean hasId() {
        return actId != null;
    }


    public void setJustEstimate( boolean flag ) {
        justEstimate = flag;
    }


    public boolean isJustEstimate() {
        return justEstimate;
    }


    public abstract @NotNull String getDescription();


    @SuppressWarnings({ "ReturnOfCollectionOrArrayField" })
    public HashMap<String, String> getActContext() {
        return actContext;
    }


    public void setActContext( HashMap<String, String> context ) {
	    actContext = context;
    }


    public boolean hasContext() {
        return actContext != null;
    }


	@SuppressWarnings({ "ConstantConditions", "OverlyLongMethod" })
	public String getLoggableDescription() {
		final String descr = getDescription();
		final StringBuilder result = new StringBuilder(INITIAL_STRING_BUILDER_CAPACITY);
		result.append("GORFX_ID: ");
		result.append('\'');
		result.append(getActId());
		result.append("'; ");
		result.append("OFFER_TYPE: ");
		result.append('\'');
		result.append(getOfferType());
		result.append("'; ");
		result.append("CLASS: ");
		result.append('\'');
		result.append(getClass().getName());
		result.append("'; ");
		if (descr != null) {
			result.append(" DESCR: ");
			result.append('\'');
			result.append(descr);
			result.append("'; ");
		}
		final Map<String, String> context = getActContext();
		if (context != null) {
			result.append(" CONTEXT: ");
			result.append('\'');
			result.append('{');
			boolean first = true;
			for (String key : context.keySet()) {
				if (first)
					first = false;
				else
					result.append(", ");
				result.append(key);
				result.append('=');
				final String str = context.get(key);
				result.append(str == null ? "(null)" : str);
			}
			result.append("}'; ");
		}
		return result.toString();
	}
}

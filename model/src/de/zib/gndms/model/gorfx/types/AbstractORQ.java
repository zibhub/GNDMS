package de.zib.gndms.model.gorfx.types;

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



import de.zib.gndms.stuff.copy.CopyMode;
import de.zib.gndms.stuff.copy.Copyable;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * An base class for model classes representing ORQ's.
 *
 * @author  try ma ik jo rr a zib
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 3:38:17 PM
 */
@Copyable(CopyMode.SERIALIZE)
public abstract class AbstractORQ implements Serializable {
    private static final long serialVersionUID = 5782532835559987893L;
    private String offerType;  ///< Type of the requested task.
	private transient boolean justEstimate; ///< Flag for the contract calculation

    private String actId; ///< A unique id inherited from the ORQResource
    private HashMap<String,String> actContext; ///< The context of the ORQResource
                                               /// Contains stuff like, delegation epr, and workflow id
	private static final int INITIAL_STRING_BUILDER_CAPACITY = 256;
    private String localUser; ///< The user name map to the credential of the request.


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


   public void setLocalUser( String localUser ) {
        this.localUser = localUser;
    }


    public String getLocalUser() {
        return localUser;
    }
}

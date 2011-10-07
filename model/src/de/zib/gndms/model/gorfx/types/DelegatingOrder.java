package de.zib.gndms.model.gorfx.types;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */


import de.zib.gndms.common.model.gorfx.types.Order;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * @author try ma ik jo rr a zib
 * @date 13.07.11  18:48
 * @brief
 */
public class DelegatingOrder<T extends Order> implements Order {
	private static final int INITIAL_STRING_BUILDER_CAPACITY = 256;
    private static final long serialVersionUID = -4963712675181368456L;

    private T orderBean;
    private String actId; ///< A unique id inherited from the ORQResource
    private Map<String, String> actContext; ///< The context of the ORQResource
                                            /// Contains stuff like, delegation epr, and workflow id
    private String localUser; ///< The user name map to the credential of the request.


    public DelegatingOrder() {
    }


    public DelegatingOrder( T orderBean ) {
        this.orderBean = orderBean;
    }


    @Override
    public String getTaskFlowType() {
        return orderBean.getTaskFlowType();
    }


    @Override
    public boolean isJustEstimate() {
        return orderBean.isJustEstimate();
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


    public @NotNull String getDescription() {
        // todo remove form order class???
        return "";
    }


    @SuppressWarnings({ "ReturnOfCollectionOrArrayField" })
    public Map<String, String> getActContext() {
        return actContext;
    }


    public void setActContext( Map<String, String> context ) {
	    actContext = context;
    }


    public boolean hasContext() {
        return actContext != null;
    }


    public String getLocalUser() {
        return localUser;
    }


   public void setLocalUser( String localUser ) {
        this.localUser = localUser;
    }


    public T getOrderBean() {
        return orderBean;
    }


    public void setOrderBean( T orderBean ) {
        this.orderBean = orderBean;
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
		result.append( getTaskFlowType());
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

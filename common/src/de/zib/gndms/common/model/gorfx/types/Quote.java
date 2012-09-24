/*
 * Copyright 2008-${YEAR} Zuse Institute Berlin (ZIB)
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

package de.zib.gndms.common.model.gorfx.types;

import org.joda.time.DateTime;

import java.util.Collections;
import java.util.Map;

/**
 * @author try ma ik jo rr a zib
 * @date 15.07.11  12:33
 * @brief
 */
public class Quote implements Cloneable {

    private DateTime accepted;
    private FutureTime deadline;
    private FutureTime resultValidity;
    // expected size of task in case of a transfer or staging
    private Long expectedSize;
    // map for additional notes
    private Map<String,String> additionalNotes;
    // what is the origin of the quote
    private String site;


    public DateTime getAccepted() {
		return accepted;
	}


    public boolean hasDeadline() {
        return deadline != null;
    }


    public void setAccepted( DateTime accepted ) {
        this.accepted = accepted;
    }


    public FutureTime getDeadline() {
		return deadline;
	}


    public void setDeadline(final FutureTime deadlineParam) {
		deadline = deadlineParam;
	}


    public boolean hasResultValidity() {
        return resultValidity != null;
    }


    public FutureTime getResultValidity() {
		return resultValidity;
	}


    public void setResultValidity(final FutureTime resultValidityParam) {
		resultValidity = resultValidityParam;
	}


    public boolean hasExpectedSize( ) {
	    return getExpectedSize() != null;
	}


    public Long getExpectedSize() {
		return expectedSize;
	}


    public void setExpectedSize(final Long expectedSizeParam) {
		expectedSize = expectedSizeParam;
	}


    public boolean hasAdditionalNotes( ) {
        return additionalNotes != null && additionalNotes.size() > 0;
    }


    @SuppressWarnings({ "ReturnOfCollectionOrArrayField" })
	public Map<String, String> getAdditionalNotes() {
		return additionalNotes;
	}


    public void setAdditionalNotes(final Map<String, String> additionalNotesParam) {
        if( additionalNotesParam != null )
            additionalNotes = Collections.unmodifiableMap( additionalNotesParam );
        else
            additionalNotes = null;
	}


    @Override
	public Quote clone() throws CloneNotSupportedException {
		final Quote clone = (Quote) super.clone();
		clone.setAccepted(getAccepted());
		clone.setResultValidity(getResultValidity().clone());
		clone.setDeadline(getDeadline().clone());
		clone.setAdditionalNotes(getAdditionalNotes());
        clone.setSite( getSite() );
		if (hasExpectedSize())
			clone.setExpectedSize(getExpectedSize());
		return clone;
	}


    public String getSite() {
        return site;
    }


    public void setSite( String site ) {
        this.site = site;
    }
}

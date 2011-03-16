package de.zib.gndms.model.gorfx.types;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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



import org.joda.time.DateTime;

import java.util.Collections;
import java.util.Map;


/**
 * ThingAMagic.
 *
 * @author  ich
 * @date 10. Feb 2011
 *
 * @brief striped version of transient contract.
 */
public final class Quote implements Cloneable {
	private DateTime accepted;
	private DateTime deadline;
	private DateTime resultValidity;

    // expected size of task in case of a transfer or staging
    private Long expectedSize;

    // map for additional notes
    private Map<String,String> additionalNotes;




    public DateTime getAccepted() {
		return accepted;
	}


	public void setAccepted(final DateTime acceptedParam) {
		accepted = acceptedParam;
	}


    private boolean hasDeadline() {
        return deadline != null;
    }


	public DateTime getDeadline() {
		return deadline;
	}


	public void setDeadline(final DateTime deadlineParam) {
		deadline = deadlineParam;
	}


    private boolean hasResultValidity() {
        return resultValidity != null;
    }


	public DateTime getResultValidity() {
		return resultValidity;
	}


	public void setResultValidity(final DateTime resultValidityParam) {
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
		additionalNotes = Collections.unmodifiableMap(additionalNotesParam);
	}
}

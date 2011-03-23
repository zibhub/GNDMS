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
 * @author  ich
 * @date 10. Feb 2011
 *
 * @brief Striped version of transient contract.
 *
 * This calls contains all values of a transient contract, which might concern a client.
 */
public final class Quote implements Cloneable {
	private DateTime accepted;  ///< The time the contract was accepted. Only available if the contract was accepted.
	private DateTime deadline;  ///< The deadline for the execution.
	private DateTime resultValidity; ///< The date until the results are kept.

    private Long expectedSize; ///< Expected size of task in case of a transfer or staging.

    private Map<String,String> additionalNotes; ///< Map for additional notes, e.g. a warning if the result will be huge.


    /**
     * @brief Delivers the value of ::accepted.
     * 
     * @return The value of ::accepted.
     */
    public DateTime getAccepted() {
		return accepted;
	}


	/**
	 * @brief Sets the value of ::accepted to \e acceptedParam.
	 * 
	 * @param acceptedParam The new value of ::accepted.
	 */
	public void setAccepted(final DateTime acceptedParam) {
		accepted = acceptedParam;
	}


    private boolean hasDeadline() {
        return deadline != null;
    }


	/**
	 * @brief Delivers the value of ::deadline.
	 * 
	 * @return The value of ::deadline.
	 */
	public DateTime getDeadline() {
		return deadline;
	}


	/**
	 * @brief Sets the value of ::deadline to \e deadlineParam.
	 * 
	 * @param deadlineParam The new value of ::deadline.
	 */
	public void setDeadline(final DateTime deadlineParam) {
		deadline = deadlineParam;
	}


    private boolean hasResultValidity() {
        return resultValidity != null;
    }


	/**
	 * @brief Delivers the value of ::resultValidity.
	 * 
	 * @return The value of ::resultValidity.
	 */
	public DateTime getResultValidity() {
		return resultValidity;
	}


	/**
	 * @brief Sets the value of ::resultValidity to \e resultValidityParam.
	 * 
	 * @param resultValidityParam The new value of ::resultValidity.
	 */
	public void setResultValidity(final DateTime resultValidityParam) {
		resultValidity = resultValidityParam;
	}


	public boolean hasExpectedSize( ) {
	    return getExpectedSize() != null;
	}


	/**
	 * @brief Delivers the value of ::expectedSize.
	 * 
	 * @return The value of ::expectedSize.
	 */
	public Long getExpectedSize() {
		return expectedSize;
	}


	/**
	 * @brief Sets the value of ::expectedSize to \e expectedSizeParam.
	 * 
	 * @param expectedSizeParam The new value of ::expectedSize.
	 */
	public void setExpectedSize(final Long expectedSizeParam) {
		expectedSize = expectedSizeParam;
	}


    public boolean hasAdditionalNotes( ) {
        return additionalNotes != null && additionalNotes.size() > 0;
    }


	/**
	 * @brief Delivers the value of ::additionalNotes.
	 * 
	 * @return The value of ::additionalNotes.
	 */
    @SuppressWarnings({ "ReturnOfCollectionOrArrayField" })
	public Map<String, String> getAdditionalNotes() {
		return additionalNotes;
	}


	/**
	 * @brief Sets the value of ::additionalNotes to \e additionalNotesParam.
	 * 
	 * @param additionalNotesParam The new value of ::additionalNotes.
	 */
	public void setAdditionalNotes(final Map<String, String> additionalNotesParam) {
		additionalNotes = Collections.unmodifiableMap(additionalNotesParam);
	}
}

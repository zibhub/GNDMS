package de.zib.gndms.model.common;

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



import de.zib.gndms.model.common.types.FutureTime;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.stuff.copy.Copier;
import de.zib.gndms.stuff.copy.Copyable.CopyMode;
import de.zib.gndms.stuff.copy.Copyable;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Calendar;



/**
 *
 * A PersistenContract can be transformed to an TransientContract
 *
 *
 * @see de.zib.gndms.model.common.types.TransientContract
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 24.11.2008 Time: 15:22:43
 */
@Embeddable @Copyable(CopyMode.MOLD)
public class PersistentContract implements Serializable {
    private static final long serialVersionUID = -7695057432890400329L;

    private Calendar accepted;
	private Calendar deadline;
	private Calendar resultValidity;

    // expected size of task in case of a transfer or staging
    private Long expectedSize;

    /**
     * @see de.zib.gndms.stuff.mold.Molder
     */
    public void mold(final @NotNull PersistentContract instance) {
        instance.accepted = Copier.copy(true, accepted);
        instance.deadline = Copier.copy(true, deadline);
        instance.resultValidity = Copier.copy(true, resultValidity);
        setExpectedSize(Copier.copy(true, getExpectedSize()));
    }

    /**
     * Transforms {@code this} to a {@code TransientContract} and returns the newly created instance.
     * Transformation is done by setting all fields of the {@code TransientContract} instance
     * to the field values of {@code this}.
     *
     * @return a corresponding TransientContract object out of {@code this}.
     */
    @SuppressWarnings({ "FeatureEnvy" })
	public @NotNull TransientContract toTransientContract() {
		final TransientContract tc = new TransientContract();
		final DateTime acceptedDt = new DateTime(getAccepted());
		tc.setAccepted(acceptedDt);
		tc.setDeadline(FutureTime.atFixedTime(getDeadline()));
		tc.setResultValidity(FutureTime.atFixedTime(resultValidity));
		if (hasExpectedSize())
			tc.setExpectedSize(getExpectedSize());
		return tc;
	}

    /**
     * Compares {@link #deadline} and {@link #resultValidity} and returns the lastest of both times.
     *
     * @return the latest time, when comparing {@link #deadline} and {@link #resultValidity}
     */
	@Transient
	public Calendar getCurrentTerminationTime() {
		final Calendar curDeadline = getDeadline();
		final Calendar curRV = getResultValidity();
		return curDeadline.compareTo(curRV) > 0 ? curDeadline : curRV; 
	}

    /**
     * Returns a clone of {@link #accepted}
     *
     * @return a clone of {@link #accepted}
     */
    @Temporal(value = TemporalType.TIMESTAMP )
    public Calendar getAccepted() {
		return nullSafeClone(accepted);
	}

   /**
     * Sets {@link #accepted}. Note that a clone of {@code acceptedParam} will be stored
     *
     * @param acceptedParam a chosen value for {@link #accepted}
     */
	public void setAccepted(final Calendar acceptedParam) {
		accepted = nullSafeClone(acceptedParam);
	}

   /**
     * Returns a clone of {@link #deadline}
     *
     * @return a clone of {@link #deadline}
     */
    @Temporal(value = TemporalType.TIMESTAMP)
	public Calendar getDeadline() {
		return nullSafeClone(deadline);
	}


    /**
     * Sets {@link #deadline}. Note that a clone of {@code deadlineParam} will be stored
     *
     * @param deadlineParam a chosen value for {@link #deadline}
     */
	public void setDeadline(final Calendar deadlineParam) {
		deadline = nullSafeClone(deadlineParam);
	}

   /**
     * Returns a clone of {@link #resultValidity}
     *
     * @return a clone of {@link #resultValidity}
     */
    @Temporal(value = TemporalType.TIMESTAMP)
	public Calendar getResultValidity() {
		return nullSafeClone(resultValidity);
	}

    /**
     * Sets {@link #resultValidity}. Note that a clone of {@code resultValidityParam} will be stored
     *
     * @param resultValidityParam a chosen value for {@link #resultValidity}
     */
	public void setResultValidity(final Calendar resultValidityParam) {
		resultValidity = nullSafeClone(resultValidityParam);
	}


	public boolean hasExpectedSize( ) {
	    return getExpectedSize() != null;
	}


	public Long getExpectedSize() {
		return expectedSize;
	}


	public void setExpectedSize(final Long expectedSizeParam) {

        if( expectedSizeParam != null )
            if ( expectedSizeParam < 0)
                throw new IllegalArgumentException();
        
        expectedSize = expectedSizeParam;
	}


    /**
     * Checks if this contract is valid. A valid contract has to meet the following requirements
     *
     *      accepted, deadline and resultValidity != null
     *      accepted < deadline
     *      accepted < resultValidity
     * 
     * If strict also
     *      deadline < resultValidity
     * must apply.
     *
     * @param strict Activates strict (s.a.) checking
     * @return True if the contact is valid.
     */
    public boolean isValid( boolean strict ) {

        return accepted != null
            && deadline != null
            && resultValidity != null
            && accepted.compareTo( deadline ) < 0
            && accepted.compareTo( resultValidity ) < 0
            && ( !strict || deadline.compareTo( resultValidity ) < 0 );
    }


    @Override
    public String toString( ) {
	    final Long theExpectedSize = getExpectedSize();
	    return "Accepted: " + isoForCalendar( accepted )
            + "; Deadline: " + isoForCalendar( deadline )
            + "; ResultValidity: " + isoForCalendar( resultValidity )
            + "; ExpectedSize: "
            + ( theExpectedSize != null ? theExpectedSize.toString() : "null" );
    }


    /**
     * Returns either {@code null} if {@code cal} is null or if not, a clone of {@code cal}
     * 
     * @param cal a Calendar which may be cloned.
     * @return
     */
    private static Calendar nullSafeClone(Calendar cal) {
		return cal == null ? null : (Calendar) cal.clone();
	}

    /**
     * Returns a String representation of {@code cal}, using {@link org.joda.time.format.ISODateTimeFormat},
     * or "null" if {@code cal==null} is {@code true}.
     * 
     * @param cal a Calendar to be printed as a String, in ISO format 
     * @return
     */
    private static String isoForCalendar( Calendar cal ) {

        if( cal == null )
            return "null";
        
        return ISODateTimeFormat.dateTime().print( new DateTime( cal ) );
    }
}

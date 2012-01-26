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


import de.zib.gndms.common.model.gorfx.types.FutureTime;
import de.zib.gndms.common.model.gorfx.types.Quote;
import de.zib.gndms.stuff.copy.Copier;
import de.zib.gndms.stuff.copy.Copyable;
import de.zib.gndms.stuff.copy.Copyable.CopyMode;
import org.apache.openjpa.persistence.Externalizer;
import org.apache.openjpa.persistence.Factory;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.io.Serializable;



/**
 *
 * A PersistenContract can be transformed to an Quote
 *
 *
 * @see de.zib.gndms.common.model.gorfx.types.Quote
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 24.11.2008 Time: 15:22:43
 */
@Embeddable @Copyable(CopyMode.MOLD)
public class PersistentContract implements Serializable {
    private static final long serialVersionUID = -7695057432890400329L;

    private DateTime accepted;
	private DateTime deadline;
	private DateTime resultValidity;

    // expected size of task in case of a transfer or staging
    private Long expectedSize;


    public static PersistentContract acceptQuoteAt( DateTime dt, Quote quote ) {
        quote.setAccepted( dt );
        return acceptQuoteAsIs( quote );
    }


    public static PersistentContract acceptQuoteNow( Quote quote ) {
        return acceptQuoteAt( new DateTime(), quote );
    }


    /**
     * Creates a persistent-contract form this contract by fixing future time using the accepted time stamp.
     *
     * @return A persistent-contract representing this contract.
     *         <p/>
     *         todo maybe set fixed values here
     * @note The created contract may be invalid wrt its jpa constraints.
     * @param quote
     */
    @SuppressWarnings( { "FeatureEnvy" } )
    protected static PersistentContract acceptQuoteAsIs( Quote quote ) {

        PersistentContract pc = new PersistentContract();
        pc.setAccepted( quote.getAccepted() );

        DateTime fixedDeadline = null;
        if ( quote.hasDeadline() ) {
            fixedDeadline = quote.getDeadline().fixedWith( quote.getAccepted() ).getFixedTime();
            pc.setDeadline( fixedDeadline );
        }

        if ( quote.hasResultValidity() ) {
            DateTime fixedResultValidity;
            if ( fixedDeadline != null )
                fixedResultValidity = quote.getResultValidity().fixedWith( fixedDeadline ).getFixedTime();
            else {
                fixedResultValidity = quote.getResultValidity().fixedWith( quote.getAccepted() ).getFixedTime();
            }
            pc.setResultValidity( fixedResultValidity );
        }

        if ( quote.hasExpectedSize() )
            pc.setExpectedSize( quote.getExpectedSize() );
        return pc;
    }


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
     * @return a corresponding Quote object out of {@code this}.
     */
    @SuppressWarnings({ "FeatureEnvy" })
	public @NotNull
    Quote toTransientContract() {
		final Quote tc = new Quote();
		final DateTime acceptedDt = new DateTime(getAccepted());
		tc.setAccepted(acceptedDt);
		tc.setDeadline(FutureTime.atFixedTime(getDeadline()));
		tc.setResultValidity( FutureTime.atFixedTime( resultValidity ));
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
	public DateTime getCurrentTerminationTime() {
		final DateTime curDeadline = getDeadline();
		final DateTime curRV = getResultValidity();
		return curDeadline.compareTo(curRV) > 0 ? curDeadline : curRV; 
	}

    /**
     * Returns a clone of {@link #accepted}
     *
     * @return a clone of {@link #accepted}
     */
    @Temporal(value = TemporalType.TIMESTAMP )
    @Factory( "de.zib.gndms.model.util.JodaTimeForJPA.toDateTime" )
    @Externalizer( "de.zib.gndms.model.util.JodaTimeForJPA.fromDateTime" )
    public DateTime getAccepted() {
		return nullSafeClone(accepted);
	}

   /**
     * Sets {@link #accepted}. Note that a clone of {@code acceptedParam} will be stored
     *
     * @param acceptedParam a chosen value for {@link #accepted}
     */
	public void setAccepted(final DateTime acceptedParam) {
		accepted = nullSafeClone(acceptedParam);
	}

   /**
     * Returns a clone of {@link #deadline}
     *
     * @return a clone of {@link #deadline}
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    @Factory( "de.zib.gndms.model.util.JodaTimeForJPA.toDateTime" )
    @Externalizer( "de.zib.gndms.model.util.JodaTimeForJPA.fromDateTime" )
	public DateTime getDeadline() {
		return nullSafeClone(deadline);
	}


    /**
     * Sets {@link #deadline}. Note that a clone of {@code deadlineParam} will be stored
     *
     * @param deadlineParam a chosen value for {@link #deadline}
     */
	public void setDeadline(final DateTime deadlineParam) {
		deadline = nullSafeClone(deadlineParam);
	}

   /**
     * Returns a clone of {@link #resultValidity}
     *
     * @return a clone of {@link #resultValidity}
     */
    @Temporal(value = TemporalType.TIMESTAMP)
    @Factory( "de.zib.gndms.model.util.JodaTimeForJPA.toDateTime" )
    @Externalizer( "de.zib.gndms.model.util.JodaTimeForJPA.fromDateTime" )
	public DateTime getResultValidity() {
		return nullSafeClone(resultValidity);
	}

    /**
     * Sets {@link #resultValidity}. Note that a clone of {@code resultValidityParam} will be stored
     *
     * @param resultValidityParam a chosen value for {@link #resultValidity}
     */
	public void setResultValidity(final DateTime resultValidityParam) {
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
	    return "Accepted: " + isoForDateTime( accepted )
            + "; Deadline: " + isoForDateTime( deadline )
            + "; ResultValidity: " + isoForDateTime( resultValidity )
            + "; ExpectedSize: "
            + ( theExpectedSize != null ? theExpectedSize.toString() : "null" );
    }


    /**
     * Returns either {@code null} if {@code cal} is null or if not, a clone of {@code cal}
     * 
     * @param cal a DateTime which may be cloned.
     * @return
     */
    private static DateTime nullSafeClone(DateTime cal) {
		return cal == null ? null : new DateTime( cal );
	}

    /**
     * Returns a String representation of {@code cal}, using {@link org.joda.time.format.ISODateTimeFormat},
     * or "null" if {@code cal==null} is {@code true}.
     * 
     * @param cal a DateTime to be printed as a String, in ISO format 
     * @return
     */
    private static String isoForDateTime( DateTime cal ) {

        if( cal == null )
            return "null";
        
        return ISODateTimeFormat.dateTime().print( cal );
    }
}

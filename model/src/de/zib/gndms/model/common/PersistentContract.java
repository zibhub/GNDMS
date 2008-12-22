package de.zib.gndms.model.common;

import de.zib.gndms.model.common.types.FutureTime;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.stuff.copy.CopyMode;
import de.zib.gndms.stuff.copy.Copyable;
import de.zib.gndms.stuff.copy.Copier;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Calendar;



/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 24.11.2008 Time: 15:22:43
 */
@Embeddable @Copyable(CopyMode.MOLD)
public class PersistentContract {
	private Calendar accepted;
	private Calendar deadline;
	private Calendar resultValidity;

    // expected size of task in case of a transfer or staging
    private Long expectedSize;

    public void mold(final @NotNull PersistentContract instance) {
        instance.accepted = Copier.copy(true, accepted);
        instance.deadline = Copier.copy(true, deadline);
        instance.resultValidity = Copier.copy(true, resultValidity);
        setExpectedSize(Copier.copy(true, getExpectedSize()));
    }

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

	@Transient
	public Calendar getCurrentTerminationTime() {
		final Calendar curDeadline = getDeadline();
		final Calendar curRV = getResultValidity();
		return curDeadline.compareTo(curRV) > 0 ? curDeadline : curRV; 
	}
	
    @Temporal(value = TemporalType.TIMESTAMP )
    public Calendar getAccepted() {
		return nullSafeClone(accepted);
	}


	public void setAccepted(final Calendar acceptedParam) {
		accepted = nullSafeClone(acceptedParam);
	}


    @Temporal(value = TemporalType.TIMESTAMP)
	public Calendar getDeadline() {
		return nullSafeClone(deadline);
	}


	public void setDeadline(final Calendar deadlineParam) {
		deadline = nullSafeClone(deadlineParam);
	}


    @Temporal(value = TemporalType.TIMESTAMP)
	public Calendar getResultValidity() {
		return nullSafeClone(resultValidity);
	}


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


    private static Calendar nullSafeClone(Calendar cal) {
		return cal == null ? null : (Calendar) cal.clone();
	}


    private static String isoForCalendar( Calendar cal ) {

        if( cal == null )
            return "null";
        
        return ISODateTimeFormat.dateTime().print( new DateTime( cal ) );
    }
}

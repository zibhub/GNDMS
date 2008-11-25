package de.zib.gndms.model.common;

import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Calendar;

import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.common.types.FutureTime;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 24.11.2008 Time: 15:22:43
 */
@Embeddable
public class PersistentContract {
	private Calendar accepted;
	private Calendar deadline;
	private Calendar resultValidity;

    // expected size of task in case of a transfer or staging
    private Long expectedSize;

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
		return curDeadline.compareTo(curRV) < 0 ? curDeadline : curRV; 
	}
	
    @Temporal(value = TemporalType.TIMESTAMP)
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
		if (expectedSizeParam < 0)
			throw new IllegalArgumentException();
		expectedSize = expectedSizeParam;
	}

	private static Calendar nullSafeClone(Calendar cal) {
		return cal == null ? null : (Calendar) cal.clone();
	}
}

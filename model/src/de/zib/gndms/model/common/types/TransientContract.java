package de.zib.gndms.model.common.types;

import de.zib.gndms.model.common.PersistentContract;
import de.zib.gndms.stuff.copy.Copiable;
import de.zib.gndms.stuff.copy.CopyMode;
import org.joda.time.DateTime;

import java.util.Collections;
import java.util.Map;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 24.11.2008 Time: 15:29:51
 */
@Copiable(CopyMode.CLONE)
public final class TransientContract implements Cloneable {
	private DateTime accepted;
	private FutureTime deadline;
	private FutureTime resultValidity;

    // expected size of task in case of a transfer or staging
    private Long expectedSize;

    // map for additional notes
    private Map<String,String> additionalNotes;


	public PersistentContract acceptAt(DateTime dt) {
		setAccepted(dt);
		return acceptAsIs();
	}

	public PersistentContract acceptNow() {
		return acceptAt(new DateTime());
	}
	
	@SuppressWarnings({ "FeatureEnvy" })
	public PersistentContract acceptAsIs() {
		PersistentContract pc = new PersistentContract();
		pc.setAccepted(accepted.toGregorianCalendar());
		final DateTime fixedDeadline = getDeadline().fixedWith(accepted).getFixedTime();
		pc.setDeadline(fixedDeadline.toGregorianCalendar());
		final DateTime fixedResultValidity =
			  getResultValidity().fixedWith(fixedDeadline).getFixedTime();
		pc.setResultValidity(fixedResultValidity.toGregorianCalendar());
		if (hasExpectedSize())
			pc.setExpectedSize(getExpectedSize());
		return pc;
	}

	public DateTime getAccepted() {
		return accepted;
	}


	public void setAccepted(final DateTime acceptedParam) {
		accepted = acceptedParam;
	}


	public FutureTime getDeadline() {
		return deadline;
	}


	public void setDeadline(final FutureTime deadlineParam) {
		deadline = deadlineParam;
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


	@SuppressWarnings({ "ReturnOfCollectionOrArrayField" })
	public Map<String, String> getAdditionalNotes() {
		return additionalNotes;
	}


	public void setAdditionalNotes(final Map<String, String> additionalNotesParam) {
		additionalNotes = Collections.unmodifiableMap(additionalNotesParam);
	}


	@Override
	public TransientContract clone() throws CloneNotSupportedException {
		final TransientContract clone = (TransientContract) super.clone();
		clone.setAccepted(getAccepted());
		clone.setResultValidity(getResultValidity().clone());
		clone.setDeadline(getDeadline().clone());
		clone.setAdditionalNotes(getAdditionalNotes());
		if (hasExpectedSize())
			clone.setExpectedSize(getExpectedSize());
		return clone;    // Overridden method
	}
}

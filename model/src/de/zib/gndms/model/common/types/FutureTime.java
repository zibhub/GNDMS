package de.zib.gndms.model.common.types;

import de.zib.gndms.stuff.copy.Copyable;
import de.zib.gndms.stuff.copy.CopyMode;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.ISODateTimeFormat;

import java.io.Serializable;
import java.util.Calendar;


/**
 * This class calculates based on a reference time, a time in the future,
 * no matter whether it is denoted absolute or relative.
 *
 *
 * Once an instance has been created,a reference date has to be denoted,which can be done by invoking
 * {@code fix(referenceDate)} or {@code fixedWith(referenceDate)} first. The computed value can now be returned
 * using {@code getFixedTime()}.
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 24.11.2008 Time: 14:48:08
 */
@SuppressWarnings({ "ClassReferencesSubclass", "StaticMethodOnlyUsedInOneClass" })
@Copyable(CopyMode.CLONE)
public abstract class FutureTime implements Serializable, Cloneable {
	private static final long serialVersionUID = -8564712372634917528L;

    /**
     * the computed time in future based on a given reference date
     */
	private DateTime fixed;

	private FutureTime() {}

	public boolean isFixed() {
		return fixed != null;
	}

    /**
     * Returns the computed DateTime.
     * Note: the computation must be invoked elsewhere before.
     * @return the computed DateTime
     */
	public DateTime getFixedTime() {
		if (isFixed())
			return fixed;
		else
			throw new IllegalStateException("Not fixed!");
	}

    /**
     * Computes the {@code DateTime} of the denoted time in the future, based on {@code referenceData}.
     * A reference date can be denoted only one time, therefore it throws an IllegalStateExecption, if invoked twice.
     *
     * 
     * @param referenceDate the {@code DateTime}, the computation will be based on
     */
	public final void fix(@NotNull DateTime referenceDate) {
		if (isFixed())
			throw new IllegalStateException("Already fixed!");
		else
			fixed = computeFixedTime(referenceDate);
	}

    /**
     * Returns itself, after computing the wanted time in the future.
     *
     * @see de.zib.gndms.model.common.types.FutureTime#fix(org.joda.time.DateTime) ;
     */
	@SuppressWarnings({ "ReturnOfThis" })
	public final FutureTime fixedWith(@NotNull DateTime referenceDate) {
		fix(referenceDate);
		return this;
	}


    /**
     *
     * @return If the wanted time in the future has already been computed, then it will be returned
     * in ISO8601-format. Otherwise the denoted duration or absolute time will be returned.
     */
	@Override
	public String toString() {
		if (isFixed()) {
			return ISODateTimeFormat.dateTime( ).print(getFixedTime());
		}
		else
			return toStringIfUnfixed();
	}


	@Override
	protected FutureTime clone() throws CloneNotSupportedException {
		final FutureTime clone = (FutureTime) super.clone();
		clone.fixed = fixed;
		return clone;    // Overridden method
	}


	public abstract @NotNull DateTime computeFixedTime(@NotNull DateTime referenceDate);


	public abstract @NotNull String toStringIfUnfixed();


	public abstract boolean isAbsolute();


	public static @NotNull FutureTime atTime(@NotNull DateTime date) {
		final AbsoluteFutureTime absoluteFutureTime = new AbsoluteFutureTime();
		absoluteFutureTime.setAbsoluteDate(date);
		return absoluteFutureTime;
	}



	public static @NotNull FutureTime atOffset(@NotNull Duration dur) {
		final RelativeFutureTime relativeFutureTime = new RelativeFutureTime();
		relativeFutureTime.setDuration(dur);
		return relativeFutureTime;
	}


	public static FutureTime atFixedTime(final Calendar deadlineParam) {
		DateTime dt = new DateTime(deadlineParam);
		return atTime(dt).fixedWith(dt);
	}


    /**
     * This class provides a default implementation for absolute future times. 
     */
	public static final class AbsoluteFutureTime extends FutureTime {
		private static final long serialVersionUID = -3761845904752596863L;

		private DateTime absoluteDate;

		private AbsoluteFutureTime() {super();}


		public DateTime getAbsoluteDate() {
			return absoluteDate;
		}


		public void setAbsoluteDate(final DateTime absoluteDateParam) {
			absoluteDate = absoluteDateParam;
		}


		@Override
		@NotNull
		public DateTime computeFixedTime(@NotNull final DateTime referenceDate) {
			return referenceDate.isBefore(absoluteDate) ? absoluteDate : referenceDate;
		}


		@Override
		@NotNull
		public String toStringIfUnfixed() {
			return ISODateTimeFormat.dateTime( ).print(absoluteDate);
		}

		@Override
		public boolean isAbsolute() {
			return true;
		}


		@Override
		protected AbsoluteFutureTime clone() throws CloneNotSupportedException {
			final AbsoluteFutureTime clone = (AbsoluteFutureTime) super.clone();
			clone.absoluteDate = absoluteDate;
			return clone;    // Overridden method
		}
	}



    /**
     * This class provides a default implementation for relative future times.
     */
	public static final class RelativeFutureTime extends FutureTime {
		
		private static final long serialVersionUID = 5678544512153663725L;

		private Duration duration;

		private RelativeFutureTime() {super();}


		public Duration getDuration() {
			return duration;
		}


		public void setDuration(final Duration durationParam) {
			duration = durationParam;
		}


		@Override
		@NotNull
		public DateTime computeFixedTime(@NotNull final DateTime referenceDate) {
			final DateTime targetDate = referenceDate.plus(duration);
			return referenceDate.isBefore(targetDate) ? targetDate : referenceDate;
		}


		@Override
		@NotNull
		public String toStringIfUnfixed() {
			return Long.toString(duration.getMillis());
		}


		@Override
		public boolean isAbsolute() {
			return false;
		}

		@Override
		protected RelativeFutureTime clone() throws CloneNotSupportedException {
			final RelativeFutureTime clone = (RelativeFutureTime) super.clone();
			clone.duration = duration;
			return clone;    // Overridden method
		}
	}
}

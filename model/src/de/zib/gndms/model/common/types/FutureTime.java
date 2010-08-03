package de.zib.gndms.model.common.types;

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



import de.zib.gndms.stuff.copy.Copyable;
import de.zib.gndms.stuff.copy.CopyMode;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.ISODateTimeFormat;

import java.io.Serializable;
import java.util.Calendar;


/**
 * A FutureTime compares a reference {@code DateTime} with a preset duration or {@code DateTime} and reset itself to
 * the lastet of both {@code DateTime}s.
 *
 * This is done using {@code fix()}, which will be called with a reference {@code DateTime}.
 * 
 * If a duration has been set (using  {@link #atOffset(org.joda.time.Duration)}),
 * the method checks if the reference time plus the duration is later than the {@code DateTime} itself.
 * In this case, the method returns the addition of these times.
 * Otherwise, only the {@code DateTime} is returned.
 *
 * If a {@code DateTime} has been set (using {@link #atTime(org.joda.time.DateTime)} ) ,
 * the method returns the latest of both {@code DateTime}s.
 *
 * A FutureTime object can be fixed only once.
 *
 * @see DateTime
 * @see Duration
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 24.11.2008 Time: 14:48:08
 */
@SuppressWarnings({ "ClassReferencesSubclass", "StaticMethodOnlyUsedInOneClass" })
@Copyable(CopyMode.CLONE)
public abstract class FutureTime implements Serializable, Cloneable {
	private static final long serialVersionUID = -8564712372634917528L;

    /**
     * holds the fixed dateTime, i.e. the lastest of the reference time and the preset time / duration+time. 
     * (See description above)
     */
	private DateTime fixed;

	private FutureTime() {}

    /**
     * Returns true, if time has already been fixed.
     * @return
     */
	public boolean isFixed() {
		return fixed != null;
	}

    /**
     * Returns the fixed DateTime.
     * Note: the {@link #fix(org.joda.time.DateTime)}  must be called before.
     * 
     * @return the fixed DateTime
     */
	public DateTime getFixedTime() {
		if (isFixed())
			return fixed;
		else
			throw new IllegalStateException("Not fixed!");
	}

    /**
     * Computes the fixed {@code DateTime}, which can be retrieved using {@link #getFixedTime()}.
     * This method must be invoked only once.
     *
     * @param referenceDate the {@code DateTime} to be compared with the preset time or duration
     */
	public final void fix(@NotNull DateTime referenceDate) {
		if (isFixed())
			throw new IllegalStateException("Already fixed!");
		else
			fixed = computeFixedTime(referenceDate);
	}

    /**
     * Computes the fixed {@code DateTime} and returns itself.
     *
     * @see de.zib.gndms.model.common.types.FutureTime#fix(org.joda.time.DateTime) ;
     */
	@SuppressWarnings({ "ReturnOfThis" })
	public final FutureTime fixedWith(@NotNull DateTime referenceDate) {
		fix(referenceDate);
		return this;
	}


    /**
     * Returns a String representation of the {@code DateTime} object.
     * If {@code fix()} or {@code fixedWith()} has been called before, it will use the fixed {@code DateTime}.
     *
     * Otherwise it will just return a String representation of the preset values.
     * @return A String representation of a {@code DateTime} or a {@code Duration}.
     * @see de.zib.gndms.model.common.types.FutureTime.AbsoluteFutureTime#toStringIfUnfixed()
     * @see de.zib.gndms.model.common.types.FutureTime.RelativeFutureTime#toStringIfUnfixed() 
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

    /**
     * Compares the refrence {@code DateTime} with the preset time or duration and returns the of both DateTime.
     * If a duration has been set, it compares the {@code DateTime} with the {@code DateTime} plus the duration.
     *
     * @param referenceDate
     * @return
     */
	public abstract @NotNull DateTime computeFixedTime(@NotNull DateTime referenceDate);

    /**
     * A String representation of the preset {@code DateTime} or {@code Duration}
     *
     * @return
     */
	public abstract @NotNull String toStringIfUnfixed();


    /**
     * Returns true if the {@code FutureTime} instance uses an absolute time as its preset value.
     *
     * @return
     */
	public abstract boolean isAbsolute();


    /**
     * Returns a {@code FutureTime} object, with a preset absolute {@code DateTime}.
     * 
     * @param date the time which will be compared later on with a reference time.
     *
     * @return
     */
	public static @NotNull FutureTime atTime(@NotNull DateTime date) {
		final AbsoluteFutureTime absoluteFutureTime = new AbsoluteFutureTime();
		absoluteFutureTime.setAbsoluteDate(date);
		return absoluteFutureTime;
	}


    /**
     * Returns a {@code FutureTime} object, with a preset duration.
     *
     * @param dur the duration which will be used when {@code computedFixedTime()} is called.
     * 
     * @return
     */
	public static @NotNull FutureTime atOffset(@NotNull Duration dur) {
		final RelativeFutureTime relativeFutureTime = new RelativeFutureTime();
		relativeFutureTime.setDuration(dur);
		return relativeFutureTime;
	}


    /**
     * Returns a {@code FutureTime} object, which will allready be fixed with the value of {@code deadlineParam}.
     * This means the object cant be fixed again with another reference time, as the invokation of
     * {@link #fix(org.joda.time.DateTime)} throws an exception in this case.
     *
     * @param deadlineParam a time to be fixed
     *
     * @return a FutureTime object with the date/time of {@code deadlineParam}. It cant be fixed to another time
     */
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

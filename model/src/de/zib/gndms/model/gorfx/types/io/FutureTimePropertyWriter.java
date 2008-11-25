package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.common.types.FutureTime;

import java.util.Properties;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 24.11.2008 Time: 17:26:42
 */
public class FutureTimePropertyWriter extends AbstractPropertyIO implements FutureTimeWriter {
	private String key;


	public FutureTimePropertyWriter() {
		super();
	}


	public FutureTimePropertyWriter(final Properties properties) {
		super(properties);
	}


	public String getKey() {
		return key;
	}


	public void setKey(final String keyParam) {
		key = keyParam;
	}


	@Override
	public void begin() {
		super.begin();    // Overridden method
		if (getKey() == null)
			throw new IllegalStateException("Missing key");
	}


	public void writeAbsoluteFutureTime(final FutureTime.AbsoluteFutureTime t) {
		PropertyReadWriteAux.writeFutureTime(getProperties(), getKey(), t);
	}


	public void writeRelativeFutureTime(final FutureTime.RelativeFutureTime t) {
		PropertyReadWriteAux.writeFutureTime(getProperties(), getKey(), t);
	}

	public void done() {
	}


}

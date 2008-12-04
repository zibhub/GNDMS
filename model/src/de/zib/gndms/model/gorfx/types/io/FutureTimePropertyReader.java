package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.common.types.FutureTime;

import java.util.Properties;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 24.11.2008 Time: 17:11:32
 */
public class FutureTimePropertyReader extends AbstractPropertyReader<FutureTime> {
	private String key;

	public FutureTimePropertyReader() {
		super(FutureTime.class);
	}


	public FutureTimePropertyReader(Properties props) {
		super(FutureTime.class, props);
	}


	public String getKey() {
		return key;
	}


	public void setKey(final String keyParam) {
		key = keyParam;
	}


	@Override
	public FutureTime makeNewProduct() {
		return PropertyReadWriteAux.readFutureTime(getProperties(), key);
	}


	@Override
	public void begin() {
		super.begin();    // Overridden method
		if (key == null)
			throw new IllegalStateException("key missing");
	}


	@Override
	public void read() {

	}


	public void done() {
	}
}

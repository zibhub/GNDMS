package de.zib.gndms.kit.configlet;

import de.zib.gndms.kit.config.ConfigProvider;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import org.apache.commons.logging.Log;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Iterator;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 10.11.2008 Time: 14:22:34
 */
public class PublishConfiglet extends DefaultConfiglet {
    /**
     * provides an iterator over all publishers
     */
    private volatile Iterable<String> publishingSites;

	@Override
	public void init(
		  final @NotNull Log loggerParam, @NotNull final String aName, final Serializable data) {
		super.init(loggerParam, aName, data);    // Overridden method
		configPublishingSites();
	}

    /**
     *  Updates {@link PublishConfiglet#publishingSites} with the newest publishers. Should be invoked by {@link PublishConfiglet#update}
     */
	private void configPublishingSites() {
		try {
			final ConfigProvider mapConfig = getMapConfig().getDynArrayOption("publishers");
			publishingSites = new Iterable<String>() {
				public Iterator<String> iterator() {
						final Iterator<String> keys = mapConfig.dynArrayKeys();
						return new Iterator<String>() {
							public boolean hasNext() {
								return keys.hasNext();
							}


							public String next() {
								return mapConfig.getOption(keys.next(), "").trim();
							}


							public void remove() {
								keys.remove();
							}
						};
				}
			};
		}
		catch (ParseException e) {
			getLog().warn(e);
		}
		catch ( MandatoryOptionMissingException e) {
			getLog().warn(e);
		}
	}

    /**
     * Returns an iterable over the publishing Site, as described in the current loaded configuration
     * @return
     */
	public Iterable<String> getPublishingSites() {
        return publishingSites;
	}


	@Override
	public void update(@NotNull final Serializable data) {
		super.update(data);    // Overridden method
        configPublishingSites();
	}
}

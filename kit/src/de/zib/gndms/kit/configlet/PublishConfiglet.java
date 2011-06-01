package de.zib.gndms.kit.configlet;

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



import de.zib.gndms.kit.config.ConfigProvider;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import org.slf4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Iterator;


/**
 *  
 * This class stores a configuration in a map and provides an Iterable over all publishers.
 *
 * To retrieve the List of the publisher, they must be stored as dynamic arrray with '{@code publishers}' as {@code optionname}
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 10.11.2008 Time: 14:22:34
 */
public class PublishConfiglet extends DefaultConfiglet {
    /**
     * Provides an iterator over all publishers
     */
    private volatile Iterable<String> publishingSites;


    
    @Override
	public void init(
		  final @NotNull Logger loggerParam, @NotNull final String aName, final Serializable data) {
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
			getLogger().warn(e);
		}
		catch ( MandatoryOptionMissingException e) {
			getLogger().warn(e);
		}
	}

    /**
     * Returns an iterable over the publishing Site, as described in the current loaded configuration
     *
     * @return an iterable over the publishing Site, as described in the current loaded configuration
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

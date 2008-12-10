package de.zib.gndms;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 09.12.2008 Time: 18:01:03
 */
public class GNDMSVerInfo {

	@SuppressWarnings({ "HardcodedFileSeparator", "NestedAssignment" })
	private @NotNull String readInfo(@NotNull String infoTag) {
		final String resName = "META-INF/GNDMS-" + infoTag;
		final InputStream in = getClass().getClassLoader().getResourceAsStream(resName);
		try {
			try {
				final InputStreamReader inReader = new InputStreamReader(in);
				try {
					final BufferedReader reader = new BufferedReader(inReader);
					try {
						StringBuilder result = new StringBuilder(8);
						String line;
						/* intentionally kills all newlines */
						while ((line = reader.readLine()) != null)
							result.append(line);
						return result.toString().trim();
					}
					finally { reader.close(); }
				}
				finally { inReader.close(); }
			}
			finally { in.close(); }
		}
		catch (NullPointerException npe) {
			return "<none";
		}
		catch (IOException e) {
			return "<none>";
		}
	}

	public String readBuildInfo() {
		return readInfo("BUILD-INFO");
	}


	public String readRelease() {
		return readInfo("RELEASE");
	}
}

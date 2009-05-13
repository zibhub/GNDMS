package de.zib.gndms;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;


/**
 * A GNDMSVerInfo instance returns a String, containg informations about a specific version of the GNDMSystem.
 * It uses text-files located at {@code META-INF/} .
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 09.12.2008 Time: 18:01:03
 */
public class GNDMSVerInfo {

    /**
     * Reads a text file and returns it as a a String.
     * The file must be located in the folder {@code META-INF} and start with the name {@code GNDMS-}
     * @param infoTag the tag of the text file. This means the file must have the name {@code GNDMS-%infoTag}
     * @return the text of the specified text file
     */
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

    /**
     * Returns the content of the {@code GNDMS-BUILD-INFO} file, containg informations about the build version.
     * 
     * @return
     */
	public String readBuildInfo() {
		return readInfo("BUILD-INFO");
	}

    /**
     * Returns the content of the {@code GNDMS-RELEASE} file, containg informations about the release version.
     * @return
     */
	public String readRelease() {
		return readInfo("RELEASE");
	}
}

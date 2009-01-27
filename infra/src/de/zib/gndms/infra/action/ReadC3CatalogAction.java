package de.zib.gndms.infra.action;

import com.google.inject.Inject;
import de.zib.gndms.c3resource.jaxb.Site;
import de.zib.gndms.c3resource.jaxb.Workspace;
import de.zib.gndms.infra.configlet.C3MDSConfiglet;
import de.zib.gndms.kit.configlet.ConfigletProvider;
import de.zib.gndms.logic.model.config.ConfigAction;
import de.zib.gndms.logic.model.config.ConfigActionHelp;
import de.zib.gndms.logic.model.config.ConfigActionResult;
import de.zib.gndms.logic.model.config.ConfigOption;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Set;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 11.12.2008 Time: 12:47:32
 */
@ConfigActionHelp(shortHelp = "Print current C3Grid MDS Catalog", longHelp = "Retrieves and prints the current C3Grid MDS Catalog via C3MDSConfiglet")
public class ReadC3CatalogAction extends ConfigAction<ConfigActionResult> implements PublicAccessible {
	public enum OutputMode { SITES, ARCHIVES, OIDPREFIXES }

	@ConfigOption(descr = "Name of C3MDSConfiglet")
	private String name;

	@ConfigOption(descr = "Output Mode (One of SITES (default), ARCHIVES, OIDPREFIXES)")
	private OutputMode outputMode;

	private ConfigletProvider confligets;


	@Override
	public void initialize() {
		super.initialize();    // Overridden method
		name = getOption("name", "mds");
		outputMode = getEnumOption(OutputMode.class, "outputMode", true, OutputMode.SITES);
	}


	@Override
	public ConfigActionResult execute(final @NotNull EntityManager em, final @NotNull PrintWriter writer) {
		final C3MDSConfiglet mds = getConfligets().getConfiglet(C3MDSConfiglet.class, name);
		final C3MDSConfiglet.C3Catalog cat = mds.getCatalog();
		switch (outputMode) {
			case SITES:
				printSites(writer, cat);
				break;

			case ARCHIVES:
				printArchives(writer, cat);
				break;

			case OIDPREFIXES:
				printOidPrefixes(writer, cat);
				break;

			default:
				throw new IllegalArgumentException("Unknown outputMode");
		}

		return ok();
	}


	public static void printOidPrefixes(
		  final PrintWriter writer, final C3MDSConfiglet.C3Catalog catParam) {
		for (Map.Entry<String, Set<Workspace.Archive>> setEntry : catParam.getArchivesByOid().entrySet()) {
			writer.println(setEntry.getKey());
		}
	}


	public static void printSites(final PrintWriter writer, final C3MDSConfiglet.C3Catalog catParam) {
		for (Map.Entry<String, Site> siteEntry : catParam.getSiteById().entrySet()) {
			writer.println("# Site with id: " + siteEntry.getKey());
			writer.println(siteEntry.getValue().toString());
			writer.println();
		}
	}


	public static void printArchives(final PrintWriter writer, final C3MDSConfiglet.C3Catalog catParam) {
		for (Map.Entry<String, Set<Workspace.Archive>> setEntry : catParam.getArchivesByOid().entrySet()) {
			writer.println("# Archives for oid: " + setEntry.getKey());
			for (Workspace.Archive a : setEntry.getValue())
				writer.println(a);
			writer.println();
		}
	}

	@Override
	protected boolean isExecutingInsideTransaction() {
		return false;
	}


	public ConfigletProvider getConfligets() {
		return confligets;
	}



	@Inject
	public void setConfligets(final ConfigletProvider confligetsParam) {
		confligets = confligetsParam;
	}
}

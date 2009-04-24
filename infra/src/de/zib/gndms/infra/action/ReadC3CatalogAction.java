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
 * <p>An Action to read the <tt>C3Catalog</tt>.
 *
 * <p>Depending on the choosen <tt>OutputMode</tt>, it will either write
 * <ul>
 *      <li>
 *          all sites with their corresponding ID
 *      </li>
 *      <li>
 *          all archives  with their corresponding <tt>oid</tt>
 *      </li>
 *      <li>
 *          all <tt>oid</tt> prefixes
 *      </li>
 * </ul>
 * of the C3Catalog to a Printwriter.
 *
 * <p>When this action is started, it tries to retrieve the {@link #outputMode 'outputMode'} and the {@link #name 'name'}
 * of the <tt>C3MDSConfiglet</tt> from the configuration map.
 * If nothing denoted, default values will be used.
 *
 * <p>An instance of this class returns a {@code ConfigActionResult} informing about the success of its execution, when
 * the execute method is called.
 * 
 * @see de.zib.gndms.infra.configlet.C3MDSConfiglet.C3Catalog
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


    /**
     * Calls <tt>super.initialize()</tt> and tries to retrieve the name of the <tt>C3MDSConfiglet</tt> and
     * the <tt>output mode</tt> from the current configuration.
     * If nothing denoted, 'mds' will be used as default name and 'OutputMode.SITES' as default <tt>outputmode</tt>
     */
	@Override
	public void initialize() {
        super.initialize();    // Overridden method
		name = getOption("name", "mds");
		outputMode = getEnumOption(OutputMode.class, "outputMode", true, OutputMode.SITES);
	}

    /**
     * Retrieves a <tt>C3MDSConfiglet</tt> entity from the database
     * using the the primary key {@code name} from the entityclass {@code C3MDSConfiglet.class}.
     *
     * <p>Depending on the <tt>outputMode</tt>, prints either all sites, archives or <tt>oid</tt> prefixes of the entity's
     * <tt>C3Catalog</tt>.
     *
     *
     * @param em the EnityManager, managing C3MDSConiglet entities.
     * @param writer a PrintWriter, the list should be printed to
     * @return An {@code OKResult} instance, if no problem occurred. Otherwise a {@code FailedResult} instance.
     */
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


   /**
     * Prints all <tt>oid</tt> prefixes of a specific <tt>C3Catalog</tt> to a given PrintWriter.
     *
     * @param writer a PrintWriter, the list should be printed to
     * @param catParam the catalog containg all sites
     */
	public static void printOidPrefixes(
		  final PrintWriter writer, final C3MDSConfiglet.C3Catalog catParam) {
		for (Map.Entry<String, Set<Workspace.Archive>> setEntry : catParam.getArchivesByOid().entrySet()) {
			writer.println(setEntry.getKey());
		}
	}

    /**
     * Prints all sites of a specific <tt>C3Catalog</tt> with their corresponding ID to a given PrintWriter.
     * 
     * @param writer a PrintWriter, the list should be printed to
     * @param catParam the catalog containg all sites
     */
	public static void printSites(final PrintWriter writer, final C3MDSConfiglet.C3Catalog catParam) {
		for (Map.Entry<String, Site> siteEntry : catParam.getSiteById().entrySet()) {
			writer.println("# Site with id: " + siteEntry.getKey());
			writer.println(siteEntry.getValue().toString());
			writer.println();
		}
	}

    /**
     * Prints all archives of a specific <tt>C3Catalog</tt> with their corresponding <tt>oid</tt> to a given PrintWriter.
     * 
     * @param writer a PrintWriter, the list should be printed to
     * @param catParam the catalog containg all sites
     */
	public static void printArchives(final PrintWriter writer, final C3MDSConfiglet.C3Catalog catParam) {
		for (Map.Entry<String, Set<Workspace.Archive>> setEntry : catParam.getArchivesByOid().entrySet()) {
			writer.println("# Archives for oid: " + setEntry.getKey());
			for (Workspace.Archive a : setEntry.getValue())
				writer.println(a);
			writer.println();
		}
	}

    /**
     * Overriden method, returning always false
     *
     * @return
     */
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

    public static void main(String [] args){
        ReadC3CatalogAction test= new ReadC3CatalogAction();
       
    }
}

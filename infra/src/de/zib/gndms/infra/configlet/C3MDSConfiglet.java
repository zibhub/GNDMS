package de.zib.gndms.infra.configlet;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.zib.gndms.c3resource.C3ResourceReader;
import de.zib.gndms.c3resource.jaxb.Site;
import de.zib.gndms.c3resource.jaxb.Workspace;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.kit.configlet.RegularlyRunnableConfiglet;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 06.11.2008 Time: 18:33:18
 */
@SuppressWarnings({ "ClassNamingConvention", "ReturnOfCollectionOrArrayField" })
public class C3MDSConfiglet extends RegularlyRunnableConfiglet {
	private String mdsUrl;
	private volatile C3Catalog catalog;

	@Override
	protected synchronized void threadInit() {
		super.threadInit();
		configMdsUrl();
	}



	@Override
	public synchronized void update(@NotNull final Serializable data) {
		super.update(data);    // Overridden method
		configMdsUrl();
	}


	private void configMdsUrl() {
		try {
			mdsUrl = getMapConfig().getOption("mdsUrl");
		}
		catch ( MandatoryOptionMissingException e) {
			getLog().warn(e);
		}
	}

	@Override
	protected void threadRun() {
		try {
			getLog().info("Refreshing C3MDSCatalog...");
			final C3ResourceReader reader = new C3ResourceReader();
			Iterator<Site> sites = reader.readXmlSites(new java.net.URL(getMdsUrl()).openStream());
			C3Catalog newCatalog = new C3Catalog(sites);
			setCatalog(newCatalog);
			getLog().debug("Finished Refreshing C3MDSCatalog.");
		}
		catch (RuntimeException e) {
			getLog().warn(e);
		}
		catch (IOException e) {
			getLog().warn(e);
		}
	}


	public synchronized C3Catalog getCatalog() {
		while (catalog == null)
			try {
				wait();
			}
			catch (InterruptedException e) {
				/* ignored */
			}
		return catalog;
	}


	private synchronized void setCatalog(final C3Catalog newCatalogParam) {
		catalog = newCatalogParam;
		notifyAll();
	}


	@Override
	protected void threadStop() {
		getThread().interrupt();
	}


	public synchronized String getMdsUrl() {
		return mdsUrl;
	}


	public static class C3Catalog {
		/* forward maps */
		private Map<String, Site> siteById = Maps.newConcurrentHashMap();
		private Map<String, Set<Workspace.Archive>> archivesByOid = Maps.newTreeMap();

		/* reverse maps */
		private Map<Workspace.Archive, Workspace> workspaceByArchive = Maps.newIdentityHashMap();
		private Map<Workspace, Site> siteByWorkspace = Maps.newIdentityHashMap();


		@SuppressWarnings({ "FeatureEnvy" })
		public C3Catalog(Iterator<Site> sites) {
			fillMaps(sites);
			protectMaps();
		}


		private void fillMaps(final Iterator<Site> sites) {
			while (sites.hasNext()) {
				final Site site = sites.next();
				siteById.put(site.getId(), site);
				for (Workspace ws : site.getWorkspace()) {
					siteByWorkspace.put(ws, site);

					for (Workspace.Archive archive : ws.getArchive()) {
						workspaceByArchive.put(archive, ws);

						for (final String oidPrefix : archive.getOidPrefix()) {
							final Set<Workspace.Archive> set;
							if (archivesByOid.containsKey(oidPrefix))
								set  = archivesByOid.get(oidPrefix);
							else {
								set = Sets.newConcurrentHashSet();
								archivesByOid.put(oidPrefix, set);
							}
							set.add(archive);
						}
					}
				}
			}
		}


		private void protectMaps() {
			siteById = immutableMap(siteById);
			archivesByOid = immutableMap(archivesByOid);
			workspaceByArchive = immutableMap(workspaceByArchive);
			siteByWorkspace = immutableMap(siteByWorkspace);
		}


		private static <K, V> Map<K, V> immutableMap(final Map<K, V> map) {
			return Collections.unmodifiableMap(map);
		}


		public Map<String, Site> getSiteById() {
			return siteById;
		}


		public Map<String, Set<Workspace.Archive>> getArchivesByOid() {
			return archivesByOid;
		}


		public Map<Workspace.Archive, Workspace> getWorkspaceByArchive() {
			return workspaceByArchive;
		}


		public Map<Workspace, Site> getSiteByWorkspace() {
			return siteByWorkspace;
		}
	}
}

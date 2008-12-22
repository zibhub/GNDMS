package de.zib.gndms.infra.configlet;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import de.zib.gndms.c3resource.C3ResourceReader;
import de.zib.gndms.c3resource.jaxb.Site;
import de.zib.gndms.c3resource.jaxb.Workspace;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.kit.configlet.RegularlyRunnableConfiglet;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;


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
	private String requiredPrefix;

	private volatile C3Catalog catalog;

	@Override
	protected synchronized void threadInit() {
		super.threadInit();
		configProperties();
	}



	@Override
	public synchronized void update(@NotNull final Serializable data) {
		super.update(data);    // Overridden method
		configProperties();
	}


	private synchronized void configProperties() {
		try {
			mdsUrl = getMapConfig().getOption("mdsUrl");
			requiredPrefix = getMapConfig().getOption("requiredPrefix", "");
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
			final String curRequiredPrefix = getRequiredPrefix();
			Iterator<Site> sites = reader.readXmlSites(curRequiredPrefix, new java.net.URL(getMdsUrl()).openStream());
			C3Catalog newCatalog = new C3Catalog(curRequiredPrefix, sites);
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


	public synchronized String getRequiredPrefix() {
		return requiredPrefix;
	}


	public static class C3Catalog {
		/* forward maps */
		private Map<String, Site> siteById = Maps.newConcurrentHashMap();
		private Map<String, Set<Workspace.Archive>> archivesByOid = Maps.newTreeMap();

		/* reverse maps */
		private Map<Workspace.Archive, Workspace> workspaceByArchive = Maps.newIdentityHashMap();
		private Map<Workspace, Site> siteByWorkspace = Maps.newIdentityHashMap();

		private final String requiredPrefix;

		@SuppressWarnings({ "FeatureEnvy" })
		public C3Catalog(String requiredPrefixParam, Iterator<Site> sites) {
			requiredPrefix = requiredPrefixParam;
			fillMaps(sites);
			protectMaps();
		}


		@SuppressWarnings({ "FeatureEnvy", "ObjectAllocationInLoop" })
		private void fillMaps(final Iterator<Site> sites) {
			final Set<String> allOidPrefixes = Sets.newTreeSet();
			while (sites.hasNext()) {
				final Site site = sites.next();
				// remove prefix
				site.setId(site.getId().substring(requiredPrefix.length()));
				siteById.put(site.getId(), site);
				for (Workspace ws : site.getWorkspace()) {
					siteByWorkspace.put(ws, site);

					for (Workspace.Archive archive : ws.getArchive()) {
						workspaceByArchive.put(archive, ws);

						final List<String> newPrefixes = new LinkedList<String>();
						for (final String curOidPrefix : archive.getOidPrefix()) {
							if (curOidPrefix.startsWith(requiredPrefix)) {
								final String oidPrefix = curOidPrefix.substring(requiredPrefix.length());
								allOidPrefixes.add(oidPrefix);
								final Set<Workspace.Archive> set;
								if (archivesByOid.containsKey(oidPrefix))
									set  = archivesByOid.get(oidPrefix);
								else {
									set = Sets.newConcurrentHashSet();
									archivesByOid.put(oidPrefix, set);
								}
								set.add(archive);
								newPrefixes.add(oidPrefix);
							}
						}
						archive.getOidPrefix().clear();
						archive.getOidPrefix().addAll(newPrefixes);
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


		public @NotNull Set<Workspace.Archive> getArchivesByOid(final String oidPrefixIn) {
			if (oidPrefixIn != null) {
				final String oidPrefix = oidPrefixIn.trim();
				for (int i = oidPrefix.length(); i > 0; i--) {
					final String key = oidPrefix.substring(0, i);
					if (archivesByOid.containsKey(key))
						return archivesByOid.get(key);
				}
			}
			throw new IllegalArgumentException("No archive found with oidPrefix: " +
				  (oidPrefixIn == null ? "(null)" : oidPrefixIn));
		}

		@SuppressWarnings({ "OverlyNestedMethod" })
		public @NotNull Set<Workspace.Archive> getArchivesByOid(final @Nullable String siteId, final @NotNull String oidPrefixIn) {
			if (siteId == null || siteId.trim().length() == 0)
				return getArchivesByOid(oidPrefixIn);
			else {
				Site site = siteById.get(siteId.trim());
				if (site == null)
					throw new IllegalArgumentException("Unknown site: " + siteId);
				else {
					final String oidPrefix = oidPrefixIn.trim();
					final Set<Workspace.Archive> result = Sets.newHashSet();
					if (oidPrefix.length() > 0) {
						int max_len = 0;
						for (Workspace w: site.getWorkspace()) {
							for (Workspace.Archive a: w.getArchive()) {
								for (String prefix: a.getOidPrefix()) {
									if (oidPrefix.startsWith(prefix)) {
										final int cur_len = prefix.length();
										if (cur_len > 0) {
											if (max_len < cur_len) {
												result.clear();
												result.add(a);
												max_len = cur_len;
											}
											if (max_len == cur_len) {
												result.add(a);
											}
										}
									}
								}
							}
						}
					}
					if (result.size() < 1)
						throw new IllegalArgumentException("No archive found with oidPrefix: " + oidPrefix);
					return result;
				}
			}
		}

		public @NotNull Set<Workspace.Archive> getArchivesByOids(final @Nullable String siteId, final @NotNull String[] oidPrefices) {
			return getArchivesByOid(siteId, sharedPrefix(oidPrefices));
		}

		public @NotNull Set<Workspace.Archive> getArchivesByOids(final String[] oidPrefices) {
			return getArchivesByOid(sharedPrefix(oidPrefices));
		}

		private static @NotNull String sharedPrefix(String[] strings) {
			if (strings == null || strings.length == 0) return "";
			String prefix = strings[0].trim();
			if (prefix == null)	return "";
			for (int i = 1; i < strings.length; i++) {
				String str = strings[i].trim();
				if (str == null) return "";
				prefix = sharedPrefix(prefix, str);
				if (prefix.length() == 0) return "";
			}
			return prefix;
		}


		@SuppressWarnings({ "TailRecursion" })
		private static @NotNull String sharedPrefix(
			  final @NotNull String smaller, final @NotNull String larger) {
			if (larger.length() < smaller.length()) return sharedPrefix(larger, smaller);
			for (int i = 0; i < smaller.length(); i++) {
				if (smaller.charAt(i) != larger.charAt(i)) {
					if (i == 0)
						return "";
					else
						return smaller.substring(0, i - 1);
				}
			}
			return smaller;
		}


	}
}

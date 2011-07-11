package de.zib.gndms.model.dspace;
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



/**
 * Subspace model class
 * 
 * @author  try ste fan pla nti kow zib
 * @version $Id$ 
 *
 * User: stepn Date: 24.07.2008 Time: 11:17:22
 */

import de.zib.gndms.common.model.dspace.SubspaceConfiguration;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.stuff.confuror.ConfigEditor;
import de.zib.gndms.stuff.confuror.ConfigHolder;
import de.zib.gndms.stuff.confuror.ConfigEditor.UpdateRejectedException;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * Instances represent concrete subspaces in the local DSpace
 *
 */
@Entity(name="Subspaces")
@Table(name="subspaces", schema="dspace")
//@MappedSuperclass
public class Subspace extends GridResource {

    private long availableSize;

    private long totalSize;

    private MetaSubspace metaSubspace;

    private DSpaceRef dSpaceRef;

    private Set<Slice> slices = new HashSet<Slice>();

    private String path;

    private String gsiFtpPath;


    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="gridSiteId", column=@Column(name="dspace_site", nullable=true, updatable=false)),
        @AttributeOverride(name="resourceKeyValue", column=@Column(name="dspace_uuid", nullable=false, updatable=false))
        })
    public DSpaceRef getDSpaceRef() { return dSpaceRef; }
    public void setDSpaceRef( DSpaceRef newRef ) { dSpaceRef = newRef; }

    /**
     * Sets the path of the Subspace to pth.
     *
     * If pth must exists and be a valid directory with read/write access.
     *
     * @note The read permission will be removed form pth.
     */
    public void setPath( String pth ) {

        /*
        File f = new File( pth );

        if( ! ( f.exists( ) && f.isDirectory( ) ) )
            throw new IllegalStateException( pth+" does not exists or isn't a directory" );

        if( ! f.canWrite( ) )
            throw new IllegalStateException( pth+" is not writable" );
            */

//        if( pth == null )
//            throw new IllegalStateException( "path must not be null" );
        path = pth;
    }


    public void addSlice( @NotNull Slice sl ) {

        if( getSlices() == null )
            throw new IllegalStateException( "No slices set provided" );

        getSlices().add( sl );
    }


    /**
     * Remove a slice from the slice set.
     *
     * The slice itself isn't destroy, and the fold stil exists.
     */
    public void removeSlice( @NotNull Slice sl ) {

        getSlices().remove( sl );
    }


    /** 
     * @brief Delivers the absolute path to a slice sl.
     */
    public String getPathForSlice( Slice sl )  {
        return getPath() + File.separator + sl.getKind( ).getSliceDirectory() + File.separator + sl.getDirectoryId( );
    }


    public String getGsiFtpPathForSlice( Slice sl )  {
        return getGsiFtpPath() + "/" + sl.getKind( ).getSliceDirectory() + "/" + sl.getDirectoryId( );
    }


    @Column(name="avail_size", nullable=false, updatable=false)
    public long getAvailableSize() {
        return availableSize;
    }


    @Column(name="total_size", nullable=false, updatable=false)
    public long getTotalSize() {
        return totalSize;
    }


    @OneToOne(targetEntity=MetaSubspace.class, optional=false, cascade={ CascadeType.REFRESH}, fetch=FetchType.EAGER)
    @PrimaryKeyJoinColumns({@PrimaryKeyJoinColumn(name="schema_uri"),
                            @PrimaryKeyJoinColumn(name="specifier")})
    public MetaSubspace getMetaSubspace() {
        return metaSubspace;
    }


    @OneToMany( targetEntity=Slice.class, mappedBy="subspace", cascade={CascadeType.REFRESH,CascadeType.PERSIST, CascadeType.REMOVE }, fetch=FetchType.EAGER )
    public Set<Slice> getSlices() {
        return slices;
    }


    @Column(name="path", nullable=false, updatable=true)
    public String getPath() {
        return path;
    }


    public String getGsiFtpPath() {
        return gsiFtpPath;
    }


    public void setAvailableSize( long availableSize ) {
        this.availableSize = availableSize;
    }


    public void setTotalSize( long totalSize ) {
        this.totalSize = totalSize;
    }


    public void setMetaSubspace( MetaSubspace metaSubspace ) {
        this.metaSubspace = metaSubspace;
    }


    public void setSlices( Set<Slice> slices ) {
        this.slices = slices;
    }


    public void setGsiFtpPath( String gsiFtpPath ) {
        this.gsiFtpPath = gsiFtpPath;
    }
    
	/**
	 * Constructs the subspace configuration of a given subspace.
	 * 
	 * @param sub
	 *            The subspace.
	 * @return The config holder.
	 * @throws IOException 
	 * @throws UpdateRejectedException 
	 */
	public static ConfigHolder getSubspaceConfiguration(final Subspace sub) 
			throws IOException, UpdateRejectedException {
		String path = sub.getPath();
		String gsiftp = sub.getGsiFtpPath();
		boolean visible = sub.getMetaSubspace().isVisibleToPublic();
		long size = sub.getAvailableSize();

		ConfigHolder config = new ConfigHolder();
		ObjectMapper objectMapper = new ObjectMapper();
		JsonFactory factory = objectMapper.getJsonFactory();
		ConfigEditor.Visitor visitor = new ConfigEditor.DefaultVisitor();
		ConfigEditor editor = config.newEditor(visitor);
		config.setObjectMapper(objectMapper);

		JsonNode pn = ConfigHolder.parseSingle(factory, ConfigHolder.createSingleEntry(SubspaceConfiguration.PATH, path));
		JsonNode gn = ConfigHolder.parseSingle(factory, ConfigHolder.createSingleEntry(SubspaceConfiguration.GSIFTPPATH, gsiftp));
		JsonNode vn = ConfigHolder.parseSingle(factory, ConfigHolder.createSingleEntry(SubspaceConfiguration.VISIBLE, visible));
		JsonNode sn = ConfigHolder.parseSingle(factory, ConfigHolder.createSingleEntry(SubspaceConfiguration.SIZE, size));
		
		// TODO: to get a valid configuration: assume that subspace is in update mode???
		JsonNode mn = ConfigHolder.parseSingle(factory, ConfigHolder.createSingleEntry(SubspaceConfiguration.MODE, "UPDATE"));
		config.update(editor, pn);
		config.update(editor, gn);
		config.update(editor, vn);
		config.update(editor, sn);
		config.update(editor, mn);
			
		return config;
	}

}

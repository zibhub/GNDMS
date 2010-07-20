package de.zib.gndms.model.dspace;

import de.zib.gndms.model.common.AccessMask;
import de.zib.gndms.model.common.GridEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * SliceKinds are identified by a kindURI
 * 
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 01.08.2008 Time: 16:17:45
 */
@Entity(name="SliceKinds")
// @Inheritance
// @DiscriminatorColumn(name="class", discriminatorType=DiscriminatorType.STRING, length=8)
// @DiscriminatorValue("PLAIN")
// @Table(name="slice_kinds", schema="dspace")
@MappedSuperclass
public class SliceKind extends GridEntity {
    private String URI;

//    @Column(name="permission", nullable=false, updatable=false, columnDefinition="VARCHAR", length=15)
    private AccessMask permission;

    //de.zib.gndms.model.dspace.types.SliceKindMode mode

    private String sliceDirectory;

    private Set<MetaSubspace> metaSubspaces = new HashSet<MetaSubspace>();


    @Id @Column(name="uri", nullable=false, updatable=false, columnDefinition="VARCHAR")
    public String getURI() {
        return URI;
    }


    @Column(name="permission", nullable=false, updatable=false )
    public AccessMask getPermission() {
        return permission;
    }


    @Column( name="slice_directory", nullable=false, columnDefinition="VARCHAR" )
    public String getSliceDirectory() {
        return sliceDirectory;
    }


    @ManyToMany(mappedBy="creatableSliceKinds", cascade={CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST}, fetch=FetchType.EAGER)
    public Set<MetaSubspace> getMetaSubspaces() {
        return metaSubspaces;
    }


    public void setURI( String URI ) {
        this.URI = URI;
    }


    public void setPermission( AccessMask permission ) {
        this.permission = permission;
    }


    public void setSliceDirectory( String sliceDirectory ) {
        this.sliceDirectory = sliceDirectory;
    }


    public void setMetaSubspaces( Set<MetaSubspace> metaSubspaces ) {
        this.metaSubspaces = metaSubspaces;
    }
}

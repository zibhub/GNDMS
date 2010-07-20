package de.zib.gndms.model.dspace;

import de.zib.gndms.model.common.GridEntity;
import de.zib.gndms.model.common.ImmutableScopedName;
import javax.persistence.*;

/**
 * Instances reprent the logical role of a subspace in a GNDMS instance
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 01.08.2008 Time: 23:16:48
 */
@Entity(name="MetaSubspaces")
@Table(name="meta_subspaces", schema="dspace")
class MetaSubspace extends GridEntity {
    @EmbeddedId
    @AttributeOverrides([
          @AttributeOverride(name="nameScope", column=@Column(name="schema_uri", nullable=false, updatable=false, columnDefinition="VARCHAR")),
          @AttributeOverride(name="localName", column=@Column(name="specifier", nullable=false, updatable=false, columnDefinition="VARCHAR", length=64))
    ])
    private ImmutableScopedName scopedName;

    @Column(name="visible", nullable=false, updatable=false)
    private boolean visibleToPublic;

    @OneToOne(optional=true, mappedBy="metaSubspace", cascade=[CascadeType.ALL])
    private Subspace instance;

    @ManyToMany(targetEntity=SliceKind.class, cascade = [CascadeType.REFRESH, CascadeType.MERGE, CascadeType.PERSIST], fetch=FetchType.EAGER)
    @JoinTable(name = "creatable_slice_kinds", schema="dspace",
        uniqueConstraints=[@UniqueConstraint(columnNames = ["meta_subspace_schema_uri", "meta_subspace_specifier", "slice_kind_uri"])],
        joinColumns=[@JoinColumn(name="meta_subspace_schema_uri", referencedColumnName="schema_uri", columnDefinition="VARCHAR", nullable=false, updatable=true),
                     @JoinColumn(name="meta_subspace_specifier", referencedColumnName="specifier", columnDefinition="VARCHAR", nullable=false, updatable=true)],
        inverseJoinColumns=[@JoinColumn(name="slice_kind_uri", referencedColumnName="uri", columnDefinition="VARCHAR", nullable=false, updatable=true)])
    Set<SliceKind> creatableSliceKinds = new HashSet<SliceKind>();
}
 

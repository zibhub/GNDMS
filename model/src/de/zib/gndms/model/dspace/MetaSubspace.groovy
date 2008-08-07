package de.zib.gndms.model.dspace

import de.zib.gndms.model.common.GridEntity
import de.zib.gndms.model.common.ImmutableScopedName
import javax.persistence.*

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
	ImmutableScopedName scopedName;

	@Column(name="visible", nullable=false, updatable=false)
	boolean visibleToPublic

	@OneToOne(optional=true, fetch=FetchType.LAZY, mappedBy="metaSubspace", cascade=[CascadeType.ALL])
	Subspace instance

	@ManyToMany(targetEntity=SliceKind.class, fetch=FetchType.LAZY, cascade = [CascadeType.REFRESH, CascadeType.MERGE])
	@JoinTable(name = "creatable_slice_kinds", schema="dspace",
		uniqueConstraints=[@UniqueConstraint(columnNames = ["meta_subspace_schema_uri", "meta_subspace_specifier"]), 
		                   @UniqueConstraint(columnNames = ["slice_kind_uri"])],
		joinColumns=[@JoinColumn(name="meta_subspace_schema_uri", referencedColumnName="schema_uri", columnDefinition="VARCHAR", nullable=false),
		             @JoinColumn(name="meta_subspace_specifier", referencedColumnName="specifier", columnDefinition="VARCHAR", nullable=false)],
		inverseJoinColumns=[@JoinColumn(name="slice_kind_uri", referencedColumnName="uri", columnDefinition="VARCHAR", nullable=false),
		                    @JoinColumn(name="slice_kind_class", referencedColumnName="class", columnDefinition="VARCHAR", nullable=false)])
	Set<SliceKind> creatableSliceKinds
}

package de.zib.gndms.model.gorfx

import de.zib.gndms.model.common.ImmutableScopedName
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.AttributeOverride
import javax.persistence.AttributeOverrides
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.OneToMany
import javax.persistence.FetchType
import static javax.persistence.CascadeType.ALL

/**
 * ThingAMagic.
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 05.09.2008 Time: 14:48:36
 */
@Entity(name="OfferTypes")
@Table(name="offer_types", schema="gorfx")
class OfferType {
    @Id
    @Column(name="offerTypeKey", nullable=false, updatable=false, columnDefinition="VARCHAR")
    String offerTypeKey

    @Embedded
    @AttributeOverrides([
          @AttributeOverride(name="nameScope", column=@Column(name="result_scope", nullable=false, updatable=false, columnDefinition="VARCHAR")),
          @AttributeOverride(name="localName", column=@Column(name="result_lname", nullable=false, updatable=false, columnDefinition="VARCHAR", length=64))
    ])
    ImmutableScopedName offerResultType

    @Embedded
    @AttributeOverrides([
          @AttributeOverride(name="nameScope", column=@Column(name="arg_scope", nullable=false, updatable=false, columnDefinition="VARCHAR")),
          @AttributeOverride(name="localName", column=@Column(name="arg_lname", nullable=false, updatable=false, columnDefinition="VARCHAR", length=64))
    ])
    ImmutableScopedName offerArgumentType

    @Column(name="className", nullable=false, updatable=false, columnDefinition="VARCHAR")
    String calculatorClassName

    @Column(name="factoryClassName", nullable=false, updatable=false)
    String factoryClassName

    @OneToMany(cascade=ALL, mappedBy="offerType", fetch=FetchType.LAZY)
    Set<Task> tasks

    @Column(name="config", nullable=false, updatable=true)
    Map<String, String> configMap
}
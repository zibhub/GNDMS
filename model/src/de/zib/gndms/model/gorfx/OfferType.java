package de.zib.gndms.model.gorfx;

import de.zib.gndms.model.common.ImmutableScopedName;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import static javax.persistence.CascadeType.ALL
import javax.persistence.Basic;
import javax.persistence.Lob;
import javax.persistence.Transient;
import de.zib.gndms.stuff.copy.Copyable;
import de.zib.gndms.stuff.copy.CopyMode;


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
@Copyable(CopyMode.DONT)
class OfferType {
    @Id
    @Column(name="offer_type_key", nullable=false, updatable=false, columnDefinition="VARCHAR")
    private String offerTypeKey;

    @Embedded
    @AttributeOverrides([
          @AttributeOverride(name="nameScope", column=@Column(name="result_scope", nullable=false, updatable=false, columnDefinition="VARCHAR")),
          @AttributeOverride(name="localName", column=@Column(name="result_lname", nullable=false, updatable=false, columnDefinition="VARCHAR", length=64))
    ])
    private ImmutableScopedName offerResultType;

    @Embedded
    @AttributeOverrides([
          @AttributeOverride(name="nameScope", column=@Column(name="arg_scope", nullable=false, updatable=false, columnDefinition="VARCHAR")),
          @AttributeOverride(name="localName", column=@Column(name="arg_lname", nullable=false, updatable=false, columnDefinition="VARCHAR", length=64))
    ])
    private ImmutableScopedName offerArgumentType;

    @Column(name="calc_factory_class_name", nullable=false, updatable=true)
    private String calculatorFactoryClassName;

    @Column(name="task_action_factory_class_name", nullable=false, updatable=true, columnDefinition="VARCHAR")
    private String taskActionFactoryClassName;

    @OneToMany(cascade=ALL, mappedBy="offerType", fetch=FetchType.EAGER)
    Set<Task> tasks = new HashSet<Task>();

    @Column(name="config_map_data", nullable=false, updatable=true)
    private Serializable configMapData;

    Map<String, String> getConfigMap() {
        return (Map<String, String>) getConfigMapData()
    }

    void setConfigMap(Map<String, String> newMap) {
        setConfigMapData((Serializable) newMap)
    }
 }

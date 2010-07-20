package de.zib.gndms.model.gorfx;

import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.stuff.copy.CopyMode;
import de.zib.gndms.stuff.copy.Copyable;

import javax.persistence.*;
import static javax.persistence.CascadeType.ALL;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


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
public class OfferType {

    private String offerTypeKey;

    private ImmutableScopedName offerResultType;

    private ImmutableScopedName offerArgumentType;

    private String calculatorFactoryClassName;

    private String taskActionFactoryClassName;

    private Set<Task> tasks = new HashSet<Task>();

    private Serializable configMapData;

    @Transient
    public Map<String, String> getConfigMap() {
        return (Map<String, String>) getConfigMapData();
    }

    public void setConfigMap (Map<String, String> newMap) {
        setConfigMapData((Serializable) newMap);
    }


    @Id
    @Column(name="offer_type_key", nullable=false, updatable=false, columnDefinition="VARCHAR")
    public String getOfferTypeKey() {
        return offerTypeKey;
    }


    @Embedded
    @AttributeOverrides({
          @AttributeOverride(name="nameScope", column=@Column(name="result_scope", nullable=false, updatable=false, columnDefinition="VARCHAR")),
          @AttributeOverride(name="localName", column=@Column(name="result_lname", nullable=false, updatable=false, columnDefinition="VARCHAR", length=64))
    })
    public ImmutableScopedName getOfferResultType() {
        return offerResultType;
    }


    @Embedded
    @AttributeOverrides({
          @AttributeOverride(name="nameScope", column=@Column(name="arg_scope", nullable=false, updatable=false, columnDefinition="VARCHAR")),
          @AttributeOverride(name="localName", column=@Column(name="arg_lname", nullable=false, updatable=false, columnDefinition="VARCHAR", length=64))
    })
    public ImmutableScopedName getOfferArgumentType() {
        return offerArgumentType;
    }


    @Column(name="calc_factory_class_name", nullable=false, updatable=true)
    public String getCalculatorFactoryClassName() {
        return calculatorFactoryClassName;
    }


    @Column(name="task_action_factory_class_name", nullable=false, updatable=true, columnDefinition="VARCHAR")
    public String getTaskActionFactoryClassName() {
        return taskActionFactoryClassName;
    }


    @OneToMany(cascade=ALL, mappedBy="offerType", fetch=FetchType.EAGER)
    public Set<Task> getTasks() {
        return tasks;
    }


    @Column(name="config_map_data", nullable=false, updatable=true)
    public Serializable getConfigMapData() {
        return configMapData;
    }


    public void setOfferTypeKey( String offerTypeKey ) {
        this.offerTypeKey = offerTypeKey;
    }


    public void setOfferResultType( ImmutableScopedName offerResultType ) {
        this.offerResultType = offerResultType;
    }


    public void setOfferArgumentType( ImmutableScopedName offerArgumentType ) {
        this.offerArgumentType = offerArgumentType;
    }


    public void setCalculatorFactoryClassName( String calculatorFactoryClassName ) {
        this.calculatorFactoryClassName = calculatorFactoryClassName;
    }


    public void setTaskActionFactoryClassName( String taskActionFactoryClassName ) {
        this.taskActionFactoryClassName = taskActionFactoryClassName;
    }


    public void setTasks( Set<Task> tasks ) {
        this.tasks = tasks;
    }


    public void setConfigMapData( Serializable configMapData ) {
        this.configMapData = configMapData;
    }
}

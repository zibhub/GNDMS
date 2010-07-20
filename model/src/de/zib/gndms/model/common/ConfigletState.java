package de.zib.gndms.model.common;

import javax.persistence.*;
import java.io.Serializable;

/**
 * ThingAMagic.
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 06.11.2008 Time: 18:01:53
 */
@Entity(name="ConfigletStates") // @Table(name="configlet_states", schema="common")
@MappedSuperclass
@NamedQueries({
@NamedQuery(name="listAllConfiglets", query="SELECT x FROM ConfigletStates x" )
})
public class ConfigletState extends GridEntity {

    private String name;

    private String className;

    private Serializable state;


    @Id @Column(name = "name", nullable = false, updatable = false, columnDefinition="VARCHAR")
    public String getName() {
        return name;
    }


    public void setName( String name ) {
        this.name = name;
    }


    @Column(name ="class_name", nullable = false, updatable = false, columnDefinition="VARCHAR")
    public String getClassName() {
        return className;
    }


    public void setClassName( String className ) {
        this.className = className;
    }


    @Basic @Column(name = "state")
    public Serializable getState() {
        return state;
    }


    public void setState( Serializable state ) {
        this.state = state;
    }
}

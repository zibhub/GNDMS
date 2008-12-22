package de.zib.gndms.model.common

import javax.persistence.Id
import javax.persistence.Column
import javax.persistence.Basic
import javax.persistence.Entity
import javax.persistence.Table
import javax.persistence.MappedSuperclass
import javax.persistence.Query
import javax.persistence.NamedQueries
import javax.persistence.NamedQuery

/**
 * ThingAMagic.
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 06.11.2008 Time: 18:01:53
 */
@Entity(name="ConfigletStates") @Table(name="configlet_states", schema="common")
@MappedSuperclass
@NamedQueries([
@NamedQuery(name="listAllConfiglets", query="SELECT x FROM ConfigletStates x" )
])
public class ConfigletState extends GridEntity {
    @Id @Column(name = "name", nullable = false, updatable = false, columnDefinition="VARCHAR")
    String name

    @Column(name ="class_name", nullable = false, updatable = false, columnDefinition="VARCHAR")
    String className

    @Basic @Column(name = "state")
    Serializable state;
}
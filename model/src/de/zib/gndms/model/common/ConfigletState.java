package de.zib.gndms.model.common;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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



import javax.persistence.*;
import java.io.Serializable;

/**
 * ThingAMagic.
 * 
 * @author: try ste fan pla nti kow zib
 * @version $Id$ 
 *
 * User: stepn Date: 06.11.2008 Time: 18:01:53
 */
@Entity(name="ConfigletStates") 
@Table(name="configlet_states", schema="common")
//@MappedSuperclass
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

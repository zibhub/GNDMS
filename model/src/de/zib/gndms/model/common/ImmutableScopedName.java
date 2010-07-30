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



import de.zib.gndms.model.ModelId;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.xml.namespace.QName;

/**
 * Model-class for a QName
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 * User: stepn Date: 02.08.2008 Time: 00:41:09
 */
@Embeddable
public class ImmutableScopedName extends ModelId {

    private String nameScope;

    private String localName;

    public ImmutableScopedName () { super(); }

    public ImmutableScopedName (@NotNull String fullNameStr) {        
        this(QName.valueOf(fullNameStr));
    }

    public ImmutableScopedName (@NotNull String theScope, @NotNull String theName) {
        this();
        setNameScope( theScope );
        setLocalName( theName );
    }

    public ImmutableScopedName (@NotNull QName qname) {
        this(qname.getNamespaceURI(), qname.getLocalPart() );
    }

    protected boolean equalFields(@NotNull Object obj) {
        ImmutableScopedName other = (ImmutableScopedName) obj;
        return getNameScope() == other.getNameScope() && getLocalName() == other.getLocalName();
    }

    @Override
    public int hashCode() {
        return hashCode0( getNameScope() ) ^ hashCode0( getLocalName() );
    }

    public QName toQName()
        { return new QName( getNameScope(), getLocalName() ); }


    @Column(name="scope", columnDefinition="VARCHAR", nullable=true, updatable=false)
    public String getNameScope() {
        return nameScope;
    }


    protected void setNameScope( String nameScope ) {
        this.nameScope = nameScope;
    }


    @Column(name="local", columnDefinition="VARCHAR", nullable=false, updatable=false)
    public String getLocalName() {
        return localName;
    }


    protected void setLocalName( String localName ) {
        this.localName = localName;
    }
}

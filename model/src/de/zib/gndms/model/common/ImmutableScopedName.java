package de.zib.gndms.model.common;

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

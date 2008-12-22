package de.zib.gndms.model.common

import de.zib.gndms.model.ModelId
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.xml.namespace.QName
import org.jetbrains.annotations.NotNull

/**
 * Model-class for a QName
 * 
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$ 
 *
 * User: stepn Date: 02.08.2008 Time: 00:41:09
 */
@Embeddable
class ImmutableScopedName extends ModelId {
    public _Dummy_ ( final String optionParam ) { super ( ) ; }
	@Column(name="scope", columnDefinition="VARCHAR", nullable=true, updatable=false)
	private String nameScope

	@Column(name="local", columnDefinition="VARCHAR", nullable=false, updatable=false)
	private String localName

	ImmutableScopedName() {}

    ImmutableScopedName(@NotNull String fullNameStr) {        
        this(QName.valueOf(fullNameStr))
    }

	ImmutableScopedName(@NotNull String theScope, @NotNull String theName) {
		this()
		nameScope = theScope
		localName = theName
	}

	ImmutableScopedName(@NotNull QName qname) { this(qname.namespaceURI, qname.localPart) }

	protected boolean equalFields(Object obj) {
		ImmutableScopedName other = (ImmutableScopedName) obj
		return nameScope == other.nameScope && localName == other.localName
	}

	@Override
	int hashCode() {
		return hashCode0(nameScope) ^ hashCode0(localName)
	}

	QName toQName()
		{ return new QName(nameScope, localName) }

    public String getNameScope() { return nameScope }

    public String getLocalName() { return localName }
}
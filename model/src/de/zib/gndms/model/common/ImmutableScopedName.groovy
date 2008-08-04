package de.zib.gndms.model.common

import javax.persistence.Embeddable
import javax.persistence.Column
import de.zib.gndms.model.ModelEntity
import de.zib.gndms.model.ModelId
import javax.xml.namespace.QName

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
	@Column(name="scope", columnDefinition="VARCHAR", nullable=true, updatable=false)
	String nameScope

	@Column(name="local", columnDefinition="VARCHAR", nullable=false, updatable=false)
	String localName

	def public ImmutableScopedName() {}

	def public ImmutableScopedName(String theScope, String theName) {
		this()
		nameScope = theScope
		localName = theName
	}

	def public ImmutableScopedName(QName qname) { this(qname.namespaceURI, qname.localPart) }

	def protected boolean equalFields(Object obj) {
		ImmutableScopedName other = (ImmutableScopedName) obj
		return nameScope == other.nameScope && localName == other.localName
	}

	@Override
	def public int hashCode() {
		return hashCode0(nameScope) ^ hashCode0(localName)
	}

	def public QName toQName() {
		new QName(nameScope, localName)
	}
}
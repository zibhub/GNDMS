package de.zib.gndms.kit.logging;

/**
 * LDPHolders give access to an externally modifyable LDP.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 29.07.2008 Time: 18:14:55
 */
public interface LDPHolder {
	LoggingDecisionPoint getLDP();
	void setLDP(LoggingDecisionPoint newLDP);
}

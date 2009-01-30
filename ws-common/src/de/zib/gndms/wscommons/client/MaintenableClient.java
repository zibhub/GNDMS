package de.zib.gndms.wscommons.client;

import java.rmi.RemoteException;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 27.01.2009, Time: 12:21:16
 */
public interface MaintenableClient {

    public java.lang.Object callMaintenanceAction(java.lang.String action,types.ContextT options) throws RemoteException;
}

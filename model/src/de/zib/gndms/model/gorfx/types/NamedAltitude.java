package de.zib.gndms.model.gorfx.types;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 22.09.2008, Time: 17:37:51
 */
public class NamedAltitude implements Altitude {

    private String name;


    public NamedAltitude() {
    }


    public NamedAltitude( String name ) {
        this.name = name;
    }


    public String getName() {
        return name;
    }


    public void setName( String name ) {
        this.name = name;
    }
}

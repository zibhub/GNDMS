package de.zib.gndms.gndmc.dspace;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
* Created by IntelliJ IDEA.
* User: explicit
* Date: 02.12.11
* Time: 17:36
* To change this template use File | Settings | File Templates.
*/
@Entity( name = "ENTITY" )
@Table( name = "TESTTABLE", schema = "dspace" )
public class TestTable {
    private String a;
    private String b;

    private int version;

    @Id
    public void setA(String a) {
        this.a = a;
    }

    public String getA( ) {
        return a;
    }

    public void setB(String b) {
        this.b = b;
    }

    public String getB() {
        return b;
    }

    public void setVersion( int v ) {
        version = v;
    }

    @Version
    public int getVersion( ) {
        return version;
    }

}

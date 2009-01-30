package de.zib.gndms.model.test;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import de.zib.gndms.model.common.types.PermissionConfigData;

import java.io.File;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 07.01.2009, Time: 17:06:13
 */
public class PermissionConfigTest {


    @Parameters( {"configFile"} )
    @Test
    public void loadAConfig( String configFile ) throws Exception{

        PermissionConfigData conf = new PermissionConfigData();

        File f = new File( configFile );
        conf.fromPropertyFile( f );

        System.out.println( conf.toString() );
    }




    public void showStringOrNull( String lab, String dat ) {
        System.out.println( lab + ": " + ( dat != null ? dat : "null" ) );
    }

}

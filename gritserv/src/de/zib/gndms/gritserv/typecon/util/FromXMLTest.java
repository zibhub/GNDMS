package de.zib.gndms.gritserv.typecon.util;

import de.zib.gndms.model.gorfx.types.io.xml.ORQWrapper;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQStdoutWriter;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQConverter;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.kit.application.AbstractApplication;
import org.kohsuke.args4j.Option;

import java.io.FileInputStream;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 09.02.2009, Time: 16:18:19
 */
public class FromXMLTest extends AbstractApplication {

    @Option( name="-fn", required=true, usage="xml request file name" )
    private String fn;

    public static void main ( String [] args ) throws Exception {
        FromXMLTest test = new FromXMLTest();
        test.run( args );
    }

    public void run () throws Exception {

        FileInputStream ifs = new FileInputStream( fn );


        ProviderStageInXMLImpl reader = new ProviderStageInXMLImpl( );
        ORQWrapper wrp = reader.fromDocument( ifs );

        ifs.close();
        
        ProviderStageInORQ orq = (ProviderStageInORQ) wrp.getOrq();

        ProviderStageInORQStdoutWriter stdout = new ProviderStageInORQStdoutWriter();
        ProviderStageInORQConverter conv = new ProviderStageInORQConverter( stdout, orq );

        conv.convert();
    }

}

package de.zib.gndms.GORFX.action.tests;

import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.CmdLineException;
import de.zib.gndms.kit.application.AbstractApplication;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 18:28:22
 */
public class InterSliceTransferClient extends AbstractApplication {

    // do provider stagin
    // create new slice on different host
    // transfer staged data to created slice

    @Option( name="-uri", required=true, usage="URI of the gorfx service" )
    public String uri;
    @Option( name="-props", required=true, usage="properties for the staging request" )
    public String props;


    public void run() {
        // not required here
        System.out.println( uri );
        System.out.println( props );
    }


    public static void main( String[] args ) throws Exception {

        InterSliceTransferClient cnt = new InterSliceTransferClient();
        cnt.run( args );
    }
}
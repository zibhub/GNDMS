package de.zib.gndms.GORFX.tests.misc;

import types.ProviderStageInORQT;
import org.apache.axis.message.MessageElement;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 27.08.2008, Time: 13:34:23
 */
public class StageInTypeTests {

    public static void main ( String[] args ) {


        ProviderStageInORQT orq = new ProviderStageInORQT();
        MessageElement[] me = orq.get_any();
        System.out.println( "OfferType: " +orq.getOfferType() );
        if( me != null )
            for( int i=0; i < me.length; ++i )
                System.out.println( i+ ": " + me[i].getType( ).toString() );
        else
            System.out.println( "no elements received" );
    }
}

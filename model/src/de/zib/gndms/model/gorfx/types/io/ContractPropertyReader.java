package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.common.types.TransientContract;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 27.10.2008 Time: 12:29:27
 */
public class ContractPropertyReader extends AbstractPropertyReader<TransientContract> {

    public ContractPropertyReader() {
        super(TransientContract.class);
    }


    public ContractPropertyReader(final Properties properties) {
        super(TransientContract.class, properties);
    }


    @SuppressWarnings({ "FeatureEnvy" })
    @Override
    public void read() {
        final @NotNull TransientContract con = getProduct();

        String s;
        if( getProperties().containsKey( SfrProperty.EST_IF_DECISION_BEFORE.key ) ) {
            DateTime dt =  PropertyReadWriteAux.readISODateTime( getProperties(), SfrProperty.EST_IF_DECISION_BEFORE.key );
            if( dt != null )
                con.setAccepted( dt.toDateTimeISO() );
        }

        if( getProperties().containsKey( SfrProperty.EST_EXEC_LIKELY_UNTIL.key ) )
            con.setDeadline(PropertyReadWriteAux.readFutureTime(getProperties(), SfrProperty.EST_EXEC_LIKELY_UNTIL.key));

        if( getProperties().containsKey( SfrProperty.EST_RESULT_VALID_UNTIL.key ) )
            con.setResultValidity(PropertyReadWriteAux.readFutureTime(getProperties(), SfrProperty.EST_RESULT_VALID_UNTIL.key));

        final Object sizeProperty = getProperties().get(SfrProperty.EST_MAX_SIZE.key);
	    if (sizeProperty != null) {
		    final String sizePropertyStr = sizeProperty.toString().trim();
		    if (sizePropertyStr.length() > 0)
		        con.setExpectedSize(Long.parseLong(sizePropertyStr));
	    }

        if ( getProperties().containsKey( SfrProperty.EST_REQUEST_INFO.key ) )
            con.setAdditionalNotes( PropertyReadWriteAux.readMap( getProperties(), SfrProperty.EST_REQUEST_INFO.key ) );
    }


    public void done() {
    }


    public static TransientContract readFromFile( final String fileName ) throws IOException {

        InputStream is = null;
        try {
            is = new FileInputStream( fileName );
            Properties prop = new Properties( );
            prop.load( is );
            is.close( );
            ContractPropertyReader reader = new ContractPropertyReader( prop );
            reader.performReading( );
            return  reader.getProduct();
        } finally {
            if( is != null )
                is.close( );
        }
    }
}

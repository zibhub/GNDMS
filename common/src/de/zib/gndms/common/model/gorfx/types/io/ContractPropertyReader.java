package de.zib.gndms.common.model.gorfx.types.io;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import de.zib.gndms.common.model.gorfx.types.Quote;
import org.jetbrains.annotations.NotNull;
import org.joda.time.DateTime;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 27.10.2008 Time: 12:29:27
 */
public class ContractPropertyReader extends AbstractPropertyReader<Quote> {

    public ContractPropertyReader() {
        super(Quote.class);
    }


    public ContractPropertyReader(final Properties properties) {
        super(Quote.class, properties);
    }


    @SuppressWarnings({ "FeatureEnvy" })
    @Override
    public void read() {
        final @NotNull Quote con = getProduct();

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
            con.addAdditionalNotes( PropertyReadWriteAux.readMap( getProperties( ), SfrProperty.EST_REQUEST_INFO.key ) );
        if ( getProperties().containsKey( SfrProperty.CONTEXT.key ) )
            con.addContext( PropertyReadWriteAux.readMap( getProperties( ), SfrProperty.CONTEXT.key ) );
     }


    public void done() {
    }


    public static Quote readFromFile( final String fileName ) throws IOException {

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

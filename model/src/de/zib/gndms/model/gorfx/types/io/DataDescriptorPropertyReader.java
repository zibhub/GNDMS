package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.DataDescriptor;

import java.util.Properties;
import java.util.Set;

/**
 * Reads a data descriptor form a property file.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 10:36:23
 */
public class DataDescriptorPropertyReader extends AbstractPropertyReader<DataDescriptor> {


    public DataDescriptorPropertyReader() {
        super( DataDescriptor.class );
    }


    public DataDescriptorPropertyReader( Properties properties ) {
        super(  DataDescriptor.class, properties );
    }


    /**
     * Reads a provided property file.
     * PRECONDITION  begin( ) must be called.
     */
    @Override
    public void read( ) {

        getProduct( ).setObjectList(
            PropertyReadWriteAux.readListMultiLine( getProperties(), SfrProperty.OBJECT_ITEMS.key ) );
        
        getProduct( ).setSpaceConstraint( SpaceConstraintPropertyReader.readSpaceConstraint( getProperties() ) );

        getProduct( ).setTimeConstraint( TimeConstraintPropertyReader.readTimeConstraint( getProperties() ) );

        // read cfl 
        String [] cfl =  PropertyReadWriteAux.readListMultiLine( getProperties(), SfrProperty.CFLIST_ITEMS.key );
        if( cfl == null )
            cfl = PropertyReadWriteAux.readList( getProperties(), SfrProperty.CFLIST_OLD.key, ' ' );
        getProduct( ).setCFList( cfl );

        getProduct( ).setConstraintList(
            PropertyReadWriteAux.readMap( getProperties(), SfrProperty.CONSTRAINT_LIST.key ) );

        getProduct( ).setDataFormat( getProperties().getProperty( SfrProperty.FILE_FORMAT.key ) );
        getProduct( ).setMetaDataFormat( getProperties().getProperty( SfrProperty.META_FILE_FORMAT.key ) );
    }


    public void done() {
    }
}

package de.zib.gndms.model.gorfx.types.io;

import de.zib.gndms.model.gorfx.types.SpaceConstraint;
import de.zib.gndms.model.gorfx.types.TimeConstraint;

import java.util.HashMap;
import java.util.Properties;
import java.util.Arrays;
import java.util.List;

import org.jetbrains.annotations.NotNull;

/**
 * Builder to export a data descriptor into a property file.
 *
 * NOTE loading and storing of the Properties must be performed by their provider.
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 17.09.2008, Time: 15:34:01
 */
public class DataDescriptorPropertyWriter extends AbstractPropertyIO implements DataDescriptorWriter {

    public DataDescriptorPropertyWriter() {
        super( );
    }


    public DataDescriptorPropertyWriter( @NotNull Properties properties ) {
        super( properties );
    }


    public void writeObjectList( @NotNull String[] objectList ) {
        PropertyReadWriteAux.writeListMultiLine( getProperties(), SfrProperty.OBJECT_ITEMS.key, Arrays.asList( objectList ) );
    }


    public void writeTimeConstraint( @NotNull TimeConstraint timeConstraint ) {

        if( timeConstraint.hasMinTime( ) )
            TimeConstraintPropertyWriter.writeMinTimeToProperties( getProperties(), timeConstraint.getMinTime( ) );

        if( timeConstraint.hasMaxTime( ) )
            TimeConstraintPropertyWriter.writeMaxTimeToProperties( getProperties(), timeConstraint.getMaxTime( ) );
    }


    public void writeCFList( @NotNull String[] cflist ) {
        List<String> cfl = Arrays.asList( cflist );
        PropertyReadWriteAux.writeListMultiLine( getProperties(), SfrProperty.CFLIST_ITEMS.key, cfl );
        PropertyReadWriteAux.writeList( getProperties(), SfrProperty.CFLIST_OLD.key, ' ', cfl );
    }


    public void writeConstraintList( @NotNull HashMap<String, String> constraintList ) {
        PropertyReadWriteAux.writeMap( getProperties(), SfrProperty.CONSTRAINT_LIST.key, constraintList );
    }


    public void writeDataFormat( @NotNull String dataFormat ) {
        getProperties().setProperty( SfrProperty.FILE_FORMAT.key, dataFormat );
    }


    public void writeMetaDataFormat( @NotNull String metaDataFormat ) {
        getProperties().setProperty( SfrProperty.META_FILE_FORMAT.key, metaDataFormat );
    }


    public SpaceConstraintWriter getSpaceConstraintWriter() {
        return new SpaceConstraintPropertyWriter( getProperties( ) );
    }


    public void beginWritingSpaceConstraint() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void doneWritingSpaceConstraint() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void done() {
        // not required here
    }
}

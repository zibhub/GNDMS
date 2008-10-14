package de.zib.gndms.model.gorfx.types.io;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import de.zib.gndms.model.gorfx.types.TimeConstraint;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 08.10.2008, Time: 14:56:59
 */
public class DataConstraintsPropertyWriter extends AbstractPropertyIO implements DataConstraintsWriter {


    public DataConstraintsPropertyWriter() {
    }


    public DataConstraintsPropertyWriter( Properties properties ) {
        super( properties );
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


    public SpaceConstraintWriter getSpaceConstraintWriter() {
        return new SpaceConstraintPropertyWriter( getProperties( ) );
    }


    public void beginWritingSpaceConstraint() {
        // not required here
    }


    public void doneWritingSpaceConstraint() {
        // not required here
    }


    public void done() {
        // not required here
    }
}

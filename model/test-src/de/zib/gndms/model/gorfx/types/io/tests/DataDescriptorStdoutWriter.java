package de.zib.gndms.model.gorfx.types.io.tests;

import de.zib.gndms.model.gorfx.types.io.DataDescriptorWriter;
import de.zib.gndms.model.gorfx.types.io.SpaceConstraintWriter;
import de.zib.gndms.model.gorfx.types.SpaceConstraint;
import de.zib.gndms.model.gorfx.types.TimeConstraint;
import de.zib.gndms.model.gorfx.types.MinMaxPair;

import java.util.HashMap;
import java.util.Set;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 17:22:28
 */
public class DataDescriptorStdoutWriter implements DataDescriptorWriter {

    public void writeObjectList( String[] objectList ) {
        System.out.println( "ObjectList: " );
        showStringList( objectList, "    " );
    }


    public void writeTimeConstraint( TimeConstraint timeConstraint ) {
        System.out.println( "TimeConstraint" );
        System.out.println( "    MinTime: " + timeConstraint.getMinTimeString() );
        System.out.println( "    MaxTime: " + timeConstraint.getMaxTimeString() );
    }


    public void writeCFList( String[] CFList ) {
        System.out.println( "CFList: " );
        showStringList( CFList, "    " );
    }


    public void writeConstraintList( HashMap<String, String> constraintList ) {
        System.out.println( "Constraint list: " );
        Set<String> ks = constraintList.keySet();
        for( String k : ks )
            System.out.println( "    " + k + " ; " + constraintList.get( k ) );
    }


    public void writeDataFormat( String dataFormat ) {
        System.out.println( "dataFormat: " + dataFormat );
    }


    public void writeMetaDataFormat( String metaDataFormat ) {
        System.out.println( "metaDataFormat: " + metaDataFormat );
    }

    public SpaceConstraintWriter getSpaceConstraintWriter() {
        return new SpaceConstraintStdoutWriter( );

    }
    public void beginWritingSpaceConstraint() {
        System.out.println( "SpaceConstraint" );
    }


    public void doneWritingSpaceConstraint() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public void begin() {
        System.out.println( "********************* DataDescriptor *********************" );
    }

    public void done() {
        System.out.println( "******************** EODataDescriptor ********************" );
    }

    private void showStringList( String[] sl, String ind ) {
        if( sl == null )
            System.out.println( ind + "null" );
        for( int i=0; i < sl.length; ++i )
            System.out.println( ind + sl[i] );
    }
}

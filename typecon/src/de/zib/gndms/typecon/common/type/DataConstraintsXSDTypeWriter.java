package de.zib.gndms.typecon.common.type;

import de.zib.gndms.model.gorfx.types.io.DataConstraintsWriter;
import de.zib.gndms.model.gorfx.types.io.SpaceConstraintWriter;
import de.zib.gndms.model.gorfx.types.TimeConstraint;
import types.DataDescriptorTConstraints;
import types.CFListT;
import types.NameValEntryT;
import types.ConstraintListT;

import java.util.HashMap;
import java.util.Set;
import java.util.ArrayList;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 16.10.2008, Time: 09:12:27
 */
public class DataConstraintsXSDTypeWriter extends AbstractXSDTypeWriter<DataDescriptorTConstraints> implements DataConstraintsWriter {

   private SpaceConstrantXSDTypeWriter spaceWriter;


    //no writeSpaceConstraint see below
    public void writeTimeConstraint( TimeConstraint timeConstraint ) {

        TimeConstraintXSDTypeWriter tw = new TimeConstraintXSDTypeWriter();
        tw.begin( );
        if( timeConstraint.hasMinTime() )
            tw.writeMinTime( timeConstraint.getMinTime() );

        if( timeConstraint.hasMaxTime() )
            tw.writeMaxTime( timeConstraint.getMaxTime() );

        tw.done( );

        getProduct().setTimeConstr( tw.getProduct() );
    }


    public void writeCFList( String[] cflist ) {
        getProduct().setCFList( new CFListT( cflist ) );
    }


    public void writeConstraintList( HashMap<String, String> constraintList ) {

        Set<String> keys = constraintList.keySet();

        ArrayList<NameValEntryT> al = new ArrayList<NameValEntryT>( constraintList.size( ) );
        for( String s: keys ) {
            al.add( new NameValEntryT( s, constraintList.get( s ) )  );
        }

        getProduct().setConstraintList( new ConstraintListT( al.toArray( new NameValEntryT[al.size()] ) ) );
    }
    

    public SpaceConstraintWriter getSpaceConstraintWriter() {
        
        if( spaceWriter == null )
            spaceWriter = new SpaceConstrantXSDTypeWriter( );
        return spaceWriter;
    }

    public void beginWritingSpaceConstraint() {
        if( spaceWriter == null )
            throw new IllegalStateException( "no space writer present" );
    }


    public void doneWritingSpaceConstraint() {
        if( spaceWriter == null )
            throw new IllegalStateException( "no space writer present" );

        getProduct().setSpaceConstr( spaceWriter.getProduct() );
    }


    public void begin() {
        setProduct( new DataDescriptorTConstraints( ) );
    }


    public void done() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

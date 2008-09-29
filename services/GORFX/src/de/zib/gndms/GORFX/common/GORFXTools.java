package de.zib.gndms.GORFX.common;

import de.zib.gndms.model.gorfx.types.*;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.common.ImmutableScopedName;
import types.*;

import java.util.HashMap;

import org.apache.axis.types.NormalizedString;
import org.apache.axis.types.URI;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 5:08:47 PM
 */
public class GORFXTools {

    public static AbstractORQ convertFromORQT( DynamicOfferDataSeqT orq ) throws Exception {

        if( orq.getOfferType().toString().equals( GORFXConstantURIs.PROVIDER_STAGE_IN_URI ) )
            return convertProviderStageInORQFromORQT( (ProviderStageInORQT) orq );
        else
            throw new IllegalArgumentException( );

    }


    // todo implement this using builder form model.gorfx
    public static ProviderStageInORQ convertProviderStageInORQFromORQT( ProviderStageInORQT orqt ) throws Exception, InstantiationException, IllegalAccessException {

        if(! orqt.getOfferType().equals( GORFXClientTools.getProviderStageInURI() ) )
            throw new IllegalArgumentException( );

        ProviderStageInORQ orq = new ProviderStageInORQ();

        DataDescriptor dds = convertDataDescriptorT( (DataDescriptorT) orqt.get_any()[0].getObjectValue( DataDescriptorT.class ) );
        orq.setDataDescriptor( dds );
        orq.setDataFile( (String) orqt.get_any()[1].getObjectValue( String.class ));
        orq.setMetadataFile( (String) orqt.get_any()[2].getObjectValue( String.class ));

        return orq;
    }


    // todo implement this using builder form model.gorfx
    public static DataDescriptor convertDataDescriptorT( DataDescriptorT ddt ) {

        /*
        types.ObjectListT getObjectList()
        types.SpaceConstraintT getSpaceConstr()
        types.TimeConstraintT getTimeConstr()
        types.CFListT getCFList()
        types.ConstraintListT getConstraintList()
        java.lang.String getDataFormat()
        java.lang.String getMetaDataFormat()
        */

        DataDescriptor dd = new DataDescriptor( );
        dd.setObjectList( ddt.getObjectList().getItem() );
        
        SpaceConstraint sc = new SpaceConstraint();
        SpaceConstraintT sct = ddt.getSpaceConstr();
        if( sct.getLatitude() != null  ) {
            sc.setLatitude( convertMinMaxT( sct.getLatitude() ) );
        }
        
        if( sct.getLatitude() != null  ) {
            sc.setLongitude( convertMinMaxT( sct.getLatitude() ) );
        } // todo handle altitude as soon as known

        dd.setSpaceConstraint( sc );

        if( ddt.getTimeConstr() != null ) {
            TimeConstraint tc = new TimeConstraint();
            tc.setMinTime( ddt.getTimeConstr().getMinTime() );
            tc.setMaxTime( ddt.getTimeConstr().getMaxTime() );
            dd.setTimeConstraint( tc );
        }

        dd.setCFList( ddt.getCFList().getCFItem( ) );

        NameValEntryT[] nvl = ddt.getConstraintList().getItem();

        HashMap<String, String> hm = new HashMap<String, String>( );
        for( NameValEntryT nve : nvl ) {
            hm.put( nve.getPName(), nve.getPVal() );
        }

        dd.setConstraintList( hm );

        dd.setDataFormat( ddt.getDataFormat() );
        dd.setMetaDataFormat( ddt.getMetaDataFormat() );

        return dd;
    }


    /**
     * Extracts a task state form a given Task object.
     */
    public static TaskExecutionState getStateOfTask( Task tsk ) {

        TaskExecutionState stat = new TaskExecutionState( );
        stat.setDescription( new NormalizedString( tsk.getDescription() ) );
        stat.setContractBroken( tsk.getBroken() );
        stat.setStatus( getXSDTForTaskState( tsk.getState() ) );
        // todo resolve issue
        // tsk.progess float vs stat.progress BigInt
        // stat.setProgress( tsk.getProgress() );
        // tsk has no max progress
        //stat.setMaxProgress(  );

        return stat;
    }


    /**
     * Delivers a TaskStatusT object for a given ...model.gorfx.TaskState
     */
    public static TaskStatusT getXSDTForTaskState( TaskState ts ) {

        switch ( ts ) {
            case CREATED:
                return TaskStatusT.created;
            case INITIALIZED:
                return TaskStatusT.initialized;
            case IN_PROGRESS:
                return TaskStatusT.inprogress;
            case FINISHED:
                return TaskStatusT.finished;
            case FAILED:
                return TaskStatusT.failed;
            case CREATED_UNKNOWN:
            case INITIALIZED_UNKNOWN:
            case IN_PROGRESS_UNKNOWN:
                return TaskStatusT.unknown;
            default:
                throw new IllegalArgumentException( "Unknwon task state received: " + ts );
        }
    }
    

    public static MinMaxT convertMinMaxPair( MinMaxPair mmp ) {
        return new MinMaxT( mmp.getMinValue(), mmp.getMinValue() );
    }

    
    public static MinMaxPair convertMinMaxT( MinMaxT mmp ) {
        return new MinMaxPair( mmp.getMin(), mmp.getMax() );
    }


    public static URI scopedNameToURI( ImmutableScopedName sn ) {

        try {
            return new URI( sn.getNameScope(), sn.getLocalName() );
        } catch ( URI.MalformedURIException e ) {
            e.printStackTrace();
        }
        
        return null;
    }
}

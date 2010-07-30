package de.zib.gndms.gritserv.typecon;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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



import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.model.gorfx.Task;
import de.zib.gndms.model.gorfx.types.*;
import de.zib.gndms.gritserv.typecon.types.FileTransferORQXSDReader;
import de.zib.gndms.gritserv.typecon.types.InterSliceTransferORQXSDReader;
import de.zib.gndms.gritserv.typecon.types.ProviderStageInORQXSDReader;
import de.zib.gndms.gritserv.typecon.types.SliceStageInORQXSDReader;
import org.apache.axis.types.NormalizedString;
import org.apache.axis.types.URI;
import org.apache.axis.types.Token;
import org.apache.axis.types.PositiveInteger;
import org.jetbrains.annotations.NotNull;
import types.*;

import java.util.HashMap;
import java.io.StringWriter;
import java.io.PrintWriter;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 5:08:47 PM
 */
public class GORFXTools {

    public static AbstractORQ convertFromORQT( DynamicOfferDataSeqT orq, ContextT ctx ) throws Exception {

        AbstractORQ aorq = null;
        String ot = orq.getOfferType().toString();
        if( ot.equals( GORFXConstantURIs.PROVIDER_STAGE_IN_URI ) ) {
            aorq = ProviderStageInORQXSDReader.read( orq, ctx );
        } else if( ot.equals( GORFXConstantURIs.FILE_TRANSFER_URI ) )
            aorq = FileTransferORQXSDReader.read( orq, ctx );
        else if( ot.equals( GORFXConstantURIs.INTER_SLICE_TRANSFER_URI ) )
            aorq = InterSliceTransferORQXSDReader.read( orq, ctx );
        else if( ot.equals( GORFXConstantURIs.SLICE_STAGE_IN_URI ) )
             aorq = SliceStageInORQXSDReader.read( orq, ctx );
        else
            throw new IllegalArgumentException( "no handler for orq offertype: " + ot );


        return aorq;
    }


    // todo implement this using builder form model.gorfx
    public static DataDescriptor convertDataDescriptorT( DataDescriptorT ddt ) {

        DataDescriptor dd = new DataDescriptor( );
        dd.setObjectList( ddt.getObjectList().getItem() );

        if( ddt.getConstraints() != null )
            dd.setConstrains( convertDataDescriptorTConstraints( ddt.getConstraints() ) );

        dd.setDataFormat( ddt.getDataFormat() );
        dd.setDataArchiveFormat( ddt.getDataArchiveFormat() );
        dd.setMetaDataFormat( ddt.getMetaDataFormat() );
        dd.setMetaDataArchiveFormat( ddt.getMetaDataArchiveFormat() );

        return dd;
    }
    

    public static DataConstraints convertDataDescriptorTConstraints( DataDescriptorTConstraints cons ) {

        DataConstraints dc = new DataConstraints();

        SpaceConstraint sc = new SpaceConstraint();

        SpaceConstraintT sct = cons.getSpaceConstr();
        MinMaxT v = sct.getLatitude();
        if( v != null )
            sc.setLatitude( convertMinMaxT( v ) );

        v = sct.getLongitude();
        if( v != null )
            sc.setLongitude( convertMinMaxT( v ) );

        v = sct.getAltitude();
        if( v != null )
            sc.setAltitude( convertMinMaxT( v ) );

        String s = sct.getVerticalCRS();
        if( s != null )
            sc.setVerticalCRS( s );

        s = sct.getAreaCRS();
        if( s != null )
            sc.setAreaCRS( s );

        dc.setSpaceConstraint( sc );

        if( cons.getTimeConstr() != null ) {
            TimeConstraint tc = new TimeConstraint();
            tc.setMinTime( cons.getTimeConstr().getMinTime() );
            tc.setMaxTime( cons.getTimeConstr().getMaxTime() );
            dc.setTimeConstraint( tc );
        }

        dc.setCFList( cons.getCFList().getCFItem( ) );

        if( cons.getConstraintList() != null ) {
            NameValEntryT[] nvl = cons.getConstraintList().getItem();

            if( nvl != null && nvl.length > 0) {
                HashMap<String, String> hm = new HashMap<String, String>( );
                for( NameValEntryT nve : nvl ) {
                    hm.put( nve.getPName(), nve.getPVal() );
                }

                dc.setConstraintList( hm );
            }
        }

        return dc;
    }


    /**
     * Extracts a task state form a given Task object.
     */
    public static TaskExecutionState getStateOfTask( Task tsk ) {

        TaskExecutionState stat = new TaskExecutionState( );
        stat.setDescription( new NormalizedString( tsk.getDescription() ) );
        stat.setContractBroken( tsk.isBroken() );
        stat.setStatus( getXSDTForTaskState( tsk.getState() ) );
        stat.setProgress( toPositiveInteger( tsk.getProgress() ) );
        stat.setMaxProgress( toPositiveInteger( tsk.getMaxProgress() ) );

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
            return new URI( sn.getNameScope() + "/" + sn.getLocalName() );
        } catch ( URI.MalformedURIException e ) {
            e.printStackTrace();
        }
        
        return null;
    }


    public static ContextTEntry createContextEntry(String key, String value) {
        final ContextTEntry entry = new ContextTEntry();
        entry.setKey(new Token(key));
        entry.set_value(new NormalizedString(value));
        return entry;
    }


    public static PositiveInteger toPositiveInteger( int i ) {
        if( i == 0 )
            return new PositiveInteger( "1" );
        else
            return new PositiveInteger( Integer.toString( i ) );
    }


    public static TaskExecutionFailure failureFromException( @NotNull Exception e ) {

        TaskExecutionFailureImplementationFault fault = new TaskExecutionFailureImplementationFault( );
        fault.setMessage( e.getMessage() );
        StringWriter sw = new StringWriter( );
        PrintWriter pw  = new PrintWriter( sw );
        e.printStackTrace( pw );
        pw.close( );
        fault.setFaultTrace( sw.toString() );
        fault.setFaultClass( e.getClass().getName() );

        StackTraceElement[] se = e.getStackTrace();
        if( se.length > 0 )
            fault.setFaultLocation( se[0].toString() );

        TaskExecutionFailure tef = new TaskExecutionFailure( );

        tef.setImplementationFault( fault );

        return tef;
    }
}

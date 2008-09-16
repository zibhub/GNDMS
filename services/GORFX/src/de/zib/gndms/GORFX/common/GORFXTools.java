package de.zib.gndms.GORFX.common;

import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.gorfx.types.GORFXConstantURIs;
import types.ProviderStageInORQT;
import de.zib.gndms.model.gorfx.types.SpaceConstraintType;
import types.DynamicOfferDataSeqT;

import javax.xml.soap.SOAPException;
import java.util.ArrayList;
import java.util.HashMap;

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


    public static ProviderStageInORQ convertProviderStageInORQFromORQT( ProviderStageInORQT orqt ) throws Exception, InstantiationException, IllegalAccessException {

        if(! orqt.getOfferType().equals( GORFXClientTools.getPoviderStageInURI() ) )
            throw new IllegalArgumentException( );

        ProviderStageInORQ orq = new ProviderStageInORQ();

        DataDescriptor dds = convertDataDescriptorT( (DataDescriptorT) orqt.get_any()[0].getObjectValue( DataDescriptorT.class ) );
        orq.setDataDescriptor( dds );
        orq.setDataFile( (String) orqt.get_any()[1].getObjectValue( String.class ));
        orq.setMetadataFile( (String) orqt.get_any()[2].getObjectValue( String.class ));

        return orq;
    }


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
            sc.setKind( SpaceConstraintType.LATITUDE );
            sc.setMaxValue( sct.getLatitude().getMax() );
            sc.setMinValue( sct.getLatitude().getMin() );
        } else if( sct.getLatitude() != null  ) {
            sc.setKind( SpaceConstraintType.LONGITUDE );
            sc.setMaxValue( sct.getLongitude().getMax() );
            sc.setMinValue( sct.getLongitude().getMin() );
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

        HashMap<String, String> hm = new HashMap<String, String>;
        for( NameValEntryT nve : nvl ) {
            hm.put( nve.getPName(), nve.getPVal() );
        }

        dd.setConstraintList( hm );

        dd.setDataFormat( ddt.getDataFormat() );
        dd.setMetaDataFormat( ddt.getMetaDataFormat() );

        return dd;
    }


}

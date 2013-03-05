package de.zib.gndms.taskflows.staging.server.logic;

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
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.logic.action.ProcessBuilderAction;
import de.zib.gndms.model.common.types.FilePermissions;
import de.zib.gndms.taskflows.staging.client.model.ProviderStageInOrder;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.util.Properties;

/**
 * Use this class to define different io formats for staging scrips.
 *
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 17.11.2008, Time: 17:01:46
 */
public class StagingIOFormatHelper {

    public static final String FORMAT_PROPS = "PROPS";

    private String format=FORMAT_PROPS;


    public Quote getResult( StringBuilder res ) throws Exception {

        return propsToResult( res );
    }

    
    public ProcessBuilderAction createPBAction( ProviderStageInOrder order, Quote contParam, FilePermissions fp ) {

        return  createDefaultPBAction( order, contParam, fp );
    }

    public ProcessBuilderAction createPBAction( ProviderStageInOrder order, Quote contParam, FilePermissions fp , String dn) {

        return  createDefaultPBAction( order, contParam, fp , dn);
    }

    public void formatFromMap( MapConfig config ) {

        if( config.hasOption( "scriptIoFormat" ) )
            try {
                format = config.getOption( "scriptIoFormat" );
            } catch ( MandatoryOptionMissingException e ) { // this may not happen. really!
                LoggerFactory.getLogger( this.getClass() ).debug( "", e );
            }
    }

    private Quote propsToResult( StringBuilder sb ) throws Exception {

        Properties props = new Properties();
        props.load(new ByteArrayInputStream(sb.toString().getBytes()));

        de.zib.gndms.common.model.gorfx.types.io.ContractPropertyReader
                reader = new de.zib.gndms.common.model.gorfx.types.io.ContractPropertyReader(props);
        reader.performReading();
        return reader.getProduct();
    }

    private ProcessBuilderAction createDefaultPBAction( ProviderStageInOrder order, Quote contParam, FilePermissions fp, String dn ) {
       
        Properties moreProps = allProps(order, contParam,fp);

        if (moreProps == null) {
        	moreProps = new Properties();
        }
       	moreProps.put("c3grid.CommonRequest.Context.Auth.DN", dn);
               
        return ProviderStageInTools.createPBAction( order, moreProps);
        
    }


    private Properties allProps( ProviderStageInOrder order, Quote contParam, FilePermissions fp ) {
        Properties moreProps = null;
        if( contParam != null ) {
            moreProps = new Properties();
            de.zib.gndms.common.model.gorfx.types.io.ContractPropertyWriter
                    writer = new de.zib.gndms.common.model.gorfx.types.io.ContractPropertyWriter(moreProps);
            de.zib.gndms.common.model.gorfx.types.io.ContractConverter
                    conv = new de.zib.gndms.common.model.gorfx.types.io.ContractConverter(writer, contParam);
            conv.convert();
        }

        if( fp != null ) {
            if( moreProps == null )
                moreProps = new Properties( );
            fp.toProperties( "c3grid.CommonRequest.Permissions", moreProps );
        }
        return moreProps;
    }

    private ProcessBuilderAction createDefaultPBAction( ProviderStageInOrder order, Quote contParam, FilePermissions fp ) {

        Properties moreProps = allProps(order, contParam,fp);
        
        return ProviderStageInTools.createPBAction( order, moreProps );
    }
}

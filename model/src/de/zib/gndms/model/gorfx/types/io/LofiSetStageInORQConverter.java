package de.zib.gndms.model.gorfx.types.io;

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



import de.zib.gndms.model.gorfx.types.LofiSetStageInORQ;

/**
 * Created by IntelliJ IDEA.
 * User: roberto
 * Date: 13.10.2008
 * Time: 15:54:03
 * To change this template use File | Settings | File Templates.
 */
public class LofiSetStageInORQConverter extends ORQConverter<LofiSetStageInORQWriter, LofiSetStageInORQ>{

    public LofiSetStageInORQConverter(){

    }

    public LofiSetStageInORQConverter(LofiSetStageInORQWriter writer, LofiSetStageInORQ lofiSetStageInORQ){
        super( writer, lofiSetStageInORQ);
    }

    public void convert(){
        super.convert();
        DataDescriptorWriter ddw = getWriter( ).getDataDescriptorWriter();
        DataDescriptorConverter ddc = new DataDescriptorConverter( ddw,  getModel().getActDataDescriptor() );
        ddc.convert( );

        getWriter().writeDataFileName( getModel().getActDataFile() );
        if ( getModel().hasGridSite())
        getWriter().writeGridSiteName( getModel().getActGridSite() );
        getWriter().writeMetaDataFileName( getModel().getActMetadataFile() );
        getWriter().done();
    }


}

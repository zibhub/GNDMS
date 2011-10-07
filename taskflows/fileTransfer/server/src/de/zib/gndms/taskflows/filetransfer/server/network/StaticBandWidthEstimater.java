package de.zib.gndms.taskflows.filetransfer.server.network;

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



/**
 * This class is for developing and testing purpose only.
 * Its estimateBandWidthFromTo-method returns a fixed value, which is
 * independent of the given hosts.
 * 
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 01.10.2008, Time: 10:29:43
 */
public class StaticBandWidthEstimater implements BandWidthEstimater {

    // lets asume we have 10Mbit/s approx 1 250 000 byte/s ;
    private final Float bandWidth =  new Float( 10 * 1000*1000 / 8 );
    
    public Float estimateBandWidthFromTo( String src, String tgt ) {
        return bandWidth;
    }
}

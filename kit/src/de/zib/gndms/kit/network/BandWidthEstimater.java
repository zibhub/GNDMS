package de.zib.gndms.kit.network;

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
 *
 * An Interface for an Bandwith estimator.
 * 
 * @author  try ma ik jo rr a zib
 * @version  $Id$
 * <p/>
 * User: mjorra, Date: 30.09.2008, Time: 11:33:40
 */
public interface BandWidthEstimater {

    /**
     * Estimates the bandwidth between to given hosts.
     *
     * @return Returns the estimaed speed in byte/s or NULL if there is
     *         they are not connected. (Maybe because one of the hosts doesn't exist)
     */
    public Float estimateBandWidthFromTo( String src, String tgt );
}

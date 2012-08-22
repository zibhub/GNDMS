/*
 * Copyright 2008-2012 Zuse Institute Berlin (ZIB)
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
 *
 */

package de.zib.gndms.common.dspace.service;

import de.zib.gndms.common.dspace.SliceConfiguration;
import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * @date: 22.08.12
 * @time: 17:37
 * @author: Jörg Bachmann
 * @email: bachmann@zib.de
 */
public class SliceInformation extends SliceConfiguration implements Serializable {

    private static final long serialVersionUID = -5945191053405202623L;

    private Long diskUsage;
    

    public SliceInformation() {
    }


    public SliceInformation( Long size, Long termination ) {
        super( size, termination );
    }


    public SliceInformation( Long size, DateTime termination ) {
        super( size, termination );
    }


    public Long getDiskUsage() {
        return diskUsage;
    }


    public void setDiskUsage( Long diskUsage ) {
        this.diskUsage = diskUsage;
    }
}

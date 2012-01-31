package de.zib.gndms.model.common;

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



import de.zib.gndms.model.ModelEntity;
import javax.persistence.*;

/**
 * Super class of grid entities with a version field
 *
 **/
@Inheritance( strategy = InheritanceType.TABLE_PER_CLASS )
@MappedSuperclass
public abstract class GridEntity extends ModelEntity {
    
	private int version;

    @Version
    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    // @Column(name="sys_id", nullable=false, length=16, columnDefinition="CHAR", updatable=false)
	// String systemId
}







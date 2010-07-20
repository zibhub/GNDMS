package de.zib.gndms.model.common;

import de.zib.gndms.model.ModelEntity;
import javax.persistence.*;

/**
 * Super class of grid entities with a version field
 *
 **/
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







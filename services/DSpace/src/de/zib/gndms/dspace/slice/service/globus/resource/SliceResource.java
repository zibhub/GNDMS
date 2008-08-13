package de.zib.gndms.dspace.slice.service.globus.resource;

import org.globus.wsrf.InvalidResourceKeyException;
import org.globus.wsrf.NoSuchResourceException;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.ResourceKey;
import org.jetbrains.annotations.NotNull;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.infra.wsrf.ReloadablePersistentResource;


/** 
 * The implementation of this SliceResource type.
 * 
 * @created by Introduce Toolkit version 1.2
 * 
 */
public class SliceResource extends SliceResourceBase {


    @NotNull
    public Slice loadModelById( @NotNull String id ) throws ResourceException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void loadViaModelId( @NotNull String id ) throws ResourceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void loadFromModel( @NotNull Slice model ) throws ResourceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @NotNull
    public ExtSliceResourceHome getResourceHome() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setResourceHome( @NotNull ExtSliceResourceHome resourceHomeParam ) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void load( ResourceKey resourceKey ) throws ResourceException, NoSuchResourceException, InvalidResourceKeyException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void store() throws ResourceException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}

package de.zib.gndms.logic.dspace;

import java.util.List;

import de.zib.gndms.model.dspace.Slice;

// TODO documentation, implementation

public interface SliceProvider {
    boolean exists(String slice);
    List<String> listSlices();
    Slice getSlice(String slice);

}

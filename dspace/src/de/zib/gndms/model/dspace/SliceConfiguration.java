package de.zib.gndms.model.dspace;

import de.zib.gndms.logic.dspace.WrongConfigurationException;
import de.zib.gndms.stuff.confuror.ConfigHolder;

public class SliceConfiguration extends ConfigHolder {

	public SliceConfiguration(final ConfigHolder config) throws WrongConfigurationException {
		// TODO Auto-generated constructor stub
	}
	
	public SliceConfiguration(Slice slic){
		// TODO there should be a field in Subspace holding its SubspaceConfiguration
	}

}

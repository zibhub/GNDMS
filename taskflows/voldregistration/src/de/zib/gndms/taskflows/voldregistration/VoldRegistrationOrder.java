package de.zib.gndms.taskflows.voldregistration;

import de.zib.gndms.common.model.gorfx.types.AbstractOrder;

public class VoldRegistrationOrder extends AbstractOrder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7559198686091783247L;

	@Override
	public String getTaskFlowType() {
		return VoldRegistrationTaskFlowMeta.TASK_FLOW_TYPE_KEY;
	}

	@Override
	public boolean isJustEstimate() {
		return false;
	}

	@Override
	public String getDescription() {
        return "A dummy order for VolD registration";
	}

}

package de.zib.gndms.taskflows.voldregistration;

import de.zib.gndms.logic.model.gorfx.TaskFlowAction;

import javax.persistence.EntityManager;


public class VoldRegistrationTFAction extends TaskFlowAction<VoldRegistrationOrder>{

    public VoldRegistrationTFAction() {
        super( VoldRegistrationTaskFlowMeta.TASK_FLOW_TYPE_KEY );
    }

	@Override
	public Class<VoldRegistrationOrder> getOrderBeanClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setQuoteCalculator(
			VoldRegistrationQuoteCalculator quoteCalculator) {
		// TODO Auto-generated method stub
		
	}

}

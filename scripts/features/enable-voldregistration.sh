enable_voldregistration() {
moni call -v .gorfx.ConfigTaskFlowType "taskFlowType: 'VoldRegistrationTaskFlow'; \
	cfgOutFormat: 'PRINT_OK'; \
	type: $TYPE; \
	siteName: $SITE_NAME; \
	updateInterval: $UPDATE_INTERVAL; \
	estimationClass: 'de.zib.gndms.taskflows.voldregistration.VoldRegistrationQuoteCalculator'; \
	stagingClass: 'de.zib.gndms.taskflows.voldregistration.VoldRegistrationTFAction'"
}
# vim:tw=0

enable_dmsstaging() {
RW="700"

moni call -v .gorfx.ConfigTaskFlowType "taskFlowType: 'DmsStageIn'; \
	pollingInterval: $POLLING_INTERVAL; \
	updateInterval: $UPDATE_INTERVAL"
}
# vim:tw=0

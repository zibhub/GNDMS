enable_dmsstaging() {
RW="700"

moni call -v .gorfx.ConfigTaskFlowType "taskFlowType: 'DmsStageIn'; \
	pollingInterval: 60000; \
	updateInterval: 3000"
}
# vim:tw=0

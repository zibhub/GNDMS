enable_dmsstaging() {
RW="700"

moni call -v .gorfx.ConfigTaskFlowType "taskFlowType: 'DmsStageIn'; \
	updateInterval: 3000"
}
# vim:tw=0

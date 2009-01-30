enable_permissions() {
moni call -v .sys.SetupPermissionConfiglet "\
  mode: '$MODE'; \
  name: 'PermissionConfiglet'; \
  className: 'de.zib.gndms.logic.model.gorfx.permission.PermissionConfiglet'; \
  permissionProperties: \
PermissionConfig.userMode=SINGLE <EOL> \
# fallback permissions <EOL> \
PermissionConfig.defaultPermissions.user=maik <EOL> \
PermissionConfig.defaultPermissions.group=globus <EOL> \
PermissionConfig.defaultPermissions.mask=644 <EOL> \
# name of the single user in case mode is SINGLE <EOL> \
PermissionConfig.singleUserName=someone"
}

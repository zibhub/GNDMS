enable_permissions() {
moni call -v .sys.SetupPermissionConfiglet "\
  mode: '$MODE'; \
  name: 'PermissionConfiglet'; \
  className: 'de.zib.gndms.logic.model.gorfx.permission.PermissionConfiglet'; \
  permissionProperties: \
PermissionConfig.userMode=DEFAULT __EOL__ \
# fallback permissions __EOL__ \
PermissionConfig.defaultPermissions.user=globus __EOL__ \
PermissionConfig.defaultPermissions.group=globus __EOL__ \
PermissionConfig.defaultPermissions.mask=644 __EOL__
"
}
# possible addition for single user 'someone'
# name of the single user in case mode is SINGLE __EOL__ \
#PermissionConfig.singleUserName=someone __EOL__ \
#PermissionConfig.userPermissions=someone __EOL__ \
#PermissionConfig.userPermissions.someone.user=me __EOL__ \
#PermissionConfig.userPermissions.someone.group=mygrp __EOL__ \
#PermissionConfig.userPermissions.someone.mask=600

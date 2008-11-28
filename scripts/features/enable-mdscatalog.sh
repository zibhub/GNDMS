enable_mdscatalog() {
moni call -v .sys.SetupDefaultConfiglet "\
  mode: '$MODE'; \
  name: 'mds'; \
  className: 'de.zib.gndms.infra.configlet.C3MDSConfiglet'; \
  delay: '30000'; \
  initialDelay: '2000'; \
  mdsUrl: '$MDS_URLD'; \
  requiredPrefix: '$MDS_PREFIX'"
}

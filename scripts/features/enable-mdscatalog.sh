enable_mdscatalog() {
moni call -v .sys.SetupDefaultConfiglet "\
  mode: '$MODE'; \
  name: 'mds'; \
  className: 'de.zib.gndms.infra.configlet.C3MDSConfiglet'; \
  delay: '120000'; \
  initialDelay: '10000'; \
  mdsUrl: '$MDS_URL'; \
  requiredPrefix: '$MDS_PREFIX'"
}

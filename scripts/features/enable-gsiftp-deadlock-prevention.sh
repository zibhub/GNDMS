#!/bin/sh
enable_gsiftpDeadlockPrevetion() {
moni call -v .sys.SetupDefaultConfiglet "\
  mode: '$MODE'; \
  name: 'nbcf'; \
  className: 'de.zib.gndms.kit.network.NonblockingClientFactoryConfiglet'; \
  delay: '1000'; \
  timeout: '10';"
  }

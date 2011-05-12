#!/bin/sh
enable_gsiftpDeadlockPrevention() {
moni call -v .sys.SetupUpdatingConfiglet "\
  mode: '$MODE'; \
  name: 'nbcf'; \
  className: 'de.zib.gndms.kit.network.NonblockingClientFactoryConfiglet'; \
  delay: '2000'; \
  timeout: '600'; \
  buffersize: '2097152';"
  }

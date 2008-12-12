#!/bin/sh

if [ ! "$1" = "sure" ]; then
	echo "Dont call unless you know what you are doing!"
        exit 1
fi

java -cp ../extra/build-lib/cxf.jar:../extra/tools-lib/jaxb-xjc.impl:../extra/tools-lib/jaxb-api.jar:../extra/tools-lib/jaxb-xjc.jar com.sun.tools.xjc.XJCFacade -npa -p de.zib.gndms.c3resource.jaxb -d src -nv -classpath ../extra/build-lib/cxf.jar -Xts -Xts:style:multiline -verbose src/de/zib/gndms/c3resource/c3resource.xsd


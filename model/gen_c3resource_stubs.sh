#!/bin/sh

if [ ! "$1" = "sure" ]; then
	echo "Dont call unless you know what you are doing!"
        exit 1
fi

java -jar ../extra/tools-lib/jaxb-xjc.jar -npa -p de.zib.gndms.c3resource.jaxb -d src -nv src/de/zib/gndms/c3resource/c3resource.xsd


#!/bin/bash

RELEASE="0.3.4"

varcheck() {

    var="\$$1"
    var2=$( eval "echo $var" )
    if [ "x$var2" == "x" ]; then
        echo "please export $var"
        exit 1
    fi
}

varcheck "GLOBUS_LOCATION"
varcheck "GNDMS_SOURCE"

if [ "$#" == 1 ]; then
    props=$1
elif [ "x$GNDMS_SFR" != "x" ]; then
    props=$GNDMS_SFR
else
    echo "useage: $(basename $0 ) <staging.properties>"
    exit 2
fi

if [ ! -e "$props" ]; then
    echo "error properties-files \"$props\" doesn't exist"
    exit 3
fi


cp="" 
client_jar="$GNDMS_SOURCE/lib/gndmc/gndms-gndmc-$RELEASE.jar"
jars=( 
xmlsec.jar \
xml-apis.jar \
xercesImpl-2.7.1.jar \
xalan-2.6.jar \
wss4j.jar \
wsrf_tools.jar \
wsrf_core_stubs.jar \
wsrf_core.jar \
wsdl4j.jar \
serp-1.13.1.jar \
puretls.jar \
opensaml.jar \
openjpa-all-2.0.0.jar \
naming-common.jar \
log4j-1.2.15.jar \
joda-time-1.6.jar \
jgss.jar \
jetty-util-6.1.11.jar \
jetty-6.1.11.jar \
jce-jdk13-125.jar \
jaxrpc.jar \
httpcore-4.0.jar \
httpclient-4.0.1.jar \
guice-core-2.0-beta-4.jar \
groovy-1.7.1.jar \
gram-utils.jar \
gram-stubs.jar \
gram-service.jar \
google-collect-snapshot-20080530.jar \
gndms-stuff-$RELEASE.jar \
gndms-model-$RELEASE.jar \
gndms-logic-$RELEASE.jar \
gndms-kit-$RELEASE.jar \
gndms-infra-$RELEASE.jar \
gndms-gritserv-$RELEASE.jar \
gndms-gorfx-stubs.jar \
gndms-gorfx-common.jar \
gndms-gorfx-client.jar \
gndms-dspace-stubs.jar \
gndms-dspace-service.jar \
gndms-dspace-common.jar \
gndms-dspace-client.jar \
globus_wsrf_servicegroup_stubs.jar \
globus_wsrf_servicegroup.jar \
globus_wsrf_rft_stubs.jar \
globus_wsrf_mds_aggregator_stubs.jar \
globus_usage_packets_common.jar \
globus_usage_core.jar \
globus_delegation_stubs.jar \
globus_delegation_service.jar \
cxf-bundle-2.1.4.jar \
cryptix-asn1.jar \
cryptix32.jar \
concurrent.jar \
commons-logging.jar \
commons-lang-2.1.jar \
commons-fileupload-1.2.1.jar \
commons-discovery.jar \
commons-digester.jar \
commons-collections-3.2.jar \
commons-codec-1.4.jar \
commons-cli-2.0.jar \
commons-beanutils.jar \
commonj.jar \
cog-jglobus.jar \
cog-axis.jar \
caGrid-ServiceSecurityProvider-stubs-1.2.jar \
caGrid-ServiceSecurityProvider-common-1.2.jar \
caGrid-ServiceSecurityProvider-client-1.2.jar \
caGrid-metadata-security-1.2.jar \
caGrid-Introduce-serviceTools-1.2.jar \
caGrid-core-1.2.jar \
caGrid-advertisement-1.2.jar \
axis.jar \
asm-3.2.jar \
args4j-2.0.14.jar \
antlr-2.7.7.jar \
annotations-7.0.3.jar \
addressing-1.0.jar \
)

for i in ${jars[@]}; do
    cp=$GLOBUS_LOCATION/lib/$i:$cp
done

uri="https://$( hostname ):8443/wsrf/services/gndms/GORFX"
dn="$( grid-proxy-info -identity )"

java -cp "$client_jar:$cp" \
    -Daxis.ClientConfigFile=$GLOBUS_LOCATION/client-config.wsdd \
    de.zib.gndmc.GORFX.c3grid.ProviderStageInClient \
    -props "$props" \
    -uri "$uri" \
    -dn "$dn"
    

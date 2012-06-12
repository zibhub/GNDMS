#!/bin/bash

args=( "-uri" "http://localhost:8080/c3grid/" "-dn" "foo" )

cp="/home/mjorra/projects/C3Grid/impl/gndms-rest/lib/gndmc-rest/gndms-gndmc-rest-0.6.0-pre.jar:$cp"
cp="/home/mjorra/projects/C3Grid/impl/gndms-rest/lib/stuff/gndms-stuff-0.6.0-pre.jar:$cp"
cp="/home/mjorra/projects/C3Grid/impl/gndms-rest/lib/common/gndms-common-0.6.0-pre.jar:$cp"
cp="/home/mjorra/.m2/repository/args4j/args4j/2.0.14/args4j-2.0.14.jar:$cp"
cp="/home/mjorra/.m2/repository/org/slf4j/slf4j-log4j12/1.6.1/slf4j-log4j12-1.6.1.jar:$cp"
cp="/home/mjorra/.m2/repository/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar:$cp"
cp="/home/mjorra/.m2/repository/log4j/log4j/1.2.16/log4j-1.2.16.jar:$cp"
cp="/home/mjorra/.m2/repository/org/slf4j/slf4j-ext/1.6.1/slf4j-ext-1.6.1.jar:$cp"
cp="/home/mjorra/.m2/repository/ch/qos/cal10n/cal10n-api/0.7.4/cal10n-api-0.7.4.jar:$cp"
cp="/home/mjorra/.m2/repository/org/springframework/spring-aop/3.0.5.RELEASE/spring-aop-3.0.5.RELEASE.jar:$cp"
cp="/home/mjorra/.m2/repository/org/springframework/spring-asm/3.0.5.RELEASE/spring-asm-3.0.5.RELEASE.jar:$cp"
cp="/home/mjorra/.m2/repository/org/springframework/spring-aspects/3.0.5.RELEASE/spring-aspects-3.0.5.RELEASE.jar:$cp"
cp="/home/mjorra/.m2/repository/org/springframework/spring-beans/3.0.5.RELEASE/spring-beans-3.0.5.RELEASE.jar:$cp"
cp="/home/mjorra/.m2/repository/org/springframework/spring-context/3.0.5.RELEASE/spring-context-3.0.5.RELEASE.jar:$cp"
cp="/home/mjorra/.m2/repository/org/springframework/spring-core/3.0.5.RELEASE/spring-core-3.0.5.RELEASE.jar:$cp"
cp="/home/mjorra/.m2/repository/org/springframework/spring-expression/3.0.5.RELEASE/spring-expression-3.0.5.RELEASE.jar:$cp"
cp="/home/mjorra/.m2/repository/org/springframework/spring-instrument/3.0.5.RELEASE/spring-instrument-3.0.5.RELEASE.jar:$cp"
cp="/home/mjorra/.m2/repository/org/springframework/spring-jdbc/3.0.5.RELEASE/spring-jdbc-3.0.5.RELEASE.jar:$cp"
cp="/home/mjorra/.m2/repository/org/springframework/spring-orm/3.0.5.RELEASE/spring-orm-3.0.5.RELEASE.jar:$cp"
cp="/home/mjorra/.m2/repository/org/springframework/spring-oxm/3.0.5.RELEASE/spring-oxm-3.0.5.RELEASE.jar:$cp"
cp="/home/mjorra/.m2/repository/org/springframework/spring-tx/3.0.5.RELEASE/spring-tx-3.0.5.RELEASE.jar:$cp"
cp="/home/mjorra/.m2/repository/org/springframework/spring-web/3.0.5.RELEASE/spring-web-3.0.5.RELEASE.jar:$cp"
cp="/home/mjorra/.m2/repository/org/springframework/spring-webmvc/3.0.5.RELEASE/spring-webmvc-3.0.5.RELEASE.jar:$cp"
cp="/home/mjorra/.m2/repository/com/intellij/annotations/7.0.3/annotations-7.0.3.jar:$cp"
cp="/home/mjorra/.m2/repository/org/slf4j/jcl-over-slf4j/1.6.3/jcl-over-slf4j-1.6.3.jar:$cp"
cp="/home/mjorra/.m2/repository/com/thoughtworks/xstream/xstream/1.3.1/xstream-1.3.1.jar:$cp"
cp="/home/mjorra/.m2/repository/cglib/cglib-nodep/2.2/cglib-nodep-2.2.jar:$cp"
cp="/home/mjorra/.m2/repository/dom4j/dom4j/1.6.1/dom4j-1.6.1.jar:$cp"
cp="/home/mjorra/.m2/repository/org/codehaus/jettison/jettison/1.0.1/jettison-1.0.1.jar:$cp"
cp="/home/mjorra/.m2/repository/org/codehaus/woodstox/wstx-asl/3.2.7/wstx-asl-3.2.7.jar:$cp"
cp="/home/mjorra/.m2/repository/org/jdom/jdom/1.1/jdom-1.1.jar:$cp"
cp="/home/mjorra/.m2/repository/xom/xom/1.1/xom-1.1.jar:$cp"
cp="/home/mjorra/.m2/repository/xpp3/xpp3_min/1.1.4c/xpp3_min-1.1.4c.jar:$cp"
cp="/home/mjorra/.m2/repository/stax/stax/1.2.0/stax-1.2.0.jar:$cp"
cp="/home/mjorra/.m2/repository/joda-time/joda-time/1.6/joda-time-1.6.jar:$cp"
cp="/home/mjorra/projects/C3Grid/impl/gndms-rest/taskflows/dummy/lib/client/dummy-client-0.6.0-pre.jar:$cp"

props=( )

exec java -cp $cp ${props[@]} de.zib.gndms.taskflows.dummy.client.DummyTaskFlowExample ${args[@]}

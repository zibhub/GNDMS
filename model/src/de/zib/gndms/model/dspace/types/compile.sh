CPATH=/home/roberto/workspace/tmp/c3grid_ext/extra/tools-lib/

cp=$cp:$CPATH/groovy-all-1.6-beta-1.jar
cp=$cp:$CPATH/annotations.jar
cp=$cp:$CPATH/../../model/gndms-shared-model/production
cp=$cp:$CPATH/openjpa-1.1.0.jar
cp=$cp:$CPATH/google-collect-snapshot-20080820.jar
cp=$cp:$CPATH/joda-time-1.5.2.jar
cp=$cp:$CPATH/testng-5.8-jdk15.jar
#cp=$cp:$CPATH/args4j-2.0.9.jar
#cp=$cp:$CPATH/commons-collections-3.2.jar
#cp=$cp:$CPATH/commons-lang-2.1.jar
#cp=$cp:$CPATH/commons-pool-1.3.jar
#cp=$cp:$CPATH/CONTENTS.tools-lib
#cp=$cp:$CPATH/cos-05Nov2002.jar
#cp=$cp:$CPATH/derbyclient.jar
#cp=$cp:$CPATH/derby.jar
#cp=$cp:$CPATH/derbyLocale_de_DE.jar
#cp=$cp:$CPATH/derbynet.jar
#cp=$cp:$CPATH/derbyrun.jar
#cp=$cp:$CPATH/derbytools.jar
#cp=$cp:$CPATH/geronimo-jpa_3.0_spec-1.0.jar
#cp=$cp:$CPATH/geronimo-jta_1.1_spec-1.1.jar
#cp=$cp:$CPATH/jetty-6.1.11.jar
#cp=$cp:$CPATH/jetty-util-6.1.11.jar
#cp=$cp:$CPATH/serp-1.13.1.jar
#cp=$cp:$CPATH/servlet-api-2.5-6.1.11.jar

javac -cp $cp SliceRef.java

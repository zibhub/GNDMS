---
title: C3Grid GNDMS Quick Deployment Guide
layout: wikitext
---

=============================
C3GRID DEPLOYMENT QUICK GUIDE
=============================

Read the generic INSTALL instructions first.

Come back here afterwards.          

 
---------------------------------------------
DATAPROVIDER DEPLOYMENT AND SETUP QUICK GUIDE
---------------------------------------------
      
Before we start a remark on logging.  To get access to substantially more log 
messages, please add

log4j.category.de.zib=DEBUG

to $GLOBUS_LOCATION/container-log4j.properties

For even more log information, please change the line that starts with 
"log4j.appender.A1.layout.ConversionPattern=" to

log4j.appender.A1.layout.ConversionPattern=%d{ISO8601} %-5p %c{2} [%t,%M:%L] <%x> %m%n

(Techis: This enables NDC logging)


Setting up a dataprovider can be done in a few quick steps:

1) As the user who unpacked the GNDMS distribution, enter
   $GNDMS_SOURCE and execute

   ant -f build-deploy.xml prepare


2) Shutdown the globus container and execute as globus user

   ant -f build-deploy.xml dp-install
                                     

Now you should have a working installation of GNDMS.  Next, we'll configure it as a dataprovider site.
                                                                                                      

3) Run the container again with enabled GNDMS monitor. For this, 

   - Set GNDMS_MONITOR_ENABLED=true
   - Execute globus-start-container in the same shell


4) Edit the shell variables at the beginning of 
  
     scripts/c3grid/setup-dataprovider.sh
   
   according to your needs.   

   If you want to use java plugin-based staging instead of script-based staging:

   Additionally edit the line starting with "moni call -v .gorfx.ConfigOfferType".  There, enter the names of 
   your plugin classes ("estimationClass", "stagingClass") and manually add the jar containing these classes
   to $GLOBUS_LOCATION/lib before proceeding with the next step).
                               

5) Wait for globus to have finished startup (i.e. it has printed the list of running services on the console).

   As globus user, execute in $GNDMS_SOURCE

     ant -f build-deploy.xml dp-setupdb      

   This can be done only once and will produce a few pages of output that should not contain any
   java stracktraces, just consecutive monitor commands and the string "OK" as result for each command.

   ONLY IF THIS STEP FAILS, and you want to retry with a modified setup script, you have to
 
     Shutdown globus, run "ant -f build-deploy.xml dp-killdb",

   and try again.
   

6) Restart globus, possibly with disabled monitor


Now, you're done!  To test, load your grid-proxy and (assuming bash) execute in $GNDMS_SOURCE:

   ant -f build-deploy.xml dp-test \
      "-Dpsc.epr=https://$(hostname):8443/wsrf/services/gndms/GORFX" \
      "-Dpsc.sfr=test-data/sfr/dummy-sfr.properties" \
      "-Dpsc.dn=$(grid-proxy-info -identity)"

dp-test may take quite a while (< 2 min). Output should look like this:

    Buildfile: build-deploy.xml

    check-env:

    dp-test:
         [java] Waiting for staging to finish...
         [java] types.ProviderStageInResultT@838ffe5f
         [java] Address: https://130.73.72.122:8443/wsrf/services/gndms/Slice
         [java] Reference property[0]:
         [java] <ns3:SliceKey xmlns:ns3="http://dspace.gndms.zib.de/DSpace/Slice">c51ea260-a50d-11dd-adb5-ac1efd92dacf</ns3:SliceKey>
         [java] 
         [java] Collect your results at:
         [java] gsiftp://seeker.zib.de/tmp/RW/c51fb3d0-a50d-11dd-adb5-ac1efd92dacf

    BUILD SUCCESSFUL
	Total time: 7 seconds

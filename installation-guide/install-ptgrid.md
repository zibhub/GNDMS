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


Setting up a ptgrid resource can be done in a few quick steps:

1) As the user who unpacked the GNDMS distribution, enter
   $GNDMS_SOURCE and execute

   ant -f build-deploy.xml prepare


2) Shutdown the globus container and execute as globus user

   ant -f build-deploy.xml pt-install
                                     

Now you should have a working installation of GNDMS.  
Next, we'll configure it as a ptgrid resource site.
                                                                                                      

3) Run the container again with enabled GNDMS monitor. For this, 

   - Set GNDMS_MONITOR_ENABLED=true
   - Execute globus-start-container in the same shell


4) Edit the shell variables at the beginning of 
  
     scripts/ptgrid/setup-ptresource.sh
   
   according to your needs.   


5) Wait for globus to have finished startup (i.e. it has printed the list of running services on the console).

   As globus user, execute in $GNDMS_SOURCE

     ant -f build-deploy.xml pt-setupdb      

   This can be done only once and will produce a few pages of output that should not contain any
   java stracktraces, just consecutive monitor commands and the string "OK" as result for each command.

   ONLY IF THIS STEP FAILS, and you want to retry with a modified setup script, you have to
 
     Shutdown globus, run "ant -f build-deploy.xml pt-killdb",

   and try again.
   

6) Restart globus, possibly with disabled monitor

Now, you're done! 

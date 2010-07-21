---
title: GNDMS Installation
layout: wikistyle
---
 

GNDMS Installation Guide
========================

This is the installation guide for the [Generation N Data Management System](../index.html).


Prerequisites
-------------

In order to build or install GNDMS, the following prerquisites need to be fulfilled


#### Install a current version of the [Java](http://java.sun.com) 2 SE Development Toolkit Version 1.6 (JDK 1.6)
  
For compiling the services, please make sure that `$JAVA_HOME` points to this 
version and that this is also the version that is in your `$PATH`.  Naturally,
this should be the same version than the one you use(d) for building and 
running globus and ant.


#### Install [Apache Ant](http://ant.apache.org) 1.7 (1.8 might cause trouble on Linux)

Please set `$ANT_HOME`, add it to your environment, and add $ANT_HOME/bin to your `$PATH`


#### Install local UNIX software

In order to install GNDMS, please make sure you have installed the following software

* openssl
* curl
* rsync

Additionally, it is expected that your UNIX provides the following shell tools: hostname, which, bash


#### Install Globus Toolit 4.0.8

Please download and make a full installation of [Globus Toolkit 4.0.8](http://www.globus.org/toolkit/downloads/4.0.8/) 

* Setup working host and user certificates (You can build without)
* Set `$GLOBUS_LOCATION` and add it to your environment
* It is a good idea to `source $GLOBUS_LOCATION/etc/globus-user-env.sh` and 
  `source $GLOBUS_LOCATION/etc/globus-devel-env.sh` when working with Globus Toolkit 

         
#### Optionally configure Globus Toolkit 4.0.8 to generate more log messages
            
This step is optional but highly recommended.

To configure the Globus Container to generate substantially more log messages for easier debugging, please add

    log4j.category.de.zib=DEBUG

to `$GLOBUS_LOCATION/container-log4j.properties`

For even more log information, please change the line that starts with `log4j.appender.A1.layout.ConversionPattern=` to

    log4j.appender.A1.layout.ConversionPattern=%d{ISO8601} %-5p %c{2} [%t,%M:%L] <%x> %m%n

in the same file.

  
#### Set additional environment variables

Please set the following environment variables as specified below

* `$GNDMS_SOURCE` to the root directory of your GNDMS source distribution (The directory that contains `Buildfile`)
* `$GNDMS_SHARED` to `$GLOBUS_LOCATION/etc/gndms_shared`
* `GNDMS_MONI_CONFIG` to `GNDMS_SHARED/monitor.properties
* Finally, please add `$GNDMS_SOURCE/bin`to your `$PATH `

After this step, there should be no further need to adjust your environment.  Please consult `$GNDMS_SOURCE/example.profile` for an example of a properly configured environment.


#### Optionally Install Apache buildr 1.4.1 locally

This step is optional.

GNDMS is built and installed using [Apache buildr](http:///buildr.apache.org). A pre-packaged
version of buildr is included with GNDMS and can be executed by running `$GNDMS_SOURCE/bin/gndms-buildr`.
However, if you prever to install buildr locally, please

* Install Ruby 1.8
* Install buildr by executing `gem install buildr`




==================
BUILD INSTRUCTIONS
==================
               

!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!! If you dont want to build but instead just want to deploy, please skip to   !!
!! the section titled "DEPLOYMENT QUICK GUIDE" in INSTALL.<gridname>           !!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!                                                                               
                              
If you do a full build, do not load the globus environment during compile as 
this may cause weird groovy errors or set CLASSPATH='' before compiling.  
However it needs to be loaded for deployment.

Since their now is a global build script (build-compile.xml) all you have to do
is

ant -f build-compile.xml clean.all
ant -f build-compile.xml compile

in the toplevel directory of the source package ($GNDMS_SOURCE). 
"ant -f build-compile.xml" will print a list of additional targets.

However in case you need to build from hand, the next section describes the neccesary
steps.  It requires doing many things manually, meaning this guide is not for the 
faint of heart. Before you start please make sure you have succesfully installed 
all prerequisites as described in the previous section.

The following description was written for IntelliJ IDEA 8M1 but you should be
able to repeat it with any other java tool chain.

There are some problems with IDE's which don't support propert joint compilation of
Groovy and Java, e.g. Eclipse (at the time of writing).




------------------
SETUP DEPENDENCIES
------------------
                                                                                  
When describing the dependencies of a compilation unit, we'll refer to the following 
"project libraries"

- SOURCES: The sources of the respective compilation unit
- J2SE: The JDK
- "servicename"-stubs: service/"ServiceName"/build/stubs-"ServiceName"/classes
- openjpa: extra/tools-lib/geronimo* serp* openjpa*
- gt4-current: everything in $GLOBUS_LOCATION/lib *but* none of the GNDMS 
  jars and also none of the other dependecy jars!
- db-derby: extra/tools-lib/derby*.jar
- apache-commons: extra/tools-lib/commons*.jar
- jetty: extra/tools-lib/jetty*.jar
- testng: extra/tools-lib/testng*.jar
- servlet-api: extra/tools-lib/servlet-api*.jar
- collections: extras/tools-lib/google-collect*.jar
- httpcore: extras/tools-lib/http*.jar
- guice: extra/tools-lib/guice*.jar asm*.jar cglib*.jar
- GROOVY: extra/tools-lib/groovy*.jar
  If you use IntelliJ, use the global library GROOVY instead.
- annotations: extra/tools-lib/annotations.jar
  Nearly everything has a dependency to this one.
- joda-time: extra/tools-lib/joda-time*.jar
- jaxb: activation.jar jaxb*jar jsr173*jar
- args4j: extra/tools-lib/args4j*.jar
- CXF: extra/tools-lib/cxf.jar (for -Xts extension to xjc)
                     
     


---------------------------
DETAILED BUILD INSTRUCTIONS
---------------------------

If you want to do things step-by-step or understand how the project is actually build,
continue with the detailed build instructions below.


  0) Excute in services/

     for i in * ; do ( cd $i && ant compileStubs ) ; done
             
	 (If you ever want to delete all stubs, simply remove services/*/build/*)
	

  1) Execute sync.sh in the projects' top level directory. This will create all
	 necessary symlinks in the services' lib directories. Without doing this,
	 you're going to miss required jar files and the services will not compile 
	correctly.

	 (Developers: When adding/changing used libs, make sure to run sync.sh again; 
	  when deleting libs, make sure to kill the corresponding symlinks in 
	  services/*/lib)



  2) Compile gndms-stuff.jar

     Sources: stuff/src (resp. model/test-src)
     Output:  stuff/gndms-stuff/production (resp. test)
     Dependencies: GROOVY, J2EE, annotations, SOURCE, collections
     Re-exports: collections, guice



  3) Compile gndms-shared-model.jar

     Sources: model/src (resp. model/test-src)
     Output:  model/gndms-shared-model/production (resp. test)
     Dependencies: GROOVY, J2EE, annotations, SOURCE, stuff, openjpa,
                   collections, joda-time, testng, jaxb, CXF, apache-commons
     Re-exports: openjpa

     Make sure META-INF gets copied 
     from model/src to model/gndms-shared-model/production!
   

  4) Run the openjpa enhancer

     In model/, run: ant -f build-openjpa.xml enhance
     
     This enhances the jpa classes and creates extra/lib/gndms-model.jar

     (In IntelliJ: add build-openjpa.xml to the Ant-Build (right side) and
      configure the target enhance to be run post compilation.  You need to
      add gndms-groovy.jar to the classpath of the ant target for this to work)

     If you ever get weird errors during compilation or ant enhance, delete
     production/* and rebuild the whole project)


  5) Compile gndms-kit.jar
     Sources: kit/src
     Output: kit/gndms-kit/production (resp. test) classes
     Dependencies: GROOVY, J2EE, SOURCE, stuff, gt4, gndms-shared-model, annotations,
                   jetty, servlet-api, testng, joda-time, args4j, guice


  6) Compile gndms-logic.jar
     Sources: logic/src (resp. test)
     Output: logic/gndms-logic/production (resp. test) classes
     Dependencies: GROOVY, J2EE, annotations, SOURCE, stuff, gndms-shared-model,
                   testng, commons, derby, joda-time, gndms-kit, gt4

     (In IntelliJ: Use Build/Build Jars.. to build the jar on Make Project)


  7) Compile gndms-infra.jar
     Sources: infra/src (resp. test)
     Output: infra/gndms-infra/production (resp. test) classes
            extra/lib/gndms-infra.jar containing the contents of production/
     Dependencies: GROOVY, J2EE, annotations, gndms-logic, stuff, 
		   gndms-shared-model, commons, derby, servlet-api, jetty, 
		   gt4, testng, joda-time, gndms-kit, jaxb, httpcore

    (In IntelliJ: Use Build/Build Jars.. to build the jar on Make Project)


  8) Compile gndms-gritserv.jar
     Sources: gritserv/src (resp. test)
     Output: gritserv/gndms-gritserv/production (rest. test) classes
             extra/lib/gndms-gritserv.jar containing the contents of production/
     Dependencies: GROOVY, J2EE, gorfx-stubs, stuff,gndms-shared-model, annotations, 
		   joda-time, gt4, testng, 
		   and openjpa (due to javac compiler bug 6365854)

    (In IntelliJ: Use Build/Build Jars.. to build the jar on Make Project)



  9) Build all services in services/* using ant createDeploymentGar

     Recommended build order: DSpace, GORFX, LOFIS, WHORFX

  10) 
     If desired compile gndms-wscommon.jar
     Sources: ws-common/src (resp. test)
     Output: wscommon/gndms-wscommon/production (resp. test) classes
            extra/lib/gndms-wscommon.jar containing the contents of production/
     Dependencies: J2EE, annotations, gndms-logic, gndms-kit, gndms-infra, 
                   DSpace, GORFX, LOFIS, WHORFX, args4j, gt4, GORFX-stubs, 
                   GORFX-deps,
                   

  11) Though you might want to read the upcoming section on configuration
     and deployment first, you can now deploy the gar files by executing:

       "globus-deploy-gar <file>"

     as globus user.


  Additionally, for IDE based development, you may wish to set up projects per
  service with the following dependencies:

  J2EE, groovy, annotations, "servicename"-stubs, gndms-comserv, gndms-infra,
  gndms-kit, gndms-logic, gndms-shared-model, gndms-stuff, joda-time, SOURCES,
  all non-symlinks in services/ServiceName/lib,  gt4

  GORFX also depends on gndms-gritserv, DSpace, args4j, DSpace-Stubs and jaxb.
  
  compile to services/ServiceName/out/production (resp. test/), and exclude the
  services' out,  build, and lib directories from the project sources to reduce
  the size of the project index.

  You also should build a DSpace.jar form services/DSpace/src and put
  in into extra/lib. Create a GORFX.jar in the GORFX-service folder.

  This should be enought to compile and develop with your IDE; for deployment
  you still need to execute "ant createDeploymentGar" and "globus-deploy-gar"
  manually on the command line.

                  

THINGS TO DO WHEN YOUR BUILD FAILS
----------------------------------

1) Do you need to regen the stubs? Delete services/*/build and go from scratch

2) Stale symlinks

3) Reexecute sync.sh

4) Developers: If you cant deploy (i.e. globus-start-container balks with one 
   of those 40+-lines stacktraces) it's likely that you either miss some jars 
   or that introduce created an invalid jndi-config.xml which can happen but is
   easy enough to fix: Just make sure there are neither duplicate service nor
   resourceHome entries in any of the jndi-config.xml files.

5) Do not use the character ':' in your directory path (it will mess up your
   classpath if you use IntelliJ)

6) This has been written for UNIX systems; on windows your mileage may vary.
   Example: You will definitely have to manually edit all service.properties
   files to use windows-style file separators.

7) If you get an error about a missing "test/src" directory simply
   mkdir -p test/src in the respective services' directory

Other common reasons for a failed container starts are invalid credentials
(hostkey/hostcert.pem) or outdated CRLs. In the latter case, the script
contained in contrib/fetch-crl may help you. Execute
"fetch-crl -o <grid-cert-dir>" with apropriate permissions (Requries wget).
     

                                
                       
======================
DEPLOYMENT QUICK GUIDE
======================

Moved.
          
See INSTALL.<gridname> files.
 
           


=================
MANUAL DEPLOYMENT
=================
     
These are generic instructions for deployment of the GNDMS software. Unless you are
a developer, this section is not for you.  See INSTALL.<gridname> instead.

--------------------
CONTAINER DEPLOYMENT
--------------------

First, please make sure you have fullfilled all prerequisites as described in
the section above titled "PRERQUISITES".

In order to deploy, simply execute as the globus user

globus-deply-gar <garfile>

If you want to setup a data provider site, you need to deploy DSpace and
GORFX. If you want to setup a pure storage/publishing site, you need to deploy
DSpace only. If you want to setup your own GNDMS, you need to deploy WHORFX,
GORFX, and DSpace.

Next, please restart your container.

IMPORTANT: After your service container has been started once, please execute

"fixPermissions.sh <globus-group> <globus-user>".

This step is very important! If you skip it, you may suffer from potential
security breaches.

Next, we need to enable the monitoring/configuration shell.



----------------------------------
MONITORING AND CONFIGURATION SHELL
----------------------------------

If you set up your environment as described in "PREREQUISITES" you may now use
the GNDMS monitoring and configuration shell. This is a little servlet that
allows the execution of predefined actions or groovy script code inside the
running globus container in order to initialize and configure the database or
peek inside the running system for debugging purposes.

On most sites it is only accessed once to initialize the database during
installation.

GNDMS is disabled by default and protected by a randomly generated default
password. If enabled, it opens a socket on localhost for incoming connections.
Please consider that connections are *unencrypted* before configuring it to be
accessible from the network. Again, be aware that you can truely execute
arbitrary groovy code with globus user permissions through this channel and
therefore be cautious whenever using it. You should enable it only on demand and
always disable the service after use.

To enable, edit $GNDMS_MONI_CONFIG and set monitor.enabled to "true". Then
either restart the container or wait "monitor.configRefreshCycle" ms (defaults
to 30 seconds). After this period ,the container will load your new
configuration and start the monitoring servlet.     

Alternatively, you may set GNDMS_MONITOR_ENABLED to true before starting the
globus container to enable the monitor.

There are two ways to use the monitoring servlet, the first allows the
execution of predefined actions, while the second runs arbitrary groovy code.



EXECUTING ACTIONS
-----------------

To test the monitoring and configurations shell and retrieve a list of all
available actions, execute:

moni call help


To call an action, execute:

moni call <Name of action> <Action Parameters or help>


Now, please continue with the subsection titled "CONFIGURATION".



EXECUTING ARBITRARY GROOVY CODE
-------------------------------

This mode of executions is based on http sessions.

"moni init" creates a new session (Default session timeout is ca. 20 mins).
"moni open repl foo" to create a new monitor named "foo" in the current session
that accepts multiple commands ("repl" here is the run mode of the monitor.
See below for a list of possible run modes).

To use a previously opened monitor, open a second shell and execute:

"moni send foo $GNDMS_SOURCE/scrips/hello.groovy"

If you see "Hello, World!" followed by "null" in the first shell you have
succesfully enabled the monitoring console.

To close the connection named "foo", execute "moni close foo". To destroy your
session and close all named connections, execute "moni destroy". To force the
monitor to reread the confuration, execute "moni refresh". To force a restart
even if the configuration has not been altered, execute "moni restart".


             
WHEN THINGS DONT WORK
---------------------

1) If you dont get a connection, read your container log to make sure that the
   servlet has been started.

2) Make sure you have set up your environment as described in "PRERQUISITES".

3) If you execute moni and nothing happens you might have forgotten an
   argument. Currently, moni is just a bunch of helper bash scripts that call
   curl and lack proper argument checking. If you do not provide moni send with
   apropriate arguments, it may wait while attempting to read from stdin.
   

GNDMSH RUN MODES
----------------

SCRIPT (DEFAULT): Accept one send command, do not print result object.
REPL: Accept many send commands, always print result objects.
BATCH: Accept many send commands, but do not print result objects.
EVAL_SCRIPT: Accept one send command, print result object.

Specifying the <mode> in "moni open <mode> <connection-name>" is case-insensitive.

    
CONFIGURATION NOTES
-------------------

monitor.minConnections should always be >= 2


REMARKS FOR SCRIPT DEVELOPERS
-----------------------------

Inside your own groovy classes, you should always print to "out" or "err"
which contain the current monitor's output stream. Plain println only works
correctly in the script or its top-level functions.

"out" and "err" properties are added automatically to "Object.metaClass" when
instantiating a monitor. To enable them, "ExpandoMetaClass.enableGlobally()"
is called first.

Additional properties like resource homes and singleton resource instances are
made available using the same mechanism. A reference to an entity manager
guard is bound to "emg" (See EntityManagerGuard.java, basically this class
encapsulates JPA transaction processing inside GNDMS).

             
-------------
CONFIGURATION
-------------
                                 
Edit and run the apropriate configuration script from scripts/ via the enabled 
monitor.

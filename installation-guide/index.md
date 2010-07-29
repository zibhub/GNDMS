---
title: GNDMS Installation
layout: wikistyle
---
 



GNDMS Installation Guide
========================

This is the Installation Guide for the
[Generation N Data Management System](../index.html).

* Maruku will replace this with a fine Table of Contents
{:toc}

Prerequisites
-------------

In order to build or install GNDMS, the following prerequisites need to
be fulfilled



### Prepare your local software installation


#### Install the Java 2 SE Development Toolkit Version 1.6 
 
Please Install the [Java](http://java.sun.com) 2 SE Development
Toolkit Version 1.6.
 
For compiling the services, please make sure that `$JAVA_HOME` points
to this version and that this is also the version that is in your
`$PATH`.  Naturally, this should be the same version than the one you
use(d) for building and running globus and ant.



#### Install Apache Ant 1.7 

Please install [Apache Ant](http://ant.apache.org) 1.7 and set
`$ANT_HOME`, add it to your environment, and add `$ANT_HOME/bin` to your
`$PATH`

**NOTE** *Using 1.8 might cause trouble on Linux, YMMV*


#### Install local UNIX software

In order to install GNDMS, please make sure you have installed the
following software

* openssl
* curl
* rsync

Additionally, it is expected that your UNIX provides the following
shell tools: hostname, which, bash


#### Install Globus Toolit 4.0.8

Please download and make a full installation of
[Globus Toolkit 4.0.8](http://www.globus.org/toolkit/downloads/4.0.8/)

**NOTE** *To be precise, GNDMS doesn't use CAS and RLS, everything else
needs to be there.  However, due to the way the GT4 build system
works, we suggest you just install everything.*

**NOTE** *If you want to cut down the build, try `./configure
  --prefix=/opt/gt-4.0.8 --with-flavor=gcc32dbg --disable-rls
  --disable-tests --disable-wstests --disable-drs` (or
  `--flavor=gcc64dbg` on 64-Bit Linux or Mac OS X)*

* Setup working host and user certificates (You can build without)
* Set `$GLOBUS_LOCATION` and add it to your environment
* Life gets easier by putting `source
  $GLOBUS_LOCATION/etc/globus-user-env.sh` and `source
  $GLOBUS_LOCATION/etc/globus-devel-env.sh` in `$HOME/.profile` when
  working with Globus Toolkit

In the following it will be assumed that globus is run by the user
`globus` which you will have created during the installation of Globus
Toolkit.

         
#### Optionally Configure Globus Toolkit Logging
            
This step is optional but highly recommended.

To configure the Globus Container to generate substantially more log
messages for easier debugging, please add

    log4j.category.de.zib=DEBUG

to `$GLOBUS_LOCATION/container-log4j.properties`

For even more log information, please change the line that starts with
`log4j.appender.A1.layout.ConversionPattern=` to

    log4j.appender.A1.layout.ConversionPattern=%d{ISO8601} %-5p %c{2} [%t,%M:%L] <%x> %m%n

in the same file.

Please set the following environment variables as specified below

* `$GNDMS_SHARED` to `$GLOBUS_LOCATION/etc/gndms_shared`
* `GNDMS_MONI_CONFIG` to `$GNDMS_SHARED/monitor.properties`

Now it's time to start installing GNDMS.



### Preparation of GNDMS Software

The following steps need to be executed as the `globus` user that runs
the servlet container of your installation of Globus Toolkit.


#### Download and Unpack GNDMS

Download either an official GNDMS distribution package and unpack it
or get the current development version from github.
  
Please set `$GNDMS_SOURCE` to the root directory of your GNDMS
distribution (The directory that contains `Buildfile`) and
add `$GNDMS_SOURCE/bin` to your `$PATH`

After this step, there should be no further need to adjust your
environment.  Please consult `$GNDMS_SOURCE/example.profile` for an
example of a properly configured environment.


#### Optionally Install Apache buildr 1.4.1 locally

This step is optional.

GNDMS is built and installed using
[Apache buildr](http:///buildr.apache.org). A pre-packaged version of
buildr is included with GNDMS and can be executed by running
`$GNDMS_SOURCE/bin/gndms-buildr`.  However, if you prever to install
buildr locally, please

* Install Ruby 1.8
* Install buildr by executing `gem install buildr`

This guide assumes the usage of the pre-packaged version of buildr.



Installation and Deployment from Distribution Package 
-----------------------------------------------------
 
 This section describes the actual installation of the GNDMS software
 into the Globus Container. It requires that your system has been
 prepared as described in the previous section.
 
 
* Please enter `$GNDMS_SOURCE` and exeucte `gndms-buildr install`
    
  This will 
  
   * Download and install required software dependencies into
     `$GLOBUS_LOCATION/lib`. 
     
     **Please consult `$GNDMS_SOURCE/doc/licensing` for details on licensing conditions of
      3rd party software components used by the GNDMS package.**
   * Build API Documentation (Javadocs) in `$GNDMS_SOURCE/doc/api`
   * and finally install the globus packages (gar-files)
    
* Restart the globus with `globus-start-container-detached` and check
 `$GLOBUS_LOCATION/var/container.log` If everything goes right and you
 enabled additional logging as described in the previous section, the
 output should contain output like
   
      =================================================================================
      GNDMS RELEASE: Generation N Data Management System VERSION: 0.3-pre "Kylie++"
      GNDMS BUILD: built-at: Wed Jul 21 11:14:01 +0200 2010 built-by: mjorra@csr-pc35
      =================================================================================
      Container home directory is '/opt/gt-current'

 (In the case of an error, you may want to compare with a
 [full startup log](startup-log.txt)).
 
After succesful startup, please shutdown the container again by
calling `globus-stop-container-detached`. 

* Fix the permissions of database files by executing

    gndms-buildr fix-permissions
    
  **ATTENTION** *Skipping this step may cause leaing og sensitive
  information to local UNIX users*
    
At this point the GNDMS software has been succesfully installed.
Next, we'll describe how it may be configured for actual use.
 

 
Gridconfiguration of GNDMS Software
-----------------------------------
 
### Enabling the Monitoring Shell

GNDMS is configured via a builtin monitoring shell that accesses and
modifies the configuration in the database.

To enable it, after having startet the globus container with deployed
GNDMS once (as described in the previous section), please edit
`$GNDMS_MONI_CONFIG` such that `monitor.enabled` is set to `true` and
restart the globus container.  

The monitoring shell will be running now. You have nearly finished the
installation at this point. All that is left to do, is to actually
configure GNDMS for the chosen community grid platform.

**NOTE** *The shell is accessed via localhosts network interface and
protected with a clear-text password only. Do not make the monitoring
shell accessible via unsecure networks.*


### Configuring your Grid

Currently, there are specialized build targets for the setup of some
D-Grid projects directly in the `Buildfile`.

**C3-Grid Setup & Configuration** 
: Edit `$GNDMS_SOURCE/scripts/c3grid/setup-dataprovider.sh` and
execute `gndms-buildr c3grid-dp-setubdb`

**C3-Grid Quick Test** 
: `gndms-buildr c3grid-dp-test`

**PT-Grid Setup & Configuration** 
: Edit `$GNDMS_SOURCE/scripts/ptgrid/setup-resource.sh` and execute `gndms-buildr ptgrid-setubdb`

**PT-Grid Quick Test** 
: `gndms-buildr ptgrid-test`

Additionally, please consult the documentation for the respective 
community grid platform.


### Finalize installation

Please edit `$GLOBUS_LOCATION/etc/gndms_shared/monitor.properties` and
set `monitor.enabled = false`. This will disable the monitor shell
after `monitor.configRefreshCycle` ms (defaults to 30 seconds).
Alternatively, just restart the globus container.

**Congratulations** *At this point the installation is complete and you
have a running installation of GNDMS.*


### Remote Access to container.log

To enable a select group of users to read the container.log from
outside, add their DNs to either
`/etc/grid-security/gndms-support-stuff` or
`$GLOBUS_LOCATION/etc/gndms_shared/gndms-support-stuff`.  Depending on
your setup, you need to replace `gndms` with your subgrid name
(`ptgrid`, `c3grid`, etc.) in these file names.

To access the log, please load your user credentials (e.g. with
`grid-proxy-init`) and run in `$GNDMS_SOURCE`

    `env URI="<URI>" ARGS="<ARGS>" gndms-buildr show-log`
    
where `<URI>` is the EPR of either a DSpace or a GORFX service (see
container.log startup section, looks like
`https://$HOSTNAME:8443/wsrf/services/gndms/GORFX`) and `<ARGS>` are
the arguments that need to be passed to the actual show-log service
maintenance call.  Please use `env URI="<URI>" ARGS="help"` to obtain
a synopsis of possible parameters or leave it empty to retrieve
`$GLOBUS_LOCATION/var/container.log` completely.



Advanced Configuration
----------------------

### Resetting the Database

First, **shutdown the globus container**.  Next, in `$GNDMS_SOURCE`, issue

   gndms-buildr kill-db
    
This will delete your database.


### Inspecting the Database

First, **shutdown the globus container**.  Next, in `$GNDMS_SOURCE`, issue

   gndms-buildr inspect-db
   
Thisl will open a shell to the derby-ij tool for looking at the
internal database of GNDMS.


### Using the Monitor Shell

Please consult the [monitor shell guide](/moni-guide)



Building GNDMS from Source
--------------------------


#### Quick Rebuild

A quick full rebuild and reinstallation may be done by executing

    gndms-buildr rebuild
    
    
#### Regeneration of Javadocs

Manually delete `$GLOBUS_LOCATION/doc/api`. Now regenerate the
javadocs by executing

    gndms-buildr apidocs
    
    
#### Building Manually from Scratch

{% highlight shell %}
    gndms-buildr clean clean-services # Cleans everything
    gndms-buildr gndms:model:package  # Compile basic DAO classes
    gndms-buildr package-stubs        # Compile service stubs
    gndms-buildr gndms:infra:package  # Compile GNDMS framework
    globus-stop-container-detached    # Ensure globus is shutdown
    gndms-buildr install-deps         # Install dependencies
    gndms-buildr package-DSpace       # Compile DSpace service
    gndms-buildr deploy-DSpace        # Deploy DSpace
    gndms-buildr package-GORFX        # Compile GORFX service
    gndms-buildr deploy-GORFX         # Deploy GORFX
    globus-start-container-detached   # Restart globus
    gndms-buildr gndms:gndmc:package  # Build client
    gndms-buildr apidocs              # Build Javadocs (gndms is excluded)
{% endhighlight %}   
   
**NOTE** *In order to get speedier builds, developers may set
`$GNDMS_DEPS=link`. This will make `gndms-buildr install-deps` symlink
dependencies to `$GLOBUS_LOCATION/lib` instead of copying them and
therefore considerably eases trying out small changes to framework
classes.  However, when using this method, make sure that required
symlinked jar files from `$HOME/.m2/repository` and
`$GNDMS_SOURCE/lib`and `$GNDMS_SOURCE/extra` are not deleted
accidentally and remain readable for the globus user.*

**NOTE** *Once symlinks have been set up properly, developers may set
`$GNDMS_DEPS=skip` to skip install-deps alltogether.*

**NOTE** *To even setup symlinks for the service jars, use
  the `gndms-buildr link-services` target.*
   
    
    
#### Problem Shooting Tips for Development Builds

* Do you need to regen the stubs? `gndms-buildr clean-services
  package-stubs` to the rescue.

* Symlinks/copies of old jars in `$GLOBUS_LOCATION/lib`.  

      find $GLOBUS_LOCATION/lib -type l -name *.jar -exec rm -i {} \;
  
  may help
  
* If you cant deploy (i.e. globus-start-container balks with one of
   those 40+-lines stacktraces) it's possible that introduce created
   an invalid jndi-config.xml which can happen during development but
   is easy enough to fix: Just make sure there are neither duplicate
   service nor resourceHome entries in any of the jndi-config.xml
   files

* This build is not supposed to work on Microsoft Windows

* If you get an error about a missing "test/src" directory simply
  mkdir -p test/src in the respective services' directory

Other common reasons for a failed container starts are invalid credentials
(hostkey/hostcert.pem) or outdated CRLs. In the latter case, the script
contained in `$GNDMS_SOURCE/contrib/fetch-crl` may help you. Execute
`fetch-crl -o <grid-cert-dir>` with apropriate permissions (Requries `wget`).
     


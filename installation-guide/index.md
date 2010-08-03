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

**ATTENTION** *The default globus log file in `$GLOBUS_LOCATION/var/container.log` gets installed with very liberal file permisssions.  You might want to `chmod 0640 $GLOBUS_LOCATION/var/container.log` for security reasons.*


### Preparation of GNDMS Software

**ATTENTION** *The following steps need to be executed as the `globus` user that runs
the servlet container of your installation of Globus Toolkit.*


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
 prepared as described in the previous section. Again the following
 steps should be executed by the `globus` user.
 
 
* Please enter `$GNDMS_SOURCE` and exeucte `gndms-buildr install-distribution`

  This will 

   * Download and install required software dependencies into
     `$GLOBUS_LOCATION/lib`. 

     **Please consult `$GNDMS_SOURCE/doc/licensing` for details on licensing conditions of
       3rd party software components used by the GNDMS package.**

   * Build API Documentation (Javadocs) in `$GNDMS_SOURCE/doc/api`
   * and finally install the globus packages (gar-files)

* (Re)start the globus container with `globus-start-container-detached` and check
 `$GLOBUS_LOCATION/var/container.log` If everything goes right and you
 enabled additional logging as described in the previous section, the
 output should contain output like

      =================================================================================
      GNDMS RELEASE: Generation N Data Management System VERSION: 0.3 "Rob"
      GNDMS BUILD: built-at: Wed Jul 21 11:14:01 +0200 2010 built-by: mjorra@csr-pc35
      =================================================================================
      Container home directory is '/opt/gt-current'

 (In the case of an error, you may want to compare with a
 [full startup log](startup-log.txt)).
 
* *After having checked succesful startup by looking at the logs*, fix
   the permissions of database files by executing

    gndms-buildr fix-permissions

  **ATTENTION** *Skipping this step may cause leaking of sensitive
  information to local UNIX users*

At this point the GNDMS software has been succesfully installed.
Next, we'll describe how it may be configured for actual use.
 

 
Gridconfiguration of GNDMS Software
-----------------------------------
 
GNDMS is configured via a builtin monitoring shell that accesses and
modifies the configuration in the database.

If you did a fresh installation, the monitoring shell will have been
enabled temporarily at this point and you may just proceed. Otherwise
you need to enable in manually as described in the following section.


### Enabling the Monitoring Shell Manually

To enable the monitor shell manually, after having startet the globus
container with deployed GNDMS at least once (as described in the
previous section), please edit `$GNDMS_MONI_CONFIG` such that
`monitor.enabled` is set to `true` and either wait until GNDMS picks
up the new configuration or restart the globus container.

The monitoring shell will be running now. You have nearly finished the
installation at this point. All that is left to do, is to actually
configure GNDMS for the chosen community grid platform.

**NOTE** *The shell is accessed via localhosts network interface and
protected with a clear-text password only. Do not make the monitoring
shell accessible via unsecure networks.*

### Disabling the Monitoring Shell

To disable the monitor shell, please edit `$GNDMS_MONI_CONFIG` such
that *both* `monitor.enabled` and `monitor.noShutdownIfRunning` are
set to `false`.  Now, either wait until the new configuration gets
activated or just restart the globus container manually.


### Configuring your Grid

Currently, there are specialized build targets for the setup of some
D-Grid projects directly in the `Buildfile`.

{% if 1==0 %}
**C3-Grid Setup & Configuration** 
: Edit `$GNDMS_SOURCE/scripts/c3grid/setup-dataprovider.sh` and
execute `gndms-buildr c3grid-dp-setubdb`

**C3-Grid Quick Test** 
: `gndms-buildr c3grid-dp-test`
{% endif %}

**PT-Grid Setup & Configuration** 
: Edit `$GNDMS_SOURCE/scripts/ptgrid/setup-resource.sh` and execute `gndms-buildr ptgrid-setubdb`

**PT-Grid Quick Test** 
: Follow the setup instructions in the testing section <a href='#testing_your_installation'>below</a>
and execute `gndms-buildr ptgrid-test`

Additionally, please consult the documentation for the respective 
community grid platform.

**NOTE** *In case of failure during setup, please execute
  `gndms-buildr kill-db` and try again.*


### Finalize installation

Please edit `$GLOBUS_LOCATION/etc/gndms_shared/monitor.properties` and
set `monitor.enabled = false`. This will disable the monitor shell
after `monitor.configRefreshCycle` ms (defaults to 30 seconds).
Alternatively, just restart the globus container.

**Congratulations** *At this point the installation is complete and you
have a running installation of GNDMS.*


Testing your installation
-------------------------

The GNDMS contains a client application which tests some basic
functionality to ensure your setup is ready to use. In order to run
the test-client the following prerequisites must be satisfied:

- You must own a valid gird certificate,
- have access to a grid-ftp-server, which accepts your certificate
  and offers write permission,
- and of course a running globus container providing the
  GNDMS-services, with at least on subspace and file-transfer enabled.

### About

The test client simulates a standard GNDMS-use-case, it creates as
target slice, copies some files into the slice. Then it copies the
files back from the slice to some target directory and destroys the slice.

### Setup

For the scenario the following setup is required. On your grid-ftp
space create a directory and add some files, e.g. using the following
bash command-line:

{% highlight bash %}

    for i in $( seq 1 3 ); do \
        dd if=/dev/urandom bs=1024 count=1024 of=transfer_test$i.dat; done

{% endhighlight %}

Additionally create a destination directory on the grid-ftp space.

The client reads its properties from a file:
`$GNDMS_SOURCE/etc/sliceInOut.properties`. Now it's time to edit this
file. All properties whose values contain angle brackets require
attention. The file contains comments to every property and hopefully
explains itself. When you have finished the file must not contain any
angle-brackets, the client will complain if that's not the case.

### Running the test client

Once the setup is complete, load a grid-proxy using:

    grid-proxy-init

Now you can use buildr to fire up the client:

    gndms-buildr gndms:gndmc:run-test

(Or if provided: your grid specific test target)
It takes quite some time until the first output appears, be patient.
After a successful run your output start with:

    Connected to GNDMS: Generation N Data Management System VERSION: 0.3-pre "Kylie++"
    OK()
    Creating slice

(of course the version may differ) and end with:

    Okay, all done. Cleaning up!
        * Destroying Slice
        * Destroying Delegate
    Done.

Click [here](test-output.txt) to view the full output. If the test
runs successfully you should have identical files in your grid-ftp
source and destination directory, in that case CONGRATULATIONS!! you
have a working GNDMS installation, and can provide data management
service for your community.


### Trouble shooting

**The client hangs after the "`Copy gsiftp: ...`" message.**
: This can be a problem with your firewall configuration. It
happens when the control-channel can be established but the
data-channel is blocked. Please check your firewall setup especially
if the `GLOBUS_TCP_PORT_RANGE` environment variable is set correctly
and is forwarded by the firewall.

**I'm getting a "`GSSException: Defective credential detected`" exception.**
: This can have to reasons: your certificate-proxy maybe outdated or
doesn't exist or your CA directory isn't up to date. In the first case
just call `grid-proxy-init` again, in the second refer to the
`fetch-crl` section <a href="#fetch-crl">below</a>.


Advanced Configuration
----------------------

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


### Resetting the Database

First, **shutdown the globus container**.  Next, in `$GNDMS_SOURCE`, issue

   gndms-buildr kill-db

This will delete your database.


### Inspecting the Database

First, **shutdown the globus container**.  Next, in `$GNDMS_SOURCE`, issue

   gndms-buildr inspect-db

This will open a shell to the derby-ij tool for looking at the
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

{% highlight bash %}

    gndms-buildr clean clean-services # Cleans everything
    gndms-buildr artifcats            # Download all 3rd party components
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


#### Packaging GNDMS

In case you want do distribute your own spin-of GNDMS, we suggest you
follow the procedure described below when making a release:

    cd $GNDMS_SOURCE
    vi Buildfile # Set VERSION_NUMBER and optionally VERSION_NAME
    gndms-buildr release-build
    git commit -m "Made a Release"
    git tag gndms-release-ver
    git push --tags origin master
    find $GNDMS_SOURCE -type f -name '*.class' -exec rm '{}' \;
    cd ..
    mv $GNDMS_SOURCE $GNDMS_SOURCE/../gndms-release-ver

     tar zcvf GNDMS-Releases/gndms-release-ver.tgz --exclude .git \
        --exclude *.ipr --exclude *.iml --exclude *.iws \
        --exclude *.DS_Store --exclude *._.DS_Store gndms-release-ver

Now, please upload the tarball and let the world know about it.


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
<a name="fetch-crl" />


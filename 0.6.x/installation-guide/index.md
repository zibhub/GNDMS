---
title: GNDMS Installation
root: ../..
version: 0.6.x
layout: wikistyle
---
 

GNDMS {{ page.version }} Installation Guide
========================

This is the Installation Guide for the
[Generation N Data Management System]({{ page.root }}index.html).

* Maruku will replace this with a fine Table of Contents
{:toc}


Prerequisites
-------------

In order to build or install GNDMS, the following prerequisites need to
be fulfilled.



### Prepare your local software installation


#### Install the Java 2 SE Development Toolkit Version 1.6 
 
Please install the [Java](http://www.oracle.com/technetwork/java/index.html) SE Development
Toolkit Version 1.6.
 
For compiling the services, please make sure that `$JAVA_HOME` points
to this version and that this is also the version that is in your
`$PATH`. Naturally, this should be the same version than the one you
want to use for building and running globus.



#### Install Local UNIX Software

In order to install GNDMS, please make sure you have installed the
following software:

* openssl
* curl 
* Globus Toolkit (with GridFTP component)

Additionally, it is expected that your UNIX provides the following
shell tools: hostname, which, bash

GNDMS requires an Servlet 3.0 compliant application container. We have
tested GNDMS against the latest stable release of Jetty 6 to 8. You
can either grab Jetty from 

    http://download.eclipse.org/jetty/

or use the one which comes with your Linux distribution.


### Preparation of GNDMS Software

**ATTENTION** *The following steps need to be executed as the user that runs
the servlet container. We will assume that user to be named `gndms`.*


#### Download and Unpack GNDMS

Download either an official GNDMS distribution package and unpack it
or get the current development version from github.

Please set `$GNDMS_SOURCE` to the root directory of your GNDMS
distribution (i.e. the directory that contains `Buildfile`) and
add `$GNDMS_SOURCE/bin` to your `$PATH`.

Additionally, please set the following environment variables as specified below:

* `$GNDMS_SHARED` to a path where the databases can be stored (e.g. `/var/lib/gndms`)
* `$GNDMS_MONI_CONFIG` to `$GNDMS_SHARED/monitor.properties`

Please ensure, that the path `$GNDMS_SHARED` exists and that the user `gndms` has
permissions to read and write that directory.

* If you like the application container to run under another port than `8080` you can
  change that by setting `$GNDMS_PORT`.

Please consult `$GNDMS_SOURCE/etc/example.profile` for an
example of a properly configured environment.


#### Optionally Install Apache buildr 1.8 Locally

This step is optional.

GNDMS is built and installed using
[Apache buildr](http:///buildr.apache.org). A pre-packaged version of
buildr is included with GNDMS and can be executed by running
`$GNDMS_SOURCE/bin/gndms-buildr`.  However, if you prefer to install
buildr locally, please

* Install Ruby 1.8
* Install buildr by executing `gem install buildr`

This guide assumes the usage of the pre-packaged version of buildr.

*Currently buildr 1.9 is not supported*

Installation and Deployment from Distribution Package 
-----------------------------------------------------
 
 This section describes the actual installation of the GNDMS software
 into the Servlet Container. It requires that your system has been
 prepared as described in the previous section. Again, the following
 steps should be executed by the `gndms` user.



### Migrating from a Previous Installation

The REST version ot GNDMS (>= 0.6.0) has nearly nothing in common with
the last WS-based version (0.3.4).
Hence, it would be best to get rid of the old system completely.
The INSTALL file of the old system should describe how to do that.

### Installation and Initial Deployment
 
* Please enter `$GNDMS_SOURCE` and exeucte `gndms-buildr package`.

  This will compile the sources and build a .war file for the application container.

     **Please consult `$GNDMS_SOURCE/doc/licensing` for details on licensing conditions of
       3rd party software components used by the GNDMS package.**

* To install the package automatically, you need to set the `$JETTY_HOME` variable to
  the jetty directory. Then execute `gndms-buildr install` in `$GNDMS_SOURCE`.

  This will install
    
  * the compiled .war file to `$JETTY_HOME/webapps/gndms.war`,
  * a grid.properties to `$JETTY_HOME/gndms` and
  * the context gndms.xml to `$JETTY_HOME/contexts`.

  If you want to use another application server than Jetty, that's
  fine: every Servlet 2.5 compliant server should do the trick.
  However you need to do deployment of the application manually.
  
* Setup the service config.
  To do that, open 

      $JETTY_HOME/gndms/grid.properties 

  in an editor of your choice and check if the `baseURL` and
  `localBaseURL` entries are correct. Additionally, check if `gridPath` points to
  the desired folder.

* Hence GNDMS >= 0.6.2 are using SSL resp. Https for transport layer
  security, you need to activate the SSL channel of your application
  container, and provide it with decent credentials. (See [Keystore /
  Truststore Section](#keystore__truststore)

* Before (re-)starting the application container (e.g. Jetty), make sure that the `$GNDMS_SHARED`
  path is accessible by the user gndms running the application container.

  After restarting the application container, the following lines (or
  the like) should appear in the server log (see $GNDMS_SHARED/log/server.log)


      =========================================================================================                                          
      GNDMS RELEASE: Generation N Data Management System VERSION: 0.6.0 "ARTURAS" release-0.6.0                                          
      GNDMS BUILD: built-at: Mon Feb 06 20:25:46 +0100 2012 built-by: mjorra@csr-pc35                                                    
      =========================================================================================                                          
      Initializing for grid: 'C3Grid' (shared dir: '/var/lib/gndms')     


At this point the GNDMS software has been successfully installed.
Next, we will describe how it may be configured for actual use.
 

 
Grid-Configuration of GNDMS Software
-----------------------------------
 
### Configuring your Grid

The REST-based version of GNDMS uses a plug-in system to implement
certain data-management tasks. The basic installation doesn't include
any plug-ins.
Depending on the designated role of your GNDMS installation you need to
install and setup additional plug-ins.

Additionally, there are build targets to for the C3INAD project in
the `Buildfile`.


**C3-Grid Setup & Configuration for Dataprovider** 

* Enable provider-stage-in:
  Enter the directory
  
      $GNDMS_SOURCE/taskflows/staging
  
  and run
  
      gndms-buildr staging:server:package
  
  This will compile the stage-in client and server, than install it with
  
  
      gndms-buildr staging:server:deploy
  
  Now (re-)start or reload your Jetty container, this is required for
  the gndms plug-in loader to properly register the plug-ins.
  
  Finally, configure the provider staging plug-in:

    : Edit `$GNDMS_SOURCE/scripts/c3grid/setup-dataprovider.sh` and
    execute `gndms-buildr c3grid-dp-setupdb` in $GNDMS_SOURCE !

* Quick test for provider stage-in:

  We provide a script which executes a simple staging. It can be found
  under:
  
      $GNDMS_SOURCE/taskflows/staging/bin/run-staging-client.sh

  This script requires the following arguments to run:

  + The staging request as properties-file:

        -props < staging-property-file-name >

  + Login details for the MyProxyServer the same you used with
     myproxy-init. See below for additional
     information about the MyProxy-configuration

        -myProxyLogin  < login >          
        -myProxyPasswd < pass-phrase >       


**C3-Grid Setup & Configuration for ESGF-Stager** 

* Enable ESGF-stage-in:
  Enter the directory
  
      $GNDMS_SOURCE/taskflows/esgfStaging
  
  and run
  
      gndms-buildr esgf:server:package
  
  This will compile the stage-in client and server. Then install it with
  
  
      gndms-buildr esgf:server:deploy
  
  Now (re-)start or reload your application container, this is required for
  the gndms plug-in loader to properly register the plug-ins.
  
  Finally, configure the provider staging plug-in:

    : Edit `$GNDMS_SOURCE/scripts/c3grid/setup-esgfstaging.sh` and
    execute `gndms-buildr c3grid-esgf-setupdb` in $GNDMS_SOURCE !

* Quick test for ESGF-stage-in:

  We provide a script which executes a sample ESGF staging. It can be found
  under:
  
      $GNDMS_SOURCE/taskflows/esgfStaging/bin/run-esgf-client.sh

  This script requires the following arguments to run:

  + The staging request as properties-file:

        -props < staging-property-file-name >

  + The keystore with its password and the password for the private key stored in the keystore:

        -keystore < keystore >
        -keystorePasswd < keystore password >
        -privkeyPasswd < private key password >

  + The DN to get authorized with:

        -dn < distinguish name >

     Note that you have to use the dn of your certificate if it's not a host certificate.

  + Login details for the MyProxyServer the same you used with
     myproxy-init. See below for additional
     information about the MyProxy-configuration

        -myProxyLogin  < login >          
        -myProxyPasswd < pass-phrase >       


**C3-Grid Setup & Configuration for Portal** 

* Enable GridFTP-file-transfer

  Enter the directory
  
      $GNDMS_SOURCE/taskflows/fileTransfer
  
  and run
  
      gndms-buildr transfer:server:package
  
  This will compile the client- and serverside of the plug-in. It can be installed via
  
      gndms-buildr transfer:server:deploy
  
  Now (re-)start or reload your Jetty container. This is required for
  the gndms plug-in loader to properly register the plug-ins.
  
  Finally, configure the GridFTP-file-transfer plug-in:

    : Edit `$GNDMS_SOURCE/scripts/c3grid/setup-portal.sh` and
    execute `gndms-buildr c3grid-portal-setupdb` in $GNDMS_SOURCE !

* Quick test for the file transfer:

  We provide a script which executes a simple file transfer. It can be found
  under:
  
      $GNDMS_SOURCE/taskflows/fileTransfer/bin/run-transfer-client.sh

  The script requires the following arguments to run:

  + The transfer request as properties-file:

        -props < staging-property-file-name >

     An example for transfer properties can be found in:

      $GNDMS_SOURCE/taskflows/fileTransfer/etc/order.properties

  + Login details for the MyProxyServer, the same you used with
    myproxy-init. See below for additional information about the
    MyProxy-configuration

        -myProxyLogin  < login >          
        -myProxyPasswd < pass-phrase >       


Additionally, please consult the documentation for the respective 
community grid platform.

**NOTE** *In case of failure during setup, please execute
  `gndms-buildr kill-db` and try again.*



**MyProxy Setup**


File transfer and possibly provider stage-in require a user
certificate. The certificates are issued from a MyProxy-Server. The
server which should be used can be configured in the `grid.properties`
file. In the section: 

    # settings for the myproxy server
    myProxyServer=csr-pc28.zib.de
    myProxyConnectionCredentialFolder=/etc/grid-security
    myProxyConnectionCredentialPrefix=gndms

* `myProxyServer` holds FQDN to the MyProxy server. csr-pc28.zib.de
  can be used for testing purpose. However you need to check-in your
  D-Grid certificate-proxy using 

        myproxy-init -s csr-pc28.zib.de

  first.

* For the connection to the MyProxy-server host authentication is used.
  This requires a Host certificate from the D-Grid together with the
  root CA-certificates.

  + `myProxyConnectionCredentialFolder` is the folder containing
  the certificates folder with the root CA-certs in hashed form.
  Simply set it to /etc/grid-security is fine.

  + `myProxyConnectionCredentialPrefix` in case you want to use  
  containercert.pem and containerkey.pem this should be `container`.
  
  However these files must be readable for the user running the
  GNDMS-application. We recommend to make a copy of these files

      containercert.pem => gndmscert.pem 
      containerkey.pem  => gndmskey.pem 

  and leave `myProxyConnectionCredentialPrefix` alone. 
  **Note** these files must be present in the folder named by:
  `myProxyConnectionCredentialFolder`. 
  
### Keystore / Truststore ###

For the SSL connection to work most application-servers need a Java
Keystore containing the host-certificate and a truststore containing
all trusted CA certificates. Key- and truststore can be contained in
the same store.

When migrating from previous Grid installation, this root CA
certificates can usually by found in `/etc/grid-security/certificates`
In order to import this certificates into your keystore use the
following code shell commands:

      export gridCACertPath=/etc/grid-security/certificates
      for i in $gridCACertPath/*.0; do
         ali=$( basename $i .0 );
         echo importing $ali;
         openssl x509 -in $i | keytool -keystore <path-to-jks> -import -trustcacerts -alias $ali -storepass <masterpwd> -noprompt
      done

where `<masterpwd>` is the new master password for the keystore.

**NOTE** If `<path-to-jks>` points to a not existing keystore a new one will be created. 

Then the user's key (here in `gndmskey.pem`) has to be converted into pkcs12 standard

      openssl pkcs12 -inkey /etc/grid-security/gndmskey.pem -in /etc/grid-security/gndmscert.pem -export -out jetty.pkcs12
      
where a new user password `<userpwd>` is asked for. This key is then added to the keystore using

      keytool -importkeystore -srckeystore jetty.pkcs12 -srcstoretype PKCS12 -destkeystore <path-to-jks>

where first the master password for the keystore `<masterpwd>` and then the user password `<userpwd>` have to be entered.

In `grid.properties`, the following variables have to be defines:

      trustStoreLocation=<path-to-jks>
      trustStorePassword=<masterpwd>
      keyStoreLocation=<path-to-jks>
      keyStorePassword=<masterpwd>
      keyPassword=<userpwd>

**Setting up a SSLContext in Jetty**

Jetty the SSL configuration is defined in `$JETTY_HOME/etc/jetty-ssl.xml`

It must be configured as followed

     <New id="sslContextFactory" class="org.eclipse.jetty.http.ssl.SslContextFactory">
         <Set name="KeyStore"><Property name="jetty.home" default="." />/etc/keystore</Set>
         <Set name="KeyStorePassword">OBF:1uo71zly1y101ym51z0h1zsp1w261u9l1sov1u9x1w1c1zt11z0d1ym91y0q1zlk1unr</Set>
         <Set name="KeyManagerPassword">OBF:1v921x1b1v9k</Set>
         <Set name="TrustStore"><Property name="jetty.home" default="." />/etc/keystore</Set>
         <Set name="TrustStorePassword">OBF:1uo71zly1y101ym51z0h1zsp1w261u9l1sov1u9x1w1c1zt11z0d1ym91y0q1zlk1unr</Set>
         <Set name="WantClientAuth">true</Set> <!-- importend for cert based authentication -->
         <Set name="CertAlias">1</Set>
         <Set name="ValidatePeerCerts">true</Set>
         <Set name="CrlPath">/tmp/1149214e.r0</Set>
    </New>

**NOTE** *KeyStorePassword and TrustStorePassword are the obfuscated version of `<masterpwd>` and 
      KeyManagerPassword is the obfuscated form of `<userpwd>`.*

To generate an obfuscate passwords execute the following command in $JETTY_HOME

    java -cp lib/jetty-http-8.0.4.v20111024.jar:lib/jetty-util-8.0.4.v20111024.jar org.eclipse.jetty.http.security.Password  <passwd>

The *KeyStore* and *TrustStore* entrys should contain `<path-to-jks>` ($JETTY_HOME/etc/keystore in this example).


Then start jetty with:

    java -jar start.jar etc/jetty-ssl.xml

**Setting up a SSLContext in Tomcat**

Please consult the fine documentation provided [here](http://static.springsource.org/spring-security/site/docs/3.0.x/reference/x509.html#x509-ssl-config)

### Finalize Installation 

Nothing todo here ;-)

**Congratulations** *At this point the installation is complete and you
have a running installation of GNDMS.*



### Trouble Shooting

**Server responds with BAD REQUEST 400, when submit a TaskFlow**
: It might by that the plug-ins for the TaskFlow aren't installed
correctly. Check if the plug-in path in your
`$JETTY_HOME/gndms/grid.properties` is set correctly and that the
folder contains the required jar files.


**I'm using Ubuntu and GNDMS won't start and complains about invalid ELF format**
: Yep, thats what you get for using Ubuntu... Problem is that we use
JNA for low level file-system access like chmod and Ubuntu doesn't
provide a valid libc in the usual path, just a stupid script.
To get it working you need to tell JNA where the real libc is located
usually u do this by suppling 

        -Djna.library.path=< path to your libc > 

: when starting the application server 

: For jetty something like:
 
        java -Djna.library.path=/lib/i386-linux-gnu -jar start.jar

: should be fine.

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

**I'm getting an java.lang.IllegalStateException from the first transfer**
: If the exception message contains the custom message:
<pre><code>
...
Custom message: Server refused changing directory (error code 1) [Nested exception message:  
    Custom message: Unexpected reply: 550-/some/dir/2257abd0-433a-11e0-bb7b-9a0f3bfb91a0: 
        Could not change directory. : System error in stat: Permission denied
...
</code></pre>
Please make sure that `/some/dir` is owned by the gndms user and has
permissions 1777. Also make sure that your GRAM setup (especially the
sudoers entries) is correct.


**The file transfer throws an execption:**
: If the exeption looks something like
<pre><code>
    java.lang.IllegalStateException: File transfer from 
        gsiftp://some.foo.org:2811/tmp/srcDir to 
        gsiftp://more.bar.org/tmp/gndms/RW/f521ba10-a06a-11df-b70c-f2b2b7430fda failure 
        Server refused performing the request. ...`
</code></pre>
: Or the client prints out infinite `Waiting for transfer to finish...`
messages and the destination directory contains a single empty file,
please ensure the both grid-ftp servers are running, accepting your
credential and can talk to each other. Best way to verify this is to
search the test-clients output for a line like:
<pre><code>
    Copy gsiftp://some.foo.org:2811/tmp/srcDir ->
        gsiftp://more.bar.org/tmp/gndms/RW/f521ba10-a06a-11df-b70c-f2b2b7430fda
</code></pre>
: and try `globus-url-copy` with:
<pre><code>
    globus-url-copy gsiftp://some.for.org:2811/tmp/srcDir/someFile \
        gsiftp://more.bar.org/tmp/gndms/RW/f521ba10-a06a-11df-b70c-f2b2b7430fda/targetFile
</code></pre>
: If you get an error message like "No route to host" or the like
ensure that the grid-ftp servers of both hosts are listening on the
right network device and that now firewall is blocking the connection.
If this hangs infinitely something with the data-channel setup is
wrong. Consult the grid-ftp documentation about the --data-channel
argument.


Advanced Configuration
----------------------

### Resetting the Database

First, **shutdown the application container**.  Next, in `$GNDMS_SOURCE`, issue

   gndms-buildr kill-db

This will delete your database.





Building GNDMS from Source
--------------------------

See above.

#### Regeneration of Javadocs

Manually delete `$GLOBUS_LOCATION/doc/api`. Now regenerate the
javadocs by executing

    gndms-buildr apidocs




#### Packaging GNDMS

In case you want do distribute your own spin-of GNDMS, we suggest you
follow the procedure described below when making a release:

    cd $GNDMS_SOURCE
    vi Buildfile # Set VERSION_NUMBER and optionally VERSION_NAME *)
    gndms-buildr release-build
    git commit -m "Made a Release"
    git tag gndms-release-ver
    git push --tags origin master
    find $GNDMS_SOURCE -type f -name '\*.class' -exec rm '{}' \;
    find $GNDMS_SOURCE -type d -name classes | xargs rm -fR
    rm .gitignore
    # Additionally delete 
    #   \*/target 
    #   name/gndms-name
    # 
    buildr apidocs
    cd ..
    mv $GNDMS_SOURCE $GNDMS_SOURCE/../gndms-release-ver
    # mac only: export COPYFILE_DISABLE=true
    tar zcvf GNDMS-Releases/gndms-release-ver.tgz --exclude .git \
        --exclude \*.ipr --exclude \*.iml --exclude \*.iws \
        --exclude \*.DS_Store --exclude \*._.DS_Store gndms-release-ver
    mv $GNDMS_SOURCE/../gndms-release-ver $GNDMS_SOURCE  # done*


Now, please upload the tarball and let the world know about it.

\*) *Please note: Every time you change the VERSION_NUMBER you have to
call `install-deps` or building the services will not succeed.*

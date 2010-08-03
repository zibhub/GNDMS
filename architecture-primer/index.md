---
title: GNDMS Architecture Primer
root: ..
layout: wikistyle
---

GNDMS Architecture Primer
=========================

This is the GNDMS Architecture Primer.  It is developer-level
documentation that gives a general overview of the different layers
of GNDMS, how they inter-operate with Globus Toolkit, as well as
important concepts, components and classes.  It is incomplete at
this point.

* Maruku will replace this with a fine Table of Contents
{:toc}


The Layers of the Software Stack
--------------------------------

The running GNDMS software stack roughly looks like this:

    < Service Clients ------------------------------------------------------ >
    [ services: DSpace, GORFX ---------------------------------------------- ]    
    [ gritserv-------------------------------------------------------------- ]
    |                 [ infra ---------------------------------------------- ]
    |                 [ logic ---------------------------------------------- ]
    |                 |                                    < Configuration > |
    |                 |                                    < Monitor Shell > |
    |                 [ kit ------------------------------------------------ ]
    |                 |                                                      |    
    |                 |               [ model --------------------]          |
    |                 |               |                           |          |    
    |                 |               [ stuff -------- ]          |          |      
    |                 |               |                |          |          |
    [ Service Stubs ]-[ GT4 Container ]                [ Open-JPA ]-[ Groovy ]
    |                                                  [ Derby    ]-[ Jetty  ]
    |                                                                        |    
    [ Additional External Libraries ---------------------------------------- ]

Legend:

    [ Module ]        # Software Module
                      # -- modules higher in the Stack
                      # -- depend on modules below them
		      
    < Tool   >        # Software Tool
                      # -- what is below is needed to run


For the gndmc client, an additional layer is required:

    < gndmc client --------------------------------------------------------- >
    [ gndmc ---------------------------------------------------------------- ]
    [ services: DSpace, GORFX ---------------------------------------------- ]    
    ..........................................................................


The software stack for building, installing, deployment and
configuration of GNDMS roughly looks like this:

    < buildr --------------------------------------------------------------- >
    [ Build ---------------------------------------------------------------- ]
    [ Buildr 1.4 ----------------------------------------------------------- ] 
    |             [ introduce ------------ ]                                 |
    [ JRuby 1.5 ] [ Ant ]                    < Configuration --------------- >    
    [ Java 2 SDK 1.6 --------------------- ] < Monitor Shell --------------- >
    < local UNIX tools ----------------------------------------------------- > 


Components
----------


### Component Categories

There are several kinds of components that make up the GNDMS service
stack. They can be grouped into various categories


**Main**
: GNDMS source code to be found below a top-level diretory and directly
built to a jar (Java)

**Service**
: Located below `services` and compiled with introduce (XSD, WSDL, Java)

**Client**
: Located below `services` and compiled with introduce (Part of service
build process, consists of XSD, WSDL, and Java)

**External**
: downloaded from the Internet during installation.

**Globus**
: part of your local installation of Globus Toolkit 4.

**Build**
: All code needed to build and install the software (Ruby and
Shell). Of primary relevance are the `Buildfile` in `$GNDMS_SOURCE` and
everything in `$GNDMS_SOURCE/buildr`.

**Config**
: All code needed to configure GNDMS from the outside (Shell and
possibly Groovy)


### Components

**Service Clients** *(Client)*
: Any program that accesses GNDMS via WSRF.

**GORFX** *(Service)*
: The GORFX service provides the negotiation and execution of data
management activites (like Staging and File Transfer) according
to a co-scheduling protocol (located below `$GNDMS_SOURCE/services/GORFX`). 

**DSpace** *(Service)*
: The DSpace service provides management of storage resources (located below `$GNDMS_SOURCE/services/DSpace`).

**gritserv** *(Main)*
: Service-level code that is shared between different grid services,
like e.g. XSD type conversion (located below `$GNDMS_SOURCE/gritserv`).

**infra** *(Main)*
: Main infrastructure code that ties the service code to GNDMS *main*
classes. Configuration, Database setup, Dependency injection (located below `$GNDMS_SOURCE/infra`).

**logic** *(Main)*
: All 'Business' logic of GNDMS that can be implemented outside of
the actual service classes (located below `$GNDMS_SOURCE/logic`).

**Configuration** *(Config)*
: Configuration scripts below `$GNDMS_SOURCE` scripts are executing
during installation in order to configure a GNDMS instance for
its purpose in a given community grid (located below `$GNDMS_SOURCE/scrips`).

**Monitor Shell and Utilities** *(Config)*
: The monitor shell (client) allows access to the monitor shell
implemented **kit** (located below `$GNDMS_SOURCE/bin`).

**kit** *(Main)*
: Utility classes that depend on some functionality from Globus
Toolkit and/or **model**. Kit contains the implementation of the GNDMS
monitor shell and GridFTP auxiliaries (located below `$GNDMS_SOURCE/kit`).

**model** *(Main)*
: Database model classes (located below `$GNDMS_SOURCE/model`)

**stuff** *(Main)*
: Various utility classes (located below `$GNDMS_SOURCE/stuff`)

**Service Stubs** *(Service)*
: Stub code needed to communicate with the Grid WSRF Services of GNDMS
(located inside services).

**GT4 Container** *(Globus)*
: The Globus Toolkit 4 WSRF Service Container.

**Open JPA** *(External)*
: GNDMS uses Apache OpenJPA 2.0 as its Object Relational Mapper (ORM).

**Derby** *(External)*
: GNDMS uses Apache Derby 1.5 as its underlying embedded database.

**Groovy** *(External)*
: GNDMS provides support to access the system at runtime by means of
executing groovy script code via the monitor shell.

**Jetty** *(External)*
: The monitor shell is implemented atop a stripped-down version of jetty.

**Additional External Libraries** *(External)*
: GNDMS uses a large selection of 3rd party libraries.  Please either
consult the `Buildfile` or `$GNDMS_SOURCE/lib/gndms-depencies[.xml]`
(post-install) to find out more details.  Consult
`$GNDMS_SOURCE/doc/licensing` for licensing conditions of 3rd party
components.

**Build** *(Build)*
: Build scripts are written in ruby and placed in
`$GNDMS_SOURCE/Buildfile` and `$GNDMS_SOURCE/buildr/*`.

**Buildr 1.4** *(Build)*
: GNDMS relies on Apache Buildr for build, installation, and
deployment.

**introduce** *(Build)*
: The Introduce Tool from the CAGrid project was used to generate
service skeletons below `$GNDMS_SOURCE/services`.

**JRuby 1.5** *(Build)*
: **Buildr** needs this.

**Java 2 SDK 1.6** *(All)*
: GNDMS has been written in Java.

**Documentation**
: Documentation is generated using Javadoc and Jekyll (has been
installed in the included JRuby distribution)

GNDMS distribution packages contain a version of JRuby with
preinstalled buildr and Jekyll.  This is not a part of GNDMS (You
could always fallback to your local installation of these tools) and
provided for convenience only.


Suggested Code Walkthrough
--------------------------

* Read the available documentation before entering the code, it will
  give you a rough idea of how everything is connected
* Get to know the model classes
* Read the action framework (Everything that inherits from
  de.zib.gndms.logic.action.Action)
* Checkout `infra/src/de/zib/gndms/infra/system/EMTools.java` to
  understand how actions and the database are connected
* Checkout `Ext*ResourceHome` in **DSpace** to see how resources
are persisted.
* Read the `*ServiceImpl` classes to see the actual workflow that is
triggered by incoming requests.  Follow down to code in **logic** as
you see fit.
* Read `infra/src/de/zib/gndms/infra/system/GNDMSystem.java` and `infra/src/de/zib/gndms/infra/system/GNDMSystemDirectory.java`
to understand how GNDMS is bootstrapped and wired
* The monitor is in kit in case you need to touch it


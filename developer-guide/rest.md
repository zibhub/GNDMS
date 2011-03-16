---
title: GNDMS REST preview
root: ../..
layout: wikistyle
---

The GORFX-Rest Service
======================

*Note this section is only for developers which are interested in
the the progress of the REST port of GNDMS.*

The branch `rest-mock` contains ad-hoc implementation of the GORFX rest
specification.  It can be used by developers of GNDMS client services
to test the integration and the basic setup.

The interface is stable with the exception that new methods might be
added and some types may be refined. 

The branch has three imported folders:

* *gndmc-rest*     Contains the code of client side.
* *gorfx*          Holds the restified GORFX service code.
* *gndms-commons*  Contains code shared by the client and service, e.g.
               types used for exchange and the interfaces describing
               the service.

Some terms
----------

**Taskflow** Taskflows are complex tasks executed by the GORFX-Service,
    the are usually started, by sending an order to the GORFX-Service,
    the service responds with as Taskflow specifier, this specifier,
    can by used by the client, negotiate the execution timing (quote)
    of the Taskflows task (CoScheduling), and to trigger the task
    execution.

**Order** Request for a Taskflow, contains necessary information for the
    Taskflow, like the source and destination of a file transfer.

**Quote** Constrains for Taskflow execution, like memory usage or
    processing time this usually computed by an quote-calculator
    supplied with a specific Taskflow on the service side.

**Task**  Is the execution unit of a Taskflow. A task can be queried
    about the state of the execution, and provides results and errors.
    A task mustn't necessarily belong to a Taskflow. It can be
    triggered by an configuration change, or an batch action.

**batchAction**  A one-shot task execution, e.g. some maintains stuff.

The modules:
------------
The following section contains information about the modules in the
rest-mock branch.

### The GORFX service:

The service is divided in three sub services:

* *GORFX* -- entry point for the communication with the service.
             Most imported task: it creates taskFlows.
* *TaskFlowSerivce* -- Handles all requests concerning a concrete
             task.
* *TaskServce* -- Handles the life-cycle of a task, i.e. provides
             status information as well as result and error details.

Each of this service implements an interface which is documented
and can be found in: 

    de.zib.gndms.GORFX.service

### The clients:

For each of the above services there exists an extra client, which
implements the service interface and uses Springs REST-template to
exchange HTTP-Requests with the service.

Additionally to the client which are dedicated to their services,
there exist an client which mimics the basic Taskflow execution.
This client is located in:

    de.zib.gndms.gndmc.gorfx.AbstractTaskFlowExecClient

An implementation of `AbstractTaskFlowExecClient` is also provided:

    ExampleTaskFlowExecClient

This example implementation executes a dummy Taskflow an writes
lots of information to stdout. It is called by
GORFXTaskFlowExample which executes a task which should run
successfully and one which fails (intentionally).

#### The Dummy Taskflow:
The dummy Taskflow does nothing spectacular, it writes a given
message a number of times with a given delay between single
writes. An if requested can throw an exception.

The result of the task is the complete written text.


Setup and running:
------------------
Apart from Java 1.6 a jetty installation is required. I used
jetty6 for testing.

Export the environment variable `$JETTY_HOME` pointing to the root
directory of your jetty installation.

Export `$GNDMS_SOURCE` pointing to the source.

Add `$GNDMS_SOURCE/bin` to your `PATH` variable.

Call `gndms-buildr deploy-rest`

this should compile everything an copy the gndms.war to your `$JETTY_HOME/webapps`.

Set the environment variable `GORFX_URL` to

    http://<yourhost>:<and-port>/gndms/<grid-name>

Echo this to your grofx.properties

    echo gorfxBaseURL=$GORFX_URL > $JETTY_HOME/resources/gorfx.properties

Start Jetty

#### Running the client

**NOTE** this requires the `GORFX_URL` environment variable.
Additionally an env-var `GNDMS_DN` must be set, with your grid dn.

To compile and run the client simply enter:

    gndms-buildr gndms:gndmc-rest:run

Reusing client code
-------------------

A client application will need the gndmc-rest.jar and the gndms-commons.jar.
Plus the rest part of of the Spring-framework and JodaTime.
The best place to look for the complete dependency listing it the
`Build`-file that ships with the release. 

### Imported types
The following lists some types defined in gndms-commons which are
might be important to client-service developers. Additional
documentation can be found [here][]

#### ResponseEntity
Each client call returns an instance of Springs ResponseEntity&lt;T&gt; this
entity has a HTTP Status which should be checked before the response
body is used. 

#### Specifier
Often the response entity contains a Specifier&lt;T&gt;. This
specifier describes the target resource, it holds the URI of the
resource, together with a map of the path variables (see.
[REST-Spec][]). Additionally the specifier can carry a payload, which
is either the desired value in case of a GET request, or the result of
a GET request on a newly created (with POST or PUT) resource.

#### AbstractTF
Base class for all Taskflow orders. All orders for concrete Taskflows
will inherit from this class.

#### TaskStatus
This is the interface of task status objects. It has two imported
information: it shows if the task is running, finished or failed, and
contains progress information which should be interesting for the user
which stated the task(flow).

#### GNDMSResponseHeader 
This is mainly a wrapper around Springs HttpHeaders which offers
methods to set GNDMS specific header files, like the DN or workflow
Id.

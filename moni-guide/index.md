---
title: GNDMDS Monitoring and Configuration Shell Guide
root: ..
layout: wikistyle
---

GNDMS Monitor Shell Guide
=========================

This is the Monitor Shell Guide for the
[Generation N Data Management System](../index.html).

* Maruku will replace this with a fine Table of Contents
{:toc}

If you have set up your environment as described in the
[prerequisites section](/installation-guide/#prerequisites) of the
GNDMS Installation Guide you may use the GNDMS monitoring and
configuration shell to access a running instance of the GNDMS
software. This is a little servlet that allows the execution of
predefined actions or [Groovy 1.6](http://groovy.codehaus.org) script
code inside the running globus container in order to initialize and
configure the database or peek inside the running system for debugging
purposes.

On most sites, the GNDMS Monitor Shell is only accessed once to
initialize the database during installation.

The GNDMS Monitor Shell is disabled by default and protected by a
randomly generated default password. If enabled, it opens a socket on
localhost for incoming connections. Please consider that connections
are *unencrypted* before configuring it to be accessible from an
external network. Again, be aware that you can truely execute
arbitrary groovy code with globus user permissions through this
channel and therefore be cautious whenever using it. You should enable
it only on demand and always disable the service after use.

To enable, edit `$GNDMS_MONI_CONFIG` and set `monitor.enabled` to `true`. Then
either restart the container or wait `monitor.configRefreshCycl` ms (defaults
to 17 seconds). After this period, the container will load your new
configuration and start the monitor shell automatically.

Alternatively, you may set `$GNDMS_MONITOR_ENABLED` to `true` before starting the
globus container to enable the monitor.

There are two ways to use the monitor shell, the first allows the
execution of predefined actions, while the second runs arbitrary Groovy code.



Executing Actions
-----------------

To test the monitoring and configurations shell and retrieve a list of all
available actions, execute:

    moni call help

To call an action, execute:

    moni call <Name of action> <Action Parameters or help>


Executing Groovy Code
---------------------

This mode of executions is based on http sessions.

`moni init` creates a new session (Default session timeout is 22 mins).
`moni open repl foo` to create a new monitor named "foo" in the current session
that accepts multiple commands (`repl` is the *run mode* of the monitor.
See below for a list of possible run modes).

To use a previously opened monitor, open a second shell and execute:

    moni send foo $GNDMS_SOURCE/scrips/hello.groovy

If you see `Hello, World!` followed by `null` in the first shell you have
succesfully enabled the monitor shell.

To close the connection named `foo`, execute `moni close foo`. To destroy your
session and close all named connections, execute `moni destroy`. To force the
monitor to reread the confuration, execute `moni refresh`. To force a restart
even if the configuration has not been altered, execute `moni restart`.



### List of Supported Monitor Shell Run Modes

`SCRIPT` *Default mode*
: Accept one send command, do not print result object.

`REPL`
: Accept many send commands, always print result objects.

`BATCH`
: Accept many send commands, but do not print result objects.

`EVAL_SCRIPT`
: Accept one send command, print result object.

**NOTE** Specifying the `<mode>` in `moni open <mode> <connection-name>` is case-insensitive


Appendix
--------

### Troubleshooting

* If you don't get a connection, check `$GLOBUS_LOCATION/var/container.log` and ensure that the
  GNDMS Monitor Shell has been started.

* Make sure you have set up your environment as described in the
[prerequisites section](/installation-guide/#prerequisites) of the
GNDMS Installation Guide.

* If you execute moni and nothing happens you might just have
forgotten an argument. Currently, moni is just a bunch of helper `bash`
scripts that call `curl` and lack proper argument checking. If you do
not provide `moni send` with apropriate arguments, it may wait while
attempting to read from stdin.

* `monitor.minConnections` should always be >= 2


### Tips for Script Developers

Inside your own groovy classes, you should always print to `out` or `err`
which contain the current monitor's output stream. Plain println only works
correctly in the (outmost) script scope or top-level functions.

`out` and `err` properties are added automatically to `Object.metaClass` when
a monitor is instantiated. To enable them, `ExpandoMetaClass.enableGlobally()`
is called first which affects the semantics of Groovy.

Additional properties like resource homes and singleton resource instances are
made available using the same mechanism.


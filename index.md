---
title: GNDMS
root: .
version: 0.6.x
layout: main
---

Generation N Data Management System
===================================

The Generation N Data Management System (**GNDMS**) is a set of 
[Globus Toolkit 4](http://www.globus.org) WSRF services and associated tools
for distributed grid data management based on staging and
co-scheduling. It's what you might want to use if you have big chunks 
of data that need to be copied around betwen supercomputer centers in 
an orchestrated, and secure way.

GNDMS abstracts from data sources via a data integration
layer and provides logical names, data transfers via GridFTP, proper
handling of GSI certificate delegation and workspace management.

Besides data management functionality the implementation provides
components for remote logging, run-time reconfiguration, persistence,
and failover beyond what is available from [Globus Toolkit 4](http://www.globus.org).

Originally, GNDMS was written and deployed for the data management
needs of the [Collaborative Climate Community Data and Processing Grid (C3-Grid)](http://www.c3grid.de)
 and is now being used in the [Plasma-Technologie-Grid (PT-Grid)](http://www.pt-grid.de) as part of
the German [D-Grid](http://www.dgrid.de) grid computing initiative.
Nevertheless, the implementation is flexible and has been written for
reuse by other grid projects with similiar data management
requirements. Core components may be of use to developers of non-data
management GT4 services as well.

GNDMS was developed at [Zuse Institute Berlin (ZIB)](http://www.zib.de).


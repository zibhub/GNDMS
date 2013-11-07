---
title: GNDMS
root: ..
version: 0.6.2
layout: main
---

Generation N Data Management System {{ page.version }}
======================================================

The Generation N Data Management System (**GNDMS**) is a set of
RESTful services and associated tools
for distributed grid and cloud data management based on staging and
co-scheduling. It's what you might want to use if you have big chunks
of data that need to be copied around between supercomputer centers in
an orchestrated, and secure way.

GNDMS abstracts from data sources via a data integration layer and
provides logical names, data transfers via GridFTP and HTTP, proper
handling of GSI certificate delegation and workspace management.

Besides data management functionality the implementation provides
components for remote logging, run-time reconfiguration, persistence,
and fail-over.

Since version 0.6.0 GNDMS is no WSRF-based GT4 service anymore. Thus
GNDMS can be deployed in any Servlet 2.5 compliant Application
Container.  However using the CoG Globus bindings, we achieve
interoperability with GSI, MyProxy and GridFTP form both Globus
Toolkit 4 and the new plain Globus Toolkit 5.

Originally, GNDMS was written and deployed for the data management
needs of the [Collaborative Climate Community Data and Processing Grid (C3-Grid)](http://www.c3grid.de)
 and is now being used in the [Plasma-Technologie-Grid (PT-Grid)](http://www.pt-grid.de) as part of
the German [D-Grid](http://www.dgrid.de) grid computing initiative.
Nevertheless, the implementation is flexible and has been written for
reuse by other grid projects with similar data management
requirements.

GNDMS was developed at [Zuse Institute Berlin (ZIB)](http://www.zib.de).

---
title: GNDMS
layout: wikistyle
---

Generation N Data Management System
===================================

GNDMS is a set of [Globus Toolkit 4](http://www.globus.org) WSRF
services and associated tools for distributed grid data management
based on staging and co-scheduling. It abstracts from data sources via
a data integration layer and provides logical names, data transfers
via GridFTP, proper handling of GSI certificate delegation and
workspace management.

Besides data management functionality the implementation provides
components for remote logging, run-time reconfiguration, persistence,
and failover beyond what is available from [Globus Toolkit
4](http://www.globus.org).

Originally, GNDMS was written and deployed exclusively for the data
management needs of [c3grid](http://www.c3grid.de) and has been used
in [plasmagrid](http://www.ptgrid.de) as part of the German
[D-Grid](http://www.dgrid.de) grid computing initiative.

Nevertheless, the implementation is flexible enough for reuse by other
grid projects with similiar data management requirements.




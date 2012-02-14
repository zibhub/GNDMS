---
title: GNDMS Glossary
version: 0.3.x
root: ../..
layout: wikistyle
---

Glossary
========

**Contract**
: GNDMS negotiates *contracts* with clients about task execution. A contract specifies what is to be done, and optionally when and where it is to be done by GNDMS on behalf of the client.
*Contracts* are accepted *Offers*.

**Data Provider**
: In [C3-Grid](http://www.c3grid.de), data providers are sites that run GNDMS with the Staging Plugin in order to grant access to their local climate data archives.

**DMS**
: Often used for the (or a) central data management coordination site of a community grid.

**DMS-Publish**
: In [C3-Grid](http://www.c3grid.de), load balancing publish to a dedicated storage server.

**DMS-Staging**
: In [C3-Grid](http://www.c3grid.de), indirection of a staging request to a matching data provider or cache.

**DSpace**
: Workspace management service of GNDMS.  Each DSpace is structured into a set of *subspaces* (Logical stroage group).  Each subspace consists of multiple *slices* (Non-hierachical container of files).

**GNDMS**
: Generation N Data Management System. A data management solution for community grids based on the [Globus Toolkit 4 Middleware](http://www.globus.orrg).

**GORFX** (aka **G**eneric **O**ffer **R**equest **F**actory **X**)
: Service for the negotation of data management task execution.

**Offer**
: Cf. *Contract*, offers are the subjects of contract negotiation, Offers are not-yet accepted contracts.

**Offer Request**
: Description of a task and required *offer* constraints.

**Publish**
: In [C3-Grid](http://www.c3grid.de), publishing of intermediary results

**Publish-Host**
: Host that provides storage resources for *Publish*. Needs to run *DSpace* and *GORFX* configured for support of the *Publish* task.

**Staging**
: In [C3-Grid](http://www.c3grid.de), import of climate data from external archives into the data management infrastructure of the community grid.



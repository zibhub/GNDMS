---
title: GNDMS Developer Guide
layout: wikistyle
---





GNDMS Developer Guide
=====================

This is the Developer Guide for the
[Generation N Data Management System](../index.html). It is far from
complete.  It currently contains various tidbits copied together from
different Wikis.  YMMV.  Use the source, luke!

* Maruku will replace this with a fine Table of Contents
{:toc}



Writing Webservice Clients
--------------------------



### Setup a Development Environment

* Install GNDMS as described in the
  [installation guide](../installation-guide)
* Use `gndms-buildr idea` or `gndms-buildr eclipse` to generate
  template IDEA or eclipse projects.
* You might need to add `$GLOBUS_LOCATION/lib/*.jar` 
  * Skip `gndms-*.jar`, but
  * include`gndms-*-service.jar` and `gndms-*-client.jar`  
  
  
## Setup a Development Environment for Debugging

* Ensure that the generated modules in your IDE setup compile
to the same output path as buildr and that globus, buildr, and your
IDE compile using the same JDK.

* Edit your globus scripts such that Java is configured to enable
remote debugging and set up a matching run target in your IDE. 

**NOTE** *If the globus container is started with `-debug` it prints
full stacktraces, otherwise not!*

  
  
### Writing a Web Service Client

* Take a look at `ProviderStageInClient`
* Do not directly instantiate port types.  Always use the associated
   `PortTypeFooClient` classes to get port type instances.
* If you really need to instantiate port types directly, ensure that
  the used axis engine is configured with the correct `.wsdd` files.
  This work is done by `PortTypeFooClient` classes if you use them.



Notes on Certificate Delegation
-------------------------------

To use certificate delegation, two steps are necessary. First, service
security settings need to be changed. Second, client and and service
code need to be modified slightly to incorporate support for
certificate delegation.


### Security Descriptor Basics

**NOTE** This is a very brief description of security descriptors.  More
advanced configuration is possible, e.g. service method level A&A.

The security descriptor (Short: **SD**) describes authentication and
authorization requirements of clients and WSRF web services. The **SD** of
a service is configured in the `service` section of the WSDD file.

{% highlight xml %}
   <?xml version="1.0" encoding="UTF-8"?>
    <deployment>
        <service>
             <parameter name="securityDescriptor" value="etc/*-security-desc.xml" /> 
        </service>
    </deployment>
{% endhighlight %}
      
(Here `*` refers to the service name and pathes are relative to `$GLOBUS_LOCATION`)
 
Client security descriptors are loaded directly in the client software:
 
{% highlight java %} 
    // Client security descriptor file 
    String CLIENT_DESC = ".../client-security-config.xml";
    ClientSecurityDescriptor desc = new ClientSecurityDescriptor( CLIENT_DESC );
    //Set descriptor on Stub 
    ( (Stub)port )._setProperty( Constants.CLIENT_DESCRIPTOR, desc )
{% endhighlight %}    
      
For more details, please consult the
[documentation on security descriptors](http://www.globus.org/toolkit/docs/development/4.1.2/security/security-secdesc.html).


### Authentication and Authorization

The following **SD** example shows how mandatory TLS encryption is enforced:

{% highlight xml %}
    <?xml version="1.0" encoding="UTF-8"?>
    <securityConfig xmlns="http://www.globus.org">
    ...
        <auth-method>
            <GSITransport>
                <protection-level>
                    <privacy />
                </protection-level>
            </GSITransport>
        </auth-method>
    </securityConfig>
{% endhighlight %}
	
This setting must be made both on the server and the client.

For authorization, a gridmap file needs to be set:

{% highlight xml %}
    <authz value="gridmap" />
{% endhighlight %}

This enables use of the system wide gridmap-file.  To use a service
specific gridmap file, please add:

{% highlight xml %}
    <gridmap value="etc/gndms_shared/grid-mapfile" />
{% endhighlight %}

Finally, it is necessary to configure (unless you are using JAAS):


{% highlight xml %}
    <run-as>
       <system-identity />
    </run-as>
{% endhighlight %}
      
      
Below is a complete example:


{% highlight xml %}
    <?xml version="1.0" encoding="UTF-8"?>
    <securityConfig xmlns="http://www.globus.org">
        <authz value="gridmap" />
        <gridmap value="etc/c3grid_shared/grid-mapfile" />
        <auth-method>
            <GSITransport>
                <protection-level>
                    <privacy />
                </protection-level>
            </GSITransport>
        </auth-method>
        <run-as>
            <system-identity />
        </run-as>
    </securityConfig>      
{% endhighlight %}


### Client-Side Delegation

This section described delegation from the viewpoint of the client.
The client uses the Delegation Service to retrieve the Certificate
Chain.  This is used to generate a proxy certificate which is sent to
the delegation service in order to obtain an EPR for the proxy. This
EPR may be passed when accessing resources directly or is sent to
factory methods during resource instantiation.

{% highlight java %}
   // path to the file containing the proxy cert
    String proxyFile = ...;
 
    // uri of the delegation service
    String delUri =  "http://somehost/wsrf/services/DelegationFactoryService"
 
    // port type of our service acquired in the usual fashion
    PortType port = ... ;
 
     
    GlobusCredential credential = new GlobusCredential( proxyFile );
     
    // Create security descriptor for the communication with the delegation service
    // This descriptor is not the same we use to communicate with
    // the actual service
    ClientSecurityDescriptor desc = new ClientSecurityDescriptor();
    org.ietf.jgss.GSSCredential gss = 
        new org.globus.gsi.gssapi.GlobusGSSCredentialImpl( credential, 
	   org.ietf.jgss.GSSCredential.INITIATE_AND_ACCEPT );
    desc.setGSSCredential( gss );
    EndpointReferenceType delegEpr = 
        AddressingUtils.createEndpointReference( delUri, null );
    desc.setGSITransport( (Integer) Constants.SIGNATURE );
    Util.registerTransport();
    desc.setAuthz( NoAuthorization.getInstance() );
 
    // acquire cert chain 
    X509Certificate[] certs = 
        DelegationUtil.getCertificateChainRP( delegEpr, desc );
 
    if( certs == null  )
         throw new Exception( "No Certs received" );
 
    // create delegate
    int ttl = 600; // a time to life for the proxy in seconds 
    // the boolean value can be ignored
    EndpointReferenceType delegate = 
        DelegationUtil.delegate( delUri, credential, certs[0], ttl, true, desc );
 
    // reuse credentials for this call
    ( (Stub) port )._setProperty( 
        org.globus.axis.gsi.GSIConstants.GSI_CREDENTIALS, gss ); 
 
    // creates a new resource which uses the delegate, i.e. proxy cert
    EndpointReferenceType epr = 
        ( (SomePortType) port ).createResource( delegate );
{% endhighlight %}



### Server-Side Delegation

On the server side, the EPR needs to be used to retrieve the proxy
certificate. Additionally, a `DelegationListener` needs to be
registered to be informed about proxy state changes (Update, Destroy).

Example service factory method that instantiates a resource:

{% highlight java %}
    public EndpointReferenceType createResource ( EndpointReferenceType delegate ) {
        SomeResource sr = new SomeResource( );
        sr.setDelegationEPR( delegate );
        ...
        return endPointRefOf( sr );
    }
{% endhighlight %}

The resource needs to be modified accordingly as well:

{% highlight java %}
    public class SomeResource implements Resource {
 
        SomeResourceHome home;
        GlobusCredential credential;
 
        public void refreshRegistration( final boolean forceRefresh ) {
            // do refreshing stuff if required
        }
 
 
        public void setCredential( final GlobusCredential cred ) {
            credential = cred;
        }
 
 
        public GlobusCredential getCredential( ) {
            return credential;
        }
 

        public void setDelegateEPR( final EndpointReferenceType epr ) {
 
            SomeDelegationListener list = 
	        new SomeDelegationListener( getResourceKey(), home );
            try {
                // registers listener with the delegation service
                DelegationUtil.registerDelegationListener( epr, list );
            } catch ( DelegationException e ) {
                e.printStackTrace();
            }
        }
 
        // other service specific stuff here ...
     }
{% endhighlight %}

The container will be calling `get/setCredential` on the listener
interface.  A simple default implementation follows:

{% highlight java %}
    public class SomeDelegationListener implements DelegationListener {
 
        private static Logger logger = Logger.getLogger( SomeDelegationListener.class );
        private String regristrationId;
        private ResourceKey resourceKey;
        private ResourceHome home;
 
 
        public SomeDelegationListener() {
        }
 
 
        public SomeDelegationListener( final ResourceKey resourceKey, 
	final ResourceHome home ) {
            this.resourceKey = resourceKey;
            this.home = home;
        }
 
 
        public void setCredential( final GlobusCredential credential )
	throws DelegationException {
 
             try {
               SomeCredibleResource res = 
	           ( SomeCredibleResource ) home.find( resourceKey );
               res.setCredential( credential );
             } catch ( ResourceException e ) {
               logger.error( e );
             }
        }
 
 
        public void credentialDeleted() {
           // Can notify the resource
        }
 
        // getters and setters for the instance vars are omitted for the sake of shortness
        // ....
    }
{% endhighlight %}

The `setCredential` method will be called at listener registration
time.

With these extensions, a resource has access to the credentials of the
user to which the proxy belongs.




### Using Delegation with Proxy Certificates


#### Service Orchestration

The main purpose of certificate delegation is to allow a service to
call another service on behalf of the user. Let's assume `SomeService`
is a client of `AnotherService`.  In the following example
`AnotherService` is called by `SomeService` with the proxy credentials
by first loading them into the `ClientDescriptor`:

{% highlight java %}
    AnotherPortType port = ...;
        ( (Stub) port )._setProperty( org.globus.wsrf.security.Constants.GSI_TRANSPORT,
                            org.globus.wsrf.security.Constants.ENCRYPTION );
                // SIGNATUR should also work
        org.ietf.jgss.GSSCredential gss = 
	    new org.globus.gsi.gssapi.GlobusGSSCredentialImpl( credential,
                org.ietf.jgss.GSSCredential.INITIATE_AND_ACCEPT );
        ( (Stub) port )._setProperty( 
	    org.globus.axis.gsi.GSIConstants.GSI_CREDENTIALS, gss );
        ( (Stub) port ).doAnotherThing();
{% endhighlight %}

Now, in `AnotherService`, the caller DN (`null` in anonymous
communication) is obtainable by calling:

{% highlight java %}
     org.globus.wsrf.security.SecurityManager.getManager().getCaller();
{% endhighlight %}
     
This may be mapped to local UNIX users via the grid-map mechanism:

{% highlight java %}
      org.globus.wsrf.security.SecurityManager.getManager( ).getLocalUsernames()
{% endhighlight %}

     
#### Export Proxy Credentials to a File

{% highlight java %}
    public void storeCredential( Sting filename ) {
         try {
             File f = new File( filename );
             FileOutputStream fos = new FileOutputStream( f );
             GlobusGSSCredentialImpl crd = 
	         new GlobusGSSCredentialImpl( credential, GSSCredential.ACCEPT_ONLY );
             fos.write( crd.export( ExtendedGSSCredential.IMPEXP_OPAQUE  ) );
             fos.close();
         } catch( Exception e ) {
             // an exception --- do something
         }
    }
{% endhighlight %}

The resulting file is structured  as follows:

* Proxy certificate generated last
* Private key of this certificate
* Certificate chain in descending order

The exported proxy may be verified manually with openssl by first
splitting this file into the `head` (containing everything but the
certificate chain) and the `tail` (containing the certificate chain)
and setting `$OPENSSL_ALLOW_PROXY_CERTS=1`. Now execute:

    openssl verify -CApath /etc/grid-security/certificates -CAfile tail head
    
If everything is ok, `openssl` will print

    head: OK
    
Otherwise a lengthy error message will be shown.

All this is typically done by
`$GLOBUS_LOCATION/bin/grid-proxy-verify`.  A look at the source code
is an interesting read concerning the details of proxy verification
with openssl.
     



Contract Semantics
------------------

This section describes the precise semantics of offer contracts in
*GNDMS*.  An *offer* is an negotiable offer for the execution of a
data management task (like Staging, Transfer, and Publishing).  Offer
contracts may specify requirements on execution time,
duration and location.  Offers are negotiated between a client and
 server in rounds until agreement is reached and a contract is
 succesfully established.
 
 Client and server roles are taken by different participants. For
 example, a grid meta scheduler may be a client to a central data
 management site that runs GNDMS, while the same site may be a client
 to a data provider site in a different negotiation.



### Protocol

The protocol consists of three steps.

* Client send an OfferRequest with desired task and requirements
* Server replies with an offer contract that tries to match clients
  requirements.
* Client either accepts the offer. In this case, the protocol is
 finished with the creation of a task resource that allows the client
 to monitor task execution and to fetch results.  Otherwise, the
 client is free to discard the offer and redo the protocol with
 another site or another contract.
 
      Client                      GORFXServer 
         |                              |
         |     createOffer( ORQ )*      |
         X----------------------------->|--+
         |                              |  |
         |                              |  | EstimateScript( ORQ )
         |                              |  |
         |    offerContract             |<-+
         |<-----------------------------X
         |       Accept                 |
         X----------------------------->|--+
         |                              |  |
         |                              |  | StagingScript
         |                              |  |
         |        Result                |<-+
         |<-----------------------------X
         |                              |
         .                              .



### Contract Structure

A contract consists of

* An (optional) point in time called `IfDecisionBefore`, **IDB** for short
* An (optional) point in time called `ExecutionLikelyUntil`, **ELU**
  for short
* A point in time or an offet calles `ResultValidUntil`, **RVU** for
  short, or **Delta-RVU**, respectively
* An (optional) size estimation calles `EstMaxSize`, **EMS** for short
  (upper bound on the number of bytes of result data)
* An (optional) set of key-value apires called `RequestInfo`, **RI** for
  short.  **RI** may be used to pass additional information like
  remarks, warnings etc.  From a middleware point of view, **RI** is
  *not* part of the contract.

  
  
### Contract Semantics

**Precise contract semantics**
: If the Offer is accepted before **IDB**, the task will be executed
before **ELU** with high probability.  Results are made available
until **RVU** as long as they do not need more than **EMS** bytes of
storage.


If **IDB** is missing, it is interpreted as an *arbitrary, undefined
point in the future*. This is currently not supported by the software
but specifiable according to the underlying XSD schema.

If **ELU** is missing, it is interpreted as *arbitrary, unknown task execution duration*. This is currently not supported by the software
but specifiable according to the underlying XSD schema.


### Generic Client Restrictions

**IDB** is mandatory.  All mandatory invariants need to be fulfilled.


### C3-Grid Data Provider Server Restrictions

Contract semantic variables are mapped to staging properties as
detailed below:

    IDB = c3grid.StageFileRequest.Estimate.IfDecisionBefore, 
    ELU = c3grid.StageFileRequest.Estimate.ExecutionLikelyUntil, 
    RVU = c3grid.StageFileRequest.Estimate.ResultValidUntil, 
    EMS = c3grid.StageFileRequest.Estimate.MaxSize, 
    RI  = c3grid.StageFileRequest.Estimate.RequestInfo

* Data providers must specify an **ELU** that must be identical to the **ELU**
  requested from the client
* **IDB** and **RVU** may not be modified by the server. In the case
    of **RVU** < **ELU**, the client should discard the request.
* Client **EMS** may be discarded or overwritten by the server in his
  offer
* **RI** is filtered for keys

**Summary**
: For staging requests to data providers, it is sufficient to specify
**ELU** in ms and **EMS** in bytes, and to optionally include
key-value data in **RI**


### Support for Missing Timing Estimates



### Contract Invariants

Depending on how the contract requested by the client looks like, some
invariants need to be fulfilled:

**ELU, RVU** requested
: **IDB** < **ELU** and **IDB** < **RVU** (Therefore in practice,
choose **IDB** < **ELU** < **RVU**)

**Delta-ELU, RVU** requested
: **IDB** < **RVU** (Therefore in practice, choose **IDB** +
**Delta-ELU** < **RVU**)

**ELU, Delta-RVU** requested
: **IDB** < **ELU** (and **RVU** is **ELU** + **Delta-RVU** and
therefore **ELU** <= **RVU** always holds)

**Delta-ELU, Delta-RVU** requested
: No invariants, it holds that start time **ST** <= **IDB**,
completion time **CT** = **ELU** = **ST** + **Delta-ELU**, **RVU** =
**CT** + **Delta-RVU** and therefore always **ELU** <= **RVU**

It always holds that **ST**<=**IDB**<=**CT**<=**ELU**.  If
**Delta-RVU** was requested. Additionally always **CT** <= **ELU** <=
**RVU** is true.

Clients need to honor all invariants.  Servers need to honor all
invariants which do not contain **RVU**.  Servers may only modify **ELU**.


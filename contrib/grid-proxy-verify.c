/***************************************************************************
   grid-proxy-verify.c

   $Id: grid-proxy-verify.c,v 1.12 2009/02/22 22:01:36 janjust Exp $

 Sample C program that verifies a Globus GSI proxy.
 The following checks are performed:
 - certificate chain is verified , including proxy certs
 - proxy itself is verified (SUBJ/CN=proxy vs ISSUER etc)
 - proxy private key is matched against proxy public key
 - file permissions of proxy file are checked

 Build instructions:
    gcc -o grid-proxy-verify grid-proxy-verify.c \
        -I<OPENSSL-INCLUDE> -L<OPENSSL-LIB> -lssl -lcrypto
 (code is CFLAGS="-Wall -g -Wuninitialized -O2 -pedantic" clean)

 Copyright (c) 2008, 2009 by
   Jan Just Keijser (janjust@nikhef.nl)
   Oscar Koeroo (okoeroo@nikhef.nl
   Nikhef
   Amsterdam
   The Netherlands
 ***************************************************************************/

#define _GNU_SOURCE

#include <stdarg.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>       

#include <sys/types.h>
#include <sys/stat.h>

#include <openssl/x509.h>
#include <openssl/x509v3.h>
#include <openssl/x509_vfy.h>
#include <openssl/crypto.h>
#include <openssl/err.h>
#include <openssl/pem.h>

#include <openssl/rsa.h>
#include <openssl/evp.h>
#include <openssl/bio.h>    
#include <openssl/des.h>    
#include <openssl/rand.h>

#include <openssl/buffer.h>
#include <openssl/objects.h>
#include <openssl/asn1.h>

#define L_ERROR  0  /* errors */
#define L_WARN   1  /* all unusual */
#define L_INFO   2  /* all status changes etc. */
#define L_DEBUG  3  /* all, including trace */

int    log_level = 1;
char  *fileName  = NULL;

void Log( int msg_level, const char *msg, ...)
{
    va_list argp;

    if ( log_level >= msg_level )
    {
        if (msg_level == L_WARN )  fprintf( stderr, "Warning: " );
        if (msg_level == L_INFO )  fprintf( stderr, "Info:    " );
        if (msg_level == L_DEBUG ) fprintf( stderr, "Debug:   " );
        va_start( argp, msg );
        vfprintf( stderr, msg, argp );
        va_end( argp );
        fprintf( stderr, "\n" );
    }
}

void Error( const char *operation, const char *msg, ...)
{
    va_list argp;

    fprintf( stderr, "ERROR:  %s: ", operation );
    va_start( argp, msg );
    vfprintf( stderr, msg, argp );
    va_end( argp );
    fprintf( stderr, "\n" );
}

void print_usage( void )
{
    printf( "grid-proxy-verify\n");
    printf( "Usage:\n" );
    printf( "  grid-proxy-verify [-h|--help] [-d|--debug] [-q||--quiet] [-v|--version] [proxy]\n\n" );
    printf( "Repeat -d/--debug multiple times to get more debugging output.\n" );
    printf( "If no proxy is specified then %s is used.\n", fileName );

    exit(0);
}

void print_version( void )
{
    char  versionStr[255] = "$Revision: 1.12 $";
    char *versionPtr1, *versionPtr2;

    printf( "grid-proxy-verify v" );
    versionPtr1 = strchr( versionStr, ' ' );
    if ( versionPtr1 )
    { 
        versionPtr1++;
        versionPtr2 = strchr( versionPtr1, ' ' );
        if ( versionPtr2 ) *versionPtr2 = '\0';
        printf( "%s", versionPtr1 );
    }
    else
    {
        /* cannot determine version number - did you use cvs -k? */
        printf( "666");
    }
    printf( "  - written by Jan Just Keijser, Nikhef, 2008,2009\n");

    exit(0);
}


/*
 *  (Use ASN1_STRING_data() to convert ASN1_GENERALIZEDTIME to char * if
 *   necessary)
 */
                                                                                
time_t grid_asn1TimeToTimeT(char *asn1time, size_t len)
{
   char   zone;
   struct tm time_tm;
   
   if (len == 0) len = strlen(asn1time);
                                                                                
   if ((len != 13) && (len != 15)) return 0; /* dont understand */
                                                                                
   if ((len == 13) &&
       ((sscanf(asn1time, "%02d%02d%02d%02d%02d%02d%c",
         &(time_tm.tm_year),
         &(time_tm.tm_mon),
         &(time_tm.tm_mday),
         &(time_tm.tm_hour),
         &(time_tm.tm_min),
         &(time_tm.tm_sec),
         &zone) != 7) || (zone != 'Z'))) return 0; /* dont understand */
                                                                                
   if ((len == 15) &&
       ((sscanf(asn1time, "20%02d%02d%02d%02d%02d%02d%c",
         &(time_tm.tm_year),
         &(time_tm.tm_mon),
         &(time_tm.tm_mday),
         &(time_tm.tm_hour),
         &(time_tm.tm_min),
         &(time_tm.tm_sec),
         &zone) != 7) || (zone != 'Z'))) return 0; /* dont understand */
                                                                                
   /* time format fixups */
                                                                                
   if (time_tm.tm_year < 90) time_tm.tm_year += 100;
   --(time_tm.tm_mon);
                                                                                
   return timegm(&time_tm);
}


/*
 *   Print a certificate serial number in hex (e.g. 088E)
 */
                                                                                
void grid_printSerial( ASN1_INTEGER *certSerial )
{
    int             i, serialLen;
    unsigned char   serialNumberDER[127];
    unsigned char  *serialNumberPtr = serialNumberDER;
    char            serialStr[255];
    char           *serialStrPtr = serialStr;

    serialLen = i2c_ASN1_INTEGER( certSerial, &serialNumberPtr );
    bzero( serialStr, sizeof( serialStr) );
    for (i = 0; i < serialLen; i++)
    {
         sprintf( serialStrPtr, "%02X", serialNumberDER[i] );
         serialStrPtr = serialStrPtr + 2;
    }
    Log( L_DEBUG, "Serial number: %s", serialStr);
}

/*
 *  Check if certificate can be used as a CA to sign standard X509 certs
 *  Return 1 if true; 0 if not.
 */

int grid_x509IsCA(X509 *cert)
{
    int idret;

    /* final argument to X509_check_purpose() is whether to check for CAness */   
    idret = X509_check_purpose(cert, X509_PURPOSE_SSL_CLIENT, 1);
    if (idret == 1)
        return 1;
    else if (idret == 0)
        return 0;
    else
    {
        Log( L_WARN, "Purpose warning code = %d\n", idret );
        return 1;
    }
}   

int grid_X509_empty_callback(char *buf, int buf_size, int verify, void *cb_tmp)
{
     if ( buf_size > 0 ) buf = '\0';
     return 0;
}

#define PROXYCERTINFO_OID      "1.3.6.1.5.5.7.1.14"
#define OLD_PROXYCERTINFO_OID  "1.3.6.1.4.1.3536.1.222"

unsigned long grid_X509_knownCriticalExts(X509 *cert)
{
   int  i;
   char s[80];
   X509_EXTENSION *ex;

   for (i = 0; i < X509_get_ext_count(cert); ++i)
      {
        ex = X509_get_ext(cert, i);

        if (X509_EXTENSION_get_critical(ex) &&
                                 !X509_supported_extension(ex))
          {
            OBJ_obj2txt(s, sizeof(s), X509_EXTENSION_get_object(ex), 1);

            Log( L_DEBUG, "Critical extension found: %s", s );

            if (strcmp(s, PROXYCERTINFO_OID) == 0) return X509_V_OK;
            if (strcmp(s, OLD_PROXYCERTINFO_OID) == 0) return X509_V_OK;

            return X509_V_ERR_UNHANDLED_CRITICAL_EXTENSION;
          }
      }

   return X509_V_OK;
}


unsigned long grid_readProxy( char *filename, STACK_OF(X509) **certstack, EVP_PKEY **pkey )
{
    char                *oper = "Reading proxy";

    STACK_OF(X509_INFO) *sk      = NULL;
    BIO                 *certbio = NULL;
    X509_INFO           *xi;
    unsigned long        err;

    Log( L_DEBUG, "--- Welcome to the grid_readProxy function ---");

    *certstack = sk_X509_new_null();
    if (*certstack == NULL) return ERR_get_error();

    Log( L_INFO, "Reading file %s", filename );
/*
    if ( (certbio = BIO_new(BIO_s_file())) == NULL ) return ERR_get_error();

    if ( BIO_read_filename(certbio, filename) <= 0 ) return ERR_get_error();
*/
    if ( !(certbio = BIO_new_file( filename, "r" )) ) return ERR_get_error();
  
    Log( L_DEBUG, "Reading X509_INFO records" );
    if ( !(sk=PEM_X509_INFO_read_bio(certbio, NULL, NULL, NULL)) )
    {
        err = ERR_get_error();
        Error( oper, "No X509 records found" );
        BIO_free(certbio);
        sk_X509_INFO_free(sk);
        sk_X509_free(*certstack);
        return err;
    }

    Log( L_DEBUG, "Resetting BIO" );
    if ( (err = BIO_reset( certbio )) != X509_V_OK ) return err;

    Log( L_DEBUG, "Reading Private key" );
    *pkey = PEM_read_bio_PrivateKey( certbio, NULL, grid_X509_empty_callback, NULL );

    if ( *pkey == NULL ) Log( L_WARN, "No private key found." );

    while (sk_X509_INFO_num(sk))
    {
        xi=sk_X509_INFO_shift(sk);
        if (xi->x509 != NULL)
        {
            sk_X509_push(*certstack, xi->x509);
            xi->x509=NULL;
        }
        X509_INFO_free(xi);
    }
       
    if (!sk_X509_num(*certstack))
    {
        err = ERR_get_error();
        Error( oper, "No certificates found" );
        BIO_free(certbio);
        sk_X509_INFO_free(sk);
        sk_X509_free(*certstack);
        return err;
    }

    BIO_free(certbio);
    sk_X509_INFO_free(sk);
   
    return X509_V_OK;
}


int grid_X509_check_issued_wrapper(X509_STORE_CTX *ctx,X509 *x,X509 *issuer)
/* We change the default callback to use our wrapper and discard errors
   due to GSI proxy chains (ie where users certs act as CAs) */
{
    int ret;
    ret = X509_check_issued(issuer, x);
    if (ret == X509_V_OK) return 1;

    /* Non self-signed certs without signing are ok if they passed
           the other checks inside X509_check_issued. Is this enough? */
    if ((ret == X509_V_ERR_KEYUSAGE_NO_CERTSIGN) &&
        (X509_NAME_cmp(X509_get_subject_name(issuer),
                       X509_get_subject_name(x)) != 0)) return 1;

#if OPENSSL_VERSION_NUMBER < 0x00908000L
    /* If we haven't asked for issuer errors don't set ctx */
    if (!(ctx->flags & X509_V_FLAG_CB_ISSUER_CHECK)) return 0;
#else
    if (!(ctx->param->flags & X509_V_FLAG_CB_ISSUER_CHECK)) return 0;
#endif

    ctx->error = ret;
    ctx->current_cert = x;
    ctx->current_issuer = issuer;
    return ctx->verify_cb(0, ctx);
}


/******************************************************************************
Function:   grid_verifyProxy
Description:
    Tries to verify the proxies in the certstack
******************************************************************************/
unsigned long grid_verifyProxy( STACK_OF(X509) *certstack )
{
    char    *oper = "Verifying proxy";

    int      i = 0;
    X509    *cert = NULL;
    time_t   now = time((time_t *)NULL);
    size_t   len = 0;             /* Lengths of issuer and cert DN */
    size_t   len2 = 0;            /* Lengths of issuer and cert DN */
    int      prevIsLimited = 0;   /* previous cert was proxy and limited */
    char    *cert_DN = NULL;      /* Pointer to current-certificate-in-certstack's DN */
    char    *issuer_DN = NULL;    /* Pointer to issuer-of-current-cert-in-certstack's DN */
    char    *proxy_part_DN = NULL;
    int      depth = sk_X509_num (certstack);
    int      amount_of_CAs = 0;
    int      is_old_style_proxy = 0;
    int      is_limited_proxy = 0;
    ASN1_INTEGER   *cert_Serial = NULL;
    ASN1_INTEGER   *issuer_Serial = NULL;

    Log( L_DEBUG, "--- Welcome to the grid_verifyProxy function ---");

    /* And there was (current) time... */
    time(&now);

    /* How many CA certs are there in the certstack? */
    for (i = 0; i < depth; i++)
    {
        if (grid_x509IsCA(sk_X509_value(certstack, i)))
            amount_of_CAs++;
    }

    Log( L_DEBUG, "#CA's = %d , depth = %d", amount_of_CAs, depth );

    if ((amount_of_CAs + 2) > depth)
    {
        if ((depth - amount_of_CAs) > 0)
        {
            Log( L_WARN, "No proxy certificate in certificate stack to check." );
            return X509_V_OK;
        }
        else
        {
            Error( oper, "No personal certificate (neither proxy or user certificate) found in the certficiate stack." );
            return X509_V_ERR_APPLICATION_VERIFICATION;
        }
    }


    /* Changed this value to start checking the proxy and such and
       to skip the CA and the user_cert
    */
    for (i = depth - (amount_of_CAs + 2); i >= 0; i--)
    {
        /* Check for X509 certificate and point to it with 'cert' */
        if ( (cert = sk_X509_value(certstack, i)) != NULL )
        {
            cert_DN   = X509_NAME_oneline( X509_get_subject_name( cert), NULL, 0);
            issuer_DN = X509_NAME_oneline( X509_get_issuer_name( cert ), NULL, 0);
            len       = strlen( cert_DN );
            len2      = strlen( issuer_DN );

            Log( L_INFO, "Proxy to verify:" );
            Log( L_INFO, "  DN:        %s", cert_DN );
            Log( L_INFO, "  Issuer DN: %s", issuer_DN );

            if (now < grid_asn1TimeToTimeT((char *)ASN1_STRING_data(X509_get_notBefore(cert)),0))
            {
                Error( oper, "Proxy certificate is not yet valid." );
                return X509_V_ERR_CERT_NOT_YET_VALID;
            }

            if (now > grid_asn1TimeToTimeT((char *)ASN1_STRING_data(X509_get_notAfter(cert)),0))
            {
                Error( oper, "Proxy certificate expired." );
/* error will be caught later by x509_verify
                return X509_V_ERR_CERT_HAS_EXPIRED;
*/
            }

            /* User not allowed to sign shortened DN */
            if (len2 > len)
            {
                Error( oper, "It is not allowed to sign a shorthened DN.");
                return X509_V_ERR_APPLICATION_VERIFICATION;
            }

            /* Proxy subject must begin with issuer. */
            if (strncmp(cert_DN, issuer_DN, len2) != 0)
            {
                Error( oper, "Proxy subject must begin with the issuer.");
                return X509_V_ERR_APPLICATION_VERIFICATION;
            }

            /* Set pointer to end of base DN in cert_DN */
            proxy_part_DN = &cert_DN[len2];

            /* First attempt at support for Old and New style GSI
               proxies: /CN=anything is ok for now */
            if (strncmp(proxy_part_DN, "/CN=", 4) != 0)
            {
                Error( oper, "Could not find a /CN= structure in the DN, thus it is not a proxy.");
                return X509_V_ERR_APPLICATION_VERIFICATION;
            }

            if (strncmp(proxy_part_DN, "/CN=proxy", 9) == 0)
            {
                Log( L_INFO, "Current certificate is an old style proxy.");
                is_old_style_proxy = 1;
                is_limited_proxy = 0;
            }
            else if (strncmp(proxy_part_DN, "/CN=limited proxy", 17) == 0)
            {
                Log( L_INFO, "Current certificate is an old limited style proxy.");
                is_old_style_proxy = 1;
                is_limited_proxy = 1;
            } 
            else
            {
                Log( L_INFO, "Current certificate is a GSI/RFC3820 proxy.");
            }
      
            if ( is_old_style_proxy )
            {
                cert_Serial = X509_get_serialNumber( cert );
                if (log_level >= L_DEBUG) grid_printSerial( cert_Serial );
    
                issuer_Serial = X509_get_serialNumber( sk_X509_value(certstack, i+1));
                if (log_level >= L_DEBUG) grid_printSerial( issuer_Serial );
    
                if (cert_Serial && issuer_Serial)
                {
                    if ( ASN1_INTEGER_cmp( cert_Serial, issuer_Serial ) )
                    {
                        Log( L_WARN, "Serial numbers do not match." );
                    }
                }
            }

            if ( is_limited_proxy )
            {
                prevIsLimited = 1;
                if (i > 0) Log( L_WARN, "Found limited proxy.");
            }
            else
            {
                if (prevIsLimited)
                {
                    Error( oper, "Proxy chain integrity error. Previous proxy in chain was limited, but this one is a regular proxy.");
                    return X509_V_ERR_APPLICATION_VERIFICATION;
                }
            }

            if (cert_DN) free(cert_DN);
            if (issuer_DN) free(issuer_DN);
        }
    }

    return X509_V_OK;
}

/******************************************************************************
Function:   verify_callback
Description:
    Callback function for OpenSSL to put the errors
Parameters:
    ok, X509_STORE_CTX
Returns:
******************************************************************************/
static int grid_X509_verify_callback(int ok, X509_STORE_CTX *ctx)
{
    unsigned long   errnum   = X509_STORE_CTX_get_error(ctx);
    int             errdepth = X509_STORE_CTX_get_error_depth(ctx);
    STACK_OF(X509) *certstack;

    if (ok != 1)
    {
        if (errnum == X509_V_ERR_INVALID_CA) ok=1;
        if (errnum == X509_V_ERR_UNABLE_TO_GET_CRL) ok=1;
#if OPENSSL_VERSION_NUMBER >= 0x009070afL
        /* I don't want to do this, really, but I have yet to figure out
           how to get openssl 0.9.8 to accept old-style proxy certificates...
        */
        if (errnum == X509_V_ERR_INVALID_PURPOSE) ok=1;
#endif
        if (errnum == X509_V_ERR_UNHANDLED_CRITICAL_EXTENSION)
        {
            errnum = grid_X509_knownCriticalExts(X509_STORE_CTX_get_current_cert(ctx));
            ctx->error = errnum;
            if (errnum == X509_V_OK) ok=1;
        }

#if 0
        if (ctx->error == X509_V_ERR_CERT_HAS_EXPIRED) ok=1;
        /* since we are just checking the certificates, it is
         * ok if they are self signed. But we should still warn
         * the user.
         */
        if (ctx->error == X509_V_ERR_DEPTH_ZERO_SELF_SIGNED_CERT) ok=1;
        /* Continue after extension errors too */
        if (ctx->error == X509_V_ERR_PATH_LENGTH_EXCEEDED) ok=1;
        if (ctx->error == X509_V_ERR_INVALID_PURPOSE) ok=1;
        if (ctx->error == X509_V_ERR_DEPTH_ZERO_SELF_SIGNED_CERT) ok=1;
        if (ctx->error == X509_V_ERR_CRL_HAS_EXPIRED) ok=1;
        if (ctx->error == X509_V_ERR_CRL_NOT_YET_VALID) ok=1;
#endif
    }
    
    /*
     * We've now got the last certificate - the identity being used for
     * this connection. At this point we check the whole chain for valid
     * CAs or, failing that, GSI-proxy validity using grid_verifyProxy
     */
    if ( (errdepth == 0) && (ok == 1) )
    {
        certstack = (STACK_OF(X509) *) X509_STORE_CTX_get_chain( ctx );

        errnum = grid_verifyProxy( certstack );

        Log( L_DEBUG, "grid_verify_callback: verifyProxy returned: %s",
                      X509_verify_cert_error_string (errnum));

        ctx->error = errnum;
        ok = (errnum == X509_V_OK);
    }
  
    if (ok != 1)
    {
        Log( L_INFO, "grid_verify_callback: error message=%s",
                     X509_verify_cert_error_string (errnum));
    }

    return ok;
}


/******************************************************************************
Function:   grid_verifyCert
Description:
    Validates a certificate with the CRL list and the signing CA
******************************************************************************/
unsigned long grid_verifyCert( char * CA_DIR, STACK_OF(X509) *certstack )
{
    char           *oper = "Verifying certificate chain";

    X509_STORE     *store      = NULL;
    X509_LOOKUP    *lookup     = NULL;
    X509_STORE_CTX *verify_ctx = NULL;
    X509           *cert       = NULL;
    char           *cert_DN;
    char           *issuer_DN;
    int             i = 0;
    int             depth      = sk_X509_num( certstack );
    unsigned long   ret        = X509_V_OK;

    Log( L_DEBUG, "--- Welcome to the grid_verifyCert function ---");

    /* Initials must be good */
    if ( CA_DIR == NULL )
    {
        Error( oper, "No CA certificate directory specified." );
        return X509_V_ERR_APPLICATION_VERIFICATION;
    }
    if ( certstack == NULL )
    {
        Error( oper, "Certificate stack is empty." );
        return X509_V_ERR_APPLICATION_VERIFICATION;
    }

    Log( L_INFO, "Using CA Directory: %s", CA_DIR);

    Log( L_DEBUG, "X509_STORE_new");
    if (!(store = X509_STORE_new()))
    {
       Error( oper, "Could not create a X509 STORE." );
       return ERR_get_error();
    }

    Log( L_DEBUG, "X509_STORE_set_verify_cb_func");
    X509_STORE_set_verify_cb_func (store, grid_X509_verify_callback);
    
    /* Executing the lookups to the CA and CRL files */
    Log( L_DEBUG, "X509_STORE_load_locations");
    if (X509_STORE_load_locations (store, NULL, CA_DIR) != 1)
    {
        Error( oper, "Could not load the CA directory.");
        return ERR_get_error();
    }

    Log( L_DEBUG, "X509_STORE_set_default_paths");
    if (X509_STORE_set_default_paths(store) != 1)
    {
        Error( oper, "Could not load the system wide CA certificates.");
        return ERR_get_error();
    }

    Log( L_DEBUG, "X509_STORE_add_lookup");
    if (!(lookup = X509_STORE_add_lookup (store, X509_LOOKUP_hash_dir())))
    {
        Error( oper, "Could not create X509_LOOKUP object.");
        return ERR_get_error();
    }

    Log( L_DEBUG, "X509_LOOKUP_add_dir");
    i=X509_LOOKUP_add_dir (lookup, CA_DIR, X509_FILETYPE_PEM);
    if (!i)
    {
        Error( oper, "Coult not add CA_DIR.");
        return ERR_get_error();
    }

    Log( L_DEBUG, "X509_STORE_set_flags");

    store->check_issued = grid_X509_check_issued_wrapper;
#if OPENSSL_VERSION_NUMBER < 0x00908000L
    X509_STORE_set_flags( store, X509_V_FLAG_CRL_CHECK | 
                                 X509_V_FLAG_CRL_CHECK_ALL );
#else
    X509_STORE_set_flags( store, X509_V_FLAG_CRL_CHECK |
                                 X509_V_FLAG_CRL_CHECK_ALL |
                                 X509_V_FLAG_ALLOW_PROXY_CERTS );
#endif

    Log( L_DEBUG, "X509_STORE_CTX_new");
    /* Creating a verification context and initialize it */
    if (!(verify_ctx = X509_STORE_CTX_new()))
    {
        Error( oper, "Could not create a X509 STORE CTX (context).");
        return ERR_get_error();
    }

    for (i=depth-1; i >= 0; --i) 
    {
        if ((cert = sk_X509_value(certstack, i)))
        {
            cert_DN   = X509_NAME_oneline(X509_get_subject_name(cert),NULL,0);
            issuer_DN = X509_NAME_oneline(X509_get_issuer_name(cert),NULL,0);

            Log( L_DEBUG, "DN[%d]:        %s", i, cert_DN );
            Log( L_DEBUG, "Issuer DN[%d]: %s", i, issuer_DN);

            free( cert_DN );
            free( issuer_DN );

            if (grid_x509IsCA(cert))
            {
                Log( L_DEBUG, "This certificate is a CA certificate" );
                Log( L_DEBUG, "continue search for user certificate..." );
            }
            else
            {
                break;
            }
        }
    }

    cert = sk_X509_value( certstack, 0 );
    cert_DN   = X509_NAME_oneline( X509_get_subject_name( cert ) , NULL, 0 );
    issuer_DN = X509_NAME_oneline( X509_get_issuer_name( cert )  , NULL, 0 );

    Log( L_INFO, "Certificate to verify:" );
    Log( L_INFO, "  DN:        %s", cert_DN );
    Log( L_INFO, "  Issuer DN: %s", issuer_DN );

    free( cert_DN );
    free( issuer_DN );

    Log( L_DEBUG, "X509_STORE_CTX_init" );
    if ( X509_STORE_CTX_init( verify_ctx, store, cert, certstack) != 1 )
    {
        Error( oper, "Could not initialize verification context.");
        return ERR_get_error();
    }

    X509_STORE_CTX_set_purpose( verify_ctx, X509_PURPOSE_SSL_CLIENT );
#if OPENSSL_VERSION_NUMBER >= 0x00908000L
    cert->ex_flags |= EXFLAG_PROXY;
#endif

    Log( L_DEBUG, "X509_verify");
    if ( (X509_verify_cert( verify_ctx ) ) != 1 )
    {
        ret = verify_ctx->error;
    }
    else
    {
        Log( L_INFO, "The verification of the certicate has succeeded.");
    }

    if ( verify_ctx ) X509_STORE_CTX_free( verify_ctx );
    if ( store )      X509_STORE_free( store );

    return ret;
}


/******************************************************************************
Function:   verifyPrivateyKey
Description:
    Tries to verify a privatekey with the first cert in the certstack
******************************************************************************/
unsigned long grid_verifyPrivateKey( STACK_OF(X509) *certstack, EVP_PKEY *pkey )
{
    X509 *cert = NULL;

    Log( L_DEBUG, "--- Welcome to the grid_verifyPrivateKey function ---");

    if ( pkey == NULL ) 
    {
        Log( L_WARN, "No private key available." );
        return X509_V_OK;
    }

    /* Check for X509 certificate and point to it with 'cert' */
    if ((cert = sk_X509_value(certstack, 0)) != NULL)
    {
        Log( L_DEBUG, "X509_check_private_key" );
        if ( X509_check_private_key( cert, pkey ) != 1 )
        {
            return ERR_get_error();
        }
    }

    return X509_V_OK;
}

int main( int argc, char **argv )
{
    char             *CA_dir = NULL;

    unsigned long     i, result = 0;
    char             *long_opt;
    struct stat       my_stat;
    STACK_OF(X509)   *certStack = NULL;
    EVP_PKEY         *pkey = NULL;
    int               free_fileName = 0;

    fileName = getenv( "X509_USER_PROXY" );
    if ( fileName == NULL )
    {
        fileName = calloc( 255, sizeof( char ) );
        snprintf( fileName, 255, "/tmp/x509up_u%d", getuid() );
        free_fileName = 1;
    }

    OpenSSL_add_all_algorithms();
    ERR_load_crypto_strings();

    for (i = 1; i < argc; i++)
    {
        if ( (strlen(argv[i]) >= 2 ) && ( argv[i][0] == '-' ) )
        {
            switch (argv[i][1])
            {
                case '-': long_opt = argv[i]+2;
                          if ( strcmp( long_opt, "help" ) == 0 )
                              print_usage();
                          else if ( strcmp( long_opt, "debug") == 0 )
                              log_level++;
                          else if ( strcmp( long_opt, "quiet") == 0 )
                              log_level = 0;
                          else if ( strcmp( long_opt, "version") == 0 )
                              print_version();
                          else
                          {
                              fprintf( stderr, "Unknown option: %s\n", argv[i] );
                              print_usage();
                          }
                          break;
                case 'h': print_usage();
                          break;
                case 'd': log_level++;
                          break;
                case 'q': log_level = 0;
                          break;
                case 'v': print_version();
                          break;
                default:  fprintf( stderr, "Unknown option: %s\n", argv[i] );
                          print_usage();
            }
        }
        else
        {
            if (free_fileName) free( fileName );
            free_fileName = 0;
            fileName = argv[i];
        }
    }

    /* First, find the trusted CA directory */
    CA_dir = getenv( "X509_CERT_DIR" );
    if ( CA_dir == NULL ) CA_dir = "/etc/grid-security/certificates/";

    Log ( L_DEBUG, "Testing CA directory %s", CA_dir );
    if ( (result = stat( CA_dir, &my_stat ) ) != 0 )
    {
        CA_dir = getenv( "HOME" );
        strcat( CA_dir, "/.globus/certificates/" );

        Log ( L_DEBUG, "Testing CA directory %s", CA_dir );
        result = stat( CA_dir, &my_stat );
    }
    if (result != 0 ) Log( L_WARN, "Trusted certificate directory not found!" );

    /* Check the file permissions on the proxy */
    Log( L_INFO, "Checking file permissions for %s", fileName );

    stat( fileName, &my_stat );
    if ( my_stat.st_mode & (S_IRWXG | S_IRWXO ) )
    {
        Error( "Checking file permissions",
               "should be 0600, are currently %04o.", 
                my_stat.st_mode & 0xFFF);
    }

    /* read the file and build the certificate stack, plus the proxy private key */
    result = grid_readProxy( fileName, &certStack, &pkey );
    if ( result == X509_V_OK ) 
    {
        /* first, verify the certificate chain */
        result = grid_verifyCert( CA_dir, certStack );
        if ( result == X509_V_OK )
        {
            /* still OK? then match the proxy public and private keys */
            result = grid_verifyPrivateKey( certStack, pkey );
            if ( result == X509_V_OK )
            {
                /* aaah, nirwana */
                printf( "OK\n" );
            }
            else
                Error( "Verifying private key", "%s\n",
                        ERR_reason_error_string( result ) );
        }
        else
            Error( "Verifying certificate chain", "%s\n",
                   X509_verify_cert_error_string( result ) );
    }
    else
        Error( "Reading proxy", "%s\n",
               ERR_reason_error_string( result ) );

    if (free_fileName) free( fileName );
/*
 *  sk_X509_free does NOT free the entire stack!
    sk_X509_free( certStack );
 */
    sk_X509_pop_free( certStack, X509_free );

    /* make valgrind happy */
    EVP_PKEY_free(pkey);
    ERR_remove_state(0);
    ERR_free_strings();
    EVP_cleanup();
    CRYPTO_cleanup_all_ex_data();
    
    return ( !( result == X509_V_OK ) );
}


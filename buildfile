VERSION_NUMBER = '0.3-pre'
GROUP_NAME = 'de.zib.gndms'
MF_COPYRIGHT = '2008-2010 (c) Zuse Institute Berlin (ZIB)'
TARGET = '1.5'
ENV['JAVA_OPTS'] ||= '-Xms512m -Xmx768m'

# ----------------------------------------------------------------------------
require 'buildr/gt4'
include GT4

require 'buildr/gndms'
include GNDMS

require 'buildr/groovy'

repositories.remote << 'http://www.ibiblio.org/maven2'
repositories.remote << 'http://guiceyfruit.googlecode.com/svn/repo/releases'
repositories.remote << 'http://download.java.net/maven/2'
repositories.remote << 'http://static.appfuse.org/repository'
repositories.remote << 'http://repository.jboss.org/maven2/'

OPENJPA = transitive(['org.apache.openjpa:openjpa:jar:2.0.0-beta',
                      'net.sourceforge.serp:serp:jar:1.12.0'])

require 'buildr/openjpa2'
include Buildr::OpenJPA2

GOOGLE_COLLECTIONS = transitive('com.google.collections:google-collections:jar:0.9')

GUICE = transitive('org.guiceyfruit:guice-core:jar:2.0-beta-4')

JETBRAINS_ANNOTATIONS = 'com.intellij:annotations:jar:7.0.3'

JODA_TIME = transitive('joda-time:joda-time:jar:1.6')

CXF = 'org.apache.cxf:cxf-bundle:jar:2.1.4'

JAXB = 'javax.xml.bind:jaxb-api:jar:2.2.1'

COMMONS_COLLECTIONS = transitive(['commons-collections:commons-collections:jar:3.2'])

COMMONS_CODEC = transitive(['commons-codec:commons-codec:jar:1.4'])

COMMONS_LANG = transitive(['commons-lang:commons-lang:jar:2.4'])

COMMONS_FILEUPLOAD = transitive(['commons-fileupload:commons-fileupload:jar:1.2.1'])

JETTY = ['jetty:jetty:jar:6.0.2', 'jetty:jetty-util:jar:6.0.2']

ARGS4J = 'args4j:args4j:jar:2.0.14'

TESTNG = download(artifact('org.testng:testng:jar:5.1-jdk15') => 'http://static.appfuse.org/repository/org/testng/testng/5.1/testng-5.1-jdk15.jar')

GT4_COMMONS = gt4jars(['commons-beanutils.jar', 
                       'commons-digester.jar',
                       'commons-discovery.jar',
                       'commons-pool.jar'])

GT4_LOG = gt4jars(['commons-logging.jar', 'log4j-1.2.15.jar'])

GT4_COG = gt4jars(['cog-axis.jar', 'cog-jglobus.jar', 'cog-url.jar'])

GT4_AXIS = gt4jars(['axis.jar', 'axis-url.jar'])

GT4_WSRF = gt4jars(['addressing-1.0.jar',
                    'axis-url.jar',
                    'axis.jar',
                    'commonj.jar',
                    'concurrent.jar',
                    'globus_wsrf_rft_stubs.jar',
                    'naming-common.jar',
                    'wsdl4j.jar',
                    'wsrf_common.jar',
                    'wsrf_core.jar',
                    'wsrf_core_stubs.jar',
                    'wsrf_tools.jar'])

GT4_SERVLET = gt4jars(['servlet.jar'])

GT4_SEC = gt4jars(['puretls.jar', 'opensaml.jar', 
                   'cryptix-asn1.jar', 'cryptix.jar', 'cryptix32.jar', 
                   'jce-jdk13-125.jar', 'wss4j.jar', 'jgss.jar', 
                   'globus_delegation_service.jar',
                   'globus_delegation_stubs.jar'])

GT4_XML = gt4jars(['xalan-2.6.jar', 'xercesImpl-2.7.1.jar', 'xml-apis.jar', 'xmlsec.jar'])

DB_DERBY = 'org.apache.derby:derby:jar:10.5.3.0'

desc 'Germanys Next Data Management System'
define 'gndms' do
    project.version = VERSION_NUMBER
    project.group = GROUP_NAME
    manifest['Copyright'] = MF_COPYRIGHT
    compile.options.target = TARGET

    desc 'GT4-independent Utility classes for GNDMS'
    define 'stuff', :layout => dmsLayout('stuff', 'gndms-stuff') do
       compile.with GUICE, GOOGLE_COLLECTIONS, JETBRAINS_ANNOTATIONS
       package :jar
    end

    desc 'Shared database model classes'
    define 'model', :layout => dmsLayout('model', 'gndms-model') do
       compile.with project('stuff'), COMMONS_COLLECTIONS, JODA_TIME, JETBRAINS_ANNOTATIONS, GUICE, CXF, OPENJPA
       # buildr rox!
       compile { open_jpa_enhance }
       package :jar
    end

    desc 'GT4-dependent utility classes for GNDMS'
    define 'kit', :layout => dmsLayout('kit', 'gndms-kit') do
       compile.with JETTY, COMMONS_FILEUPLOAD, COMMONS_CODEC, project('stuff'), project('model'), JETBRAINS_ANNOTATIONS, GT4_LOG, GT4_COG, GT4_AXIS, GT4_SEC, GT4_XML, JODA_TIME, ARGS4J, GUICE, GT4_SERVLET, COMMONS_LANG
       package :jar
    end

    desc 'GNDMS logic classes (actions for manipulating resources)'
    define 'logic', :layout => dmsLayout('logic', 'gndms-logic') do
       compile.with JETBRAINS_ANNOTATIONS, project('stuff'), project('model'), project('kit'), JODA_TIME, GOOGLE_COLLECTIONS, GUICE, DB_DERBY, GT4_LOG, GT4_AXIS, GT4_COG, GT4_SEC, GT4_XML, COMMONS_LANG, OPENJPA
       package :jar
    end
end


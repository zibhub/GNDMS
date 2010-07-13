# Target Platform, GT4/GNDMS have been developed against J2SE 1.5, but in case your production 
# system is 1.6 or 1.7 change this here
TARGET = '1.5'

# Large amounts of memory ensure a fast build
ENV['JAVA_OPTS'] ||= '-Xms512m -Xmx768m'


# Additional maven repositories 
repositories.remote << 'http://www.ibiblio.org/maven2'
repositories.remote << 'http://guiceyfruit.googlecode.com/svn/repo/releases'
repositories.remote << 'http://download.java.net/maven/2'
repositories.remote << 'http://static.appfuse.org/repository'
repositories.remote << 'http://repository.jboss.org/maven2/'
repositories.remote << 'http://google-maven-repository.googlecode.com/svn/repository/'


# Don't touch below unless you know what you are doing
# --------------------------------------------------------------------------------------------------

VERSION_NUMBER = '0.3-pre'
GROUP_NAME = 'de.zib.gndms'
MF_COPYRIGHT = 'Copyright 2008-2010 Zuse Institute Berlin (ZIB)'
MF_LICENSE ='This software has been licensed to you under the terms and conditions of the Apache License 2.0 (APL 2.0) only.  See META-INF/LICENSE for detailed terms and conditions.'
USERNAME = ENV['USER'].to_s

# Helper to create non-standard GNDMS sub-project layouts
require 'buildr/gndms'
include GNDMS


# Test environment
testEnv('GLOBUS_LOCATION', 'the root directory of Globus Toolkit 4.0.X (minimal requirements: GridFTP, ws-core, GRAMS)')
testEnv('ANT_HOME', 'the root directory of Apache Ant')
testEnv('JAVA_HOME', 'the root directory of J2SE ' + TARGET)
testEnv('GNDMS_SOURCE', 'the root directory of GNDMS source distribution (i.e. the toplevel directory in which the Buildfile resides)')
testEnv('USER', 'your user\'s login (your UNIX is weird)')
testTool('rsync')
testTool('curl')
testTool('openssl')
testTool('hostname')
HOSTNAME = `hostname`


# Helper to construct GT4 jar pathes
require 'buildr/gt4'
include GT4

# WSRF GT4 services to be built
SERVICES = ['GORFX', 'DSpace']
DSPACE_STUBS = file('services/DSpace/build/lib/gndms-dspace-stubs.jar')
GORFX_STUBS  = file('services/GORFX/build/lib/gndms-gorfx-stubs.jar')
SERVICE_STUBS = [GORFX_STUBS, DSPACE_STUBS]


# Groovy support is needed by the monitor
require 'buildr/groovy'


# OpenJPA is required by gndms:model
OPENJPA = transitive(['org.apache.openjpa:openjpa:jar:2.0.0-beta',
                      'net.sourceforge.serp:serp:jar:1.12.0'])

require 'buildr/openjpa2'
include Buildr::OpenJPA2


# Non-GT4 dependencies
ACTI = 'javax.activation:activation:jar:1.1.1'
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
DB_DERBY = 'org.apache.derby:derby:jar:10.5.3.0'
HTTP_CORE = ['org.apache.httpcomponents:httpcore:jar:4.0', 'org.apache.httpcomponents:httpcore-nio:jar:4.0', 'org.apache.httpcomponents:httpclient:jar:4.0.1']


# Grouped GT4 dependencies
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
GT4_XML = gt4jars(['xalan-2.6.jar', 'xercesImpl-2.7.1.jar', 'xml-apis.jar', 'xmlsec.jar', 'jaxrpc.jar'])
GT4_GRAM = gt4jars(['gram-monitoring.jar', 'gram-service.jar', 'gram-stubs.jar', 'gram-utils.jar'])


desc 'Germanys Next Data Management System'
define 'gndms' do
    project.version = VERSION_NUMBER
    project.group = GROUP_NAME
    manifest['Copyright'] = MF_COPYRIGHT
    manifest['License'] = MF_LICENSE
    compile.options.target = TARGET

    desc 'GT4-independent utility classes for GNDMS'
    define 'stuff', :layout => dmsLayout('stuff', 'gndms-stuff') do
       compile.with GUICE, GOOGLE_COLLECTIONS, JETBRAINS_ANNOTATIONS
       compile { add_build_info('stuff') }                              
       package :jar
    end

    desc 'Shared database model classes'
    define 'model', :layout => dmsLayout('model', 'gndms-model') do
       compile.with project('stuff'), COMMONS_COLLECTIONS, JODA_TIME, JETBRAINS_ANNOTATIONS, GUICE, CXF, OPENJPA
       # buildr rox!
       compile { 
        open_jpa_enhance 
        add_build_info('model')
       }
       package :jar
    end

    desc 'GT4-dependent utility classes for GNDMS'
    define 'kit', :layout => dmsLayout('kit', 'gndms-kit') do
       compile.with JETTY, COMMONS_FILEUPLOAD, COMMONS_CODEC, project('stuff'), project('model'), JETBRAINS_ANNOTATIONS, GT4_LOG, GT4_COG, GT4_AXIS, GT4_SEC, GT4_XML, JODA_TIME, ARGS4J, GUICE, GT4_SERVLET, COMMONS_LANG, OPENJPA, Buildr::Groovy::Groovyc.dependencies
       package :jar
    end

    desc 'GNDMS logic classes (actions for manipulating resources)'
    define 'logic', :layout => dmsLayout('logic', 'gndms-logic') do
       compile.with JETBRAINS_ANNOTATIONS, project('kit'), project('stuff'), project('model'), JODA_TIME, GOOGLE_COLLECTIONS, GUICE, DB_DERBY, GT4_LOG, GT4_AXIS, GT4_COG, GT4_SEC, GT4_XML, COMMONS_LANG, OPENJPA, Buildr::Groovy::Groovyc.dependencies
       package :jar
    end

    desc 'GNDMS classes for dealing with wsrf and xsd types'
    define 'gritserv', :layout => dmsLayout('gritserv', 'gndms-gritserv') do
      compile.with JETBRAINS_ANNOTATIONS, project('kit'), project('stuff'), project('model'), ARGS4J, JODA_TIME, GORFX_STUBS, OPENJPA, GT4_LOG, GT4_WSRF, GT4_COG, GT4_SEC, GT4_XML, GT4_COMMONS, COMMONS_LANG, COMMONS_COLLECTIONS
      package :jar
    end

    desc 'GNDMS core infrastructure classes'
    define 'infra', :layout => dmsLayout('infra', 'gndms-infra') do
      # Infra *must* have all dependencies since we use this list in copy/link-deps
      compile.with JETBRAINS_ANNOTATIONS, OPENJPA, project('gritserv'), project('logic'), project('kit'), project('stuff'), project('model'), ARGS4J, ACTI, SERVICE_STUBS, JODA_TIME, JAXB, GT4_SERVLET, JETTY, CXF, GOOGLE_COLLECTIONS, GUICE, DB_DERBY, GT4_LOG, GT4_WSRF, GT4_GRAM, GT4_COG, GT4_SEC, GT4_XML, JAXB, GT4_COMMONS, COMMONS_CODEC, COMMONS_LANG, COMMONS_COLLECTIONS, HTTP_CORE
      package :jar

      desc 'Symlink dependencies to $GLOBUS_LOCATION/lib'
      task 'link-deps'  => :package do
        deps = Buildr.artifacts(project('infra').compile.dependencies).map(&:to_s)
        depsFile = File.new(_('../extra/DEPENDENCIES'), 'w')
        deps.select { |jar| jar[0, GT4LIB.length] != GT4LIB }.each { |file| 
           puts 'ln_sf: \'' + file + '\' to: \'' + GT4LIB + '\''
           ln_sf(file, GT4LIB); 
           depsFile.syswrite(file + "\n") 
        }
        depsFile.close
      end

      desc 'Copy dependencies to $GLOBUS_LOCATION/lib'
      task 'copy-deps'  => :package do
        deps = Buildr.artifacts(project('infra').compile.dependencies).map(&:to_s)
        depsFile = File.new(_('../extra/DEPENDENCIES'), 'w')
        deps.select { |jar| jar[0, GT4LIB.length] != GT4LIB }.each { |file| 
           puts 'cp: \'' + file + '\' to: \'' + GT4LIB + '\''
           cp(file, GT4LIB); 
           depsFile.syswrite(file + "\n") 
        }
        depsFile.close
      end

    end

    desc 'rsync type schemata between services and types'
    task :typesync  => :package do
      SERVICES.each { |service|
        system 'rsync -aurl ' + _('../services' + service + '/build/schema/') + ' ' + _('types/')
        system 'rsync -aurl ' + _('../services' + service + '/schema/') + ' ' + _('types/')
      }
    end

    task 'package-stubs' do
      SERVICES.each { |service| 
        system 'cd ' + _('services/'+service) + ' && ant jarStubs'
      }
    end

    desc 'Create GARs for deployment (Requires packaged GNDMS and installed dependencies)'
    task :package_gars => 'package' do
      SERVICES.each { |service| 
        system 'cd ' + _('services/'+service) + ' && ant createDeploymentGar'
      }
    end
end

task 'package-stubs' => 'gndms:package_stubs' do
end

task 'install-deps' => 'gndms:infra:copy-deps' do
end

task 'copy-deps' => 'gndms:infra:copy-deps' do
end

task 'link-deps' => 'gndms:infra:link-deps' do
end

task 'package-gars' => 'gndms:package_gars' do
end

task 'pt-install' => ['package-stubs', 'install-deps', 'package-gars'] do
end

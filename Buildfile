# -*- coding: utf-8 -*-
# -*- mode: ruby -*-

# Large amounts of memory ensure a fast build
ENV['JAVA_OPTS'] ||= '-Xms512m -Xmx768m'


# Additional maven repositories 
repositories.remote << 'http://www.ibiblio.org/maven2'
repositories.remote << 'http://repo1.maven.org/maven2'
repositories.remote << 'http://repository.codehaus.org'
repositories.remote << 'http://google-gson.googlecode.com/svn/mavenrepo'
repositories.remote << 'http://guiceyfruit.googlecode.com/svn/repo/releases'
repositories.remote << 'http://download.java.net/maven/2'
repositories.remote << 'http://static.appfuse.org/repository'
repositories.remote << 'http://repository.jboss.org/maven2'
repositories.remote << 'http://google-maven-repository.googlecode.com/svn/repository'
repositories.remote << 'http://repository.jboss.org/nexus/content/groups/public'
repositories.remote << 'http://repo.marketcetera.org/maven'
repositories.remote << 'http://people.apache.org/repo/m2-incubating-repository'


# Don't touch below unless you know what you are doing
# --------------------------------------------------------------------------------------------------
#
require "open3"

VERSION_NUMBER = '0.6.1'
VERSION_NAME = 'ARTURAS'
FALLBACK_VERSION_TAG = 'release-Arturas'
GROUP_NAME = 'de.zib.gndms'
MF_COPYRIGHT = 'Copyright 2008-2011 Zuse Institute Berlin (ZIB)'
LICENSE ='This software has been licensed to you under the terms and conditions of the Apache License 2.0 (APL 2.0) only.'
MF_LICENSE="#{LICENSE}  See META-INF/LICENSE for detailed terms and conditions."
USERNAME = ENV['USER'].to_s
Open3.popen3( "git describe --tags" ) do |stdin,stdout,stderr|
    commit_id = stdout.gets
    if ( commit_id.nil? )
        VERSION_TAG = FALLBACK_VERSION_TAG 
    else 
        VERSION_TAG = commit_id.chomp
    end
end

# Yes, this project uses java
require 'buildr/java'
include Java
include Commands


# Helper to create non-standard GNDMS sub-project layouts
require './buildr/gndms'
include GNDMS


# Test environment
# if you dont have cog installed yet, download it from:
# http://dev.globus.org/wiki/CoG_JGlobus_1.8.0
#testEnv('COG_LOCATION', 'the root directory of COG 1.8.0')
COG_LOCATION=[ ENV['GNDMS_SOURCE'], 'lib', 'cog-jglobus' ].join( File::SEPARATOR )
VOLD_LOCATION=[ ENV['GNDMS_SOURCE'], 'lib', 'VolD' ].join( File::SEPARATOR )

#testEnv('GLOBUS_LOCATION', 'the root directory of Globus Toolkit 4.0.8')
#GNDMS_DB=[ ENV['GLOBUS_LOCATION'], 'etc', 'gndms_shared', 'db', 'gndms' ].join(File::SEPARATOR)
GNDMS_DB=[ '', 'tmp', 'c3grid', 'TESTDB' ].join(File::SEPARATOR)
#GNDMS_DB=[ '', 'tmp', 'c3grid', 'c3db' ].join(File::SEPARATOR)
#DEPLOY_GAR=[ ENV['GLOBUS_LOCATION'], 'bin', 'globus-deploy-gar' ].join(File::SEPARATOR)
#testEnv('ANT_HOME', 'the root directory of Apache Ant')
testEnv('JAVA_HOME', 'the root directory of J2SE')
JAVA_HOME = ENV['JAVA_HOME']
# ENV['PATH'] = File.join([ENV['JAVA_HOME'], 'bin']) + File::PATH_SEPARATOR + ENV['PATH']
SOURCE = '1.6'
TARGET = '1.6'
testEnv('GNDMS_SOURCE', 'the root directory of GNDMS source distribution (i.e. the toplevel directory in which the Buildfile resides)')
#testEnv('GNDMS_SHARED', '$GLOBUS_LOCATION/etc/gndms_shared')
#testEnv('GNDMS_MONI_CONFIG', '$GNDMS_SHARED/monitor.properties')
testEnv('USER', 'your user\'s login (your UNIX is weird)')
testTool('rsync')
testTool('curl')
testTool('openssl')
testTool('hostname')
HOSTNAME = `hostname`.split[0]

puts "GNDMS #{VERSION_NUMBER} \"#{VERSION_NAME}\" #{VERSION_TAG}"
puts MF_COPYRIGHT
puts "#{LICENSE}  Please consult doc/licensing about licensing conditions of downloaded 3rd party software."
if ENV['GNDMS_DEPS']=='skip' then 
	puts 'This run will not provide GT4 with dependencies.'
else
	if ENV['GNDMS_DEPS']=='link' then 
		puts 'This run will use symlinks to provide GT4 with dependencies.' 
	else
		puts 'This run will use file copying to provide GT4 with dependencies.'
	end
end

# ant binary to use
gndms_ant = ENV['GNDMS_SOURCE'] + '/bin/gndms-ant'

# Helper to construct GT4 jar pathes
require ENV['GNDMS_SOURCE'] + '/buildr/cog'
include COG

require ENV['GNDMS_SOURCE'] + '/buildr/vold'
include VOLD

# Essentially GT4 package management is classloading unaware crap
# Therefore we have to filter out some jars in order to avoid invalid jar-shadowing through dependencies
def skipDeps(deps) 
  deps = deps.select { |ding| !ding.include?("/commons-cli") }
  deps = deps.select { |ding| !ding.include?("/commons-logging") }
  deps = deps.select { |ding| !ding.include?("/commons-lang-2.1") }
  deps = deps.select { |ding| !ding.include?("/commons-pool") }
  deps = deps.select { |ding| !ding.include?("/commons-io") }
  return deps
end

# Non-GT4 dependencies
#SPRING_VERSION = "3.0.6.RELEASE"
SPRING_VERSION = "3.1.0.RELEASE"
SPRING_CLIENT = [
           "org.springframework:spring-aop:jar:#{SPRING_VERSION}",
           "org.springframework:spring-asm:jar:#{SPRING_VERSION}",
           "org.springframework:spring-beans:jar:#{SPRING_VERSION}",
           "org.springframework:spring-context:jar:#{SPRING_VERSION}",
           "org.springframework:spring-core:jar:#{SPRING_VERSION}",
           "org.springframework:spring-expression:jar:#{SPRING_VERSION}",
           "org.springframework:spring-oxm:jar:#{SPRING_VERSION}",
           "org.springframework:spring-web:jar:#{SPRING_VERSION}"
]
SPRING = [ 
           "org.springframework:spring-aspects:jar:#{SPRING_VERSION}",
           "org.springframework:spring-instrument:jar:#{SPRING_VERSION}",
           "org.springframework:spring-jdbc:jar:#{SPRING_VERSION}",
           "org.springframework:spring-orm:jar:#{SPRING_VERSION}",
           "org.springframework:spring-tx:jar:#{SPRING_VERSION}",
           "org.springframework:spring-webmvc:jar:#{SPRING_VERSION}",
            SPRING_CLIENT
         ] 
SPRING_SECURITY = [
    "org.springframework.security:spring-security-aspects:jar:3.1.0.RELEASE",
    "org.springframework.security:spring-security-core:jar:3.1.0.RELEASE",
    "org.springframework.security:spring-security-config:jar:3.1.0.RELEASE",
    "org.springframework.security:spring-security-web:jar:3.1.0.RELEASE"
]


ASPECTJ = [
        'org.aspectj:aspectjrt:jar:1.6.11',
        'org.aspectj:aspectjweaver:jar:1.6.11',
        'aopalliance:aopalliance:jar:1.0'
]
SERVLET = 'javax.servlet:servlet-api:jar:2.5'
INJECT  =  'javax.inject:javax.inject:jar:1'
XSTREAM = 'com.thoughtworks.xstream:xstream:jar:1.3.1'
#required by XSTREAM
CGLIB='cglib:cglib-nodep:jar:2.2'
DOM4J='dom4j:dom4j:jar:1.6.1'
JETTISON='org.codehaus.jettison:jettison:jar:1.0.1'
WSTX='org.codehaus.woodstox:wstx-asl:jar:3.2.7'
JDOM='org.jdom:jdom:jar:1.1'
XOM='xom:xom:jar:1.1'
XPP='xpp3:xpp3_min:jar:1.1.4c'
# together with STAX JODA_TIME
# JSON/Jackson
JSON=['org.codehaus.jackson:jackson-core-lgpl:jar:1.9.2', 
      'org.codehaus.jackson:jackson-mapper-lgpl:jar:1.9.2']
GSON='com.google.code.gson:gson:jar:1.6'
TESTNG="org.testng:testng:jar:6.5.2"

# logging
SLF4J = transitive( ['org.slf4j:slf4j-log4j12:jar:1.6.1', 'org.slf4j:slf4j-ext:jar:1.6.1'])

GUICE = 'com.google.code.guice:guice:jar:2.0'
#GOOGLE_COLLECTIONS = 'com.google.code.google-collections:google-collect:jar:snapshot-20080530'
GUAVA = 'com.google.guava:guava:jar:10.0.1'
GOOGLE_COLLECTIONS = GUAVA
JETBRAINS_ANNOTATIONS = 'com.intellij:annotations:jar:7.0.3'
JODA_TIME = transitive('joda-time:joda-time:jar:1.6')
CXF = 'org.apache.cxf:cxf-bundle:jar:2.1.4'
JAXB = 'javax.xml.bind:jaxb-api:jar:2.2.1'
STAX_API = 'stax:stax-api:jar:1.0.1'
STAX = 'stax:stax:jar:1.2.0'
COMMONS_COLLECTIONS = transitive(['commons-collections:commons-collections:jar:3.2'])
COMMONS_CODEC = 'commons-codec:commons-codec:jar:1.4'
COMMONS_LANG = 'commons-lang:commons-lang:jar:2.1'
#COMMONS_LOGGING = 'commons-logging:commons-logging:jar:1.1.1'
COMMONS_LOGGING = 'org.slf4j:jcl-over-slf4j:jar:1.6.3'
COMMONS_FILEUPLOAD = transitive(['commons-fileupload:commons-fileupload:jar:1.2.1'])
COMMONS_IO = transitive(['commons-io:commons-io:jar:2.0.1'])
JETTY = ['org.mortbay.jetty:jetty:jar:6.1.11', 'org.mortbay.jetty:jetty-util:jar:6.1.11']
GROOVY = ['org.codehaus.groovy:groovy:jar:1.6.9']
ARGS4J = 'args4j:args4j:jar:2.0.14'
DB_DERBY = ['org.apache.derby:derby:jar:10.5.3.0', 'org.apache.derby:derbytools:jar:10.5.3.0']

HTTP_CORE = ['org.apache.httpcomponents:httpcore:jar:4.0', 'org.apache.httpcomponents:httpcore-nio:jar:4.0', 'org.apache.httpcomponents:httpclient:jar:4.0.1']
JNA = [ 'com.sun.jna:jna:jar:3.0.9' ]

# Grouped GT4 dependencies
#GT4_COMMONS = cogjars(['commons-beanutils.jar', 
#                       'commons-digester.jar',
#                       'commons-discovery.jar',
#                       'commons-pool.jar'])
#GT4_LOG = cogjars(['commons-logging.jar', 'log4j-1.2.15.jar'])
GT4_COG = cogjars([ 'cog-jglobus-1.8.0.jar', 'cog-url-1.8.0.jar'])
#GT4_AXIS = cogjars(['axis.jar', 'axis-url.jar', 'saaj.jar'])
#GT4_WSRF = cogjars(['addressing-1.0.jar',
#                    'axis-url.jar',
#                    'axis.jar',
#                    'commonj.jar',
#                    'concurrent.jar',
#                    # 'globus_wsrf_rft_stubs.jar',
#                    'naming-common.jar',
#                    'wsdl4j.jar',
#                    'saaj.jar',
#                    'wsrf_common.jar',
#                    'wsrf_core.jar',
#                    'wsrf_core_stubs.jar',
#                    'wsrf_tools.jar'])
#GT4_SERVLET = cogjars(['servlet.jar'])
GT4_SEC = cogjars(['puretls.jar', 
                   'cryptix-asn1.jar', 'cryptix.jar', 'cryptix32.jar', 
                   'jgss.jar',
                   'jce-jdk13-131.jar'
                   ])
#GT4_XML = cogjars(['xalan-2.6.jar', 'xercesImpl-2.7.1.jar', 'xml-apis.jar', 'xmlsec.jar', 'jaxrpc.jar'])
#GT4_GRAM = cogjars(['gram-monitoring.jar', 'gram-service.jar', 'gram-stubs.jar', 'gram-utils.jar'])
GT4_GRAM = cogjars([ 'cog-jobmanager-1.8.0.jar' ])
#GT4_USEAGE = cogjars([ 'globus-usage-core.java' ])
#GT4_MDS = cogjars(['globus_wsrf_mds_aggregator.jar',
#                   'globus_wsrf_mds_aggregator_stubs.jar',
#                   'webmds-0.1-dev.jar',
#                   'wsrf_mds_index.jar',
#                   'wsrf_mds_index_stubs.jar',
#                   'wsrf_mds_trigger.jar',
#                   'wsrf_mds_trigger_stubs.jar',
#                   'wsrf_mds_usefulrp.jar',
#                   'wsrf_mds_usefulrp_schema_stubs.jar'])

GT4_COMMONS = []
GT4_LOG     = []
#GT4_AXIS = cogjars(['axis.jar', 'axis-url.jar', 'saaj.jar'])
#GT4_WSRF    = cogjars(['addressing-1.0.jar', 'axis-url.jar', 'axis.jar' ])
GT4_SERVLET = []
GT4_XML     = []
GT4_USEAGE  = []
GT4_MDS     = []

VOLD_CLIENT = voldjars([ 'VolDClient.jar' ])

XSTREAM_DEPS= [ CGLIB, DOM4J, JETTISON, WSTX, JDOM, XOM, XPP, STAX, JODA_TIME ]
# OpenJPA is required by gndms:model
OPENJPA = [ COMMONS_LANG, 'org.apache.openjpa:openjpa-all:jar:2.1.1']

# NEODATAGRAPH = transitive('org.springframework.data:spring-data-neo4j:jar:1.0.0.M2')

require './buildr/neo4j' 
include NEO4J

NEODATAGRAPH = neo4jars(['geronimo-jta_1.1_spec-1.1.1.jar',
                        'neo4j-examples-1.2.jar',
                        'neo4j-graph-algo-0.7-1.2.jar',
                        'neo4j-ha-0.5-1.2.jar',
                        'neo4j-index-1.2-1.2.jar',
                        'neo4j-kernel-1.2-1.2.jar',
                        'neo4j-lucene-index-0.2-1.2.jar',
                        'neo4j-management-1.2-1.2.jar',
                        'neo4j-online-backup-0.7-1.2.jar',
                        'neo4j-remote-graphdb-0.8-1.2.jar',
                        'neo4j-shell-1.2-1.2.jar',
                        'neo4j-udc-0.1-1.2-neo4j.jar',
                        'netty-3.2.1.Final.jar',
                        'org.apache.servicemix.bundles.jline-0.9.94_1.jar',
                        'org.apache.servicemix.bundles.lucene-3.0.1_2.jar',
                        'protobuf-java-2.3.0.jar'
])

require './buildr/openjpa2'
include Buildr::OpenJPA2

# helper for derby ij
require './buildr/derbyij'
include DERBYIJ


desc 'Germanys Next Data Management System'
define 'gndms' do
    project.version = VERSION_NUMBER
    project.group = GROUP_NAME
    manifest['Copyright'] = MF_COPYRIGHT
    manifest['License'] = MF_LICENSE
    compile.options.source = SOURCE
    compile.options.target = TARGET
    # compile.options.lint = 'all'
    meta_inf << file(_('LICENSE'))
    meta_inf << file(_('GNDMS-RELEASE'))
    test.using :testng
    @buildInfo = nil
    @releaseInfo = nil


    def updateBuildInfo()
      if (@buildInfo == nil) then
        buildFile = File.new(_('GNDMS-BUILD-INFO'), 'w')
        timestamp = Time.now.to_s
        @buildInfo = 'built-at: ' + timestamp + ' built-by: ' + USERNAME + '@' + HOSTNAME
        buildFile.syswrite(@buildInfo)
        buildFile.close
        puts '>>>> '
        puts '>>>> GNDMS_BUILD_INFO is \'' + @buildInfo + '\''
        puts '>>>> '
      end
    end

    task 'update-build-info' do updateBuildInfo() end

    def updateReleaseInfo()
      if (@releaseInfo == nil) then
        @releaseInfo = "Generation N Data Management System VERSION: #{VERSION_NUMBER} \"#{VERSION_NAME}\" #{VERSION_TAG}"
        relFile = File.new(_('GNDMS-RELEASE'), 'w')
        relFile.syswrite(@releaseInfo)
        relFile.close
        puts '>>>> '
        puts '>>>> GNDMS_RELEASE is \'' + @releaseInfo + '\''
        puts '>>>> '
      end
    end

    task 'update-release-info' do updateReleaseInfo() end

    meta_inf << file(_('GNDMS-BUILD-INFO'))


    desc 'independent utility classes for GNDMS'
    define 'stuff', :layout => dmsLayout('stuff', 'gndms-stuff') do
       compile.with INJECT, GOOGLE_COLLECTIONS, JETBRAINS_ANNOTATIONS, JSON, SPRING, SLF4J
       compile 
       test.compile
       test.using :testng
       package :jar
    end

    desc 'Shared database model classes'
    define 'model', :layout => dmsLayout('model', 'gndms-model') do
      # TODO: Better XML
      compile.with project('common'), project('stuff'), COMMONS_COLLECTIONS, COMMONS_LANG, GOOGLE_COLLECTIONS, JODA_TIME, JETBRAINS_ANNOTATIONS, INJECT, CXF, OPENJPA, JAXB, STAX_API, JSON, SLF4J
      compile { open_jpa_enhance }
      package :jar
    end

    desc 'Shared graph database model classes'
    define 'neomodel', :layout => dmsTestLayout('neomodel', 'gndms-neomodel') do
      compile.with project('common'), project('model'), project('stuff'), JETBRAINS_ANNOTATIONS, NEODATAGRAPH, JODA_TIME, OPENJPA, COMMONS_LANG
      
      test.with COMMONS_IO
      test.compile

      test.include 'de.zib.gndms.neomodel.gorfx.tests.NeoTaskFlowTypeTest'
      test.include 'de.zib.gndms.neomodel.gorfx.tests.NeoTaskTest'
      
      package :jar      
    end

    desc 'GT4-dependent utility classes for GNDMS'
    define 'kit', :layout => dmsLayout('kit', 'gndms-kit') do
      compile.with GROOVY, GOOGLE_COLLECTIONS, COMMONS_FILEUPLOAD, COMMONS_CODEC, project('common'), project('stuff'), project('model'), project('neomodel'), JETBRAINS_ANNOTATIONS, GT4_LOG, GT4_COG, GT4_SEC, GT4_XML, JODA_TIME, ARGS4J, INJECT, GT4_SERVLET, COMMONS_LANG, OPENJPA, SLF4J, JSON, SPRING, SPRING_SECURITY
      compile
      test.compile
      test.using :testng
      package :jar
    end

    desc 'GNDMS logic classes (actions for manipulating resources)'
    define 'logic', :layout => dmsLayout('logic', 'gndms-logic') do
       compile.with JETBRAINS_ANNOTATIONS, project('kit'), project('common'), project('stuff'), project('model'), project('neomodel'), JODA_TIME, GOOGLE_COLLECTIONS, INJECT, DB_DERBY, GT4_LOG, GT4_COG, GT4_SEC, GT4_XML, COMMONS_LANG, OPENJPA, SLF4J, SPRING, SPRING_SECURITY
       compile
       package :jar
    end


    desc 'GNDMS core infrastructure classes'
    define 'infra', :layout => dmsLayout('infra', 'gndms-infra') do
        # Infra *must* have all dependencies since we use this list in copy/link-deps
        compile.with JETBRAINS_ANNOTATIONS, OPENJPA, project('common'), project('logic'), project('kit'), project('stuff'), project('neomodel'), project('model'), ARGS4J, JODA_TIME, JAXB, GT4_SERVLET, GROOVY, GOOGLE_COLLECTIONS, INJECT, DB_DERBY, GT4_LOG, GT4_GRAM, GT4_COG, GT4_SEC, GT4_XML, JAXB, GT4_COMMONS, COMMONS_CODEC, COMMONS_LANG, COMMONS_COLLECTIONS, HTTP_CORE, TestNG.dependencies, COMMONS_FILEUPLOAD, NEODATAGRAPH, SLF4J, SPRING, JNA
        compile

        meta_inf << file(_('src/META-INF/00_system.xml'))
        meta_inf << file(_('src/META-INF/grid.properties'))
        meta_inf << file(_('src/META-INF/legacyConfigMeta.xml'))
        package :jar
        doc projects('gndms:stuff', 'gndms:model', 'gndms:kit', 'gndms:logic')

        # Symlink or copy all dependencies of infra + the infra jar - whatever gets filtered by skipDeps to GT4LIB
        # and log source jars used to lib/DEPENDENCIES and lib/dependencies.xml (both files are not further used by the build - FYI only)
        def installDeps(copy)
            deps = Buildr.artifacts(project('infra').compile.dependencies).map(&:to_s)
            deps << project('infra').package.to_s
            deps = skipDeps(deps)

            classpathFile = File.new(GT4LIB + '/gndms-dependencies.xml', 'w')
            classpathFile.syswrite('<?xml version="1.0"?>' + "\n" + '<project><target id="setGNDMSDeps"><path id="service.build.extended.classpath">' + "\n")
            depsFile = File.new(GT4LIB + '/gndms-dependencies', 'w')
            deps.select { |jar| jar[0, GT4LIB.length] != GT4LIB }.each { |file| 
                basename = File.basename( file )
                newname = GT4LIB+'/'+basename
                if (copy)
                    puts 'cp: \'' + file + '\' to: \'' + newname + '\''
                    cp(file, newname)
                    puts 'yay'
                    chmod 0644, newname
                else
                    puts 'ln_sf: \'' + file + '\' to: \'' + newname + '\''
                    chmod 0644, file
                    ln_sf(file, newname)
                end
                depsFile.syswrite(basename + "\n") 
                classpathFile.syswrite('<pathelement location="' + basename + '" />' + "\n")
            }
            depsFile.close
            classpathFile.syswrite('</path></target></project>' + "\n\n")
            classpathFile.close
        end

        desc 'Install dependencies to $GLOBUS_LOCATION/lib (execute as globus user)'
        task 'install-deps' => 'artifacts' do
            if (ENV['GNDMS_DEPS'] != 'skip') then
                installDeps(ENV['GNDMS_DEPS']!='link')
            end
            # Fix monitor.properties permissions
            system "test -d '#{ENV['GNDMS_SHARED']}' || mkdir '#{ENV['GNDMS_SHARED']}'"
            propsFile = [ ENV['GNDMS_SHARED'], 'monitor.properties' ].join(File::SEPARATOR)
            system "touch '#{propsFile}'"
            system "chmod 0600 '#{propsFile}'"
        end

    end


    desc 'Peek into the gndms derby database'
    task 'derby-ij' do
        if( ENV["dbDir"] == nil ) then
            dbDir=GNDMS_DB 
        else 
            dbDir = ENV["dbDir"]
        end 
        puts "DB dir: #{dbDir}"
        callIJ( dbDir, DB_DERBY )
    end


    desc 'Common gndms service classes'
    define 'common', :layout => dmsLayout('common', 'gndms-common') do
        compile.with project( 'stuff' ), JETBRAINS_ANNOTATIONS, SPRING, ARGS4J, JODA_TIME, SLF4J, JSON
        compile
        meta_inf << file(_('src/META-INF/converter-setup.xml'))
        package :jar
    end

    desc 'REST client classes'
    define 'gndmc-rest', :layout => dmsLayout('gndmc-rest', 'gndms-gndmc-rest') do
        compile.with project('stuff'), project('common'), SPRING, ARGS4J, JODA_TIME, SLF4J, COMMONS_LOGGING, XSTREAM, XSTREAM_DEPS, JSON, ASPECTJ, TESTNG
        meta_inf << file(_('src/META-INF/client-context.xml'))
        package(:jar).with :manifest=>manifest.merge( 'Main-Class'=>'de.zib.gndms.gndmc.gorfx.GORFXClientMain' )

        task 'run' do

            testEnv('GORFX_URL', 'http://<yourhost>:<and-port>/gndms/<grid-name>')
            testEnv('GNDMS_DN', 'your grid DN')
            #testEnv('GNDMS_WID', 'your whatever fuuuu')
            jars = compile.dependencies.map(&:to_s)
            jars << project('gndms:gndmc-rest')
            args = [ '-uri', ENV['GORFX_URL'], 
                '-dn', ENV['GNDMS_DN']
                #'-wid', ENV['GNDMS_WID']
            ]
            Commands.java('de.zib.gndms.gndmc.gorfx.GORFXTaskFlowExample',  args, { :classpath => jars } )
        end

        task 'run2' do

            testEnv('GORFX_URL', 'http://<yourhost>:<and-port>/gndms/<grid-name>')
            testEnv('GNDMS_DN', 'your grid DN')
            #testEnv('GNDMS_WID', 'your whatever fuuuu')
            jars = compile.dependencies.map(&:to_s)
            jars << project('gndms:gndmc-rest')
            args = [ '-uri', ENV['GORFX_URL'], 
                '-dn', ENV['GNDMS_DN']
                #'-wid', ENV['GNDMS_WID']
            ]
            Commands.java('de.zib.gndms.gndmc.gorfx.GORFXClientMain',  args, { :classpath => jars } )
        end

        task 'run3' do

            jars = compile.dependencies.map(&:to_s)
            jars << project('gndms:gndmc-rest')
            args = []
            jargs = [
                "-Xdebug", "-Xrunjdwp:transport=dt_socket,server=n,suspend=n,address=localhost:5005"
            ]
            Commands.java('de.zib.gndms.gndmc.offline.JsonTest',  args, { :classpath => jars, :java_args => jargs } )
        end

        task 'run3-nodbg' do

            jars = compile.dependencies.map(&:to_s)
            jars << project('gndms:gndmc-rest')
            args = []
            jargs = []
            Commands.java('de.zib.gndms.gndmc.offline.JsonTest',  args, { :classpath => jars, :java_args => jargs } )
        end
    end
          
    desc 'DSpace rest service'
    define 'dspace', :layout => dmsLayout('dspace', 'gndms-dspace-rest') do
        compile.with project('infra'), project('logic'), project('kit'), project('stuff'), project('neomodel'), project('model'), project('gndmc-rest'), project('common'), SPRING, SPRING_SECURITY, SLF4J, XSTREAM, COMMONS_LOGGING, COMMONS_IO, SERVLET,  CGLIB, DOM4J, JETTISON, WSTX, JDOM, XOM, XPP, STAX, JODA_TIME, OPENJPA, INJECT, JETBRAINS_ANNOTATIONS, TESTNG, ASPECTJ

        compile
        meta_inf << file(_('src/META-INF/dspace.xml'))
        package :jar
    end    
          
    desc 'GORFX rest service'
    define 'gorfx', :layout => dmsLayout('gorfx', 'gndms-gorfx-rest') do
        compile.with project('infra'), project('logic'), project('kit'), project('stuff'), project('neomodel'), project('model'), project('gndmc-rest'), project('common'), SPRING, SPRING_SECURITY, SLF4J, XSTREAM, COMMONS_LOGGING, SERVLET,  CGLIB, DOM4J, JETTISON, WSTX, JDOM, XOM, XPP, STAX, JODA_TIME, OPENJPA, INJECT, SPRING_SECURITY, TESTNG, ASPECTJ

        compile
        meta_inf << file(_('src/META-INF/gorfx.xml'))
        package :jar
    end    

    desc 'Creating the gndms war'
    define 'gndms', :layout => dmsLayout('gndms', 'gndms-rest') do

        compile.with project('stuff'), SPRING, SPRING_SECURITY, SLF4J

        package(:war).include _('src/log4j.properties'), :path=>"WEB-INF/classes"
        package(:war).include _('../LICENSE'), :path=>"WEB-INF/classes/META-INF"
        package(:war).include _('../GNDMS-RELEASE'), :path=>"WEB-INF/classes/META-INF"

        libs = []
        [ 'gorfx', 'dspace', 'infra', 'logic', 'kit', 'stuff', 'neomodel', 'model', 'gndmc-rest', 'common' ].each { |mod| 
            project( mod ).compile.dependencies.map( &:to_s ).each  { |lib| libs << lib }
            libs << project( mod ).package(:jar)
        }

        # workaround for builder dependence bug
        package(:war).enhance FileList[_(:web,  '**/*'), _( '../lib', '**/gndms-*.jar' )]
        package(:war).libs += libs.uniq
    end
end


task 'deploy-gndms-rest' do

    def mkJettyProps( src, hostname, port)
        mkProps(  src, "#{ENV['JETTY_HOME']}/gndms/", hostname, port )
    end 

    def mkProps( src, tgt, hostname, port)
        props = eval IO.read ( "etc/#{src}" )
        propFile = File.new( "#{tgt}/#{src}" , 'w')
        propFile.write( props )
        propFile.close
    end 

    src = project('gndms:gndms').package(:war).to_s
    testEnv('JETTY_HOME', 'the root directory of your jetty installation')
    tgt = "#{ENV['JETTY_HOME']}/webapps/gndms.war"
    puts "Deploying #{src} => #{tgt}"
    cp( src, tgt );
    gndms_dir = "#{ENV['JETTY_HOME']}/gndms"
    puts "Creating #{gndms_dir}"
    if ( not File.exists?( gndms_dir ) ) 
        mkdir( "#{gndms_dir}" )
    else 
        puts "already exists. Skipping..."
    end

    hostname = `hostname -f`.chomp
    if ( ENV['GNDMS_PORT'] == nil )
        port = '8080'
    else 
        port = ENV['GNDMS_PORT'] 
    end

    mkJettyProps( 'grid.properties', hostname, port )
    mkJettyProps( 'log4j.properties', hostname, port )

    puts "installing monitor.properties to #{ENV['GNDMS_SHARED']}"
    mkProps( 'monitor.properties', "#{ENV['GNDMS_SHARED']}", hostname, port )

    puts "installing context to #{ENV['JETTY_HOME']}/contexts"
    cp( "etc/gndms.xml",  "#{ENV['JETTY_HOME']}/contexts" )

end


# Utility stuff

task 'show-log' => task('gndms:gndmc:show-log')


# Database stuff

task 'kill-db' do
    rm_rf GNDMS_DB
    puts 'ATTENTION Do not forget to call fix-permissions after you have recreated the database'
end

task 'inspect-db' => task('gndms:derby-ij') 

# Nice toplevel targets that redirect to service targets

task 'package-DSpace' => task('gndms:package-DSpace')
task 'deploy-DSpace' => task('gndms:deploy-DSpace')
task 'rebuild-DSpace' => task('gndms:rebuild-DSpace')

task 'package-GORFX' => task('gndms:package-GORFX') 
task 'deploy-GORFX' => task('gndms:deploy-GORFX') 
task 'rebuild-GORFX' => task('gndms:rebuild-GORFX') 

task 'clean-services' => task('gndms:clean-services')
task 'package-stubs' => task('gndms:package-stubs')

task 'rebuild-services' => [task('rebuild-DSpace'), task('rebuild-GORFX')] 
task 'package-services' => [task('package-DSpace'), task('package-GORFX')] 
task 'deploy-services' => [task('deploy-DSpace'), task('deploy-GORFX')] 
task 'link-services' => task('gndms:link-services') 

desc 'deployes GNMDS-rest war'
task 'deploy-rest' => ['gndms:gorfx-rest:package', 'deploy-gorfx-rest']


# Doc targets

task('clean-apidocs') do
    rm_rf [ENV['GNDMS_SOURCE'], 'doc', 'api'].join(File::SEPARATOR)
end

task('compile-apidocs') do
    task('gndms:infra:doc').invoke
end

task :apidocs => task('compile-apidocs')

# Per-Grid Targets

task 'ptgrid-setupdb' do
    system "#{ENV['GNDMS_SOURCE']}/scripts/ptgrid/setup-resource.sh CREATE"
end

task 'ptgrid-test' do
    system "#{ENV['GNDMS_SOURCE']}/scripts/ptgrid/test-resource.sh"
end

desc 'Sets up the c3 data-provider database'
task 'c3grid-dp-setupdb' do
    system "#{ENV['GNDMS_SOURCE']}/scripts/c3grid/setup-dataprovider.sh CREATE"
end

task 'c3grid-portal-setupdb' do
    system "#{ENV['GNDMS_SOURCE']}/scripts/c3grid/setup-portal.sh CREATE"
end

task 'c3grid-dp-test' => task('gndms:gndmc:run-staging-test') 

desc 'Test the c3 data-provider setup'
task 'c3grid-dp-test' => ['gndms:gndmc:run-staging-test']

  
task 'c3grid-dp-post-deploy-test' do
    host = `hostname`.chomp
    dn = `grid-proxy-info -identity`
    dn = dn.chomp
    if (ENV['GNDMS_SFR'] == nil)
      prop = 'etc/sfr/dummy-sfr.properties'
    else 
      prop = ENV['GNDMS_SFR']
    end
    # Yes, this is a hack
    cp = deployedJars()
    cp << "#{ENV['GNDMS_SOURCE']}/lib/gndmc/gndms-gndmc-#{VERSION_NUMBER}.jar"
    print cp
    args = [ '-props', prop, 
             '-uri', 'https://' + host + ':8443/wsrf/services/gndms/GORFX',
             '-dn', dn
           ]
    Commands.java('de.zib.gndmc.GORFX.c3grid.ProviderStageInClient',  args, 
                  { :classpath => cp,
                    :properties => { "axis.ClientConfigFile" => ENV['GLOBUS_LOCATION'] + "/client-config.wsdd" } } )
end

# Main targets

desc 'Install missing dependencies (execute as globus user)'
task 'install-deps' => 'gndms:infra:install-deps'

desc 'Do a full rebuild'
task 'rebuild' => ['clean', 'clean-services', 'gndms:stuff:package', 'package-stubs', 'gndms:infra:package', 'install-deps', 'package-services', 'gndms:gndmc:package']

desc 'Do a full rebuild and deploy (execute as globus user)'
task 'redeploy' => ['clean', 'clean-services', 'gndms:stuff:package', 'package-stubs', 'gndms:infra:package', 'install-deps', 'package-DSpace', 'deploy-DSpace', 'package-GORFX', 'deploy-GORFX', 'gndms:gndmc:package'] do
end

desc 'Do a full release build and deploy (execute as globus user)'
task 'release-build' => ['gndms:update-release-info', 'redeploy' ]

desc 'Build all docs'
task 'build-docs' => ['apidocs']

desc 'Install and deploy a release build'
task 'install-distribution' => ['install-deps', 'deploy-DSpace', 'deploy-GORFX']

task 'fix-permissions' do
    system "#{ENV['GNDMS_SOURCE']}/scripts/internal/fix-permissions.sh"
end


desc 'Test REST setup'
task 'restTest' => task( 'gndms:rest:package' )

task 'artifcats' => ['artifacts']

def hasPath?(path)
    return ( File.exists?(path) or File.symlink?(path) )
end

desc 'Guesses the previous installed version and removes it' 
task 'auto-clean' do
    puts 'Guessing installed version...'
    path = "#{ENV['GLOBUS_LOCATION']}/lib/"
    if( hasPath?( "#{path}gndms-shared-model.jar" ) )   
        puts 'GNDMS 0.2.8 detected.'
        cleanRev( '0.2.8' )
    elsif( hasPath?( "#{path}gndms-model-0.3.0.jar" ) )
        puts 'GNDMS 0.3.0 detected.'
        cleanRev( '0.3.0' )
    elsif( hasPath?( "#{path}gndms-model-0.3.2.jar" ) )
        puts 'GNDMS 0.3.2 detected.'
        cleanRev( '0.3.2' )
    elsif( hasPath?( "#{path}gndms-model-0.3.3.jar" ) )
        puts 'GNDMS 0.3.3 detected.'
        cleanRev( '0.3.3' )
    elsif( hasPath?( "#{path}gndms-model-0.3.4.jar" ) )
        puts 'GNDMS 0.3.4 detected.'
        cleanRev( '0.3.4' )
    else
        puts 'No previously installed version detected.'
   end
    puts 'About to remove old c3grid service directories (if existing)'
    rm_rf( "#{ENV['GLOBUS_LOCATION']}/lib/c3grid_DSpace" )
    rm_rf( "#{ENV['GLOBUS_LOCATION']}/etc/gpt/packages/c3grid_DSpace" )
    rm_rf( "#{ENV['GLOBUS_LOCATION']}/lib/c3grid_GORFX" )
    rm_rf( "#{ENV['GLOBUS_LOCATION']}/etc/gpt/packages/c3grid_GORFX" )
    rm_rf( "#{ENV['GLOBUS_LOCATION']}/lib/c3grid_WHORFX" )
    rm_rf( "#{ENV['GLOBUS_LOCATION']}/etc/gpt/packages/c3grid_WHORFX" )
    puts 'About to remove old gndms service directories (if existing)'
    rm_rf( "#{ENV['GLOBUS_LOCATION']}/lib/gndms_DSpace" )
    rm_rf( "#{ENV['GLOBUS_LOCATION']}/etc/gpt/packages/gndms_DSpace" )
    rm_rf( "#{ENV['GLOBUS_LOCATION']}/lib/gndms_GORFX" )
    rm_rf( "#{ENV['GLOBUS_LOCATION']}/etc/gpt/packages/gndms_GORFX" )
    rm_rf( "#{ENV['GLOBUS_LOCATION']}/lib/gndms_WHORFX" )
    rm_rf( "#{ENV['GLOBUS_LOCATION']}/etc/gpt/packages/gndms_WHORFX" )
end


task 'clean-0.2.8' do
    cleanRev( '0.2.8' )
end

task 'clean-0.3.0' do
    cleanRev( '0.3.0' )
end

task 'clean-0.3.2' do
    cleanRev( '0.3.2' )
end

task 'clean-0.3.3' do
    cleanRev( '0.3.3' )
end

task 'clean-0.3.4' do
    cleanRev( '0.3.4' )
end


def cleanRev( version )
    IO.foreach( "#{ENV['GNDMS_SOURCE']}/buildr/#{version}/files" )  { |block|
        fn = eval( '"'+block+'"' ).chomp
        puts "Removing #{fn}" if( hasPath?( fn ) )  
        File.delete( fn ) if( hasPath?( fn ) )  
    }
end 

desc 'checks if the list of repositories is up to date'
task 'check-repos' do
    for x in repositories.remote do
        puts "\ncurl #{x}"

        system "curl --write-out %{http_code} --silent --output /dev/null #{x}/"
    end 
end


def nope()
     puts ''
     puts 'Please read the documentation on how to build, install, and deploy this software (doc/html or doc/md).'
     puts 'The installation of GNDMS is considerably easy, but not straightforward.'
     puts ''
end


def genScript( scriptName, runClass, args, jars, props )
    script = File.new(_( scriptName ), 'w')
    script.write( "#!/bin/bash\n\n" )
    script.write( "args=( " );
    args.each { |x| script.write( "\"#{x}\" " ) }
    script.write( ")\n\n")
    jars.each { |x| script.write( "cp=\"#{x}:$cp\"\n") } 
    script.write( "\n" )
    script.write( "props=( " )
    props.each { |k, v| script.write( "\"-D#{k}=#{v}\" " ) }
    script.write( ")\n\n" )
    script.write( "exec java -cp $cp ${props[@]} #{runClass} ${args[@]}\n" )

    script.close
end

def runJava( scriptName, runner, args, jars, props={} ) 
    unless ENV["GEN_SCRIPT"] == "1"
        Commands.java( runner,  args, 
                      { :classpath => jars, :properties => props } )
    else
        raise Exception.new( "#{_( scriptName )} already exists. Please remove and try again." ) if File.exists?( _( scriptName ) )
        genScript( scriptName, runner, args, jars, props )
    end
end

Rake::Task[:default].prerequisites.clear
Rake::Task[:release].prerequisites.clear
Rake::Task[:release].clear
Rake::Task[:install].prerequisites.clear
Rake::Task[:install].clear
Rake::Task[:package].prerequisites.clear
Rake::Task[:package].clear

task :default => task( 'gndms:gorfx:package' )
task :install => task( 'deploy-gndms-rest' )
task :package => task( 'gndms:gndms:package' )

#todo for release use the following :default behaviour
#task :default do nope end
#task :install do nope() end
task :release do nope() end

desc 'try some features of buildr'
task 'sandbox' do 

    puts "generates the projects atom name"
    puts project( 'gndms' ).project('common').inspect
    puts

    puts "lists all packages from a project"
    puts project('gndms').project('gorfx').packages.first
    puts

    puts "echos the full path to the jar created by given project"
    puts project('gndms').project('common').packages.select { |pkg| pkg.type == :jar }
    puts

    puts "lists all sub-projects"
    puts project( 'gndms' ).projects
    puts

    puts "lists all sub-projects base dirs"
    puts project( 'gndms' ).projects.map( &:base_dir ) 
    puts
    
    puts "lists all sub-projects name in uppercase "
    list = project( 'gndms' ).projects.map( &:name ).each { |name| puts name.split(":")[1].upcase.tr( "-", "_") }
    puts

    puts "the parent package name"
    puts project('gndms:stuff').parent.name
    puts
end

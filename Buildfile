# -*- mode: ruby -*-
# Large amounts of memory ensure a fast build
ENV['JAVA_OPTS'] ||= '-Xms512m -Xmx768m'


# Additional maven repositories 
repositories.remote << 'http://www.ibiblio.org/maven2'
repositories.remote << 'http://people.apache.org/repo/m2-incubating-repository'
repositories.remote << 'http://guiceyfruit.googlecode.com/svn/repo/releases'
repositories.remote << 'http://download.java.net/maven/2'
repositories.remote << 'http://static.appfuse.org/repository'
repositories.remote << 'http://repository.jboss.org/maven2'
repositories.remote << 'http://google-maven-repository.googlecode.com/svn/repository'


# Don't touch below unless you know what you are doing
# --------------------------------------------------------------------------------------------------

VERSION_NUMBER = '0.6.0-pre'
VERSION_NAME = 'RESTIFY'
GROUP_NAME = 'de.zib.gndms'
MF_COPYRIGHT = 'Copyright 2008-2011 Zuse Institute Berlin (ZIB)'
LICENSE ='This software has been licensed to you under the terms and conditions of the Apache License 2.0 (APL 2.0) only.'
MF_LICENSE="#{LICENSE}  See META-INF/LICENSE for detailed terms and conditions."
USERNAME = ENV['USER'].to_s

# Yes, this project uses java
require 'buildr/java'
include Java
include Commands

# Helper to create non-standard GNDMS sub-project layouts
require 'buildr/gndms'
include GNDMS


# Test environment
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

puts "GNDMS #{VERSION_NUMBER} \”#{VERSION_NAME}\""
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
require 'buildr/gt4'
include GT4

# Essentially GT4 package management is classloading unaware crap
# Therefore we have to filter out some jars in order to avoid invalid jar-shadowing through dependencies
def skipDeps(deps) 
  deps = deps.select { |ding| !ding.include?("/commons-cli") }
  deps = deps.select { |ding| !ding.include?("/commons-logging") }
  deps = deps.select { |ding| !ding.include?("/commons-lang-2.1") }
  deps = deps.select { |ding| !ding.include?("/commons-pool") }
  return deps
end

# Non-GT4 dependencies
SPRING_VERSION = "3.0.5.RELEASE"
SPRING = [ 
           "org.springframework:spring-asm:jar:#{SPRING_VERSION}",
           "org.springframework:spring-core:jar:#{SPRING_VERSION}",
           "org.springframework:spring-beans:jar:#{SPRING_VERSION}",
           "org.springframework:spring-context:jar:#{SPRING_VERSION}",
           "org.springframework:spring-expression:jar:#{SPRING_VERSION}",
           "org.springframework:spring-oxm:jar:#{SPRING_VERSION}",
           "org.springframework:spring-web:jar:#{SPRING_VERSION}",
           "org.springframework:spring-webmvc:jar:#{SPRING_VERSION}",
         ] 
SERVLET = 'javax.servlet:servlet-api:jar:2.5'
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
JSON=['org.codehaus.jackson:jackson-core-lgpl:jar:1.6.4', 
      'org.codehaus.jackson:jackson-mapper-lgpl:jar:1.6.4']

# logging
SLF4J = transitive( ['org.slf4j:slf4j-log4j12:jar:1.5.8'])

JETBRAINS_ANNOTATIONS = 'com.intellij:annotations:jar:7.0.3'
JODA_TIME = transitive('joda-time:joda-time:jar:1.6')
CXF = 'org.apache.cxf:cxf-bundle:jar:2.1.4'
JAXB = 'javax.xml.bind:jaxb-api:jar:2.2.1'
STAX_API = 'stax:stax-api:jar:1.0.1'
STAX = 'stax:stax:jar:1.2.0'
COMMONS_COLLECTIONS = transitive(['commons-collections:commons-collections:jar:3.2'])
COMMONS_CODEC = 'commons-codec:commons-codec:jar:1.4'
COMMONS_LANG = 'commons-lang:commons-lang:jar:2.1'
COMMONS_LOGGING = 'commons-logging:commons-logging:jar:1.1.1'
COMMONS_FILEUPLOAD = transitive(['commons-fileupload:commons-fileupload:jar:1.2.1'])
JETTY = ['org.mortbay.jetty:jetty:jar:6.1.11', 'org.mortbay.jetty:jetty-util:jar:6.1.11']
GROOVY = ['org.codehaus.groovy:groovy:jar:1.6.9']
ARGS4J = 'args4j:args4j:jar:2.0.14'
# TESTNG = download(artifact('org.testng:testng:jar:5.1-jdk15') => 'http://static.appfuse.org/repository/org/testng/testng/5.1/testng-5.1-jdk15.jar')

HTTP_CORE = ['org.apache.httpcomponents:httpcore:jar:4.0', 'org.apache.httpcomponents:httpcore-nio:jar:4.0', 'org.apache.httpcomponents:httpclient:jar:4.0.1']

XSTREAM_DEPS= [ CGLIB, DOM4J, JETTISON, WSTX, JDOM, XOM, XPP, STAX, JODA_TIME ]
# OpenJPA is required by gndms:model
OPENJPA = [ COMMONS_LANG, 'org.apache.openjpa:openjpa-all:jar:2.0.0']

require 'buildr/openjpa2'
include Buildr::OpenJPA2

# helper for derby ij
require 'buildr/derbyij'
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
        @releaseInfo = 'Generation N Data Management System VERSION: ' + VERSION_NUMBER + ' "' + VERSION_NAME + '"'
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


    desc 'Common gndms service classes'
    define 'gndms-commons', :layout => dmsLayout('gndms-commons', 'gndms-commons') do
        compile.with SPRING, ARGS4J, JODA_TIME
        compile
        package :jar
    end

    desc 'Gorfx client classes'
    define 'gndmc-rest', :layout => dmsLayout('gndmc-rest', 'gndms-gndmc-rest') do
        compile.with project('gndms-commons'), SPRING, ARGS4J, JODA_TIME, SLF4J, COMMONS_LOGGING, XSTREAM, XSTREAM_DEPS
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

    end

    desc 'GORFX rest service'
    define 'gorfx-rest', :layout => dmsLayout('gorfx', 'gndms-gorfx-rest') do
        compile.with project('gndms-commons'), project('gndmc-rest'), SPRING, SLF4J, XSTREAM, COMMONS_LOGGING, SERVLET,  CGLIB, DOM4J, JETTISON, WSTX, JDOM, XOM, XPP, STAX, JODA_TIME
        compile

       # web_inf << file(_('../gorfx/src/META-INF/gorfx.xml'))
       # web_inf << file(_('../gorfx/src/META-INF/gorfx-mockups.xml'))
        # workaround for builder dependence bug
        package(:war).enhance FileList[_(:web,  '**/*')]
        package(:war).include _('../gorfx/src/META-INF/*'), :path=>"WEB-INF/classes/META-INF"
        package :war
    end
end


task 'deploy-gorfx-rest' do
    src = project('gndms:gorfx-rest').package(:war).to_s
    testEnv('JETTY_HOME', 'the root directory of your jetty installation')
    tgt = "#{ENV['JETTY_HOME']}/webapps/gndms.war"
    puts "deploying #{src} => #{tgt}"
    cp( src, tgt ) 
end


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

task 'artifcats' => ['artifacts']



def nope()
     puts ''
     puts 'Please read the documentation on how to build, install, and deploy this software (doc/html or doc/md).'
     puts 'The installation of GNDMS is considerably easy, but not straightforward.'
     puts ''
end

Rake::Task[:default].prerequisites.clear
Rake::Task[:release].prerequisites.clear
Rake::Task[:release].clear
Rake::Task[:install].prerequisites.clear
Rake::Task[:install].clear

task :default do nope() end
task :release do nope() end
task :install do nope() end




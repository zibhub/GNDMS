
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with this
# work for additional information regarding copyright ownership.  The ASF
# licenses this file to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
# WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
# License for the specific language governing permissions and limitations under
# the License.


require 'buildr/java'
include Java
include Commands


module DERBYIJ

    def callIJ( db_location, derby_jars ) 
        puts 'callIJ'
        #cmd_args = []
        # tools.jar contains the Java compiler.
        #dependencies << Java.tools_jar if Java.tools_jar
        #dependencies = derby_jars
        #cmd_args << '-classpath' << dependencies.join(File::PATH_SEPARATOR) unless dependencies.empty?
        #puts cmd_args << 'ij:database=jdbc:derby:' << db_location
        #puts cmd_args 
        #cmd_args_str = cmd_args.join( '' )
        #trace((['java'] + cmd_args).join(' ')
        #
        prop = 'ij.database'
        Commands.java( 'org.apache.derby.tools.ij' ,#  '-D'+prop+'=jdbc:derby:' + db_location,
                     { :classpath => derby_jars,
                       :verbose => true,
                     #  :java_args => [],
                       :properties => { prop => "jdbc:derby:" + db_location } 
                      }
                     )
        
        #ENV_JAVA[prop] = 'jdbc:derby:' + db_location
        #puts ENV_JAVA
        #Java.classpath << "/opt/gt-current/lib/derbytools-10.5.3.0.jar" << "/opt/gt-current/lib/derby-10.5.3.0.jar"
        #Java.load
        ##puts Java.classpath.join( ':' )
        #Java.org.apache.derby.tools.ij.main()
        

       # == 0 or
       #     fail 'Failed to launch java, see errors above'
    end

end

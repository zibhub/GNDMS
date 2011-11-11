module ADIS_MODULES


@@knownBuildfiles = Hash.new()

# if you like ruby please enjoy some wonderful ruby foo, if not skip
# next function to avoid a terribly headache
def setupModules( baseDir, prj )     
    unless @@knownBuildfiles.has_key?( baseDir ) 
    # use chdir to get the gndms project path right the following block
        Dir.chdir baseDir do
            eval IO.read( "buildfile" ) 
            project( prj ).projects.map( &:name ).each { |name| 
                varname = "#{prj.upcase}_#{name.split(":")[1].upcase.tr( "-", "_")}"
                package = project( name ).packages.select { |pkg| pkg.type == :jar } 
                eval "#{varname} = [\"#{package}\"]" unless package.empty?
            } 
        end
        @@knownBuildfiles[ baseDir ] = true
    end
end
end

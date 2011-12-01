module GT4

  COGLIB = ENV['COG_LOCATION'] + '/lib'

  def cogjars(jarList)
    return jarList.map { |jar| COGLIB + '/' + jar }
  end

  def deployedJars()    
    return Dir.glob("#{GT4LIB}/*.jar")
  end

end


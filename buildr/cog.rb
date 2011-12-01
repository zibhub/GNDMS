module COG

  COGLIB = ENV['COG_LOCATION'] + '/lib'

  def cogjars(jarList)
    return jarList.map { |jar| COGLIB + '/' + jar }
  end

  def deployedJars()    
    return Dir.glob("#{COGLIB}/*.jar")
  end

end


module GT4

  GT4LIB = ENV['GLOBUS_LOCATION'] + '/lib'

  def gt4jars(jarList)
    return jarList.map { |jar| GT4LIB + '/' + jar }
  end

  def deployedJars()    
    return Dir.glob("#{GT4LIB}/*.jar")
  end

end


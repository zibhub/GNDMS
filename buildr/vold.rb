module VOLD

  VOLDLIB = VOLD_LOCATION

  def voldjars(jarList)
    return jarList.map { |jar| VOLDLIB + '/' + jar }
  end

  def deployedJars()    
    return Dir.glob("#{VOLDLIB}/*.jar")
  end

end


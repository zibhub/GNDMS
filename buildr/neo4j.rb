module NEO4J

  NEO4JLIB = 'lib/neo4j-1.2'

  def neo4jars(jarList)
    return jarList.map { |jar| NEO4JLIB + '/' + jar }
  end

end

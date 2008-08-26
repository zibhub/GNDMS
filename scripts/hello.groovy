class Greet {
  def name
  def what

  Greet(who, what) { name = who[0].toUpperCase() +
                     who[1..-1]; this.what = what }
  def salute() { 
	out.println "Hello, $name!" 
	if (what.length() > 0)
		out.println "\'$what\' is groovy!"
  }
}
g = new Greet('world', args)  // create object
g.salute()                    // Output "Hello World!"

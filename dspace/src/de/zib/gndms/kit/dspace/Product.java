package de.zib.gndms.kit.dspace;

public class Product<A, B> {
	private A a;
	private B b;
	
   public Product(A a, B b) {
		this.a = a;
		this.b = b;
	}
   
   public A getFirst() {
	   return a;
   }
   
   public B getSecond() {
	   return b;
   }
}

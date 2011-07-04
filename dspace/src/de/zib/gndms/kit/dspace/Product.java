package de.zib.gndms.kit.dspace;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * A simple implementation of a product type AxB.
 * 
 * @author Ulrike Golas
 */

public class Product<A, B> {

	// The first entry
	private A a;
	// The second entry
	private B b;
	
	/**
	 * Constructs a product element (a,b) in AxB.
	 * @param a The first entry.
	 * @param b The second entry.
	 */
	public Product(A first, B second) {
		this.a = first;
		this.b = second;
	}
   
	/**
	 * Returns the first element of a product.
	 * @return The first entry.
	 */
	public A getFirst() {
	   return a;
	}
   
	/**
	 * Returns the second element of a product.
	 * @return The second entry.
	 */
	public B getSecond() {
	   return b;
	}
	
	/**
	 * Sets the first element of a product
	 * @param a The first entry.
	 */
	public void setFirst(A first) {
		this.a = first;
	}
	
	/**
	 * Sets the second element of a product.
	 * @param b The second entry.
	 */
	public void setSecond(B second) {
		this.b = second;
	}
}

package de.zib.gndms.model.gorfx.types.io;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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



import java.util.Properties;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 18.09.2008, Time: 13:46:02
 */
public abstract class AbstractPropertyReader<M> extends AbstractPropertyIO {

    private M product;
    private Class<? extends M> productClass;

    protected AbstractPropertyReader( Class<? extends M> productClass ) {
        this.productClass = productClass;
    }


    protected AbstractPropertyReader(  Class<? extends M> productClass, Properties properties ) {
        super( properties );
        this.productClass = productClass;
    }


    public abstract void read( );


    public void performReading( ) {
        begin( );
        read( );
        done( );
    }
    

    public M getProduct() {
        return product;
    }


	public void newProduct() {
		product = makeNewProduct();
	}
	
    public M makeNewProduct( )  {
        try {
            return productClass.newInstance();
        } catch ( InstantiationException e ) {
            e.printStackTrace();
	        throw new RuntimeException(e);
        } catch ( IllegalAccessException e ) {
            e.printStackTrace();
	        throw new RuntimeException(e);
        }
    }


    protected void deleteProduct( ) {

        product = null;
    }


    @Override
    public void begin( ) {
        super.begin( );
        newProduct( );
    }
}

package de.zib.gndms.model.gorfx.types.io;

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


	public M newProduct() {
		return makeNewProduct();
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

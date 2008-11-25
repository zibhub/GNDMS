package de.zib.gndms.model.gorfx.types.io;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 22.09.2008, Time: 18:09:02
 */
public abstract class GORFXConverterBase<W,M> {

    private W writer;
    private M model;


    public GORFXConverterBase() {
    }


    public GORFXConverterBase( W writer, M model ) {
        this.writer = writer;
        this.model = model;
    }


    /**
     * Performs the writing by calling the getters of the model and
     * the corresponding write* methods of the writer.
     *
     * Note: implementations of this method are required to ensure that
     * model and writer are provided and to call the open and done method of
     * the writer.
     */
    public abstract void convert( );


    public W getWriter() {
        return writer;
    }


    public void setWriter( W writer ) {
        this.writer = writer;
    }


    public M getModel() {
        return model;
    }


    public void setModel( M model ) {
        this.model = model;
    }


    public static String NotNullString( String s ) {
        return ( s == null ) ? new String( ) : s;
    }

    public static String[] NotNullStringArray( String[] sa ) {
        return ( sa == null ) ? new String[0] : sa;
    }
}

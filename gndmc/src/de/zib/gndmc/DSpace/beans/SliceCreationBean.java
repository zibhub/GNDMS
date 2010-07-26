package de.zib.gndmc.DSpace.beans;

import java.util.Properties;

/**
 * @author Maik Jorra <jorra@zib.de>
 * @version $Id$
 *          <p/>
 *          User: mjorra, Date: 26.07.2010, Time: 13:15:59
 */
public class SliceCreationBean extends SubSpaceBean {

    public final static String SLICE_KIND_URI_KEY = "DSpace.Slice.creation.sliceKindeURI";
    public final static String SIZE_KEY = "DSpace.Slice.creation.size";
    public final static String LIFE_SPAN_KEY = "DSpace.Slice.creation.lifeSpan";


    private String sliceKindURI;
    private long size;
    private long lifeSpan; //lifetime in minutes


    public String getSliceKindURI() {
        return sliceKindURI;
    }


    public void setSliceKindURI( String sliceKindURI ) {
        this.sliceKindURI = sliceKindURI;
    }


    public long getSize() {
        return size;
    }


    public void setSize( long size ) {
        this.size = size;
    }


    public long getLifeSpan() {
        return lifeSpan;
    }


    public void setLifeSpan( long lifeSpan ) {
        this.lifeSpan = lifeSpan;
    }


    @Override
    public void createExampleProperties( Properties prop ) {
        super.createExampleProperties( prop );
        prop.setProperty( SLICE_KIND_URI_KEY, "<the-key-of-the-slice-kind>" );
        prop.setProperty( SIZE_KEY, "<the-max-space-of-the-new-slice-in-byte>" );
        prop.setProperty( LIFE_SPAN_KEY, "<the-time-to-life-of-the-slice-in-minutes>" );
    }


    @Override
    public void setProperties( Properties prop ) {
        super.setProperties( prop );

        sliceKindURI = prop.getProperty( SLICE_KIND_URI_KEY );
        if( prop.contains( SIZE_KEY ) )
            size = Integer.parseInt( prop.getProperty( SIZE_KEY ) );
        if( prop.contains( LIFE_SPAN_KEY ) )
            lifeSpan = Integer.parseInt( prop.getProperty( LIFE_SPAN_KEY ) );
    }

}

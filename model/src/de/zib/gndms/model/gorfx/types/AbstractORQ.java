package de.zib.gndms.model.gorfx.types;

/**
 * An base class for model classes representing ORQ's.
 *
 * @author Maik Jorra <jorra@zib.de>
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 3:38:17 PM
 */
public abstract class AbstractORQ {

    private String offerType;
    private boolean justEstimate = false;


    protected AbstractORQ() {
    }


    protected AbstractORQ( String offerType ) {
        this.offerType = offerType;
    }


    public String getOfferType() {
        return offerType;
    }


    protected void setOfferType( String URI ) {
        this.offerType = URI;
    }


    public void setJustEstimate( boolean b ) {
        justEstimate = b;
    }


    public boolean getJustEstimate() {
        return justEstimate;
    }
}

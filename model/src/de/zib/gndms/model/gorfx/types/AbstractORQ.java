package de.zib.gndms.model.gorfx.types;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;


/**
 * An base class for model classes representing ORQ's.
 *
 * @author Maik Jorra <jorra@zib.de>
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 3:38:17 PM
 */
public abstract class AbstractORQ implements Serializable {

    private String offerType;
    private transient boolean justEstimate = false;


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


    public boolean isJustEstimate() {
        return justEstimate;
    }


    public abstract @NotNull String getDescription();
}

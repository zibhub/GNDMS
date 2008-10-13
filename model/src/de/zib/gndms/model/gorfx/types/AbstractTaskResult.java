package de.zib.gndms.model.gorfx.types;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 13.10.2008, Time: 12:35:03
 */
public class AbstractTaskResult {

    private String offerType;

    protected AbstractTaskResult() {
    }


    protected AbstractTaskResult( String offerType ) {
        this.offerType = offerType;
    }


    public String getOfferType() {
        return offerType;
    }


    protected void setOfferType( String uri ) {
        this.offerType = uri;
    }

 }

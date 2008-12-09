package de.zib.gndms.model.gorfx.types;

import de.zib.gndms.stuff.copy.Copyable;
import de.zib.gndms.stuff.copy.CopyMode;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;


/**
 * An base class for model classes representing ORQ's.
 *
 * @author Maik Jorra <jorra@zib.de>
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 3:38:17 PM
 */
@Copyable(CopyMode.SERIALIZE)
public abstract class AbstractORQ implements Serializable {
    private static final long serialVersionUID = 5782532835559987893L;
    private String offerType;
	private transient boolean justEstimate;

    private String actId;
    private HashMap<String,String> actContext;


	protected AbstractORQ() {
    }


    protected AbstractORQ( String offerTypeParam ) {
        this.offerType = offerTypeParam;
    }


    public String getOfferType() {
        return offerType;
    }


    protected void setOfferType( String URI ) {
	    offerType = URI;
    }


    public String getActId() {
        return actId;
    }


    public void setActId( String id ) {
	    actId = id;
    }


    public boolean hasId() {
        return actId != null;
    }


    public void setJustEstimate( boolean b ) {
        justEstimate = b;
    }


    public boolean isJustEstimate() {
        return justEstimate;
    }


    public abstract @NotNull String getDescription();


    @SuppressWarnings({ "ReturnOfCollectionOrArrayField" })
    public HashMap<String, String> getActContext() {
        return actContext;
    }


    public void setActContext( HashMap<String, String> context ) {
	    actContext = context;
    }


    public boolean hasContext() {
        return actContext != null;
    }
}

package de.zib.gndms.model.gorfx.types;

import org.jetbrains.annotations.NotNull;

/**
 * Created by IntelliJ IDEA.
 * User: roberto
 * Date: 13.10.2008
 * Time: 11:17:06
 */
public class SliceStageInORQ extends ProviderStageInORQ {

    private static final long serialVersionUID = 4197416353040463983L;

    private String gridSite;

    // this isn't part of the initial request, but part of the offer creation result.
    private String actGridSiteURI;


	public SliceStageInORQ( ){
        super();
        super.setOfferType( GORFXConstantURIs.SLICE_STAGE_IN_URI );
    }

    @Override
    public @NotNull String getDescription() {
        return "Slice-staging";
    }


    public void setGridSite( String gridSiteParam ){
	    gridSite = gridSiteParam;
    }


    public boolean hasGridSite(){
        return gridSite != null && gridSite.length()!=0;
    }


    public String getGridSite() {
        return gridSite;
    }


    public String getActGridSiteURI() {
        return actGridSiteURI;
    }


    public void setActGridSiteURI( String gridSiteURI ) {
	    actGridSiteURI = gridSiteURI;
    }


    public boolean hasGridSiteURI() {
        return actGridSiteURI != null;
    }
}


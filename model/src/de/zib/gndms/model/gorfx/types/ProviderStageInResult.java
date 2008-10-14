package de.zib.gndms.model.gorfx.types;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 13.10.2008, Time: 14:10:06
 */
public class ProviderStageInResult extends AbstractTaskResult {

    private static final long serialVersionUID = -3003504928510518008L;

    private String sliceKey;


    public ProviderStageInResult() {
        super( GORFXConstantURIs.PROVIDER_STAGE_IN_URI ),
    }


    public String getSliceKey() {
        return sliceKey;
    }


    public void setSliceKey( String sk ) {
        this.sliceKey = sk;
    }
}

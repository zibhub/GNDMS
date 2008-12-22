package de.zib.gndms.model.gorfx.types;

/**
 * ORQ for a slice publishing.
 *
 * ORQ consists of an source slice ref and possibly file list.
 *
 * Anyway for processing additional data like the destination slice and
 * the gsiftp addresses of the slices are required, so this ORQ requires the
 * same data like an inter-slice transfer.
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 31.10.2008, Time: 17:10:30
 */
public class RePublishSliceORQ extends InterSliceTransferORQ {

    private static final long serialVersionUID = -3698350953236158296L;

    public RePublishSliceORQ() {
        super( );
        super.setOfferType( GORFXConstantURIs.RE_PUBLISH_SLICE_URI );
    }
}

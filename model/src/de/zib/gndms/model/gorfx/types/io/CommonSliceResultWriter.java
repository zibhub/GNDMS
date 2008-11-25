package de.zib.gndms.model.gorfx.types.io;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 04.11.2008, Time: 12:40:38
 */
public interface CommonSliceResultWriter extends TaskResultWriter {

    public SliceRefWriter getSliceRefWriter( );
    public void beginWritingSliceRef( );
    public void doneWritingSliceRef( );

}
package de.zib.gndms.model.gorfx.types.io;

/**
 * Interface for writer classes of the gorfx model.
 *
 * A gndms class can be written to Stdout, it can be written as a Properties instance, or a their corresponding axis type.
 *
 * @see de.zib.gndms.model.gorfx.types.io.GORFXConverterBase
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 17.09.2008, Time: 15:27:47
 */
public interface GORFXWriterBase {

    /**
     * Defines the writer's action, before the convertion starts.
     */
    public void begin ( );

    /**
     * Defines the writer's action, after the convertion has finished.
     */
    public void done ( );
}

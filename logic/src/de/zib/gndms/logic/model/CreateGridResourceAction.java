package de.zib.gndms.logic.model;

import de.zib.gndms.model.common.GridResource;
import org.jetbrains.annotations.NotNull;

/**
 * An AbstractModelAction with an ID.
 * An ID has to be denoted before {@link #initialize()} is called.
 *
 * The first template parameter describes the model type. It must be a {@link GridResource}.
 * The second parameter is the return type of this action.
 *
 *
 * @see de.zib.gndms.logic.model.AbstractModelAction
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 13.08.2008, Time: 14:51:10
 */
public abstract class CreateGridResourceAction<M extends GridResource, R> extends AbstractModelAction<M, R> {

    private String id;


    public CreateGridResourceAction( ) {
        super();

    }


    public CreateGridResourceAction( @NotNull String idParam ) {
        super();
        id = idParam;
    }


    @Override
    public void initialize( ) {
        requireParameter("id", getId());
        super.initialize();
    }


    public String getId() {
        return id;
    }


    public void setId( @NotNull String idParam ) {
        id = idParam;
    }
}

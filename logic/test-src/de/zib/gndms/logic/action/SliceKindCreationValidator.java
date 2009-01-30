package de.zib.gndms.logic.action;

import de.zib.gndms.model.dspace.MetaSubspace;
import de.zib.gndms.model.dspace.SliceKind;
import de.zib.gndms.model.common.AccessMask;
import de.zib.gndms.logic.model.dspace.CreateSliceKindAction;
import static org.testng.AssertJUnit.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Set;
import java.util.List;

/**
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 *
 * User: mjorra, Date: 18.08.2008, Time: 17:30:31
 */
public class SliceKindCreationValidator {

    private String  URI; // must be unique
    private AccessMask mode; // must not be null
    private Set<MetaSubspace> metaSubspaces; // can be null

    private CreateSliceKindAction Action;
    private SliceKind sliceKind;


  public SliceKindCreationValidator( ) {

    }

    /**
     * Delivers a newly created action, w/o entity manager.
     */
    CreateSliceKindAction createCreateSliceAction( ) {

        Action = new CreateSliceKindAction( URI, mode );

        sliceKind =  new SliceKind( );
        Action.setModel( sliceKind );
        return Action;
    }


    void validate( SliceKind sk ) {
        assertEquals( URI, sk.getURI( ) );
        assertEquals( mode, sk.getPermission( ) );
     //   assertSame( null, sk.getMetaSubspaces( ) );
    }

    
    void validateFromDB( SliceKind sl, EntityManager em ) {

        Query q = em.createQuery( "SELECT x FROM SliceKinds x WHERE x.URI = :uriParam " );
        q.setParameter( "uriParam", URI );
        List<SliceKind> rl = (List<SliceKind>) q.getResultList();
        System.out.println( "validiate slice kind form db " );
        validate(  rl.get( 0 ) );

        q = em.createQuery( "SELECT x.permission FROM SliceKinds x WHERE x.URI = :uriParam " );
        q.setParameter( "uriParam", URI );
        AccessMask msk = (AccessMask) q.getSingleResult();
        assertEquals( mode, msk);

       // q = em.createQuery( "SELECT x.metaSubspaces FROM SliceKinds x WHERE x.URI = :uriParam " );
       // q.setParameter( "uriParam", URI );
       // List<MetaSubspace> srl = (List<MetaSubspace>) q.getResultList();
       // assertTrue( "no meta subspaces?", srl.size( ) == 0 );
    }

    
    public String getURI() {
        return URI;
    }

    public void setURI( String URI ) {
        this.URI = URI;
    }

    public AccessMask getPermission() {
        return mode;
    }

    public void setPermission( AccessMask perm ) {
        this.mode = perm;
    }

    public Set<MetaSubspace> getMetaSubspaces() {
        return metaSubspaces;
    }

    public void setMetaSubspaces( Set<MetaSubspace> metaSubspaces ) {
        this.metaSubspaces = metaSubspaces;
    }

    public CreateSliceKindAction getAction() {
        if( Action != null )
            return  createCreateSliceAction();

        return Action;
    }

    public SliceKind getSliceKind() {
        return sliceKind;
    }

    public void setSliceKind( SliceKind sliceKind ) {
        this.sliceKind = sliceKind;
    }
}

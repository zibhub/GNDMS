package de.zib.gndms.gndmc.dspace;

import de.zib.gndms.common.logic.config.Configuration;
import de.zib.gndms.common.logic.config.SetupMode;
import de.zib.gndms.logic.model.DefaultBatchUpdateAction;
import de.zib.gndms.logic.model.NoWSDontNeedModelUpdateListener;
import de.zib.gndms.logic.model.dspace.SetupSubspaceAction;
import de.zib.gndms.logic.model.dspace.SubspaceConfiguration;
import de.zib.gndms.model.common.GridResource;
import de.zib.gndms.model.common.ImmutableScopedName;
import de.zib.gndms.model.dspace.Subspace;

import javax.persistence.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.UUID;

import static org.testng.Assert.assertEquals;

/**
 * Created by IntelliJ IDEA.
 * User: explicit
 * Date: 02.12.11
 * Time: 15:31
 * To change this template use File | Settings | File Templates.
 */
public class Test extends JPATest {
    @org.testng.annotations.Test( groups = { "jpa" } )
    public void test1() {
        EntityManager em = emf.createEntityManager();

        String subspace = "sub";
        TestTable t = new TestTable();

        t.setA( "blub A" );
        t.setB( "laber B" );

        final EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(t);

            Subspace space = new Subspace();
            space.setAvailableSize(100);
            space.setGsiFtpPath("gridftppath");
            space.setPath("/var/tmp/gndms/sub");
            space.setId(UUID.randomUUID().toString());
            em.persist( space );

            SubspaceConfiguration subspaceConfig = new SubspaceConfiguration( "/var/tmp/gndms/subpath", "gridftppath", true, 100, SetupMode.CREATE, "sub" );
            SetupSubspaceAction action = new SetupSubspaceAction( subspaceConfig );

            StringWriter sw = new StringWriter();

            action.setClosingEntityManagerOnCleanup( false );
            action.setOwnEntityManager(em);
            action.setPrintWriter(new PrintWriter(sw));
            log.info("Calling action for setting up the supspace "
                    + subspace + ".");
            action.setOwnPostponedEntityActions(new DefaultBatchUpdateAction<GridResource>());
            action.getPostponedEntityActions().setListener( new NoWSDontNeedModelUpdateListener() );
            action.call();

            transaction.commit();
        }
        catch( Exception e )
        {
            e.printStackTrace();
        }
        finally {
            em.close();
        }
    }
}

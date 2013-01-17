package de.zib.gndms.taskflows.staging.server.logic;

/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import de.zib.gndms.common.rest.Specifier;
import de.zib.gndms.infra.action.SlicedTaskFlowAction;
import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.kit.security.AsFileCredentialInstaller;
import de.zib.gndms.kit.security.CredentialProvider;
import de.zib.gndms.kit.security.GetCredentialProviderFor;
import de.zib.gndms.model.dspace.Slice;
import de.zib.gndms.model.gorfx.types.TaskState;
import de.zib.gndms.model.util.TxFrame;
import de.zib.gndms.neomodel.common.Dao;
import de.zib.gndms.neomodel.common.Session;
import de.zib.gndms.neomodel.gorfx.Taskling;
import de.zib.gndms.taskflows.staging.client.ProviderStageInMeta;
import de.zib.gndms.taskflows.staging.client.model.ProviderStageInOrder;
import de.zib.gndms.taskflows.staging.client.model.ProviderStageInResult;
import org.jetbrains.annotations.NotNull;

import javax.persistence.EntityManager;
import java.io.File;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 02.10.2008 Time: 15:04:39
 */
@SuppressWarnings({ "FeatureEnvy" })
public abstract class AbstractProviderStageInAction extends SlicedTaskFlowAction<ProviderStageInOrder> {

    public static final String PROXY_FILE_NAME = File.separator + "x509_proxy.pem";

	protected StagingIOFormatHelper stagingIOHelper = new StagingIOFormatHelper();
    protected AbstractProviderStageInQuoteCalculator quoteCalculator;


    protected AbstractProviderStageInAction( @NotNull String offerTypeId ) {
        super( offerTypeId );
    }


    public AbstractProviderStageInAction( @NotNull String offerTypeId, @NotNull EntityManager em, @NotNull Dao dao, @NotNull Taskling model) {
        super( offerTypeId, em, dao, model );
    }


    @Override
    protected void onCreated(@NotNull String wid,
                             @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState) throws Exception {
        ensureOrder();
        MapConfig config = getOfferTypeConfig();
	    getScriptFileByParam(config, "stagingCommand");

        restoreSecurityContext();
        createNewSlice();
        super.onCreated(wid, state, isRestartedTask, altTaskState);
    }


    @Override
    protected void onInProgress(@NotNull String wid, @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState) throws Exception {
        ensureOrder();

        // check for quotas
        if( !isRestartedTask ) {
            checkQuotas();
        }

        final Slice sliceModel = findSlice();

        doStaging(getOfferTypeConfig(), getOrderBean(), sliceModel);
      // TODO: wieso war das auskommentiert? Wie sollte das stattdessen fuktionieren?
       changeSliceOwner( sliceModel ) ;
        transitWithPayload( createResult(), TaskState.FINISHED );
        //super.onInProgress(wid, state, isRestartedTask, altTaskState);
    }


    protected abstract void doStaging(
        final MapConfig offerTypeConfigParam, final ProviderStageInOrder orderParam,
        final Slice sliceParam );


    private ProviderStageInResult createResult() {

        Session session = getDao().beginSession();
        Specifier<Void> sliceSpec;
        try {
            sliceSpec = ( Specifier<Void> ) getTask(session).getPayload();
            session.success();
        } finally {
            session.finish();
        }

        return new ProviderStageInResult( sliceSpec );
    }


    @Override
    protected void onFailed(@NotNull String wid, @NotNull TaskState state, boolean isRestartedTask, boolean altTaskState) throws Exception {
        try {
            cancelStaging();
        }
        finally {
            killSlice();
            super.onFailed(wid, state, isRestartedTask, altTaskState);
        }
    }


	@SuppressWarnings({ "NoopMethodInAbstractClass" })
	protected void callCancel(final MapConfig offerTypeConfigParam, final ProviderStageInOrder orderParam,
            final File sliceParam) {
		// Implement in subclass
	}

	protected void cancelStaging() {
		final Slice slice;
		final File sliceDir;

		final EntityManager em = getEntityManager();
		final TxFrame txf = new TxFrame(em);
		try {
			slice = findSlice();
			if (slice == null)
				// MAYBE log this somewhere?
				return;
			sliceDir = new File(slice.getSubspace().getPathForSlice(slice));
			txf.commit();
		}
		finally { txf.finish(); }
		// potentially unsafe but do not want to keep the transaction open
		// we just dont change the slice dir after creation
		callCancel(getOfferTypeConfig(), getOrderBean(), sliceDir);
	}


    protected void prepareProxy( Slice slice ) {
        //final Slice slice = findSlice();
        try {
            File sliceDir = new File( slice.getSubspace().getPathForSlice( slice ) +
                                      PROXY_FILE_NAME );
            getCredentialProvider().installCredentials( sliceDir );
        } catch ( Exception e ) {
            logger.debug( "couldn't deploy credentials: ", e );
        }
    }


    protected boolean removeProxy( final String s ) {
        File f = new File( s );
        return f.exists() && f.delete();
    }


	protected ProcessBuilder createProcessBuilder(String name, File dir) {
       try {
           MapConfig opts = new MapConfig( getTaskFlowTypeConfigMapData());
		   if (opts.getOption(name, "").trim().length() == 0)
			   return null;
		   final File fileOption = getScriptFileByParam(opts, name);
		   ProcessBuilder builder = new ProcessBuilder();
			   builder.directory(dir);
		   builder.command(fileOption.getPath());
			   return builder;
       }
       catch (MandatoryOptionMissingException e) {
           throw new RuntimeException("createProcessBuilder " + (name==null?"null":name) + " " + (dir==null?"(null)":dir), e);
       }
   }


	@SuppressWarnings({ "ThrowableInstanceNeverThrown" })
	protected @NotNull File getScriptFileByParam(final MapConfig configParam, String scriptParam)
            throws MandatoryOptionMissingException {

		final @NotNull File scriptFile = configParam.getFileOption( scriptParam );
		if (! isValidScriptFile(scriptFile))
		    throw new IllegalArgumentException("Invalid " + scriptParam + " script: " + scriptFile.getPath());
		return scriptFile;
	}

	@Override
    @NotNull
    public Class<ProviderStageInOrder> getOrderBeanClass( ) {
        return ProviderStageInOrder.class;
    }


    @Override
    public CredentialProvider getCredentialProvider() {

        CredentialProvider credentialProvider = new GetCredentialProviderFor( getOrder(),
                ProviderStageInMeta.REQUIRED_AUTHORIZATION.get( 0 ), getMyProxyFactoryProvider()
        ).invoke();
        credentialProvider.setInstaller( new AsFileCredentialInstaller() );
        return  credentialProvider;
    }


	@SuppressWarnings({ "MethodWithMoreThanThreeNegations" })
	public static boolean isValidScriptFile(final File scriptFile) {
		// do we need "r"?
		return scriptFile != null &&
			   scriptFile.exists() && scriptFile.canRead() && scriptFile.isFile();
    }


    public AbstractProviderStageInQuoteCalculator getQuoteCalculator( ) {
        return quoteCalculator;
    }


    public void setQuoteCalculator( AbstractProviderStageInQuoteCalculator quoteCalculator ) {
        this.quoteCalculator = quoteCalculator;
    }
}


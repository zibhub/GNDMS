package de.zib.gndms.logic.model.gorfx;

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



import de.zib.gndms.kit.access.CredentialProvider;
import de.zib.gndms.kit.access.RequiresCredentialProvider;
import de.zib.gndms.kit.configlet.ConfigletProvider;
import de.zib.gndms.model.common.types.TransientContract;
import de.zib.gndms.model.common.types.factory.KeyFactory;
import de.zib.gndms.model.common.types.factory.KeyFactoryInstance;
import de.zib.gndms.model.gorfx.types.AbstractOrder;
import de.zib.gndms.neomodel.common.Dao;
import org.jetbrains.annotations.NotNull;


/**
 * @author  try ma ik jo rr a zib
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 1:43:46 PM
 */
public abstract class AbstractQuoteCalculator<M extends AbstractOrder, C extends AbstractQuoteCalculator<M, C>>
    implements KeyFactoryInstance<String,AbstractQuoteCalculator<?,?>>, RequiresCredentialProvider {

    private ConfigletProvider configletProvider;
    private TransientContract preferredOfferExecution;
    private Class<M> orqModelClass;
    private M orqArguments;
    //private NetworkAuxiliariesProvider netAux;

    private KeyFactory<String, AbstractQuoteCalculator<?, ?>> factory;
    private String offerType;
    private CredentialProvider credentialProvider;
    private Dao dao;


    public AbstractQuoteCalculator() {
    }

    
    public AbstractQuoteCalculator( Class<M> mc ) {
        orqModelClass = mc;
    }

    // here the computation of the required offer should be performed
    public abstract TransientContract createOffer( ) throws Exception;


    public TransientContract getPreferredOfferExecution() {
        return preferredOfferExecution;
    }


    public void setPreferredOfferExecution( TransientContract preferredOfferTransientContract ) {
        preferredOfferExecution = preferredOfferTransientContract;
    }


    public M getORQArguments( ) {
        return orqArguments;
    }


    public void setORQArguments( M args ) {
        orqArguments = args;
    }


    public Class<M> getORQModelClass( ) {

        return orqModelClass;
    }

    
    protected void setORQModelClass( Class<M> orqModelClassParam ) {

        orqModelClass = orqModelClassParam;
    }


    public void setJustEstimate( boolean est ) {

        orqArguments.setJustEstimate( true );
    }

    
    public boolean isJustEstimate( ) {

        return orqArguments.isJustEstimate( );
    }


    //public NetworkAuxiliariesProvider getNetAux() {
    //    return netAux;
    //}


    //public void setNetAux( NetworkAuxiliariesProvider networkAuxiliariesProvider ) {
    //    this.netAux = networkAuxiliariesProvider;
    //}


    public KeyFactory<String, AbstractQuoteCalculator<?, ?>> getFactory() {
        return factory;
    }


    public void setFactory(
            final @NotNull KeyFactory<String, AbstractQuoteCalculator<?, ?>> factoryParam) {
        factory = factoryParam;
    }


    public String getKey() {
        return offerType;
    }


    public void setKey(final String keyParam) {
        offerType = keyParam;
    }


    public ConfigletProvider getConfigletProvider() {
        return configletProvider;
    }


    public void setConfigletProvider( ConfigletProvider configletProvider ) {
        this.configletProvider = configletProvider;
    }


    public void setCredentialProvider( CredentialProvider cp ) {
        credentialProvider = cp;
    }


    public CredentialProvider getCredentialProvider() {
        return credentialProvider;
    }

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }
}

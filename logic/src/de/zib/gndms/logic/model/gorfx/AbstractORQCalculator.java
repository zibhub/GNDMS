package de.zib.gndms.logic.model.gorfx;

/*
 * Copyright 2008-2010 Zuse Institute Berlin (ZIB)
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
import de.zib.gndms.model.gorfx.OfferType;
import de.zib.gndms.model.gorfx.types.AbstractORQ;
import org.jetbrains.annotations.NotNull;


/**
 * @author  try ma ik jo rr a zib
 * @verson \$id$
 * <p/>
 * User: bzcjorra Date: Sep 5, 2008 1:43:46 PM
 */
public abstract class AbstractORQCalculator<M extends AbstractORQ, C extends AbstractORQCalculator<M, C>>
    implements KeyFactoryInstance<OfferType, AbstractORQCalculator<?, ?>>, RequiresCredentialProvider {

    private ConfigletProvider configletProvider;
    private TransientContract perferredOfferExecution;
    private Class<M> orqModelClass;
    private M orqArguments;
    //private NetworkAuxiliariesProvider netAux;

    private KeyFactory<OfferType, AbstractORQCalculator<?,?>> factory;
    private OfferType offerType;
    private CredentialProvider credentialProvider;


    public AbstractORQCalculator() {
    }

    
    public AbstractORQCalculator( Class<M> mc ) {
        orqModelClass = mc;
    }

    // here the computation of the required offer should be performed
    public abstract TransientContract createOffer( ) throws Exception;


    public TransientContract getPreferredOfferExecution() {
        return perferredOfferExecution;
    }


    public void setPerferredOfferExecution( TransientContract preferredOfferTransientContract ) {
        perferredOfferExecution = preferredOfferTransientContract;
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


    public KeyFactory<OfferType, AbstractORQCalculator<?, ?>> getFactory() {
        return factory;
    }


    public void setFactory(
            final @NotNull KeyFactory<OfferType, AbstractORQCalculator<?, ?>> factoryParam) {
        factory = factoryParam;
    }


    public OfferType getOfferTypeId() {
        return offerType;
    }


    public void setOfferTypeId(final @NotNull OfferType keyParam) {
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
}

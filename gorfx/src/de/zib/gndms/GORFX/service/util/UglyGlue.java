package de.zib.gndms.GORFX.service.util;
/*
 * Copyright 2008-2011 Zuse Institute Berlin (ZIB)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import de.zib.gndms.infra.system.PlugableTaskFlowProvider;
import de.zib.gndms.neomodel.common.Dao;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * @author Maik Jorra
 * @email jorra@zib.de
 * @date 03.02.12  12:33
 * @brief
 */
public class UglyGlue {

    private XStreamMarshaller marshaller;
    private PlugableTaskFlowProvider tfProvider;
    private Dao dao;

    @PostConstruct
    public void ductTapeIt( ) {
        marshaller.getXStream().setClassLoader( tfProvider.getCl() );
        dao.setClassLoader( tfProvider.getCl() );
    }

    public XStreamMarshaller getMarshaller() {

        return marshaller;
    }


    @Inject
    public void setMarshaller( final XStreamMarshaller marshaller ) {

        this.marshaller = marshaller;
    }


    public PlugableTaskFlowProvider getTfProvider() {

        return tfProvider;
    }


    @Inject
    public void setTfProvider( final PlugableTaskFlowProvider tfProvider ) {

        this.tfProvider = tfProvider;
    }


    public Dao getDao() {

        return dao;
    }

    @Inject
    public void setDao( final Dao dao ) {

        this.dao = dao;
    }
}

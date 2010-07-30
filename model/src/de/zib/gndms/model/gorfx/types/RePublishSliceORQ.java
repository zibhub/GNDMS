package de.zib.gndms.model.gorfx.types;

/*
 * Copyright 2008-2010 Zuse Institut Berlin (ZIB)
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

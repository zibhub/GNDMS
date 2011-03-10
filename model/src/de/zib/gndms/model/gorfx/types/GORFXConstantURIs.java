package de.zib.gndms.model.gorfx.types;

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



import java.util.Vector;


/**
 * User: bzcjorra
 * Date: Sep 4, 2008
 * Time: 2:16:57 PM
 *
 * // todo maybe replace this by an enum type.
 */
public class GORFXConstantURIs {

    public static final String FILE_TRANSFER_URI = "http://gndms.zib.de/ORQTypes/FileTransfer";
    public static final String PROVIDER_STAGE_IN_URI = "http://www.c3grid.de/ORQTypes/ProviderStageIn";
    public static final String SLICE_STAGE_IN_URI = "http://www.c3grid.de/ORQTypes/SliceStageIn";
    public static final String INTER_SLICE_TRANSFER_URI = "http://gndms.zib.de/ORQTypes/InterSliceTransfer";
    public static final String RE_PUBLISH_SLICE_URI = "http://www.c3grid.de/ORQTypes/RePublishSlice";
    public static final String LOFI_SET_STAGE_IN_URI = "http://www.c3grid.de/ORQTypes/LofiSetStageIn";
    public static final String PIN_URI = "http://www.c3grid.de/ORQTypes/Pin";
    public static final String RE_PUBLISH_LOFI_SET_URI = "http://www.c3grid.de/ORQTypes/RePublishLofiSet";

    public static final Vector<String> ALL_URIS;

    static {
        ALL_URIS = new Vector<String>();
        ALL_URIS.add( FILE_TRANSFER_URI );
        ALL_URIS.add( PROVIDER_STAGE_IN_URI );
        ALL_URIS.add( SLICE_STAGE_IN_URI );
        ALL_URIS.add( INTER_SLICE_TRANSFER_URI );
        ALL_URIS.add( RE_PUBLISH_SLICE_URI );
        ALL_URIS.add( LOFI_SET_STAGE_IN_URI  );
        ALL_URIS.add( PIN_URI );
    }

    // no task type but also important
    public static final String PUBLISH_SLICE_KIND_URI = "http://www.c3grid.de/G2/SliceKind/Publish";
}

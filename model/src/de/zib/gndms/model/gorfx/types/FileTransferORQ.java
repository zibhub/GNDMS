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



import org.jetbrains.annotations.NotNull;

import java.util.TreeMap;

/**
 * An ORQ for file transfer.
 *
 *
 * @author: Maik Jorra <jorra@zib.de>
 * @version: $Id$
 * <p/>
 * User: mjorra, Date: 29.09.2008, Time: 17:49:09
 */
public class FileTransferORQ extends AbstractORQ {

    private static final long serialVersionUID = 2695933268050863494L;

    /**
     * URI of the source files
     */
    private String sourceURI;

    /**
     * URI of the target files
     */
    private String targetURI;

    /**
     * The map consists of pairs of source and target file names.
     * The target file name may be null, if it should be identical to the source files name.
     */ 
    private TreeMap<String,String> fileMap;

    public FileTransferORQ( ) {
        super( );
        super.setOfferType( GORFXConstantURIs.FILE_TRANSFER_URI );
    }


    @Override
    public @NotNull String getDescription() {
        return "File transfer from " + sourceURI + " to " + targetURI;
    }


    public String getSourceURI() {
        return sourceURI;
    }


    public void setSourceURI( String sourceURI ) {
        this.sourceURI = sourceURI;
    }


    public String getTargetURI() {
        return targetURI;
    }


    public void setTargetURI( String targetURI ) {
        this.targetURI = targetURI;
    }


    public boolean hasFileMap( ) {
        return  fileMap != null;
    }
    

    public TreeMap<String, String> getFileMap() {
        return fileMap;
    }


    public void setFileMap( TreeMap<String, String> fileMap ) {
        this.fileMap = fileMap;
    }
}

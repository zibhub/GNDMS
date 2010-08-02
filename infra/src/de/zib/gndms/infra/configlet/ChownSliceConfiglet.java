package de.zib.gndms.infra.configlet;

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



import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.kit.configlet.DefaultConfiglet;
import de.zib.gndms.logic.action.ProcessBuilderAction;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/**
 * Configlet that stores the path to a script that is able to chown slices
 *
 * DOES NOT WORK
 * 
 * @author: try ste fan pla nti kow zib
 *
 **/
public class ChownSliceConfiglet extends DefaultConfiglet {

    String chownScript;
    ScriptKind scriptKind;

    private static final int SB_CAPACITY = 80;

    @Override
    protected synchronized void configConfig(Serializable data) {
        super.configConfig(data);
        try {
            setChownScript(getMapConfig().getOption("chownScript"));
        } catch (MandatoryOptionMissingException e) {
            throw new IllegalArgumentException(e);
        }
        setScriptKind(getMapConfig().getEnumOption(ScriptKind.class, "scriptKind", true, ScriptKind.EXEC));
    }

    public String getChownScript() {
        return chownScript;
    }

    public void setChownScript(String aChownScript) {
        chownScript = aChownScript;
    }

    public ProcessBuilderAction createChownSliceAction(String uid, String path, String sliceId) {
        switch (getScriptKind()) {
            case EXEC:
                final ProcessBuilder pb = new ProcessBuilder();
                pb.command(getChownSliceCommand(uid, path, sliceId));
                pb.directory(new File(path));
                final ProcessBuilderAction pba = new ProcessBuilderAction() {
                    @Override
                    protected void writeProcessStdIn(@NotNull BufferedOutputStream stream) throws IOException {
                        stream.close();
                    }
                };
                pba.setProcessBuilder(pb);
                pba.setOutputReceiver(new StringBuilder(SB_CAPACITY));
                pba.setErrorReceiver(new StringBuilder(SB_CAPACITY));
                return pba;
            
            default:
                throw new IllegalArgumentException("Unsupported script kind");
        }
    }

    private String[] getChownSliceCommand(String uid, String path, String sliceId) {
        String[] cmd = new String[4];
        cmd[0] = getChownScript();
        cmd[1] = uid;
        cmd[2] = path;
        cmd[3] = sliceId;
        return cmd;
    }

    public ScriptKind getScriptKind() {
        return scriptKind;
    }

    public void setScriptKind(final ScriptKind scriptKind) {
        this.scriptKind = scriptKind;
    }
}

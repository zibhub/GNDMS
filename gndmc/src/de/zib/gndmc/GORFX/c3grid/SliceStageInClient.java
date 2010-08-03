package de.zib.gndmc.GORFX.c3grid;

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



import de.zib.gndms.model.gorfx.types.SliceStageInORQ;
import de.zib.gndms.model.gorfx.types.io.SliceStageInORQStdoutWriter;
import de.zib.gndms.model.gorfx.types.io.SliceStageInORQConverter;
import de.zib.gndms.model.gorfx.types.io.SliceStageInORQPropertyReader;
import de.zib.gndms.model.gorfx.types.io.SliceStageInORQWriter;
import de.zib.gndms.gritserv.typecon.types.SliceStageInORQXSDTypeWriter;
import org.jetbrains.annotations.NotNull;
import types.DynamicOfferDataSeqT;

import java.io.IOException;
import java.util.Properties;


/**
 * ThingAMagic.
 *
 * @author  try ste fan pla nti kow zib
 * @version $Id$
 *
 *          User: stepn Date: 17.12.2008 Time: 18:10:04
 */
public class SliceStageInClient extends AbstractStageInClient {
	@SuppressWarnings({ "UseOfSystemOutOrSystemErr" })
    public static void main(String[] args) throws Exception {
		// System.out.println(System.getProperties().toString());
        SliceStageInClient cnt = new SliceStageInClient();
        cnt.run( args );

    }


    protected SliceStageInClient() {super();}


	@Override
	protected DynamicOfferDataSeqT loadSFR() throws IOException {// Load SFR from Props and convert to XML objects
		final SliceStageInORQXSDTypeWriter writer = new SliceStageInORQXSDTypeWriter();
		loadSFRFromProps(writer, sfrPropFile);
		return writer.getProduct();
	}


	protected void loadSFRFromProps(
		  final SliceStageInORQWriter orqtWriter, final String sfrPropFileParam)
		  throws IOException {
		final @NotNull Properties sfrProps = loadSFRProps(sfrPropFileParam);

		// Create XSD SliceStageInORQT from sfrPropFile
		final SliceStageInORQPropertyReader reader =
			  new SliceStageInORQPropertyReader(sfrProps);
		reader.begin();
		reader.read();
		SliceStageInORQ sfrORQ = reader.getProduct();
		SliceStageInORQConverter orqConverter =
			  new SliceStageInORQConverter(orqtWriter, sfrORQ);
		orqConverter.convert();
	}


	@Override
	protected void printSFR() throws IOException {
		System.out.println("# Staging request");
		loadSFRFromProps(new SliceStageInORQStdoutWriter(), sfrPropFile);
	}
}

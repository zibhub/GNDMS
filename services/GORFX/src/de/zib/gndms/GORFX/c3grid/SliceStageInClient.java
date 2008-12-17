package de.zib.gndms.GORFX.c3grid;

import de.zib.gndms.model.gorfx.types.SliceStageInORQ;
import de.zib.gndms.model.gorfx.types.SliceStageInORQStdoutWriter;
import de.zib.gndms.model.gorfx.types.io.SliceStageInORQConverter;
import de.zib.gndms.model.gorfx.types.io.SliceStageInORQPropertyReader;
import de.zib.gndms.model.gorfx.types.io.SliceStageInORQWriter;
import de.zib.gndms.typecon.common.type.SliceStageInORQXSDTypeWriter;
import org.jetbrains.annotations.NotNull;
import types.DynamicOfferDataSeqT;

import java.io.IOException;
import java.util.Properties;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
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

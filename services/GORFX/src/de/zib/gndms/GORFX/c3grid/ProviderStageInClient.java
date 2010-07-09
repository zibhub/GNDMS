package de.zib.gndms.GORFX.c3grid;

import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQConverter;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQPropertyReader;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQStdoutWriter;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQWriter;
import de.zib.gndms.gritserv.typecon.types.ProviderStageInORQXSDTypeWriter;
import org.jetbrains.annotations.NotNull;
import org.kohsuke.args4j.Option;
import types.DynamicOfferDataSeqT;

import java.io.IOException;
import java.util.Properties;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow <plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 15.10.2008 Time: 13:32:25
 */
@SuppressWarnings({ "UseOfSystemOutOrSystemErr" })
public class ProviderStageInClient extends AbstractStageInClient {
	@Option( name="-oidprefix", required=false, usage="oidPrefix to be stripped vom Object-Ids")
	protected String oidPrefix = "";


	@SuppressWarnings({ "UseOfSystemOutOrSystemErr" })
    public static void main(String[] args) throws Exception {
		System.out.println(System.getProperties().toString());
        ProviderStageInClient cnt = new ProviderStageInClient();
        cnt.run( args );

    }


    protected ProviderStageInClient() {super();}


	@Override
	protected DynamicOfferDataSeqT loadSFR() throws IOException {// Load SFR from Props and convert to XML objects
		final ProviderStageInORQXSDTypeWriter writer = new ProviderStageInORQXSDTypeWriter();
		loadSFRFromProps(writer, sfrPropFile);
		return writer.getProduct();
	}


	protected void loadSFRFromProps(
		  final ProviderStageInORQWriter orqtWriter, final String sfrPropFileParam)
		  throws IOException {
		final @NotNull Properties sfrProps = loadSFRProps(sfrPropFileParam);

		// Create XSD ProviderStageInORQT from sfrPropFile
		final ProviderStageInORQPropertyReader reader =
			  new ProviderStageInORQPropertyReader(sfrProps);
		reader.begin();
		reader.read();
		ProviderStageInORQ sfrORQ = reader.getProduct();
		fixOids(sfrORQ.getDataDescriptor().getObjectList());
		ProviderStageInORQConverter orqConverter =
			  new ProviderStageInORQConverter(orqtWriter, sfrORQ);
		orqConverter.convert();
	}


	@Override
	protected void printSFR() throws IOException {
		System.out.println("# Staging request");
		loadSFRFromProps(new ProviderStageInORQStdoutWriter(), sfrPropFile);
	}

	protected void fixOids(final String[] objs) {
		if (oidPrefix.length() > 0) {
			if (objs != null)
				for (int i = 0; i < objs.length; i++) {
					if (objs[i] != null && objs[i].startsWith(oidPrefix)) {
						final String obj = objs[i].trim();
						objs[i] = obj.substring(oidPrefix.length());
					}
				}
		}
	}
}
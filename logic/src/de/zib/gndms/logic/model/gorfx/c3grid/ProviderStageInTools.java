package de.zib.gndms.logic.model.gorfx.c3grid;

import de.zib.gndms.kit.util.WidAux;
import de.zib.gndms.logic.action.ProcessBuilderAction;
import de.zib.gndms.model.gorfx.types.ProviderStageInORQ;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQConverter;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQPropertyWriter;
import de.zib.gndms.model.gorfx.types.io.ProviderStageInORQWriter;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Properties;


/**
 * ThingAMagic.
 *
 * @author Stefan Plantikow<plantikow@zib.de>
 * @version $Id$
 *
 *          User: stepn Date: 27.10.2008 Time: 11:08:44
 */
public final class ProviderStageInTools {
    private ProviderStageInTools() {}


    public static Properties getSFRProps(
            final @NotNull ProviderStageInORQ orq, final Properties moreProps) {
        final Properties props = moreProps == null ? new Properties() : moreProps;
        final ProviderStageInORQWriter writer = new ProviderStageInORQPropertyWriter(props);
        final ProviderStageInORQConverter converter = new ProviderStageInORQConverter(writer, orq);
        converter.convert();
        props.put("c3grid.CommonRequest.Context.Workflow.Id", WidAux.getWid());
        return props;
    }


    public static ProcessBuilderAction createPBAction(final @NotNull ProviderStageInORQ orqParam,
                                                      final Properties moreProps) {
        final Properties props = getSFRProps(orqParam, moreProps);
        return new ProcessBuilderAction() {
            protected @Override void writeProcessStdIn(final @NotNull BufferedOutputStream stream)
                    throws IOException {
                props.store(stream, "ProviderStageIn");
	            stream.flush();
            }
        };
    }


    public static ProcessBuilderAction createPBActionForXML(final @NotNull String orq_parms ) {

        return new ProcessBuilderAction() {
            protected @Override void writeProcessStdIn(final @NotNull BufferedOutputStream stream)
                throws IOException {

                OutputStreamWriter os = new OutputStreamWriter( stream );
                os.write( orq_parms );
                os.flush( );
                os.close( );
            }
        };
    }
}
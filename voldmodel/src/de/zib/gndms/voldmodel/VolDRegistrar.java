package de.zib.gndms.voldmodel;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import de.zib.gndms.kit.config.MandatoryOptionMissingException;
import de.zib.gndms.kit.config.MapConfig;
import de.zib.gndms.stuff.threading.PeriodicalJob;

public class VolDRegistrar extends PeriodicalJob {

    private Adis adis;
    private String gorfxEP;
    private String name;
    private Type type;
    long update = 60000;
    private MapConfig config;
    
    public VolDRegistrar( final Adis adis, final String gorfxEP, final Type type, final String name) throws MandatoryOptionMissingException {
        this.adis = adis;
        this.gorfxEP = gorfxEP;
        this.type = type;
        this.name = name;
    }

    public VolDRegistrar( final Adis adis, final String gorfxEP, final Type type, final String name, final long update ) throws MandatoryOptionMissingException {
        this.adis = adis;
        this.gorfxEP = gorfxEP;
        this.type = type;
        this.name = name;
        this.update = update;
    }

    public VolDRegistrar( final Adis adis, final String gorfxEP, final Type type, final String name, final MapConfig config) throws MandatoryOptionMissingException {
        this.adis = adis;
        this.gorfxEP = gorfxEP;
        this.type = type;
        this.name = name;
        this.config = config;
        this.update = config.getLongOption( "updateInterval", 60000);
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public Long getPeriod() {
        return update;
    }


    @Override
    public void call() throws Exception {
        if (type.equals(Type.IMPORT)) {
        	adis.setImport(name, gorfxEP);
        } else if (type.equals(Type.EXPORT)) {
        	adis.setExport(name, gorfxEP);
        } else if (type.equals(Type.CPID_GRAM)) {
        	adis.setCP(name, gorfxEP);
        } else if (type.equals(Type.TRANSFER)) {
        	adis.setTransfer(name, gorfxEP);
        } else if (type.equals(Type.PUBLISHER)) {
            if( !config.hasOption( "oidPrefix" ) ) {
                throw new IllegalStateException( "Dataprovider not configured: no OID_PREFIX given." );
            }
            // register publishing site itself
            adis.setPublisher(name, gorfxEP);

            // also register OID prefix of harvested files
            final Set< String > oidPrefixe = buildSet(config.getOption("oidPrefix"));
            adis.setOIDPrefixe( gorfxEP, oidPrefixe );
        } else if (type.equals(Type.ESGF)) {
        	adis.setESGFStager(name, gndms);
        }

    }


private static Set< String > buildSet( String s ) {
    return new HashSet< String >( Arrays.asList( s.split( "(\\s|,|;)+" ) ) );
}

}

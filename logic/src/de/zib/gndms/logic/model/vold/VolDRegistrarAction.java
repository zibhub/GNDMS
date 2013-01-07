package de.zib.gndms.logic.model.vold;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import de.zib.gndms.logic.model.config.ConfigOption;
import de.zib.gndms.voldmodel.Adis;
import de.zib.gndms.voldmodel.Type;
import de.zib.gndms.voldmodel.VolDRegistrar;

public class VolDRegistrarAction {
	private VolDRegistrar registrar;
	private Adis adis;
    @ConfigOption(descr="The type of the registration site")
    private String type;
    @ConfigOption(descr="The name of the registration site")
    private String siteName;
    @ConfigOption(descr="The update interval for VolD")
    private Long updateInterval;

    @PostConstruct
    public void startVoldRegistration() throws Exception {
    	Type typ = Type.valueOf(type);
    	// TODO: where to get that from
    	String baseUrl = "";
    	registrar = new VolDRegistrar(adis, baseUrl, typ, siteName, updateInterval);
        registrar.start();
    }

    @Inject
    public void setAdis(final Adis adis) {
        this.adis = adis;
    }

    @PreDestroy
    public void stopVoldRegistration() {
        registrar.stop();
    }

}

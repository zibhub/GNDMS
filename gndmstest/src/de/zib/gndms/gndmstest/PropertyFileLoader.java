package de.zib.gndms.gndmstest;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertyFileLoader {
	
	public static Properties loadPropertyFile(String properties) throws Exception {
		Properties p = new Properties();
		p.load(new FileInputStream(new File(properties)));
		return p;
	}

}

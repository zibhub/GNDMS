package de.zib.adis;

import com.beust.jcommander.Parameter;

import de.zib.adis.abi.ABI;
import de.zib.adis.abi.ABIi;

public class Main extends ABI
{
        @Parameter( names={ "--baseurl", "-b" }, description="Base URL of VolD database", required=true )
        private String voldURL;

        @Parameter( names = { "--grid", "-g" }, description="Grid name" )
        private String grid = "c3grid";

        @Parameter( names = { "--enc", "-e" }, description="Encoding" )
        private String enc = "utf-8";

        private Main( ABIi iface, String[] args )
        {
                super( iface, args );
        }

        public static void main( String[] args )
        {
                Adis adis = new Adis();
                Main main = new Main( adis, args );

                adis.setVoldURL( main.voldURL );
                adis.setEnc( main.enc );
                adis.setGrid( main.grid );

                main.invoke();
        }
}

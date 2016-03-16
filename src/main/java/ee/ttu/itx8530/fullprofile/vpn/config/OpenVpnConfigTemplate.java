package ee.ttu.itx8530.fullprofile.vpn.config;

import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

/**
 * Created by Kasutaja on 4.03.2016.
 */
public class OpenVpnConfigTemplate {

    static Logger logger = Logger.getLogger(OpenVpnConfigTemplate.class);

    private static final MustacheFactory mf = new DefaultMustacheFactory();

    public static void compile(OpenVpnConfig vpnConfig) {
        Mustache mustache = mf.compile("openvpn_config.mustache");

        try {
            mustache.execute(new PrintWriter(System.out), vpnConfig).flush();
        } catch (IOException e) {
            logger.error("Failed to create OpenVPN configuration file from template", e);
        }
    }

}

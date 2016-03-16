package ee.ttu.itx8530.fullprofile.vpn.vpngate;

import java.io.IOException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class VPNGateURLReader {

    static Logger logger = Logger.getLogger(VPNGateURLReader.class);

    static final String API_ADDRESS = "http://www.vpngate.net/api/iphone/";

    public static String getText() {
        String result = "";
        try {
            logger.info("Querying " + API_ADDRESS);
            result = IOUtils.toString(new URL(API_ADDRESS), "UTF-8");
            logger.info("Result size from " + API_ADDRESS + " : " + result.length() + " bytes");
            // API seems not very good at quoting and escaping some values, so
            // better result = result.replaceAll("\"", "");
        } catch (IOException e) {
            logger.error("Error reading from " + API_ADDRESS, e);
        }
        logger.debug("VPNGate API (" + API_ADDRESS + ") result:\n" + result);
        return result;
    }

}

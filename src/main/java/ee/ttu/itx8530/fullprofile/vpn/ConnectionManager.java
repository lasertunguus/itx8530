package ee.ttu.itx8530.fullprofile.vpn;

import java.net.Socket;

import org.apache.log4j.Logger;

import ee.ttu.itx8530.fullprofile.vpn.tap.NoUnusedTapAdaptersException;
import ee.ttu.itx8530.fullprofile.vpn.tap.TapAdapter;
import ee.ttu.itx8530.fullprofile.vpn.tap.TapAdapterManager;

public class ConnectionManager {

    static Logger logger = Logger.getLogger(ConnectionManager.class);

    TapAdapterManager tapAdapterManager = new TapAdapterManager();

    public Connection createConnection() {
        Connection conn = null;
        try {
            TapAdapter adapter = tapAdapterManager.getFirstUnusedAdapter(false);
            conn = new Connection(adapter.getIp(), null);
        } catch (NoUnusedTapAdaptersException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return conn;
    }

    public static boolean isRemotePortAccessible(String hostName, int portNumber) {
        try {
            logger.info("Checking " + hostName + ":" + portNumber + " for availability");
            new Socket(hostName, portNumber).close();
            // remote port can be opened, this is a listening port on remote machine
            // this port is in use on the remote machine !
            logger.info(hostName + ":" + portNumber + " is accessible");
            return true;
        } catch (Exception e) {
            logger.warn(hostName + ":" + portNumber + " was not accessible");
            return false;
        }
    }

}

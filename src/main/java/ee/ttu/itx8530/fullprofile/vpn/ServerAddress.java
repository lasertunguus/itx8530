package ee.ttu.itx8530.fullprofile.vpn;

import org.apache.log4j.Logger;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class ServerAddress {
    
    Logger logger = Logger.getLogger(this.getClass());
    
    private InetSocketAddress address;
    
    public ServerAddress(String host, int port) throws UnknownHostException {
        try {
            address = new InetSocketAddress(InetAddress.getByName(host), port);
        } catch (IllegalArgumentException iae) {
            logger.error("Port for host is not valid. Must be in range 0-65535.", iae);
        }
    }
    
    public InetAddress getInetAddress() {
        return address.getAddress();
    }
    
    public InetSocketAddress getAddress() {
        return address;
    }
    
    public int getPort() {
        return address.getPort();
    }
    
    @Override
    public String toString() {
        return address.getAddress().getHostAddress() + ":" + address.getPort();
    }

}

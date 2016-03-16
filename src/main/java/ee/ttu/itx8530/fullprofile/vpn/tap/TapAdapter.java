package ee.ttu.itx8530.fullprofile.vpn.tap;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;

import org.apache.log4j.Logger;

public class TapAdapter {
    
    private Logger logger = Logger.getLogger(getClass());

    private NetworkInterface netint;
    private boolean used;

    public TapAdapter(NetworkInterface netint) {
        this.netint = netint;
        this.used = false;
    }
    
    public void bind() {
    }

    public String getName() {
        return netint.getDisplayName();
    }
    
    public NetworkInterface getInterface() {
        return netint;
    }

    public String getInterfaceName() {
        return getName().split("%")[1];
    }
    
    public String getIp() {
        // TODO probably need to loop through
        ArrayList<String> ips = new ArrayList<String>();
        for (InetAddress address : Collections.list(netint.getInetAddresses())) {
            ips.add(address.toString());
        }
        logger.info(getName() + " has addresses: " + String.join(", ", ips));
        return Collections.list(netint.getInetAddresses()).get(0).getHostName().split("%")[0];
    }
    
    public void setUsed(boolean isUsed) {
        used = isUsed;
    }
    
    public boolean isUsed() {
        return used;
    }
    
}

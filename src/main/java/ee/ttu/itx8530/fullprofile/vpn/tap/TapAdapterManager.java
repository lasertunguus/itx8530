package ee.ttu.itx8530.fullprofile.vpn.tap;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;

/**
 * From <a>https://community.openvpn.net/openvpn/wiki/HOWTO#
 * Editingtheserverconfigurationfile</a>: "If you are using Windows, each
 * OpenVPN configuration needs to have its own TAP-Windows adapter."
 * 
 * @author Kasutaja
 *
 */
public class TapAdapterManager {

    private Logger logger = Logger.getLogger(this.getClass());

    private ArrayList<TapAdapter> adapters = new ArrayList<>();

    private String displayName = "TAP-Windows Adapter V9";
    
    public TapAdapterManager() {
        initialize();
    }

    private void initialize() {
        adapters = queryTapAdapters();
    }

    public ArrayList<TapAdapter> queryTapAdapters() {
        ArrayList<TapAdapter> adapters = new ArrayList<>();
        Enumeration<NetworkInterface> nets;
        try {
            nets = NetworkInterface.getNetworkInterfaces();
            for (NetworkInterface netint : Collections.list(nets)) {
                // discard others
                if (netint.getDisplayName().contains(displayName) && netint.getInetAddresses().hasMoreElements()
                        && netint.getHardwareAddress() != null) {
                    adapters.add(parseTapAdapter(netint));
                }
            }
        } catch (SocketException e) {
            logger.error("Exception was thrown when querying TAP adapters", e);
        }
        return adapters;
    }

    private TapAdapter parseTapAdapter(NetworkInterface netint) throws SocketException {        
        return new TapAdapter(netint);
    }
    
    public TapAdapter getFirstUnusedAdapter(boolean randomize) throws NoUnusedTapAdaptersException {
        // �kki peaks olema limit(2)
        List<TapAdapter> unusedAdapter = adapters.stream().filter(p -> !p.isUsed()).collect(Collectors.toList());
        if (unusedAdapter.isEmpty())
            throw new NoUnusedTapAdaptersException();
        if (unusedAdapter.size() > 1 && randomize) {
            Collections.shuffle(unusedAdapter, new Random(System.nanoTime()));
        }
        return unusedAdapter.get(0);
    }
    
    public int getTapAdapterCount() {
        return adapters.size();
    }

    public boolean hasUnused() {
        return adapters.stream().anyMatch(p -> !p.isUsed());
    }

}

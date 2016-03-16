package ee.ttu.itx8530.fullprofile.vpn;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.zeroturnaround.exec.ProcessResult;

import ee.ttu.itx8530.fullprofile.proxy.ProxyServer;
import ee.ttu.itx8530.fullprofile.vpn.config.ConfigManager;
import ee.ttu.itx8530.fullprofile.vpn.config.OpenVpnConfig;
import ee.ttu.itx8530.fullprofile.vpn.tap.NoUnusedTapAdaptersException;
import ee.ttu.itx8530.fullprofile.vpn.tap.TapAdapter;
import ee.ttu.itx8530.fullprofile.vpn.tap.TapAdapterManager;
import ee.ttu.itx8530.fullprofile.vpn.tap.TapInstaller;
import ee.ttu.itx8530.fullprofile.vpn.vpngate.VPNGateCSVData;
import ee.ttu.itx8530.fullprofile.vpn.vpngate.VPNGateCSVRecord;

public class Main {

    static Logger logger = Logger.getLogger(Main.class);

    final static ArrayList<ServerAddress> servers = new ArrayList<ServerAddress>();
    final static int SERVER_TIMEOUT_MS = 100;
    final static int TAP_ADAPTER_COUNT = 3;

    final static int PROXY_PORT = 8888;

    // static {
    // try {
    // servers.add(new ServerAddress("www.neti.ee", 80));
    // servers.add(new ServerAddress("74.125.232.116", 80)); // www.google.com
    // } catch (UnknownHostException e) {
    // logger.error("Unknown host", e);
    // }
    // }

    public static void main(String[] args) throws SocketException {

        // killing all previous openvpn.exe processes. Win-specific
        try {
            logger.info("Killing all openvpn.exe processes forcefully ...");
            int exitCode = Runtime.getRuntime().exec("taskkill /im openvpn.exe /f").waitFor();
            logger.info("\"taskkill /im openvpn.exe\" resulted in exit code of " + exitCode);
        } catch (InterruptedException | IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        // configure the logger
        File configurationFile = new File("log4j.properties".replace("/", File.separator));
        PropertyConfigurator.configure(configurationFile.getAbsolutePath());
        logger.info("Retrieving log4j configuration from " + configurationFile.getAbsolutePath());

        logger.info("Testing servers");
        for (ServerAddress serverAddress : servers) {
            logger.info("Testing " + serverAddress.getInetAddress().getHostAddress());
            try {
                if (serverAddress.getInetAddress().isReachable(SERVER_TIMEOUT_MS)) {
                    logger.warn("Testing of " + serverAddress + " timed System.out");
                }
            } catch (IOException e) {
                logger.error("Testing of " + serverAddress + " failed", e);
            }
        }

        TapAdapterManager tapManager = new TapAdapterManager();
        logger.info("Tap adapter count: " + tapManager.getTapAdapterCount());
        logger.info("Any unused TAP adapters? " + tapManager.hasUnused());

        for (TapAdapter adapter : tapManager.queryTapAdapters()) {
            logger.info("Installed TAP adapter: \"" + adapter.getName() + "\" (" + adapter.getIp() + ")");
        }

        TapInstaller installer = TapInstaller.getInstance();
        logger.info("Starting TapInstaller");
        installer.install(TAP_ADAPTER_COUNT - tapManager.getTapAdapterCount());

        TapAdapter unused = null;
        try {
            unused = tapManager.getFirstUnusedAdapter(true);
            logger.info("First unused TAP adapter \"" + unused.getName() + "\" (" + unused.getIp() + ")");
        } catch (NoUnusedTapAdaptersException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("localhost", PROXY_PORT));


        ConfigManager configManager = ConfigManager.getInstance();
        VPNGateCSVData vpnGateData = new VPNGateCSVData();
        int amount = 0, maxAmount = 1;
        List<Future<ProcessResult>> futures = new ArrayList<>();
        for (VPNGateCSVRecord vpnRecord : vpnGateData.fetch()) {
            // create config from API record
            if (amount < maxAmount) {
                OpenVpnConfig vpnConfig = new OpenVpnConfig(vpnRecord.getConfig());
                if (ConnectionManager.isRemotePortAccessible(vpnConfig.getRemoteHost(), vpnConfig.getRemotePort())
                        && !vpnRecord.getCountryLong().equals("Japan")) {
                    logger.info("Cycling through VPN record: " + vpnRecord.getIP() + " (" + vpnRecord.getCountryLong()
                            + ")");
                    try {
                        File configFile = configManager.addConfigFile(vpnConfig);
                        OpenVpnProcess process = new OpenVpnProcess(configFile);
                        futures.add(process.start());
                        amount++;
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            } else {
                break;
            }
        }

        new ProxyServer(new InetSocketAddress(unused.getIp(), 0), tapManager).start();

        Runtime.getRuntime().addShutdownHook(new ShutDownHook(futures));

        // try {
        // Thread.sleep(1000 * 100);
        // } catch (InterruptedException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }

        // kill openvpn.exe processes, otherwise they will continue to run. even
        // after the JVM exits


        /**
         * should bind to different adapters?
         */

        // test vpn600818835.opengw.net / 14.43.226.4 TCP: 1274

        // finally remove all adapters?
        // installer.removeAll();

    }

}

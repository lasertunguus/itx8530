package ee.ttu.itx8530.fullprofile.proxy;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.HttpProxyServerBootstrap;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import ee.ttu.itx8530.fullprofile.vpn.tap.TapAdapterManager;

public class ProxyServer {

    Logger logger = Logger.getLogger(getClass());

    private static final int DEFAULT_PORT = 8888;
    private static final String NAME = "Proxy Server";

    private int port = DEFAULT_PORT;
    private String host;

    private InetSocketAddress networkInterface;
    private TapAdapterManager adapterManager;

    public ProxyServer(int port, TapAdapterManager adapterManager) {
        this.port = port;
        this.adapterManager = adapterManager;
    }

    public ProxyServer(InetSocketAddress networkInterface, TapAdapterManager adapterManager) {
        this(DEFAULT_PORT, adapterManager);
        this.networkInterface = networkInterface;
    }

    // public HttpProxyServer(String host) {
    // this(host, DEFAULT_PORT);
    // }

    public void start() {
        HttpProxyServerBootstrap bootstrap = DefaultHttpProxyServer.bootstrap();
        bootstrap.withName(NAME);
        bootstrap.withPort(port);
        if (networkInterface != null) {
            bootstrap.withNetworkInterface(networkInterface);
        }
        // server.withNetworkInterface(new InetSocketAddress(host, port));
        // bootstrap.withChainProxyManager(new ChainedProxyManager() {
        //
        // @Override
        // public void lookupChainedProxies(HttpRequest httpRequest,
        // Queue<ChainedProxy> chainedProxies) {
        // // TODO Auto-generated method stub
        // ChainedProxyAdapter chainedProxyAdapter = new ChainedProxyAdapter();
        // ChainedProxy proxy = chainedProxies.poll();
        // proxy.filterRequest(httpRequest);
        // }
        // });
        bootstrap.withListenOnAllAddresses(false);
        // bootstrap.withFiltersSource(new
        // AdapterSwitchingHttpFiltersSourceAdapter(adapterManager));
        logger.info("Starting " + NAME + " on " + port);
        HttpProxyServer server = bootstrap.start();
        logger.info("Started " + NAME + " on " + server.getListenAddress().getHostName() + ":"
                + server.getListenAddress().getPort());
    }

}

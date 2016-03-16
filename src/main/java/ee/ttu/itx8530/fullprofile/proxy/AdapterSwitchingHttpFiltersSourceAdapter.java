package ee.ttu.itx8530.fullprofile.proxy;

import java.net.InetSocketAddress;

import org.apache.log4j.Logger;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;

import ee.ttu.itx8530.fullprofile.vpn.tap.NoUnusedTapAdaptersException;
import ee.ttu.itx8530.fullprofile.vpn.tap.TapAdapter;
import ee.ttu.itx8530.fullprofile.vpn.tap.TapAdapterManager;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

public class AdapterSwitchingHttpFiltersSourceAdapter extends HttpFiltersSourceAdapter {
    
    Logger logger = Logger.getLogger(getClass());

    private TapAdapterManager adapterManager;

    public AdapterSwitchingHttpFiltersSourceAdapter(TapAdapterManager adapterManager) {
        this.adapterManager = adapterManager;
    }

    @Override
    public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
        try {
            TapAdapter adapter = adapterManager.getFirstUnusedAdapter(true);
            adapter.setUsed(true);
            String adapterIP = adapter.getIp();
            ChannelHandler channel = ctx.handler();
            logger.info("Binding " + originalRequest.getUri() + " request to adapter " + adapter.getInterfaceName()
                    + " with address " + adapterIP);
            ctx.bind(new InetSocketAddress(adapterIP, 0));
        } catch (NoUnusedTapAdaptersException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return filterRequest(originalRequest);
    }

}

package ee.ttu.itx8530.fullprofile.proxy;

import org.littleshoot.proxy.HttpFiltersAdapter;

import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

public class IPPoolingHttpFiltersAdapter extends HttpFiltersAdapter {

    public IPPoolingHttpFiltersAdapter(HttpRequest originalRequest) {
        super(originalRequest);
    }

    @Override
    public HttpResponse proxyToServerRequest(HttpObject httpObject) {
        return null;
    }

    @Override
    public HttpResponse clientToProxyRequest(HttpObject httpObject) {
        // TODO: implement your filtering here
        org.littleshoot.proxy.HttpFilters filter;

        return null;
    }

    @Override
    public HttpObject serverToProxyResponse(HttpObject httpObject) {
        // TODO: implement your filtering here
        return httpObject;
    }

}

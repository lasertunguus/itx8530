package ee.ttu.itx8530.fullprofile.vpn;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Connection {

    private URL query;
    private String localIP;

    public Connection(String localIP, URL query) {
        this.localIP = localIP;
        this.query = query;
    }

    public String connect() {
        String result = "";
        try {
            HttpURLConnection connection = (HttpURLConnection) query.openConnection();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }

}

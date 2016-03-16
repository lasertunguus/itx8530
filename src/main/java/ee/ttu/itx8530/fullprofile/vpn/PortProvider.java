package ee.ttu.itx8530.fullprofile.vpn;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

public class PortProvider {

    final static int PORT_RANGE_START = 49152;
    final static int PORT_RANGE_END = 65535;
    private final static int PORT_RANGE = PORT_RANGE_END - PORT_RANGE_START + 1;

    public static int getRandomLocalOpenPort() {
        Random random = new Random();
        while (true) {
            int port = random.nextInt(PORT_RANGE) + PORT_RANGE_START;
            try (ServerSocket socket = new ServerSocket(port)) {
                return socket.getLocalPort();
            } catch (IOException e) {
            }
        }
    }

}

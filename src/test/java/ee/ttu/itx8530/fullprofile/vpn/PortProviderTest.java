package ee.ttu.itx8530.fullprofile.vpn;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;

import static ee.ttu.itx8530.fullprofile.vpn.PortProvider.*;
import static org.junit.Assert.*;

public class PortProviderTest {

    @Before
    public void setUp() throws Exception {
    }
    
    @Test
    public void testMultipleGetRandomLocalOpenPort() throws IOException {
        int port = PortProvider.getRandomLocalOpenPort();
        int tries = 50;
        for (int i = 0; i < tries; i++) {
            try (ServerSocket socket = new ServerSocket(port)) {
                assertEquals(port, socket.getLocalPort());
                assertTrue(socket.getLocalPort() >= PORT_RANGE_START);
                assertTrue(socket.getLocalPort() <= PORT_RANGE_END);
            }
        }
    }

}

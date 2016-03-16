package ee.ttu.itx8530.fullprofile.vpn.config;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class OpenVpnConfigTest {

    protected static File configPath = new File("src/test/resources/vpngate_vpn249662090.opengw.net_tcp_443.ovpn");
    private static String configContent;
    private static OpenVpnConfig vpnConfig;

    @BeforeClass
    public static void setUp() throws IOException {
        configContent = new String(Files.readAllBytes(Paths.get(configPath.getAbsolutePath())),
                Charset.forName("UTF-8"));
        vpnConfig = new OpenVpnConfig(configPath);
    }

    @Ignore
    @Test
    public void testGetConnectionType() throws Exception {
        throw new RuntimeException("not yet implemented");
    }

    @Test
    public void testGetProtocol() throws Exception {
        assertEquals("tcp", vpnConfig.getProtocol());
    }

    @Test
    public void testGetRemoteHost() throws Exception {
        assertEquals("vpn249662090.opengw.net", vpnConfig.getRemoteHost());
    }

    @Test
    public void testGetRemotePort() throws Exception {
        assertEquals(443, vpnConfig.getRemotePort());
    }

    @Ignore
    @Test
    public void testGetEncryptionCipher() throws Exception {
        throw new RuntimeException("not yet implemented");
    }

    @Ignore
    @Test
    public void testGetAuthDigest() throws Exception {
        throw new RuntimeException("not yet implemented");
    }

    @Ignore
    @Test
    public void testGetTimesToTryResolvingHost() throws Exception {
        throw new RuntimeException("not yet implemented");
    }

    @Test
    public void testGetContent() throws Exception {
        assertEquals(configContent, vpnConfig.getContent());
    }

}

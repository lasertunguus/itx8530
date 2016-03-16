package ee.ttu.itx8530.fullprofile.vpn.config;

import static ee.ttu.itx8530.fullprofile.vpn.config.OpenVpnConfigTest.configPath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;;

public class ConfigManagerTest {


    private static OpenVpnConfig config;
    private ConfigManager manager;
    private static File configTestPath = new File("src/test/resources/config");

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

    }

    @Before
    public void setUp() throws Exception {
        config = new OpenVpnConfig(configPath);
        manager = ConfigManager.getInstance();
        manager.setConfigDir(configTestPath);
        manager.scanConfigFiles();
        if (!manager.getConfigDir().exists()) {
            manager.getConfigDir().mkdirs();
        }
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.deleteDirectory(configTestPath);
    }

    @Test
    public void testScanConfigFiles() throws Exception {
        File[] files = manager.getConfigDir()
                .listFiles((d, name) -> name.endsWith(ConfigManager.getFilenameExtension()));
        for (File f : files) {
            System.out.println(f.getAbsolutePath());
        }
        assertEquals(files.length, manager.getConfigs().size());
    }

    @Test
    public void testAddConfigFile() throws Exception {
        manager.addConfigFile(config);
        assertEquals(1, manager.getConfigs().size());
    }

    @Test
    public void testRemoveConfigFile() throws Exception {
        manager.addConfigFile(config);
        assertEquals(1, manager.getConfigs().size());
        manager.removeConfigFile(config);
        assertTrue(manager.getConfigs().isEmpty());
    }

    @Test
    public void testGetInstance() throws Exception {
        assertTrue(ConfigManager.getInstance() != null);
        assertTrue(ConfigManager.getInstance() instanceof ConfigManager);
    }

    @Test
    public void testGetConfigFileName() throws Exception {
        assertEquals("vpngate_vpn249662090.opengw.net_tcp_443.ovpn", ConfigManager.getConfigFileName(config));
    }

}

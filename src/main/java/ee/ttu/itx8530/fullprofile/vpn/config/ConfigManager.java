package ee.ttu.itx8530.fullprofile.vpn.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

public class ConfigManager {

    private Logger logger = Logger.getLogger(getClass());

    private static ConfigManager manager;

    // map of config file names and their respective OpenVpnConfigs
    private final HashMap<String, OpenVpnConfig> configs = new HashMap<String, OpenVpnConfig>();

    private static final String FILENAME_EXTENSION = ".ovpn";

    private static final String FILENAME_PREFIX = "vpngate";
    private static final String FILENAME_DELIMITER = "_";
    
    private File configDir = new File("src/main/resources/openvpn/config");

    private ConfigManager() {
        scanConfigFiles();
    }

    public void scanConfigFiles() {
        configs.clear();
        File[] files = getConfigDir().listFiles((d, name) -> name.endsWith(getFilenameExtension()));
        if (files == null)
            return;
        for (File file : files) {
            try {
                configs.put(file.getName(), new OpenVpnConfig(file));
            } catch (IOException e) {
                logger.error(
                        "Error when reading " + getFilenameExtension() + " files from "
                                + getConfigDir().getAbsolutePath());
            }
        }
    }

    /**
     * 
     * @param config config to add
     * @return the path to the config file
     * @throws IOException when an exception is thrown creating/writing to the file
     */
    public File addConfigFile(OpenVpnConfig config) throws IOException {
        String configFileName = getConfigFileName(config);
        File configFile = new File(getConfigDir(), configFileName);
        boolean alreadyPresent = configs.keySet().stream().filter(c -> configFileName.equals(c))
                .findFirst().isPresent();
        if (!alreadyPresent) {
            logger.info("Adding " + configFileName + " to " + configFile.getParentFile().getAbsolutePath());
            Files.write(Paths.get(configFile.getAbsolutePath()),
                    config.getContent().getBytes(config.getCharset()));
            configs.put(configFileName, new OpenVpnConfig(configFile));
        } else {
            logger.warn("Configuration file with name " + configFileName + " is already present in " + getConfigDir());
        }
        return configFile;
    }

    /**
     * 
     * @param config
     * @return true when successful, false otherwise
     * @throws IOException
     *             when not able to delete the file
     */
    public boolean removeConfigFile(OpenVpnConfig config) throws IOException {
        String configFileName = getConfigFileName(config);
        File file = new File(configDir, configFileName);
        configs.remove(getConfigFileName(config));
        boolean result = Files.deleteIfExists(Paths.get(file.getAbsolutePath()));
        if (!result) {
            logger.warn("Configuration file at path " + file.getAbsolutePath() + " could not be deleted!");
        }
        return result;
    }

    public static ConfigManager getInstance() {
        if (manager == null) {
            manager = new ConfigManager();
        }
        return manager;
    }


    public static String getConfigFileName(OpenVpnConfig config) {
        return String.join(FILENAME_DELIMITER, FILENAME_PREFIX, config.getRemoteHost(), config.getProtocol(),
                Integer.toString(config.getRemotePort())) + getFilenameExtension();
    }

    List<OpenVpnConfig> getConfigs() {
        return new ArrayList<>(configs.values());
    }

    public File getConfigDir() {
        return configDir;
    }

    public void setConfigDir(File configDir) {
        this.configDir = configDir;
        scanConfigFiles();
    }

    public static String getFilenameExtension() {
        return FILENAME_EXTENSION;
    }

}

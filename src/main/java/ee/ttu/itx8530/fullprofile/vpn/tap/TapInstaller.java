package ee.ttu.itx8530.fullprofile.vpn.tap;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;
import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;

public class TapInstaller {

    private static TapInstaller instance = null;

    // to configuration?
    private static final String ROOT_PATH = "src/main/resources/tap-windows/";
    private static final File ELEVATE_LOCATION = new File(ROOT_PATH, "bin/Elevate.exe");
    private static final File INSTALLER_LOCATION = new File(ROOT_PATH, "bin/tapinstall.exe");
    private static final File DRIVER_INF_LOCATION = new File(ROOT_PATH, "driver/OemVista.inf");
    private static final String DRIVER_NAME = "tap0901";

    private static final String[] INSTALL_COMMAND = { ELEVATE_LOCATION.getAbsolutePath(), "-wait",
            INSTALLER_LOCATION.getAbsolutePath(), "install", DRIVER_INF_LOCATION.getAbsolutePath(), DRIVER_NAME };
    private static final String[] REMOVE_COMMAND = { ELEVATE_LOCATION.getAbsolutePath(), "-wait",
            INSTALLER_LOCATION.getAbsolutePath(), "remove", DRIVER_NAME };

    private static Logger logger = Logger.getLogger(TapInstaller.class);

    private TapInstaller() {
    }

    /**
     * @param count
     *            number of times to start tapinstall.exe
     * @return
     */
    public boolean install(int count) {
        if (count < 0) {
            return false;
        }
        for (int i = 0; i < count; i++) {
            logger.info("Installing a new TAP virtual ethernet adapter");
            try {
                new ProcessExecutor().command(INSTALL_COMMAND).exitValue(0).execute();
            } catch (InvalidExitValueException e) {
                logger.error("Installing a new TAP virtual etherenet adapter returned a non-zero exit value", e);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (TimeoutException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     *
     * "C:\Program Files\TAP-Windows\bin\tapinstall.exe" install
     * "C:\Program Files\TAP-Windows\driver\OemVista.inf" tap0901
     * 
     * @return
     */
    public boolean install() {
        return install(1);
    }

    /**
     * "C:\Program Files\TAP-Windows\bin\tapinstall.exe" remove tap0901
     * 
     * @return
     */
    public boolean removeAll() {
        logger.info("Removing all TAP virtual ethernet adapters");
        try {
            new ProcessExecutor().command(REMOVE_COMMAND).exitValue(0).execute();
        } catch (InvalidExitValueException e) {
            logger.error("Removing a new TAP virtual etherenet adapter returned a non-zero exit value", e);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    public static TapInstaller getInstance() {
        if (instance == null) {
            logger.debug("TapInstaller instantiated");
            instance = new TapInstaller();
        }
        return instance;
    }

}

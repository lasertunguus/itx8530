package ee.ttu.itx8530.fullprofile.vpn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

public class OpenVpnProcess {

    private Logger logger = Logger.getLogger(getClass());

    private File configPath;

    private static final String ROOT_PATH = "src/main/resources/openvpn/";
    private static final File EXE_LOCATION = new File(ROOT_PATH, "bin/openvpn.exe");

    private static final String[] START_COMMAND = { EXE_LOCATION.getAbsolutePath(), "--config" };

    private Future<ProcessResult> future = null;

    public OpenVpnProcess(File configPath) {
        this.configPath = configPath;
    }

    public Future<ProcessResult> start() {
        new Runnable() {

            @Override
            public void run() {
                String[] cmd = appendToArray(START_COMMAND, configPath.getAbsolutePath());
                try {
                    logger.info("Starting " + String.join(" ", cmd));
                    future = new ProcessExecutor().command(cmd).redirectOutput(System.out).exitValue(0).destroyOnExit()
                            .start().getFuture();
                } catch (InvalidExitValueException | IOException e) {
                    logger.error("OpenVPN process error", e);
                }
            }


        }.run();
        return future;
    }

    private static String[] appendToArray(String[] array, String append) {
        List<String> appendedList = new ArrayList<String>(array.length + 1);
        Collections.addAll(appendedList, array);
        appendedList.add(append);
        String[] cmd = appendedList.toArray(new String[appendedList.size()]);
        return cmd;
    }

}

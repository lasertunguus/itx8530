package ee.ttu.itx8530.fullprofile.vpn;

import java.util.List;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;
import org.zeroturnaround.exec.ProcessResult;

class ShutDownHook extends Thread {

    Logger logger = Logger.getLogger(getClass());

    final List<Future<ProcessResult>> futures;

    public ShutDownHook(List<Future<ProcessResult>> futures) {
        this.futures = futures;
    }

    @Override
    public void run() {
        for (Future<ProcessResult> result : futures) {
            boolean res = result.cancel(true);
            logger.info("Canceling of future resulted in : " + res);
        }
    }
}
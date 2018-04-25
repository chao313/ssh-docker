package demo.spring.boot.demospringboot.sdxd.common.api.common.web.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.util
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/3/7     melvin                 Created
 */
public class Tasks {

    private static final Logger log = LoggerFactory.getLogger(Tasks.class);

    private static ExecutorService executorService = Executors.newFixedThreadPool(2, r -> {
        String name = "Async-tasks-executor";
        return new Thread(r, name);
    });

    public static void shutdown() {
        log.debug("Start shutdown async tasks");
        executorService.shutdown();
        try {
            boolean terminated = executorService.awaitTermination(5, TimeUnit.SECONDS);
            if (!terminated) {
                executorService.shutdownNow();
                terminated = executorService.awaitTermination(5, TimeUnit.SECONDS);
                if (!terminated) {
                    log.warn("Shutdown tasks failed");
                }
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
        log.debug("Shutdown async tasks complete");
    }

    public static void execute(Runnable runnable) {
        executorService.submit(runnable);
    }
}

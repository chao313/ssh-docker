package demo.spring.boot.service;

import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.AttachContainerResultCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 2018/4/22    Created by   chao
 */
public class AttachLog extends AttachContainerResultCallback {
    private static final Logger LOGGER = LoggerFactory.getLogger(AttachLog.class);

    @Override
    public void onNext(Frame item) {
        LOGGER.info("日志开始");
        LOGGER.info(item.toString()
        );
    }
}

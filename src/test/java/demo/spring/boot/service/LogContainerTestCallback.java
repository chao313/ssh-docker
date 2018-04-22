package demo.spring.boot.service;

import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.LogContainerResultCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kanstantsin Shautsou
 */
public class LogContainerTestCallback extends LogContainerResultCallback {
    protected final StringBuffer log = new StringBuffer();

    Logger logger = LoggerFactory.getLogger(LogContainerTestCallback.class);
    List<Frame> collectedFrames = new ArrayList<Frame>();

    boolean collectFrames = false;

    public LogContainerTestCallback() {
        this(false);
    }

    public LogContainerTestCallback(boolean collectFrames) {
        this.collectFrames = collectFrames;
    }

    @Override
    public void onNext(Frame frame) {
        logger.info("记录日志开始：{}");
        if (collectFrames) collectedFrames.add(frame);
        log.append(new String(frame.getPayload()));
        logger.info("记录日志：{}", new String(frame.getPayload()));
        logger.info("记录日志结束：{}");

    }

    @Override
    public String toString() {
        return log.toString();
    }


    public List<Frame> getCollectedFrames() {
        return collectedFrames;
    }
}

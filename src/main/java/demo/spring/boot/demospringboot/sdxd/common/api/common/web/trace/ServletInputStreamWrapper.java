package demo.spring.boot.demospringboot.sdxd.common.api.common.web.trace;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.filter
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/1/24     melvin                 Created
 */
public class ServletInputStreamWrapper extends ServletInputStream {

    private ByteArrayInputStream sourceStream;

    public ServletInputStreamWrapper(byte[] body) {
        this.sourceStream = new ByteArrayInputStream(body);
    }

    @Override
    public boolean isFinished() {
        return sourceStream.available() <= 0;
    }

    @Override
    public boolean isReady() {
        return sourceStream.available() > 0;
    }

    @Override
    public void setReadListener(ReadListener listener) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int read() throws IOException {
        return sourceStream.read();
    }

    @Override
    public void close() throws IOException {
        super.close();
        sourceStream.close();
    }
}

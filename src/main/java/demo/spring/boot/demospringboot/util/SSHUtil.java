package demo.spring.boot.demospringboot.util;

import com.jcraft.jsch.JSchException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import demo.spring.boot.demospringboot.command.options.stream.Images;
import demo.spring.boot.demospringboot.util.help.SSHHelper;
import demo.spring.boot.demospringboot.util.help.SSHResInfo;

/**
 * 2018/4/15    Created by   chao
 */
@Component
public class SSHUtil {


    private static Logger LOGGER = LoggerFactory.getLogger(SSHUtil.class);

    @Value(value = "${ssh.host}")
    private String host;

    @Value(value = "${ssh.port}")
    private Integer port;

    @Value(value = "${ssh.user}")
    private String user;

    @Value(value = "${ssh.password}")
    private String password;

    private SSHHelper helper;

    public SSHResInfo execute(String command) {

        SSHResInfo result = null;
        try {
            if (null == helper) {
                helper = new SSHHelper(host, port, user, password);
            }
            result = helper.sendCmd(command);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

package demo.spring.boot.ssh;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import demo.spring.boot.demospringboot.DemoSpringBootApplication;
import demo.spring.boot.demospringboot.command.options.stream.Base;
import demo.spring.boot.demospringboot.util.SSHUtil;
import demo.spring.boot.demospringboot.util.help.SSHResInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = DemoSpringBootApplication.class)
public class SSHLoginTest {

    private static Logger LOGGER = LoggerFactory.getLogger(SSHLoginTest.class);
    @Autowired
    private SSHUtil sshUtil;


    @Test
    public void testSSH() {

        String cmd = new Base().addOPtions(Base.options.__format) + " {{.Container}}";
        SSHResInfo result = sshUtil.execute(cmd);
        LOGGER.info("reslut:{}", result);
    }
}

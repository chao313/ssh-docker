package demo.spring.boot.demospringboot.controller.docker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.spring.boot.demospringboot.framework.Code;
import demo.spring.boot.demospringboot.framework.Response;
import demo.spring.boot.demospringboot.service.jpa.HostJpa;
import demo.spring.boot.demospringboot.util.SSHUtil;
import demo.spring.boot.demospringboot.vo.HostVo;

/**
 * 2018/4/27    Created by   chao
 */
@RestController
@RequestMapping(value = "/host")
public class HostController {
    private static Logger LOGGER = LoggerFactory.getLogger(HostController.class);

    @Autowired
    private HostJpa hostJpa;

    @PostMapping(value = "/add")
    public Response<Boolean> add(
            @RequestParam(value = "host-ip") String hostIp,
            @RequestParam(value = "account", defaultValue = "root", required = false) String account,
            @RequestParam(value = "password") String password,
            @RequestParam(value = "port", defaultValue = "22", required = false) Integer port,
            @RequestParam(value = "user-id") String userId) {
        LOGGER.info("添加主机");
        Response<Boolean> response = new Response<>();
        try {
            if (false == SSHUtil.valid(hostIp, port, account, password)) {
                //验证失败
                response.setCode(Code.System.OK);
                response.setContent(false);
                response.setMsg("ssh 无法登陆");
            } else {
                HostVo vo = new HostVo();
                vo.setAccount(account);
                vo.setHostIp(hostIp);
                vo.setPassword(password);
                vo.setUserId(userId);
                vo.setPort(port);
                vo = hostJpa.save(vo);
                response.setContent(vo == null ? false : true);
                response.setCode(Code.System.OK);
            }
        } catch (Exception e) {
            response.setCode(Code.System.FAIL);
            response.setMsg(e.getMessage());
            response.addException(e);
            LOGGER.info("添加主机 异常：", e.getMessage(), e);
        }
        return response;
    }

    @PostMapping(value = "/valid")
    public Response<Boolean> add(
            @RequestParam(value = "host-ip") String hostIp,
            @RequestParam(value = "account", defaultValue = "root", required = false) String account,
            @RequestParam(value = "port", defaultValue = "22", required = false) Integer port,
            @RequestParam(value = "password") String password) {
        LOGGER.info("验证主机");
        Response<Boolean> response = new Response<>();
        try {
            Boolean result =
                    SSHUtil.valid(hostIp, port, account, password);
            response.setCode(Code.System.OK);
            response.setContent(result);
        } catch (Exception e) {
            response.setCode(Code.System.FAIL);
            response.setMsg(e.getMessage());
            response.addException(e);
            LOGGER.info("验证主机 异常：", e.getMessage(), e);
        }
        return response;
    }
}

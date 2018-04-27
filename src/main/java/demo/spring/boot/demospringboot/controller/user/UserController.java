package demo.spring.boot.demospringboot.controller.user;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.spring.boot.demospringboot.framework.Code;
import demo.spring.boot.demospringboot.framework.Response;
import demo.spring.boot.demospringboot.service.jpa.UserJpa;

import demo.spring.boot.demospringboot.util.BillNoUtils;
import demo.spring.boot.demospringboot.util.DateUtils;
import demo.spring.boot.demospringboot.util.MD5Wrapper;
import demo.spring.boot.demospringboot.vo.UserVo;
import io.swagger.annotations.ApiOperation;

/**
 * 2018/4/23    Created by   chao
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {
    private static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserJpa userJpa;

    @ApiOperation(value = "注册新用户", notes = "注册新用户")
    @PostMapping(value = "/register")
    public Response<Boolean> register(
            @RequestParam(value = "docker-paas-id") String dockerPassId,
            @RequestParam(value = "password") String paswword) {
        Response<Boolean> response = new Response<>();
        try {
            if (null != userJpa.findFirstByDockerPassId(dockerPassId)) {
                response.setCode(Code.System.FAIL);
                response.setMsg("DockerPaaSId重复");
                response.setContent(false);
            } else {
                String salt =
                        MD5Wrapper.genSalt();
                String password = MD5Wrapper.md5WithSalt(paswword, salt);
                UserVo vo = new UserVo();
                vo.setId(BillNoUtils.GenerateBillNo());
                vo.setDockerPassId(dockerPassId);
                vo.setSalt(salt);
                vo.setPassword(password);
                vo.setCreateTime(DateUtils.generateNowTimestamp());
                vo.setUpdateTime(DateUtils.generateNowTimestamp());
                userJpa.save(vo);
                response.setCode(Code.System.OK);
                response.setMsg("保存成功");
                response.setContent(true);
            }
        } catch (Exception e) {
            response.setCode(Code.System.FAIL);
            response.setMsg(e.getMessage());
            LOGGER.error("异常：{}", e.getMessage(), e);
        }
        return response;
    }

    @ApiOperation(value = "验证id是否重复", notes = "验证id是否重复")
    @PostMapping(value = "/valid")
    public Response<Boolean> valid(
            @RequestParam(value = "docker-paas-id") String dockerPassId) {
        Response<Boolean> response = new Response<>();
        try {
            if (null != userJpa.findFirstByDockerPassId(dockerPassId)) {
                response.setCode(Code.System.OK);
                response.setMsg("DockerPaaSId重复");
                response.setContent(false);
            } else {
                response.setCode(Code.System.OK);
                response.setMsg("这个是新的docker paas id");
                response.setContent(true);
            }
        } catch (Exception e) {
            response.setCode(Code.System.FAIL);
            response.setMsg(e.getMessage());
            LOGGER.error("异常：{}", e.getMessage(), e);
        }
        return response;
    }

    @ApiOperation(value = "登陆", notes = "登陆")
    @PostMapping(value = "/login")
    public Response<Boolean> valid(
            @RequestParam(value = "docker-paas-id") String dockerPassId,
            @RequestParam(value = "password") String password) {
        Response<Boolean> response = new Response<>();
        try {
            String salt = userJpa.findSaltDockerPassId(dockerPassId);
            if (StringUtils.isBlank(salt)) {
                response.setCode(Code.System.OK);
                response.setMsg("账号异常！");
                response.setContent(false);
            } else {
                password = MD5Wrapper.md5WithSalt(password, salt);
                if (null != userJpa.findUserVoByDockerPassIdAndPassword(dockerPassId, password)) {
                    response.setCode(Code.System.OK);
                    response.setMsg("登陆成功");
                    response.setContent(true);
                } else {
                    response.setCode(Code.System.OK);
                    response.setMsg("账号或密码错误！");
                    response.setContent(false);
                }
            }
        } catch (Exception e) {
            response.setCode(Code.System.FAIL);
            response.setMsg(e.getMessage());
            LOGGER.error("异常：{}", e.getMessage(), e);
        }

        return response;
    }
}

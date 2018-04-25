package demo.spring.boot.demospringboot.sdxd.framework.web;


import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import demo.spring.boot.demospringboot.sdxd.common.pojo.dto.ResponseEntity;
import demo.spring.boot.demospringboot.sdxd.framework.constant.Constants;

/**
 * Package Name: demo.spring.boot.demospringboot.sdxd.common.api.framework.web
 * Description:
 * Author: qiuyangjun
 * Create Date:2015/6/17
 */
@Controller
@RequestMapping("error")
public class ErrorController {

    @RequestMapping("403")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity noPermissions(){
        ResponseEntity result = new ResponseEntity();
        result.setMsg(Constants.System.NO_PERMISSIONS_MSG);
        result.setError(Constants.System.NO_PERMISSIONS);
        result.setStatus(Constants.System.FAIL);
        return result;
    }

    @RequestMapping("404")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity noRequestMatch(){
        ResponseEntity result = new ResponseEntity();
        result.setMsg(Constants.System.NO_REQUEST_MATCH_MSG);
        result.setError(Constants.System.NO_REQUEST_MATCH);
        result.setStatus(Constants.System.FAIL);
        return result;
    }

    @RequestMapping("500")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity sysError(){
        ResponseEntity result = new ResponseEntity();
        result.setMsg(Constants.System.SYSTEM_ERROR_MSG);
        result.setError(Constants.System.SYSTEM_ERROR_CODE);
        result.setStatus(Constants.System.FAIL);
        return result;
    }

}

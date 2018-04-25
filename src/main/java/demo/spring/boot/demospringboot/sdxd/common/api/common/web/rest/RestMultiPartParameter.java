package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest;

import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/4/10     melvin                 Created
 */
public class RestMultiPartParameter extends RestParameter<MultipartFile> {

    public RestMultiPartParameter(String name) {
        super(name, true);
    }

    @Override
    public boolean existValue(NativeWebRequest request) {
        DefaultMultipartHttpServletRequest multipartHttpServletRequest = request.getNativeRequest(DefaultMultipartHttpServletRequest.class);
        return multipartHttpServletRequest != null && multipartHttpServletRequest.getMultiFileMap().containsKey(getName());
    }

    @Override
    protected RestValue<MultipartFile> takeValue(NativeWebRequest request) {
        DefaultMultipartHttpServletRequest multipartHttpServletRequest = request.getNativeRequest(DefaultMultipartHttpServletRequest.class);
        MultipartFile multipartFile = multipartHttpServletRequest == null ? null : multipartHttpServletRequest.getFile(getName());
        return new RestValue<>(getName(), multipartFile);
    }
}

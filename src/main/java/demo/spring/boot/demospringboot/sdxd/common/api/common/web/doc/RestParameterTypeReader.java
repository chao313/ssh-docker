package demo.spring.boot.demospringboot.sdxd.common.api.common.web.doc;

import com.fasterxml.classmate.ResolvedType;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.ParameterContext;
import springfox.documentation.spring.web.readers.parameter.ParameterTypeReader;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.doc
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 16/11/2     melvin                 Created
 */
public class RestParameterTypeReader extends ParameterTypeReader {

    @Override
    public void apply(ParameterContext context) {
        context.parameterBuilder().parameterType(findParameterType(context));
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return true;
    }

    public static String findParameterType(ParameterContext parameterContext) {
        ResolvedMethodParameter resolvedMethodParameter = parameterContext.resolvedMethodParameter();
        ResolvedType parameterType = resolvedMethodParameter.getParameterType();
        parameterType = parameterContext.alternateFor(parameterType);

        //Multi-part file trumps any other annotations
        if (MultipartFile.class.isAssignableFrom(parameterType.getErasedType())) {
            return "form";
        }
        if (resolvedMethodParameter.hasParameterAnnotation(PathVariable.class)) {
            return "path";
        } else if (resolvedMethodParameter.hasParameterAnnotation(ModelAttribute.class)) {
            return queryOrForm(parameterContext.getOperationContext());
        } else if (resolvedMethodParameter.hasParameterAnnotation(RequestBody.class)) {
            return "body";
        } else if (resolvedMethodParameter.hasParameterAnnotation(RequestParam.class)) {
            return queryOrForm(parameterContext.getOperationContext());
        } else if (resolvedMethodParameter.hasParameterAnnotation(RequestHeader.class)) {
            return "header";
        } else if (resolvedMethodParameter.hasParameterAnnotation(RequestPart.class)) {
            return "form";
        } else if (resolvedMethodParameter.hasParameterAnnotation(PathParam.class)) {
            return "path";
        } else if (resolvedMethodParameter.hasParameterAnnotation(QueryParam.class)) {
            return "query";
        } else if (resolvedMethodParameter.hasParameterAnnotation(FormParam.class)) {
            return "form";
        } else if (resolvedMethodParameter.hasParameterAnnotation(HeaderParam.class)) {
            return "header";
        }
        if (!resolvedMethodParameter.hasParameterAnnotations()) {
            return queryOrForm(parameterContext.getOperationContext());
        }
        return "body";
    }

    private static String queryOrForm(OperationContext context) {
        if (context.consumes().contains(MediaType.APPLICATION_FORM_URLENCODED) && context.httpMethod() == HttpMethod
                .POST) {
            return "form";
        }
        return "query";
    }
}

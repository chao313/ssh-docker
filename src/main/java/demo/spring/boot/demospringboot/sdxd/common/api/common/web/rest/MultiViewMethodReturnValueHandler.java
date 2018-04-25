package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest;

import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.io.IOException;
import java.util.List;

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
 * 17/1/12     melvin                 Created
 */
public class MultiViewMethodReturnValueHandler extends RequestResponseBodyMethodProcessor {

    private HeaderContentNegotiationStrategy headerContentNegotiationStrategy;

    public MultiViewMethodReturnValueHandler(List<HttpMessageConverter<?>> converters) {
        super(converters);

        this.headerContentNegotiationStrategy = new HeaderContentNegotiationStrategy();
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), MultiViewResponseBody.class) ||
                returnType.hasMethodAnnotation(MultiViewResponseBody.class));
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest)
            throws IOException, HttpMediaTypeNotAcceptableException, HttpMessageNotWritableException {

        List<MediaType> requestedMediaTypes = headerContentNegotiationStrategy.resolveMediaTypes(webRequest);
        long count = requestedMediaTypes.stream().
                filter(MediaType.APPLICATION_JSON::isCompatibleWith).count();
        boolean json = count > 0;

        if (json) {
            super.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
            return;
        }

        if (returnValue == null) {
            mavContainer.setRequestHandled(true);
            return;
        }

        MultiViewResponseBody body = returnType.getMethodAnnotation(MultiViewResponseBody.class);
        View view = body.view();
        ModelAndView mav = new ModelAndView(view.name(), view.model(), returnValue);

        mavContainer.setViewName(mav.getViewName());
        mavContainer.setStatus(mav.getStatus());
        mavContainer.addAllAttributes(mav.getModel());

//        if (returnValue == null) {
//            mavContainer.setRequestHandled(true);
//            return;
//        }
//
//        ModelAndView mav = (ModelAndView) returnValue;
//        if (mav.isReference()) {
//            String viewName = mav.getViewName();
//            mavContainer.setViewName(viewName);
//            if (viewName != null && isRedirectViewName(viewName)) {
//                mavContainer.setRedirectModelScenario(true);
//            }
//        }
//        else {
//            View view = mav.getView();
//            mavContainer.setView(view);
//            if (view instanceof SmartView) {
//                if (((SmartView) view).isRedirectView()) {
//                    mavContainer.setRedirectModelScenario(true);
//                }
//            }
//        }
//        mavContainer.setStatus(mav.getStatus());
//        mavContainer.addAllAttributes(mav.getModel());
    }

    /**
     * Whether the given view name is a redirect view reference.
     * The default implementation checks the configured redirect patterns and
     * also if the view name starts with the "redirect:" prefix.
     * @param viewName the view name to check, never {@code null}
     * @return "true" if the given view name is recognized as a redirect view
     * reference; "false" otherwise.
     */
//    protected boolean isRedirectViewName(String viewName) {
//        if (PatternMatchUtils.simpleMatch(this.redirectPatterns, viewName)) {
//            return true;
//        }
//        return viewName.startsWith("redirect:");
//    }
}

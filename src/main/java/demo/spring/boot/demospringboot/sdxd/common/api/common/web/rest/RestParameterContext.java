package demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class RestParameterContext {

    public static RestParameterContext of(HttpServletRequest request) {
        return of(request, null);
    }

    public static RestParameterContext of(HttpServletRequest request, Map<String, Object> contextValues) {
        RestParameterContext context = new RestParameterContext();
        context.request = request;
        context.contextValues = contextValues;
        return context;
    }

    private HttpServletRequest request;
    private Map<String, Object> contextValues;
}

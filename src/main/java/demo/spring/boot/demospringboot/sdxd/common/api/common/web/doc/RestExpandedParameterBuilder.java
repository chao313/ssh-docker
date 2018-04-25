package demo.spring.boot.demospringboot.sdxd.common.api.common.web.doc;

import com.google.common.base.Function;
import com.google.common.base.Optional;



import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

import demo.spring.boot.demospringboot.sdxd.common.api.common.web.rest.MultiNameHeaderParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import springfox.documentation.service.AllowableListValues;
import springfox.documentation.service.AllowableValues;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.ParameterExpansionContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;
import springfox.documentation.swagger.readers.parameter.SwaggerExpandedParameterBuilder;
import springfox.documentation.swagger.schema.ApiModelProperties;

import static com.google.common.base.Optional.fromNullable;
import static com.google.common.base.Strings.emptyToNull;
import static com.google.common.collect.Lists.transform;

import static demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.HttpUtil.toSnakeCase;
import static springfox.documentation.swagger.annotations.Annotations.findApiParamAnnotation;
import static springfox.documentation.swagger.schema.ApiModelProperties.findApiModePropertyAnnotation;

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
@Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 100)
public class RestExpandedParameterBuilder extends SwaggerExpandedParameterBuilder {

    @Override
    public void apply(ParameterExpansionContext context) {
        Optional<ApiModelProperty> apiModelPropertyOptional
                = findApiModePropertyAnnotation(context.getField().getRawMember());
        if (apiModelPropertyOptional.isPresent()) {
            fromApiModelProperty(context, apiModelPropertyOptional.get());
        }
        Optional<ApiParam> apiParamOptional = findApiParamAnnotation(context.getField().getRawMember());
        if (apiParamOptional.isPresent()) {
            fromApiParam(context, apiParamOptional.get());
        }
    }

    @Override
    public boolean supports(DocumentationType delimiter) {
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }

    private void fromApiParam(ParameterExpansionContext context, ApiParam apiParam) {
        Field field = context.getField().getRawMember();
        String name = getNameFromParamDef(apiParam, field);
        String paramType = getParamTypeFromParamDef(apiParam, field);
        String allowableProperty = emptyToNull(apiParam.allowableValues());
        AllowableValues allowable = allowableValues(fromNullable(allowableProperty), context.getField().getRawMember());
        context.getParameterBuilder()
                .name(name)
                .parameterType(paramType)
                .description(apiParam.value())
                .defaultValue(apiParam.defaultValue())
                .required(apiParam.required())
                .allowMultiple(apiParam.allowMultiple())
                .allowableValues(allowable)
                .parameterAccess(apiParam.access())
                .hidden(apiParam.hidden())
                .build();
    }

    private void fromApiModelProperty(ParameterExpansionContext context, ApiModelProperty apiModelProperty) {
        String allowableProperty = emptyToNull(apiModelProperty.allowableValues());
        AllowableValues allowable = allowableValues(fromNullable(allowableProperty), context.getField().getRawMember());
        context.getParameterBuilder()
                .description(apiModelProperty.value())
                .required(apiModelProperty.required())
                .allowableValues(allowable)
                .parameterAccess(apiModelProperty.access())
                .hidden(apiModelProperty.hidden())
                .build();
    }

    private String getNameFromParamDef(ApiParam apiParam, Field field) {
        if (field.isAnnotationPresent(QueryParam.class)) {
            QueryParam queryParam = field.getDeclaredAnnotation(QueryParam.class);
            return queryParam.value();
        }
        if (field.isAnnotationPresent(FormParam.class)) {
            FormParam formParam = field.getDeclaredAnnotation(FormParam.class);
            return formParam.value();
        }
        if (field.isAnnotationPresent(HeaderParam.class)) {
            HeaderParam headerParam = field.getDeclaredAnnotation(HeaderParam.class);
            return headerParam.value();
        }
        if (field.isAnnotationPresent(MultiNameHeaderParam.class)) {
            MultiNameHeaderParam headerParam = field.getDeclaredAnnotation(MultiNameHeaderParam.class);
            return headerParam.value();
        }
        if (field.isAnnotationPresent(PathParam.class)) {
            PathParam pathParam = field.getDeclaredAnnotation(PathParam.class);
            return pathParam.value();
        }
        if (StringUtils.isNotBlank(apiParam.name())) {
            return apiParam.name();
        }

        String name = field.getName();
        return toSnakeCase(name);
    }

    private String getParamTypeFromParamDef(ApiParam apiParam, Field field) {
        if (field.isAnnotationPresent(QueryParam.class)) {
            return "query";
        }
        if (field.isAnnotationPresent(FormParam.class)) {
            return "form";
        }
        if (field.isAnnotationPresent(HeaderParam.class)) {
            return "header";
        }
        if (field.isAnnotationPresent(MultiNameHeaderParam.class)) {
            return "header";
        }
        if (field.isAnnotationPresent(PathParam.class)) {
            return "path";
        }
        return "query";
    }

    private AllowableValues allowableValues(final Optional<String> optionalAllowable, final Field field) {

        AllowableValues allowable = null;
        if (optionalAllowable.isPresent()) {
            allowable = ApiModelProperties.allowableValueFromString(optionalAllowable.get());
        } else if (field.getType().isEnum()) {
            allowable = new AllowableListValues(getEnumValues(field.getType()), "LIST");
        }

        return allowable;
    }

    private List<String> getEnumValues(final Class<?> subject) {
        return transform(Arrays.asList(subject.getEnumConstants()), (Function<Object, String>) Object::toString);
    }
}

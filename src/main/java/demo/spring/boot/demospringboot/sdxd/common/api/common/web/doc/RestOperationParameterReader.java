//package demo.spring.boot.demospringboot.sdxd.common.api.common.web.doc;
//
//import com.google.common.base.Predicate;
//import com.google.common.collect.FluentIterable;
//
//import com.fasterxml.classmate.ResolvedType;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.ModelAttribute;
//
//import java.lang.annotation.Annotation;
//import java.util.List;
//import java.util.Set;
//
//import springfox.documentation.builders.ParameterBuilder;
//import springfox.documentation.service.Parameter;
//import springfox.documentation.service.ResolvedMethodParameter;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spi.service.contexts.OperationContext;
//import springfox.documentation.spi.service.contexts.ParameterContext;
//import springfox.documentation.spring.web.plugins.DocumentationPluginsManager;
//import springfox.documentation.spring.web.readers.operation.OperationParameterReader;
//import springfox.documentation.spring.web.readers.parameter.ModelAttributeParameterExpander;
//
//import static com.google.common.base.Predicates.not;
//import static com.google.common.collect.Lists.newArrayList;
//import static springfox.documentation.schema.Collections.isContainerType;
//import static springfox.documentation.schema.Maps.isMapType;
//import static springfox.documentation.schema.Types.isBaseType;
//import static springfox.documentation.schema.Types.typeNameFor;
//
///**
// * *****************************************************************************
// * <p>
// * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.doc
// * 系统名           ：
// * <p>
// * *****************************************************************************
// * Modification History
// * <p>
// * Date        Name                    Reason for Change
// * ----------  ----------------------  -----------------------------------------
// * 16/11/2     melvin                 Created
// */
//public class RestOperationParameterReader extends OperationParameterReader {
//
//    private final ModelAttributeParameterExpander expander;
//
//    private Class<? extends Annotation>[] shouldExpandAnnotationTypes;
//
//    @Autowired
//    private DocumentationPluginsManager pluginsManager;
//
//    public RestOperationParameterReader(ModelAttributeParameterExpander expander) {
//        super(expander);
//        this.expander = expander;
//    }
//
//    @Override
//    public void apply(OperationContext context) {
//        context.operationBuilder().parameters(context.getGlobalOperationParameters());
//        context.operationBuilder().parameters(readParameters(context));
//    }
//
//    @Override
//    public boolean supports(DocumentationType delimiter) {
//        return true;
//    }
//
//    @SafeVarargs
//    public final void setShouldExpandAnnotationTypes(Class<? extends Annotation>... shouldExpandAnnotationTypes) {
//        this.shouldExpandAnnotationTypes = shouldExpandAnnotationTypes;
//    }
//
//    private List<Parameter> readParameters(final OperationContext context) {
//
//        List<ResolvedMethodParameter> methodParameters = context.getParameters();
//        List<Parameter> parameters = newArrayList();
//
//        for (ResolvedMethodParameter methodParameter : methodParameters) {
//            ResolvedType alternate = context.alternateFor(methodParameter.getParameterType());
//            if (!shouldIgnore(methodParameter, alternate, context.getIgnorableParameterTypes())) {
//
//                ParameterContext parameterContext = new ParameterContext(methodParameter,
//                        new ParameterBuilder(),
//                        context.getDocumentationContext(),
//                        context.getGenericsNamingStrategy(),
//                        context);
//
//                if (shouldExpand(methodParameter, alternate)) {
//                    parameters.addAll(
//                            expander.expand(
//                                    "",
//                                    methodParameter.getParameterType(),
//                                    context.getDocumentationContext()));
//                } else {
//                    parameters.add(pluginsManager.parameter(parameterContext));
//                }
//            }
//        }
//        return FluentIterable.from(parameters).filter(not(hiddenParams())).toList();
//    }
//
//    private Predicate<Parameter> hiddenParams() {
//        return new Predicate<Parameter>() {
//            @Override
//            public boolean apply(Parameter input) {
//                return input.isHidden();
//            }
//        };
//    }
//
//    private boolean shouldIgnore(
//            final ResolvedMethodParameter parameter,
//            ResolvedType resolvedParameterType,
//            final Set<Class> ignorableParamTypes) {
//
//        if (ignorableParamTypes.contains(resolvedParameterType.getErasedType())) {
//            return true;
//        }
//        return FluentIterable.from(ignorableParamTypes)
//                .filter(isAnnotation())
//                .filter(parameterIsAnnotatedWithIt(parameter)).size() > 0;
//
//    }
//
//    private Predicate<Class> parameterIsAnnotatedWithIt(final ResolvedMethodParameter parameter) {
//        return new Predicate<Class>() {
//            @Override
//            public boolean apply(Class input) {
//                return parameter.hasParameterAnnotation(input);
//            }
//        };
//    }
//
//    private Predicate<Class> isAnnotation() {
//        return new Predicate<Class>() {
//            @Override
//            public boolean apply(Class input) {
//                return Annotation.class.isAssignableFrom(input);
//            }
//        };
//    }
//
//    private boolean shouldExpand(final ResolvedMethodParameter parameter, ResolvedType resolvedParamType) {
//        return (!parameter.hasParameterAnnotations() || parameter.hasParameterAnnotation(ModelAttribute.class) || includeAnnotations(parameter))
//                && !isBaseType(typeNameFor(resolvedParamType.getErasedType()))
//                && !resolvedParamType.getErasedType().isEnum()
//                && !isContainerType(resolvedParamType)
//                && !isMapType(resolvedParamType);
//
//    }
//
//    private boolean includeAnnotations(final ResolvedMethodParameter parameter) {
//        boolean include = false;
//        for (Class<? extends Annotation> type : shouldExpandAnnotationTypes) {
//            if (parameter.hasParameterAnnotation(type)) {
//                include = true;
//                break;
//            }
//        }
//        return include;
//    }
//}

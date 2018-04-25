package demo.spring.boot.demospringboot.sdxd.common.api.common.web.graphql;

import java.util.List;

import graphql.ErrorType;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.api.service.v3
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 2017/8/7     melvin                 Created
 */
public class QueryError implements GraphQLError {

    private String code;
    private String field;
    private String errorMessage;

    QueryError(String code, String errorMessage) {
        this(code, null, errorMessage);
    }

    QueryError(String code, String field, String errorMessage) {
        this.code = code;
        this.field = field;
        this.errorMessage = errorMessage;
    }

    public String getCode() {
        return code;
    }

    public String getField() {
        return field;
    }

    @Override
    public String getMessage() {
        return errorMessage;
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorType getErrorType() {
        return ErrorType.DataFetchingException;
    }
}

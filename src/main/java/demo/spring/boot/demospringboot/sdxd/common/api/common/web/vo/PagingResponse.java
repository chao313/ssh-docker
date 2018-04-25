package demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import demo.spring.boot.demospringboot.sdxd.common.api.common.web.util.Pages;
import demo.spring.boot.demospringboot.sdxd.common.pojo.dto.PaginationSupport;


/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.api.vo
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 17/1/12     melvin                 Created
 */
public class PagingResponse<T> extends RestResponse<PaginationSupport<T>> {

    public static <T> PagingResponse<T> paging(Pages<T> pages, int currentPage, int currentSize) {
        return new PagingResponse<>(pages, currentPage, currentSize);
    }

    @JsonIgnore
    private Pages<T> pages;

    @JsonIgnore
    private int currentPage;

    @JsonIgnore
    private int currentSize;

    @JsonIgnore
    private boolean initialized;

    private PagingResponse(Pages<T> pages, int currentPage, int currentSize) {
        super(SUCCESS_CODE, null);
        this.pages = pages;
        this.currentPage = currentPage;
        this.currentSize = currentSize;
        this.initialized = false;
    }

    @Override
    @JsonProperty("code")
    public String getCode() {
        initialize();
        return super.getCode();
    }

    @Override
    @JsonProperty("content")
    public PaginationSupport<T> getContent() {
        initialize();
        return super.getContent();
    }

    @Override
    @JsonProperty("error")
    public RestResponseError getError() {
        initialize();
        return super.getError();
    }

    public Pages<T> getPages() {
        return pages;
    }

    private void initialize() {
        if (initialized) {
            return;
        }
        try {
            PaginationSupport<T> page = pages.specified(currentPage, currentSize);
            super.setContent(page);
        } catch (ProcessBizException e) {
            super.setCode(e.getCode().getCode());
            super.setError(e.getError());
        } finally {
            initialized = true;
        }
    }
}

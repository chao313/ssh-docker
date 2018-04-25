package demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo;

import com.google.common.base.Optional;

import javax.ws.rs.QueryParam;

import io.swagger.annotations.ApiParam;

/**
 * *****************************************************************************
 * <p>
 * 功能名           ：demo.spring.boot.demospringboot.sdxd.common.api.common.web.vo
 * 系统名           ：
 * <p>
 * *****************************************************************************
 * Modification History
 * <p>
 * Date        Name                    Reason for Change
 * ----------  ----------------------  -----------------------------------------
 * 16/12/6     melvin                 Created
 */
public class PageParameter extends RestRequest {

    @ApiParam(value = "页码")
    @QueryParam(value = "page_no")
    private Integer pageNo;
    @ApiParam(value = "页大小")
    @QueryParam(value = "page_size")
    private Integer pageSize;
    @ApiParam(value = "当前总条数")
    @QueryParam(value = "current_total_count")
    private Integer currentTotalCount;

    public Integer getPageNo() {
        return Optional.fromNullable(pageNo).or(1);
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return Optional.fromNullable(pageSize).or(15);
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getCurrentTotalCount() {
        return Optional.fromNullable(currentTotalCount).orNull();
    }

    public void setCurrentTotalCount(Integer currentTotalCount) {
        this.currentTotalCount = currentTotalCount;
    }
}

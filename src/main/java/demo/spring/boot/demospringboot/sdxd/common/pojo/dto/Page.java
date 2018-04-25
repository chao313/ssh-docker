package demo.spring.boot.demospringboot.sdxd.common.pojo.dto;

import java.util.List;

import lombok.Data;

/**
 *
 * 类名称：Page
 * 类描述：
 * 创建人：qiuyangjun
 * 修改人：qiuyangjun
 * 修改时间：2014年10月28日 下午4:49:37
 * 修改备注：
 * @version 1.0.0
 *
 */
@Data
@SuppressWarnings("all")
public class Page {
    
	private int recordsTotal;
	private List<?> data;
    private int currentPage;
    private int pageCount;
    private int pageSize;

    public int getPageCount(){
        return recordsTotal%this.getPageSize()==0? (recordsTotal/this.getPageSize()):(recordsTotal/this.getPageSize()+1);
    }
}

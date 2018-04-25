package demo.spring.boot.demospringboot.sdxd.framework.mongodb;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class Page<T> implements Serializable{

	private Long total;
	private Integer pageNumber;
	private Integer pageSize;
	private List<T> rows;
	
	
}

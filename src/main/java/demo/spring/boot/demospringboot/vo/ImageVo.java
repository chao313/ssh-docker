package demo.spring.boot.demospringboot.vo;

import lombok.Data;
import lombok.ToString;

/**
 * 2018/4/17    Created by   chao
 */

@Data
@ToString
public class ImageVo {
    private String REPOSITORY;

    private String TAG;

    private String IMAGE_ID;

    private String CREATED;

    private String SIZE;
}

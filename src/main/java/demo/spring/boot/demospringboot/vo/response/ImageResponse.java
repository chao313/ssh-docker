package demo.spring.boot.demospringboot.vo.response;

import lombok.Data;
import lombok.ToString;

/**
 * 2018/4/30    Created by   chao
 */
@Data
@ToString
public class ImageResponse {
    private Long created;

    private String id;

    private String parentId;

    private String[] repoTags;

    private Long size;

    private Long virtualSize;

    private String app;
}

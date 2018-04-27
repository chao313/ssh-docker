package demo.spring.boot.demospringboot.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.ToString;

/**
 * 2018/4/27    Created by   chao
 */
@Entity
@Table(name = "t_host")
@Data
@ToString
public class HostVo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String hostIp;

    private Integer port;

    private String account;

    private String password;

    private String userId;

    @Column(columnDefinition = "TIME NOT NULL DEFAULT CURRENT_TIME", updatable = false)
    @Temporal(TemporalType.TIME)
    private Date createTime;

    @Column(columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

}

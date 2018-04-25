package demo.spring.boot.demospringboot.vo;

import java.sql.Time;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

/**
 * 2018/4/23    Created by   chao
 */
@Data
@ToString
@Entity
@Table(name = "t_user")
public class UserVo {
    @Id
    private String id;
    private String dockerPassId;
    private String salt;//随机生成的加密密钥
    private String password;//记录的是加密后的password
    private Timestamp createTime;
    private Timestamp updateTime;
}

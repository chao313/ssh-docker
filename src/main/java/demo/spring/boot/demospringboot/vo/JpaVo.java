package demo.spring.boot.demospringboot.vo;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 自动化生成table
 * @Entity
 */
@Entity
@Table(name="jpa_vo")
public class JpaVo {
    /**
     * 使用@Id 来指定追歼
     * 使用@GeneratedValue 来设置生成策略
     */
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    private String password;

    public JpaVo() {
    }

    public JpaVo(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

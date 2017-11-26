package demo.spring.boot.demospringboot;


import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BannerOffApplication {

    public static void main(String[] args){
        SpringApplication springApplication = new SpringApplication(BannerOffApplication.class);

        //在这里关闭 banner
        springApplication.setBannerMode(Banner.Mode.OFF);
        springApplication.run(args);
    }
}

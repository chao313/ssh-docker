package demo.spring.boot.demospringboot.command;

/**
 * 2018/4/3    Created by   chao
 */
public interface HOSTCommand {
    interface Service {
        String DockerServiceStart = "systemctl start docker.service";
    }
}

package demo.spring.boot.demospringboot.command.options.stream;

/**
 * 2018/4/15    Created by   chao
 */

/**
 * @command docker info [OPTIONS]
 * @description 显示 Docker 系统信息，包括镜像和容器数
 */
public class Base {
    String FORMAT = "容器数量{{.Container}} 运行数量{{.Running}} 暂停数量{{.Paused}} 停止数量{{.Stopped}}";

    public enum options {
        _f(" -f "),
        __format(" --format ");
        private String option;

        options(String option) {
            this.option = option;
        }
    }

    private String buffer = "docker info ";

    public Base addOPtions(Base.options options) {
        this.buffer = this.buffer.concat(options.option);
        return this;
    }

    @Override
    public String toString() {
        return this.buffer;
    }
}

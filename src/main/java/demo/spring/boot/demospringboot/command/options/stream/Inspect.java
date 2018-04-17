package demo.spring.boot.demospringboot.command.options.stream;

/**
 * @command docker inspect [OPTIONS] NAME|ID [NAME|ID...]
 * @description 获取容器/镜像的元数据
 * @OPTIONS -f :指定返回值的模板文件
 * @OPTIONS -s :显示总的文件大小
 * @OPTIONS --type :为指定类型返回JSON
 */

public class Inspect {
    public enum options {
        _s(" -s "),
        _f(" -f "),
        __type(" --type ");
        private String option;

        options(String option) {
            this.option = option;
        }
    }

    private String buffer = "docker inspect ";

    public Inspect addOPtions(Inspect.options options) {
        this.buffer = this.buffer.concat(options.option);
        return this;
    }

    public Inspect(String containerName) {
        this.buffer += containerName;
    }

    public Inspect() {
    }

    @Override
    public String toString() {
        return this.buffer;
    }
}

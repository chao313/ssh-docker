package demo.spring.boot.demospringboot.command.options.stream;


/**
 * @command docker ps [OPTIONS]
 * @description 列出容器
 * @OPTIONS -a :显示所有的容器，包括未运行的
 * @OPTIONS -f :根据条件过滤显示的内容
 * @OPTIONS --format :指定返回值的模板文件
 * @OPTIONS -l :显示最近创建的容器
 * @OPTIONS -n :列出最近创建的n个容器
 * @OPTIONS --no-trunc :不截断输出
 * @OPTIONS -q :静默模式，只显示容器编号
 * @OPTIONS -s :显示总的文件大小
 */
public class PS {

    public enum options {
        _a(" -a "),
        _f(" -f "),
        __format(" --format "),
        _l(" -l "),
        _n(" -n "),
        __no_trunc(" --no-trunc "),
        _q(" -q "),
        _s(" -s ");
        private String option;

        options(String option) {
            this.option = option;
        }
    }

    private String buffer = "docker ps ";

    public PS addOPtions(PS.options options) {
        this.buffer = this.buffer.concat(options.option);
        return this;
    }

    @Override
    public String toString() {
        return this.buffer;
    }
}

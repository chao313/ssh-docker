package demo.spring.boot.demospringboot.command.options.stream;

/**
 * 2018/4/4    Created by   chao /**
 *
 * @options -a         :列出本地所有的镜像（含中间映像层，默认情况下，过滤掉中间映像层）
 * @options --digests  :显示镜像的摘要信息
 * @options -f         :显示满足条件的镜像
 * @options --format   :指定返回值的模板文件
 * @options --no-trunc :显示完整的镜像信息
 * @options -q         :只显示镜像ID
 * @command docker images [OPTIONS] [REPOSITORY[:TAG]]
 * @description 列出本地镜像
 */


public class Images {
    public enum options {
        _a(" -a "),
        __digests(" --digests "),
        _f(" -f "),
        __format(" --format "),
        __no_trunc(" --no-trunc "),
        _q(" -q ");
        private String option;

        options(String option) {
            this.option = option;
        }
    }

    private String buffer = "docker images ";

    public Images addOPtions(Images.options options) {
        this.buffer = this.buffer.concat(options.option);
        return this;
    }

    @Override
    public String toString() {
        return this.buffer;
    }
}

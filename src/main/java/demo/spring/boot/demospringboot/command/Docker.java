package demo.spring.boot.demospringboot.command;

/**
 * 2018/4/3    Created by   chao
 */
public interface Docker {

    interface Base {
        /**
         * @command docker info [OPTIONS]
         * @description 显示 Docker 系统信息，包括镜像和容器数
         */
        String DockerInfo = "docker info";

        /**
         * @command docker version [OPTIONS]
         * @description 显示 Docker 版本信息
         * @options -f :指定返回值的模板文
         */
        String DockerVersion = "docker version";
    }

    interface LoaclImages {

        /**
         * @options -a         :列出本地所有的镜像（含中间映像层，默认情况下，过滤掉中间映像层）
         * @options --digests  :显示镜像的摘要信息
         * @options -f         :显示满足条件的镜像
         * @options --format   :指定返回值的模板文件
         * @options --no-trunc :显示完整的镜像信息
         * @options -q         :只显示镜像ID
         * @command docker images [OPTIONS] [REPOSITORY[:TAG]]
         * @description 列出本地镜像
         */
        String Docker_Images = "docker images";

        /**
         * @command docker rmi [OPTIONS] IMAGE [IMAGE...]
         * @descriptiion 删除本地一个或多少镜像。
         * @OPTIONS -f :强制删除
         * @OPTIONS --no-prune :不移除该镜像的过程镜像，默认移除
         */
        String Docker_Rmi = "docker rmi";

        /**
         * @command docker tag [OPTIONS] IMAGE[:TAG] [REGISTRYHOST/][USERNAME/]NAME[:TAG]
         * @description 标记本地镜像，将其归入某一仓库
         */
        String Docker_Tag = "docker tag";

        /**
         * @command docker history [OPTIONS] IMAGE
         * @description 查看指定镜像的创建历史
         * @OPTIONS -H :以可读的格式打印镜像大小和日期，默认为true
         * @OPTIONS --no-trunc :显示完整的提交记录
         * @OPTIONS -q :仅列出提交记录ID
         */
        String DOCKER_HISTORY = "docker history ";

        /**
         * @command docker import [OPTIONS] file|URL|- [REPOSITORY[:TAG]]
         * @description 将指定镜像保存成 tar 归档文件 docker save : 将指定镜像保存成 tar 归档文件
         *
         * OPTIONS说明： -o :输出到的文件。
         */
        String DOCKER_SAVE = "docker images";


        /**
         * @command docker import [OPTIONS] file|URL|- [REPOSITORY[:TAG]]
         * @description 从归档文件中创建镜像
         * @OPTIONS -c :应用docker 指令创建镜像
         * @OPTIONS -m :提交时的说明文字
         */
        String DOCKER_IMPORT = "docker import";
    }


    interface Repository {

        /**
         * @commond docker login [OPTIONS] [SERVER]
         * @description 登陆到一个Docker镜像仓库，如果未指定镜像仓库地址，默认为官方仓库 Docker Hub
         * @OPTIONS -u :登陆的用户名
         * @OPTIONS -p :登陆的密码
         */
        String DOCKER_LOGIN = " docker login ";

        /**
         * @commond docker out [OPTIONS] [SERVER]
         * @description 登出一个Docker镜像仓库，如果未指定镜像仓库地址，默认为官方仓库 Docker Hub
         */
        String DOCKER_OUT = " docker out ";


        /**
         * @command docker pull [OPTIONS] NAME[:TAG|@DIGEST]
         * @description 从镜像仓库中拉取或者更新指定镜像
         * @OPTIONS -a :拉取所有 tagged 镜像
         * @OPTIONS --disable-content-trust :忽略镜像的校验,默认开启
         */
        String DOCKER_PULL = " docker pull ";


        /**
         * @command docker push [OPTIONS] NAME[:TAG]
         * @description 将本地的镜像上传到镜像仓库, 要先登陆到镜像仓库
         * @OPTIONS --disable-content-trust :忽略镜像的校验,默认开启
         */
        String DOCKER_PUSH = " docker push ";

        /**
         * @command docker search [OPTIONS] TERM
         * @description 从Docker Hub查找镜像
         * @OPTIONS --automated :只列出 automated build类型的镜像
         * @OPTIONS --no-trunc :显示完整的镜像描述
         * @OPTIONS -s :列出收藏数不小于指定值的镜像
         */

        String DOCKER_SEARCH = " docker search ";


    }


    interface Rootfs {
        /**
         * @command docker commit [OPTIONS] CONTAINER [REPOSITORY[:TAG]]
         * @description 从容器创建一个新的镜像
         * @OPTIONS -a :提交的镜像作者
         * @OPTIONS -c :使用Dockerfile指令来创建镜像
         * @OPTIONS -m :提交时的说明文字
         * @OPTIONS -p :在commit时，将容器暂停
         */
        String DOCKER_COMMIT = " docker commit ";


        /**
         * @command docker cp [OPTIONS] CONTAINER:SRC_PATH DEST_PATH|
         * @command docker cp [OPTIONS] SRC_PATH|- CONTAINER:DEST_PATH
         * @description 用于容器与主机之间的数据拷贝
         * @OPTIONS -L :保持源目标中的链接
         */
        String DOCKER_CP = " docker cp ";

        /**
         * @command docker diff [OPTIONS] CONTAINER
         * @description 检查容器里文件结构的更改
         */
        String DOCKER_DIFF = " docker diff ";
    }

    interface Container {
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
        String DOCKER_PS = " docker ps ";


        /**
         * @command docker inspect [OPTIONS] NAME|ID [NAME|ID...]
         * @description 获取容器/镜像的元数据
         * @OPTIONS -f :指定返回值的模板文件
         * @OPTIONS -s :显示总的文件大小
         * @OPTIONS --type :为指定类型返回JSON
         */
        String DOCKER_INSPECT = " docker inspect ";

        /**
         * @command docker top [OPTIONS] CONTAINER [ps OPTIONS]
         * @description 查看容器中运行的进程信息，支持 ps 命令参数
         */
        String DOCKER_TOP = " docker top ";


        /**
         * @command docker attach [OPTIONS] CONTAINER
         * @description 连接到正在运行中的容器
         * @OPTIONS -sig-proxy=false :链接容器后CTRL+C不会导致容器结束
         */
        String DOCKER_ATTACH = " docker attach ";

        /**
         * @command docker events [OPTIONS]
         * @description 从服务器获取实时事件
         * @OPTIONS -f ：根据条件过滤事件
         * @OPTIONS --since ：从指定的时间戳后显示所有事件
         * @OPTIONS --until ：流水时间显示到指定的时间为
         * @NOTICE 如果指定的时间是到秒级的，需要将时间转成时间戳。如果时间为日期的话，可以直接使用，如--since="2016-07-01"
         */
        String DOCKER_EVENTS = " docker events ";

        /**
         * @command docker logs [OPTIONS] CONTAINER
         * @description 获取容器的日志
         * @OPTIONS -f : 跟踪日志输出 follow
         * @OPTIONS --since :显示某个开始时间的所有日志
         * @OPTIONS -t : 显示时间戳
         * @OPTIONS --tail :仅列出最新N条容器日志
         */
        String DOCKER_LOGS = " docker logs ";

        /**
         * @command docker wait [OPTIONS] CONTAINER [CONTAINER...]
         * @description 阻塞运行直到容器停止，然后打印出它的退出代码
         */
        String DOCKER_WAIT = " docker wait ";


        /**
         * @command docker export [OPTIONS] CONTAINER
         * @description 将文件系统作为一个tar归档文件导出到STDOUT
         * @OPTIONS -o :将输入内容写到文件。
         */
        String DOCKER_EXPORT = " docker export ";

        /**
         * @command docker port [OPTIONS] CONTAINER [PRIVATE_PORT[/PROTO]]
         * @description 列出指定的容器的端口映射，或者查找将PRIVATE_PORT NAT到面向公众的端口
         */
        String DOCKER_PORT = " docker port ";


    }

    interface ContainerLifeCycle {

        /**
         * @command docker run [OPTIONS] IMAGE [COMMAND] [ARG...]
         * @description 将文件系统作为一个tar归档文件导出到STDOUT
         * @OPTIONS -a stdin: 指定标准输入输出内容类型，可选 STDIN/STDOUT/STDERR 三项
         * @OPTIONS -d: 后台运行容器，并返回容器ID
         * @OPTIONS -i: 以交互模式运行容器，通常与 -t 同时使用
         * @OPTIONS -t: 为容器重新分配一个伪输入终端，通常与 -i 同时使用
         * @OPTIONS --name="nginx-lb": 为容器指定一个名称
         * @OPTIONS --dns 8.8.8.8: 指定容器使用的DNS服务器，默认和宿主一致
         * @OPTIONS --dns-search example.com: 指定容器DNS搜索域名，默认和宿主一致
         * @OPTIONS -h "mars": 指定容器的hostname
         * @OPTIONS -e username="ritchie": 设置环境变量
         * @OPTIONS --env-file=[]: 从指定文件读入环境变量
         * @OPTIONS --cpuset="0-2" or --cpuset="0,1,2": 绑定容器到指定CPU运行
         * @OPTIONS -m :设置容器使用内存最大值
         * @OPTIONS ---net="bridge": 指定容器的网络连接类型，支持 bridge/host/none/container: 四种类型
         * @OPTIONS --link=[]: 添加链接到另一个容器
         * @OPTIONS --expose=[]: 开放一个端口或一组端口
         */
        String DOCKER_RUN = " docker run ";

        /**
         * @command docker create [OPTIONS] IMAGE [COMMAND] [ARG...]
         * @description 创建一个新的容器但不启动它
         * @OPTIONS -a stdin: 指定标准输入输出内容类型，可选 STDIN/STDOUT/STDERR 三项
         * @OPTIONS -d: 后台运行容器，并返回容器ID
         * @OPTIONS -i: 以交互模式运行容器，通常与 -t 同时使用
         * @OPTIONS -t: 为容器重新分配一个伪输入终端，通常与 -i 同时使用
         * @OPTIONS --name="nginx-lb": 为容器指定一个名称
         * @OPTIONS --dns 8.8.8.8: 指定容器使用的DNS服务器，默认和宿主一致
         * @OPTIONS --dns-search example.com: 指定容器DNS搜索域名，默认和宿主一致
         * @OPTIONS -h "mars": 指定容器的hostname
         * @OPTIONS -e username="ritchie": 设置环境变量
         * @OPTIONS --env-file=[]: 从指定文件读入环境变量
         * @OPTIONS --cpuset="0-2" or --cpuset="0,1,2": 绑定容器到指定CPU运行
         * @OPTIONS -m :设置容器使用内存最大值
         * @OPTIONS ---net="bridge": 指定容器的网络连接类型，支持 bridge/host/none/container: 四种类型
         * @OPTIONS --link=[]: 添加链接到另一个容器
         * @OPTIONS --expose=[]: 开放一个端口或一组端口
         * @NOTICE 和run一样
         */
        String DOCKER_CREATE = " docker create ";


        /**
         * @command docker create [OPTIONS] IMAGE [COMMAND] [ARG...]
         * @description 启动一个或多少已经被停止的容器
         */
        String DOCKER_START = " docker start ";

        /**
         * @command docker stop [OPTIONS] CONTAINER [CONTAINER...]
         * @description 停止一个运行中的容器
         */
        String DOCKER_STOP = " docker stop ";

        /**
         * @command docker restart [OPTIONS] CONTAINER [CONTAINER...]
         * @description 重启容器
         */
        String DOCKER_RESTART = " docker restart ";

        /**
         * @command docker kill [OPTIONS] CONTAINER [CONTAINER...]
         * @description 杀掉一个运行中的容器
         */
        String DOCKER_KILL = " docker kill ";

        /**
         * @command docker rm [OPTIONS] CONTAINER [CONTAINER...]
         * @description 删除一个或多少容器
         * @OPTIONS -f :通过SIGKILL信号强制删除一个运行中的容器
         * @OPTIONS -l :移除容器间的网络连接，而非容器本身
         * @OPTIONS -v :-v 删除与容器关联的卷
         */
        String DOCKER_RM = " docker rm ";

        /**
         * @command docker pause [OPTIONS] CONTAINER [CONTAINER...]
         * @description 暂停容器中所有的进程
         */
        String DOCKER_PAUSE = " docker pause ";

        /**
         * @command docker unpause [OPTIONS] CONTAINER [CONTAINER...]
         * @description 恢复容器中所有的进程
         */
        String DOCKER_UNPAUSE = " docker unpause ";


        /**
         * @command docker exec [OPTIONS] CONTAINER COMMAND [ARG...]
         * @description 在运行的容器中执行命令
         * @OPTIONS -d :分离模式: 在后台运行
         * @OPTIONS -i :即使没有附加也保持STDIN 打开
         * @OPTIONS -t :分配一个伪终端
         */
        String DOCKER_EXEC = " docker exec ";


    }

    interface DockerFile {
        interface Bulid {

            String DockerImages = "docker images";
        }

    }
}

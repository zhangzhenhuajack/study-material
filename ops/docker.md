## docker 工作中常用的命令，不同的版本可能有微妙差异
#### 当前使用的版本
`docker  version`

````
 Client: Docker Engine - Community
 Cloud integration  0.1.18
 Version:           19.03.13
 API version:       1.40
 Go version:        go1.13.15
 Git commit:        4484c46d9d
 Built:             Wed Sep 16 16:58:31 2020
 OS/Arch:           darwin/amd64
 Experimental:      false
````
#### docker help 查看帮助手册
#### docker build -h  查看当前命令的详细参数及其帮助手册
#### 删除所有正在运行的容器
`docker ps -a | xargs docker rm -f`  或者 `docker rm $(docker ps -aq)`
#### 删除本地所有镜像，本地没空间了，清理镜像用
`docker rmi $(docker images -q) -f`
#### 反向查询一个镜像的构建历史，既可以用来推导原来的Dockerfile,但是COPY的内容会不准
`docker history registry-mirrors.aliyun.com/semantic-release-gitlab:1.0.0 --no-trunc=true`
#### 拉取镜像到本地
`docker pull registry-mirrors.aliyun.com/ops/aliyun:latest`
#### --rm 运行一个镜像，并且推出的时候，同时stop容器，并删除容器运行产生的文件，并且覆盖CMD传递参数
`docker run -it --rm aliyun:2.3 oss sync help`
#### 运行一个镜像，并且推出的时候，同时stop容器，覆盖Entrypoint，注意entrypoint的参数顺序
`docker run  -it --entrypoint sh --rm aliyun:latest`
#### 运行一个镜像，挂载一个本地目录进入到容器里面
`docker run --mount type=bind,source=/Users/jack/abc,target=/mnt/data -it --entrypoint sh --rm aliyun:latest`
#### 根据指定的dockerfile 编译一个指定的tag名字
`docker build -f Dockerfile-aliyun -t  registry-mirrors.aliyun.com/ops/aliyun:1.0.0 .`
#### 生成镜像的时候给镜像传递参数，给ARG使用
`docker build -f Dockerfile-aliyun -t registry-mirrors.aliyun.com/ops/aliyun:1.0.0 . --build-arg AccessKeyId=AK --build-arg AccessKeyId=xxxx AccessKeySecret=yyyyyy`
#### docker 登录到docker的服务器上
`docker login -u myusername -p mypassword registry-mirrors.aliyun.com`
#### 将编译好的镜像推到服务器上
`docker push registry-mirrors.aliyun.com/ops/aliyun:1.0.0`
#### 重新给镜像绑定一个新的tag
`docker tag aliyun:1.0.0 registry-mirrors.aliyun.com/ops/aliyun:1.0.0-mynew`
#### 进入到正在运行的一个docker 容器里面去
`docker exec -it  cdde783e2818 bash`
#### attach 到一个正在运行的容器里面，需要注意的是，attach进去之后，退出会stop容器，建议尽量用exec。加上--no-stdin就不会退出了，并且进去之后不能做任何操作
`docker attach --no-stdin ff8a66aa1e58`  
#### -p 端口映射，8080服务器端口，8080为容器端口;-d 是后台运行
`docker run -d -p 8080:8080 tomcat:8.0.1`
#### docker cp文件到本地
`docker cp containerID:container_path host_path`
#### 查看一个docker正在运行的日志
`docker logs -f 72e797ba3e73`
#### 对一个正在进行的容器进行端口查看，容器内部的端口的映射是什么样的
`docker port 72e797ba3e73 80
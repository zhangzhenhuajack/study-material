> 摘自：https://alexei-led.github.io/post/k8s_node_shell/
## 首先我们按照如下步骤就可以进入到node里面：
````
└─(09:56:20)──> kubectl get node -o wide                                                                                                                                                                                                                ──(Thu,Dec23)─┘
NAME                           STATUS   ROLES    AGE    VERSION            INTERNAL-IP      EXTERNAL-IP   OS-IMAGE                                                      
cn-beijing.10.129.104.154      Ready    <none>   85d    v1.18.8-aliyun.1   10.129.104.154   <none>        Alibaba Cloud Linux (Aliyun Linux)
cn-beijing.10.129.104.158      Ready    <none>   84d    v1.18.8-aliyun.1   10.129.104.158   <none>        Alibaba Cloud Linux (Aliyun Linux)
virtual-kubelet-cn-beijing-f   Ready    agent    8d     v1.18.8-aliyun.1   10.129.134.137   <none> 
┌─(~/tmp)───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────(jack@jackzhang:s001)─┐
└─(09:56:24)──> chmod a+x nsenter-node.sh                                                                                                                                                                                                               ──(Thu,Dec23)─┘
┌─(~/tmp)───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────(jack@jackzhang:s001)─┐
└─(09:56:46)──> ./nsenter-node.sh cn-beijing.10.129.104.154                                                                                                                                                                                             ──(Thu,Dec23)─┘
+ node=cn-beijing.10.129.104.154
++ kubectl get node cn-beijing.10.129.104.154 -o template '--template={{index .metadata.labels "kubernetes.io/hostname"}}'
+ nodeName=cn-beijing.10.129.104.154
+ nodeSelector='"nodeSelector": { "kubernetes.io/hostname": "cn-beijing.10.129.104.154" },'
+ podName=jack-nsenter-cn-beijing.10.129.104.154
+ kubectl run jack-nsenter-cn-beijing.10.129.104.154 --restart=Never -it --rm --image overriden --overrides '
{
  "spec": {
    "hostPID": true,
    "hostNetwork": true,
    "nodeSelector": { "kubernetes.io/hostname": "cn-beijing.10.129.104.154" },
    "tolerations": [{
        "operator": "Exists"
    }],
    "containers": [
      {
        "name": "nsenter",
        "image": "alexeiled/nsenter:2.34",
        "command": [
          "/nsenter", "--all", "--target=1", "--", "su", "-"
        ],
        "stdin": true,
        "tty": true,
        "securityContext": {
          "privileged": true
        }
      }
    ]
  }
}' --attach cn-beijing.10.129.104.154

ls
If you don't see a command prompt, try pressing enter.

[root@iZ2zefggylkva3ee7sxqsnZ ~]# ls
[root@iZ2zefggylkva3ee7sxqsnZ ~]# pwd
/root
[root@iZ2zefggylkva3ee7sxqsnZ ~]# ls
[root@iZ2zefggylkva3ee7sxqsnZ ~]# /
-bash: /: Is a directory
[root@iZ2zefggylkva3ee7sxqsnZ ~]# ps -ef
UID          PID    PPID  C STIME TTY          TIME CMD
root           1       0  4 Sep28 ?        3-21:50:09 /usr/lib/systemd/systemd --switched-root --system --deserialize 22
root           2       0  0 Sep28 ?        00:00:01 [kthreadd]
root           3       2  0 Sep28 ?        00:00:00 [rcu_gp]
[root@iZ2zefggylkva3ee7sxqsnZ ~]# ps -ef| grep nsenter
root     1829602 1829583  0 09:57 ?        00:00:00 /nsenter --all --target=1 -- su -
root     1998761 1829683  0 10:13 ?        00:00:00 grep --color=auto nsenter
[root@iZ2zefggylkva3ee7sxqsnZ /]# nsenter -h

Usage:
 nsenter [options] <program> [<argument>...]

Run a program with namespaces of other processes.

Options:
 -t, --target <pid>     target process to get namespaces from
 -m, --mount[=<file>]   enter mount namespace
 -u, --uts[=<file>]     enter UTS namespace (hostname etc)
 -i, --ipc[=<file>]     enter System V IPC namespace
 -n, --net[=<file>]     enter network namespace
 -p, --pid[=<file>]     enter pid namespace
 -U, --user[=<file>]    enter user namespace
 -S, --setuid <uid>     set uid in entered namespace
 -G, --setgid <gid>     set gid in entered namespace
     --preserve-credentials do not touch uids or gids
 -r, --root[=<dir>]     set the root directory
 -w, --wd[=<dir>]       set the working directory
 -F, --no-fork          do not fork before exec'ing <program>
 -Z, --follow-context   set SELinux context according to --target PID

 -h, --help     display this help and exit
 -V, --version  output version information and exit

For more details see nsenter(1).

````
#### 其中nsenter-node.sh 内容如下：
````
# cat ~/tmp/nsenter-node.sh                                                                                                                                                                                                     ──(Thu,Dec23)─┘
#!/bin/sh
set -x

node=${1}
## 获取nodename这里需要注意的是，我们自己的kubectl命令的context和kubectl --kubeconfig ~/.kube/prod_ali_config 是否正确
nodeName=$(kubectl get node ${node} -o template --template='{{index .metadata.labels "kubernetes.io/hostname"}}')
nodeSelector='"nodeSelector": { "kubernetes.io/hostname": "'${nodeName:?}'" },'
podName=${USER}-nsenter-${node}
## 上面几个命令就是生成一个临时podname，并获取指定的node名字参数
kubectl run ${podName:?} --restart=Never -it --rm --image overriden --overrides ' # 临时运行一个pod，退出既消失，需要注意结合自己的kubectl上下文环境
{
  "spec": {
    "hostPID": true,
    "hostNetwork": true,
    '"${nodeSelector?}"'
    "tolerations": [{
        "operator": "Exists"
    }],
    "containers": [
      {
        "name": "nsenter",
        "image": "alexeiled/nsenter:2.34", #使用nsenter镜像，对应的镜像地址：https://github.com/alexei-led/nsenter
        "command": [
          "/nsenter", "--all", "--target=1", "--", "su", "-"   # 镜像启动起来之后执行的命令
        ],
        "stdin": true,
        "tty": true,
        "securityContext": {
          "privileged": true  # pod采用 https://kubernetes.io/zh/docs/concepts/policy/pod-security-policy/#privileged
        }
      }
    ]
  }
}' --attach "$@"

````
## 第二步：我们解析一下里面的原理：这样我们就可以知道临时运行一个这样的pod是否会造成生产事故
#### 1）我们熟悉kubernate的同学可能就会知道，pod运行的时候可以指定pod的安全策略：https://kubernetes.io/zh/docs/concepts/policy/pod-security-policy/#privileged
Privileged - 决定是否 Pod 中的某容器可以启用特权模式。 默认情况下，容器是不可以访问宿主上的任何设备的，不过一个“privileged（特权的）” 容器则被授权访问宿主上所有设备。 这种容器几乎享有宿主上运行的进程的所有访问权限。 对于需要使用 Linux 权能字（如操控网络堆栈和访问设备）的容器而言是有用的。
#### 2）我们看一下alexeiled/nsenter:2.34 镜像 https://github.com/alexei-led/nsenter
通过官方github我们可以发现，其实里面很简单，我们直接看它的dockerfile看看如何构建的就知道了
````
FROM debian:buster as builder

# intall gcc and supporting packages
RUN apt-get update && apt-get install -yq make gcc gettext autopoint bison libtool automake pkg-config

WORKDIR /code

# download util-linux sources
ARG UTIL_LINUX_VER
ADD https://github.com/util-linux/util-linux/archive/v${UTIL_LINUX_VER}.tar.gz .
RUN tar -xf v${UTIL_LINUX_VER}.tar.gz && mv util-linux-${UTIL_LINUX_VER} util-linux

# make static version
WORKDIR /code/util-linux
RUN ./autogen.sh && ./configure
RUN make LDFLAGS="--static" nsenter

# Final image
FROM scratch

COPY --from=builder /code/util-linux/nsenter /

ENTRYPOINT ["/nsenter"]
````

里面利用 scratch docker镜像，也就是直接封装了 nsenter的命令而已，所以docker images大小很小：这里面其实如果我们要做为一个资深运维其实要知道我们可以利用 docker builder多阶段 、 scratch 镜像 可以减少我们的镜像大小的技巧；
````
└─(10:48:26 on master)──> docker images -a                                                                                                                                                                                                              ──(Thu,Dec23)─┘
REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
alexeiled/nsenter   2.34                62123586442f        12 minutes ago      925kB
````

#### 3）nsenter 是linux提供的进入指定namespace的util：官方手册：https://man7.org/linux/man-pages/man1/nsenter.1.html
> 我们根据官方手册就可以知道这个命令，就是我们的pod以特权方式运行起来之后，通过nsenter命令进入到进程为1的namespace里面打开all,并且执行 su - 命令；su - 命令就是以root身份运行
````
"/nsenter", "--all", "--target=1", "--", "su", "-"   
````
总结：放心大胆使用，这里面牵涉到几个基础知识k8s的pod的安全策略应用，linux底层的namespace原理，以及nsenter、su命令的使用。如何减少docker image的技巧。https://yeasy.gitbook.io/docker_practice/image/build 
提示：我们发现一个问题深入研究，养成这样的习惯，时间越长，遇到的问题越多我们成长的才会越快；





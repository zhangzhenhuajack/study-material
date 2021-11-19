## 工作中常用linux命令
#### 1. ssh流量转发，制作跳板机方案
ssh -L [收听接口:]收听端口:目标主机:目标端口 username@hostname 默认命令中方括号内的部分，即第一个参数可以不写；它的默认值一般是0.0.0.0，意味着SSH隧道会收听所有接口，接受来自任何地址的应用访问请求并进行转发
`/usr/bin/ssh -g -N -o UserKnownHostsFile /dev/null -o StrictHostKeyChecking no -o ServerAliveInterval 30 -o ServerAliveCountMax 3 -L 0.0.0.0:3306:192.168.50.199:3306 172.30.253.219 -p 22 -l tunnel -i /etc/secret-volume/ssh-privatekey`
#### 2. 利用paste 拼接列数据
`paste <(kubectl get node --context prod -o wide) <(kubectl top node --context prod)`

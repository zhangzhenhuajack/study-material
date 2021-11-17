## 工作中常用linux命令
#### 1. ssh流量转发，制作跳板机方案
`/usr/bin/ssh -g -N -o UserKnownHostsFile /dev/null -o StrictHostKeyChecking no -o ServerAliveInterval 30 -o ServerAliveCountMax 3 -L 0.0.0.0:3306:192.168.50.199:3306 172.30.253.219 -p 22 -l tunnel -i /etc/secret-volume/ssh-privatekey`

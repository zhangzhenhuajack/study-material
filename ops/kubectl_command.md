## 工作中常用的kubectl命令
#### 1. 临时运行一个pod在一个namespace下面，exit之后pod会自动删除 
`kubectl --kubeconfig ~/.kube/prod_ali_config -n picturebook-parent run -it --rm --restart=Never node-lizhanwei --image=node:10 --image-pull-policy=IfNotPresent  --command bash`
#### 2. 扩容、缩容命令
`kubectl scale --replicas=0 deployment/rds-exporter-production-api -n prometheus --context  test`
#### 3. 缩容所有的tiller
`kubectl get deployment --all-namespaces --context prod |grep tiller-deploy | grep -v 0/0 | awk '{print $1}' | xargs -I {} kubectl scale --replicas=0 deployment/tiller-deploy -n {} --context prod`
#### 4. 查看剩余的pod上面的label
`kubectl get pod --all-namespaces --context prod -o wide | grep -v tiller | awk '{print $1}' | sort | uniq | xargs -I {} kubectl --context prod get ns {} -o jsonpath="{['.metadata.name','.metadata.labels']}{'\n'}"`
#### 5. 映射pod的80端口到本地的9000端口
`kubectl port-forward pod/wx-aimall-beta-ali-wx-aimall-6684c64854-xl6pw  9000:80 -n wx-aimall`
#### 6. HPA按照利用率排序
`kubectl --kubeconfig ~/.kube/prod_ali_config get hpa --all-namespaces  --sort-by=.status.currentCPUUtilizationPercentage`
#### 7. 查看 对应 deployment的 cpu 内存 
`kubectl get deployment --all-namespaces --context prod |grep -v 0/0 |grep -v kube-system |awk '{print "get deployment "$2 " -n "$1}' | xargs -n 5  kubectl --context prod -o jsonpath='{.metadata.name}{"\t"}{.spec.template.spec.containers[0].resources.requests.cpu}{"\t"}{.spec.template.spec.containers[0].resources.requests.memory}{"\t"}{.status.availableReplicas}{"\n"}' `
#### 8. 查看node的情况
`paste <(kubectl get node --context prod -o wide) <(kubectl top node --context prod)`
#### 9. 禁止pod调度到node上
`kubectl crodon node ip-172-31-48-151.cn-north-1.compute.internal --context prod`
#### 10. node排水
`kube_prod drain ip-172-31-51-41.cn-north-1.compute.internal --ignore-daemonsets --delete-local-data`
#### 11. 删除node
`kubectl delete node ip-172-31-51-41.cn-north-1.compute.internal`
#### 12. cp文件到本地
`kubectl cp aoc-admin-beta-ali-aoc-admin-7f565f467f-vcp8k:/Command_Line_Tools_for_Xcode_12.2.dmg ./Command_Line_Tools_for_Xcode_12.2.dmg -n aoc-admin`
#### 13. 去掉master的污点，方便node节点上的资源扩容到master上，极端情况节省node资源；以及恢复master的污点
`kubectl taint node ip-172-31-41-213.cn-north-1.compute.internal  node-role.kubernetes.io/master-`
`kubectl taint node ip-172-31-41-213.cn-north-1.compute.internal  node-role.kubernetes.io/master:NoSchedule`
#### 14. 修改HPA的最小值、最大值
`kubectl -n alo7-proxy-nginx  patch hpa nginx-beta-ali-nginx   --patch '{"spec":{"minReplicas":1}}'`
` kubectl patch hpa sentry-web  -p '{"spec": {"minReplicas": 0,"maxReplicas":0}}' --context prod -n sentry`
#### 15. helm还原发布历史
`~/company/www/helm/helm2.9/helm rollback elasticsearch 6 --tiller-namespace picturebook-parent`
#### 16. 给namespace添加label，例如添加ECI支持，动态容器扩容
` kubectl label namespace aoc-aggregator alibabacloud.com/eci=true`
#### 17. 通过set命令升级镜像的版本号
`kubectl set image deployments/tiller-deploy tiller=registry-vpc.cn-beijing.aliyuncs.com/alo7/tiller:v2.16.3 -n kube-system`
#### 18. Show all Failed/Evicted Pods
`kubectl get pod --all-namespaces --field-selector=status.phase==Failed`
#### 19. Delete all Failed/Evicted Pods
`kubectl delete pod --all-namespaces --field-selector=status.phase==Failed`
#### 20. 给node 添加污点、删除污点
`kubectl taint nodes mixbio-k8s-worker-3 gwas=:NoSchedule`
`kubectl taint nodes mixbio-k8s-worker-3 gwas-`
#### 21. 给node添加 标签、删除标签
`kubectl label node mixbio-k8s-worker-3 gwas=true`
`kubectl label node mixbio-k8s-worker-3 gwas-`




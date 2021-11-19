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

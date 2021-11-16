## 工作中常用的kubectl命令
1. 临时运行一个pod在一个namespace下面，exit之后pod会自动删除 
`kubectl --kubeconfig ~/.kube/prod_ali_config -n picturebook-parent run -it --rm --restart=Never node-lizhanwei --image=node:10 --image-pull-policy=IfNotPresent  --command bash`
2. 扩容、缩容命令
`kubectl scale --replicas=0 deployment/rds-exporter-production-api -n prometheus --context  test`

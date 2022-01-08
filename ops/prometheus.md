#### prometheus常用表达式
1. 查看某个ingress 的host 的访问流量,及其状态码
`sum by(ingress,path,exported_namespace,status) (increase(nginx_ingress_controller_request_size_count{host="h5.alo7.com"}[2m])>0)`
1. 查看某个ingress的host的异常请求状态码分布
`sum(increase(nginx_ingress_controller_request_size_count{namespace=~"aot-student-api|aot-operation-api|aot-class-report", status=~"^(400｜499|5.*)"}[2m])) by (host, path, method, status) > 0`
1. 查看ingress请求响应时间的分位图,例如.95分位
`histogram_quantile(0.95, sum(rate(nginx_ingress_controller_request_duration_seconds_bucket{ingress!="",controller_class=~"$controller_class",controller_namespace=~"$controller_namespace",ingress=~"$ingress"}[2m])) by (le, ingress))`
2. 查看pod对应的cpu的浪费情况
`(sum by(pod_name) (kube_pod_container_resource_requests_cpu_cores{namespace="$namespace",pod_name!=""}>0) - sum (rate (container_cpu_usage_seconds_total{image!="",namespace="$namespace"}[1m])>0) by (pod_name)) > 0.1`
3. 查看pod对应的cpu使用情况
`sum (rate (container_cpu_usage_seconds_total{image!="",namespace="$namespace",pod_name="$podname"}[1m])>0) by (pod_name))`
4. 查看对应的pod的cpu request情况
`sum by(pod_name) (kube_pod_container_resource_requests_cpu_cores{namespace="$namespace",pod_name!=""}>0)`
5. 查看对应的pod的memory申请的内存是多少
`sum(kube_pod_container_resource_requests_memory_bytes{namespace="$namespace",pod_name="$Pod"}) by(pod_name)`
6. 查看对应的pod浪费的内存是多少
`sum(kube_pod_container_resource_requests_memory_bytes{namespace="$namespace",pod_name="$Pod"}) by(pod_name) - sum(container_memory_working_set_bytes{id!="/",image!="",namespace="$namespace",pod_name="$Pod"}) by (pod_name)`


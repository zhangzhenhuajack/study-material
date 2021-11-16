## prometheus常用表达式
1. 查看某个ingress 的host 的访问流量
`某个域名的请求sum by(ingress,path,exported_namespace,status) (increase(nginx_ingress_controller_request_size_count{host="h5.alo7.com"}[2m])>0)`

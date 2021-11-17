1. 利用环境变量替换成aa.yaml 同时
````
cat ../../../git/assessment-report/aa.yaml                                                                                                                                                                        1 ↵ ──(Tue,Nov16)─┘
# envsubst < aa.yaml | ~/company/www/helm/helm2.9/helm --tiller-namespace assessment upgrade --install --wait --namespace=assessment -f - cat-wechat-client-production-ali  alo7/general-frontend
image:
  repository: git-registry-ali.saybot.net/cat/assessment-report/production
  tag: d7c69917fc7891d0c32221cfa10c6103337e56e1
imageCredentials:
  username: $IMAGE_CREDENTIALS_USERNAME
  password: $IMAGE_CREDENTIALS_PASSWORD
ingress:
  hosts: ["h5.alo7.com"]
  path: /assessment/report
  https: false
  annotations:
    kubernetes.io/ingress.class: nginx
gitlab:
  app: assessment-report
  env: production
````
`envsubst < aa.yaml | ~/company/www/helm/helm2.9/helm --tiller-namespace assessment upgrade --install --wait --namespace=assessment -f - learn-h5-k8s-production-h5  alo7/h5-ingress`

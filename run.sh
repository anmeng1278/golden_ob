
关闭
setenforce 0
getenforce


编译打包
docker run -it --rm -v "$PWD":/usr/src/mymaven -v "$HOME/.m2":/root/.m2 -v "$PWD:/usr/src/mymaven" -w /usr/src/mymaven maven mvn clean package -Dmaven.test.skip=true

构建docker镜像
docker build -t 192.168.31.200:9911/member/member-ob:latest .

运行实例
docker run -it -d -p 8000:8000 -v /usr/local/src/member-ob/src/:/app/src 3b


发布
1.编辑daemon.json文件
vim /etc/docker/daemon.json

{
  "registry-mirrors": ["https://2su76jbp.mirror.aliyuncs.com"],
  "insecure-registries": ["192.168.31.200:9911"]
}

2.重启docker
systemctl daemon-reload
systemctl restart docker

3.登录
docker login 192.168.31.200:9911
admin
admin123

4.发布
docker push 192.168.31.200:9911/member/member-ob:latest


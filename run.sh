
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
***
***

4.发布
docker push 192.168.31.200:9911/member/member-ob:latest


yml 脚本

stages:
- build

build_ob:
  stage: build
  tags:
    - build
  only:
    - master.v2
  script:
    - echo "开始编译"
    - echo $PWD
    - mvn clean package -DskipTests

    - echo "开始发布"
    - pname="memberob"
    - if [ $(docker ps -a -q -f "name=test-$pname" | wc -l) -eq 1 ] ;
    -   then docker stop $(docker ps -a -q -f "name=test-$pname") && docker rm $(docker ps -a -q -f "name=test-$pname") ;
    - fi
    - docker run -d -v "$PWD/target/":/usr/src/myapp -e TZ="Asia/Shanghai" -p 8090:80 --name "test-$pname"  -w /usr/src/myapp  docker.io/java java -jar memberob.jar --spring.profiles.active=pub -Duser.timezone=Asia/Jerusalem
    - echo "发布成功"


build_deploy:
  stage: build
  tags:
    - build
  only:
    - master.deploy
  script:
    - echo "开始编译"
    - echo $PWD
    - echo "开始发布"
    - pname="memberob"

    - docker run -i --rm -v /opt/.m2:/root/.m2 -v $PWD:/usr/src/mymaven -w /usr/src/mymaven maven:3.5.3-jdk-8 mvn clean package -DskipTests
    - echo "编译并打包服务 [OK]"

    - docker build --no-cache -t domain:9911/member/ob:latest .
    - echo "构建镜像 [OK]"

    - docker push domain:9911/member/ob:latest
    - echo "推送镜像 [OK]"

    - if [[ $(docker ps -f "name=${pname}" -q | wc -l) -ne 0 ]]; then
    -   docker stop $(docker ps -f "name=${pname}" -q)
    - fi

    - docker run -d --rm -p 8090:80 -e RUN_MODEL="--spring.profiles.active=pub" --name ${pname} domain:9911/member/ob:latest

    - echo "服务创建 [OK]"






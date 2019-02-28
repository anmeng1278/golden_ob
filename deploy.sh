#!/bin/bash

# Java 服务发布脚本
# 此脚本包含了测试与生产环境的发布过程，基于docker容器进行服务的发布和维护

# 使用说明
# 将deploy.sh放在项目的根目录，和.gitlab-ci.yml放在一起
# deploy.sh接受4个参数
# g: 业务组名，全公司唯一的标识，例如空铁组ktgj
# m: 运行环境/模式，目前两个可选参数dev,prod，分别代码测试环境和生产环境
# P: 开放的端口号
# v: 构建的docker镜像版本，推荐都默认使用latest
# n: 服务的名称，我们约定，服务的名称，模块的名称，文件名称请保持一致

# 调用：bash deploy.sh -g 业务组名 -m 环境 -p 端口号 -v 镜像版本号 -n 服务名称
# 测试环境发布
# 申请到服务器后，在服务器上安装docker，并如下运行deploy.sh脚本
# bash deploy.sh -g 业务组名 -m 环境 -p 端口号 -v 镜像版本号 -n 服务名称
# 生产环境发布【以下操作仅在第一次上线的时候操作一次】
# * 找运维申请服务器IP，运维会给一个IP地址，然后将次IP写到deploy.sh 中的prod_local_ip字段
# * 在`http://git.jsjit.cn`自己的项目主页中`Settings => CI/CD => Runners settings => Specific Runners栏目`，
#    将token发送给运维，token样例：`RxDns--zy_pg6xqBzcen`，运维会在生产环境上注册该项目的Runner权限，此注册过程仅需一次。
#    在同样的界面中，`Runners activated for this project`栏目出现新的Runner，证明项目已经可以在线上环境部署。
#    然后CI/CD程序会自动根据项目根目录下的`.gitlab-ci.yml`配置文件自动进行发布任务

echo '***************************************'
echo '* docker swarm 集群服务部署脚本       *'
echo '***************************************'
# 关闭防火墙
setenforce 0

# 环境的服务器地址
team_group=""
server_port=0
run_model=""
env="dev"
docker_image_version="latest"


while getopts ":g:m:p:v:n:e:" opt; do
    case ${opt} in
        g)
            echo "参数team_group的值$OPTARG"
            team_group=$OPTARG
            ;;
        m)
            echo "参数model的值$OPTARG"
            run_model=$OPTARG
            ;;
        n)
            echo "参数name的值$OPTARG"
            name=$OPTARG
            ;;
        p)
            echo "参数server_port的值$OPTARG"
            server_port=$OPTARG
            ;;
        v)
            echo "参数docker_image_version的值$OPTARG"
            docker_image_version=$OPTARG
            ;;
        e)
            echo "参数evn的值$OPTARG"
            env=$OPTARG
            ;;
        ?)
            echo "未知参数"
            exit 1
            ;;
    esac
done

case ${env} in
"single")
    echo -e "\n测试环境，单节点部署..."
    dev_local_ip=$(ifconfig | grep -E 'inet.[0-9]' | grep -v '.0.1' |awk '{print $2}' )
    echo -e "测试环境自动绑定宿主ip:${dev_local_ip} [OK]"

    docker run -i --rm -v /opt/.m2:/root/.m2 -v $PWD:/usr/src/mymaven -w /usr/src/mymaven maven:3.5.3-jdk-8 mvn clean package -DskipTests
    echo "编译并打包服务 [OK]"

    # 不要忘了后面的点，指定Dockerfile文件目录
    docker build --no-cache -t maven.jsjit.cn:9911/${team_group}/${name}:${docker_image_version} .
    echo "构建镜像 [OK]"

    docker push maven.jsjit.cn:9911/${team_group}/${name}:${docker_image_version}
    echo "推送镜像 [OK]"

    if [[ $(docker ps -a -f name=^/member-ob$ -q) ]]; then
        echo "准备删除容器："$(docker ps -a -f name=^/${name}$ -q)
        docker stop $(docker ps -a -f name=^/${name}$ -q)
        docker rm   $(docker ps -a -f name=^/${name}$ -q)
    fi

    docker run -d \
                -p ${server_port}:80 \
                -e RUN_MODEL="${run_model}" \
                --name ${name} \
        maven.jsjit.cn:9911/${team_group}/${name}:${docker_image_version} ;

    echo "服务创建 [OK]"
#    if [[ $(docker images | grep "none" | wc -l) -ne 0 ]];then
#        docker rmi $(docker images | grep "none" | awk '{print $3}')
#    fi
   ;;

esac




























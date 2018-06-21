# 一、Docker

## 1、简介

**Docker**是一个开源的应用容器引擎；

Docker支持讲软件编译成一个镜像；然后再镜像中各种软件做好配置，将镜像发布出去，其他使用者可以直接使用这个镜像；

运行中的这个镜像称为容器，容器启动时非常快速的。



## 2、核心概念

docker主机（Host）：安装了Docker程序的机器（Docker直接安装在操作系统之上的）；

docker客户端（Client）：连接了docker主机进行操作；

dcoker仓库（Registry）：用来保存各种打包好的软件镜像；

docker镜像（Images）：软件打包好的镜像；放在docker仓库中；

docker容器（Container）：镜像启动后的实例称为一个容器；容器是独立运行的一个或者一组应用；

使用Docker的步骤：

1、安装Docker

2、去Docker仓库找到这个软件对应的镜像；

3、使用Docker运行这个镜像，这个镜像就会生成一个Docker容器；

4、对容器的启动停止就是对软件的启动停止



## 3、安装Docker

### 1、安装Linux虚拟机

​	1、VMWare、VitualBox（安装）；

​	2、在VitualBox上新建虚拟机，并且安装CentOS7镜像（root/123456  登录）

​	3、在Linux虚拟机上安装Docker；

​	4、使用客户端连接linux服务器，进行命令操作；

​	5、设置虚拟机网路；

​		桥接网络—>选好网卡—>接入网线；

​	6、设置好网络以后使用命令重启虚拟机的网络

```shell
service netword restart
```

​	7、查看Linux的ip地址

```shell
ip addr
```

​	8、使用客户端连接Linux

### 2、在Linux虚拟机上安装Docker

步骤

1、检查内核版本，必须是3.10以上

```shell
uname -r
```

2、安装docker

```shell
yum install docker
```

3、输入“y”，确认安装

4、启动docker

```shell
systemctl start docker
docker -v
```

5、开机启动docker

```shell
systemctl enable docker
```

6、停止docker

```shell
systemctl stop docker
```

### 3、Docker常用命令&操作

#### 1、镜像操作

| 操作 | 命令                                          | 说明                                                    |
| ---- | --------------------------------------------- | ------------------------------------------------------- |
| 检索 | docker search关键字<br>eg:docker search redis | 我们经常去docker hub上检索镜像的详细信息，如镜像的TAG   |
| 拉取 | docker pull 镜像名：tag                       | :tag是可选的，tag表示标签，多为软件的版本，默认是latest |
| 列表 | docker images                                 | 查看所有本地镜像                                        |
| 删除 | docker rmi image-id                           | 删除指定的本地镜像                                      |

https://hub.docker.com/

#### 2、容器操作

软件镜像（如：QQ安装程序）---运行镜像---产生一个容器（正在运行的软件，如：运行的QQ）；

步骤：

1、搜索镜像

```shell
docker search tomcat
```

2、拉取镜像

```shell
docker pull tomcat
```

3、根据镜像启动容器

```shell
docker run --name mytomcat -d tomcat:latest
```

4、查看运行中的容器

```shell
docker ps
```

5、停止运行中的容器

```shell
docker stop 容器的id
```

6、查看所有的容器

```shell
docker ps (-a)
```

7、启动容器

```shell
docker start 容器的id
```

8、删除一个容器

```shell
docker rm 容器的id
```

9、启动一个做了端口映射的tomcat

```shell
docker run -d -p 8888:8080 tomcat
-d：后台运行
-p：将主机的端口映射到容器的一个端口  主机端口：容器内部的端口
```

10、为了操作简单，关闭了Linux防火墙

```shell
service firewalld status；  #查看防火墙状态
systemctl stop firewalld.service;  #停止firewall
systemctl disable firewalld.service;  #禁止firewall开机启动

systemctl restart iptables.service;  #重启防火墙使配置生效
systemctl enable iptables.service;  #设置防火墙开机启动
```

11、查看容器的日志

```shell
docker logs container-name/container-id
```

#### 3、 安装MySQL示例

```shell
docker pull mysql
```

错误的启动

```shell
[root@promote ~]# docker run --name mysql01 -d mysql
82fc79256f3b0111f061345bad6c4315999010ecd151460fa6a599bf22f57824


# mysql退出
[root@promote ~]# docker ps -a
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS                      PORTS               NAMES
82fc79256f3b        mysql               "docker-entrypoint..."   45 seconds ago      Exited (1) 42 seconds ago                       mysql01
afcc6736f030        tomcat              "catalina.sh run"        2 days ago          Exited (143) 2 days ago                         naughty_pasteur


# 错误日志
[root@promote ~]# docker logs 82fc79256f3b0111f061345bad6c4315999010ecd151460fa6a599bf22f57824
error: database is uninitialized and password option is not specified 
  You need to specify one of MYSQL_ROOT_PASSWORD, MYSQL_ALLOW_EMPTY_PASSWORD and MYSQL_RANDOM_ROOT_PASSWORD  //这三个参数必须指定一个
```

正确的启动

```shell
[root@promote ~]# docker run --name mysql01 -e MYSQL_ROOT_PASSWORD=123456 -d mysql
d23cc3bfafef22233f2f2c01fcf6516fc3f0470c1bdf5942aefc6091ebe59a0e
[root@promote ~]# docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS               NAMES
d23cc3bfafef        mysql               "docker-entrypoint..."   18 seconds ago      Up 17 seconds       3306/tcp            mysql01
```

做了端口映射

```shell
[root@promote ~]# docker run -p 3360:3360 --name mysql02 -e MYSQL_ROOT_PASSWORD=123456 -d mysql
b2b055e82499c37736495c000f60e834253af72668a1fa27655745d516c49f32
[root@promote ~]# docker ps
CONTAINER ID        IMAGE               COMMAND                  CREATED             STATUS              PORTS                              NAMES
b2b055e82499        mysql               "docker-entrypoint..."   12 seconds ago      Up 11 seconds       3306/tcp, 0.0.0.0:3360->3360/tcp   mysql02
d23cc3bfafef        mysql               "docker-entrypoint..."   4 minutes ago       Up 4 minutes        3306/tcp                           mysql01
```

几个其他的高级操作

```shell
docker run --name some-mysql -v /my/custom:/etc/mysql/conf.d -e MYSQL_ROOT_PASSWORD=my-secret-pw -d mysql:tag
把主机的/my/custom文件夹挂载到 mysqldocker容器的/etc/mysql/conf.d文件夹里面
改mysql的配置文件就只需要把mysql配置文件放在


指定mysql的一些参数
```


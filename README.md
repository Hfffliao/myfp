# myfp

## 简介

这是一个我存放我感兴趣的spring项目的仓库，现在不完善，但会慢慢完善的
**目前有一个项目，内有三个功能模块和一些辅助模块**：

#### 1.技术栈

Spring SpringMVC MySQL MyBatis

#### 2.功能模块

1.一个是线上文件夹模块，类似云盘，实现用户文件及文件夹上传，下载，删除，更改，查询等功能，每个用户的文件是私有的，只能通过登录账号操作；

2.一个是用户模块，实现用户的注册、登录、查询、以及用户权益管理等功能；

3.最后一个是物联网小车的服务端管理模块，负责中转及校验小车拍摄的视频，客户端发送的小车操控信号，视频延迟得到大幅度优化，客户端采用用网页形式，在浏览器中运行，有较好的跨平台能力

#### 3.辅助模块

1.日志

2.测试

3.接口文档

#### 4.其他

使用git进行版本管理，用maven管理依赖

## 快速开始

windows

1.打开命令提示符，进入一个空目录，运行`git clone https://github.com/Hfffliao/myfp.git ` 克隆完整的仓库到电脑本地；成功的话会在这个目录下生成myfp文件夹。

2.进入myfp项目的目录，使用`mvn clean` 清空编译完成的文件，然后使用`mvn package` 编译myfp项目；编译完成之后在myfp目录中能找到"\target\springmvc-1.0-SNAPSHOT.war"文件。

3.把springmvc-1.0-SNAPSHOT.war文件复制到tomcat的webapps目录下，改名为ROOT.war并启动tomcat（使用jetty也是类似的）

## 项目配置

#### 1.tomcat

###### 1.server.xml

1.在这个文件里面，我添加了一个connecter，端口443，使用https，配置了腾讯云的证书，使得网站用<mark>https</mark>访问（可选）；  

2.不知道为什么，dispatch调用control执行multiple文件上传的时候超过10个就会失败，报错是fileCountReachlimit=10；  

2025.10 解决了，在**server.xml**  中相应的 <Connector>中设置文件数量就能解决，具体是设置maxPartCount="200"  （必须）

#### 2.spring

1.配置都在配置类里面（除了controller.Code,这里面有些配置）；  

2.除了和本地文件系统交互的函数，其他时候文件分隔符号都是“/”，按道理来说是windows的文件分隔符，没想到这样也能用  

#### 3.前端文件

配置都在webapp/pages/css/hanbege.js,目前只有一个配置（除了camera相关），就是后端路径；  

#### 4.mysql

###### 1.基本配置

1.账号密码等在 "src\main\resources\love\linyi\config\jdbc.properties"里面配置

###### 2.库和表

1.库名是jiankong，  

表有四张，folder，user，userprofit,shijian;  

2.他们的创表代码分别为

**2.1 folder表**

CREATE TABLE `folder` (    
  `id` int unsigned NOT NULL AUTO_INCREMENT,    
  `path` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'liaoyi',    
  `name` varchar(255) NOT NULL DEFAULT 'liaoyi',    
  `type` char(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'liaoyi',    
  `user_id` int unsigned NOT NULL,    
  PRIMARY KEY (`id`),    
  KEY `user_id` (`user_id`),    
  CONSTRAINT `folder_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT    
) ENGINE=InnoDB AUTO_INCREMENT=2409 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci  

**2.2 user表**

CREATE TABLE `user` (    
  `username` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,    
  `password` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,    
  `id` int unsigned NOT NULL AUTO_INCREMENT,    
  PRIMARY KEY (`id`)    
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci  

**2.3  userprofit表**

CREATE TABLE `userprofit` (    
  `totalSize` int unsigned NOT NULL,    
  `usedSize` int unsigned NOT NULL,    
  `remainingSize` int unsigned NOT NULL,    
  `id` int unsigned NOT NULL AUTO_INCREMENT,    
  `user_id` int unsigned NOT NULL,    
  PRIMARY KEY (`id`),    
  UNIQUE KEY `user_id` (`user_id`),    
  CONSTRAINT `userprofit_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT    
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci  

**2.4 shijian表**

CREATE TABLE `shijian` (    
  `time` datetime DEFAULT '2020-01-02 00:00:00' COMMENT '日期和时间',    
  `distance` int DEFAULT '0' COMMENT '距离'    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

## 模块详解

#### 1.

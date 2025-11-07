# 项目介绍文档

# 1.概述

# 2.配置

## 1.tomcat

#### 1.server.xml

1.在这个文件里面，我添加了一个connecter，端口443，使用https，配置了腾讯云的证书，使得网站用<mark>https</mark>访问；

2.不知道为什么，dispatch调用control执行multiple文件上传的时候超过10个就会失败，报错是fileCountReachlimit=10；

2025.10 解决了，在**web.xml**<max-file-count>设置文件数量就能解决

## 2.spring

1.配置都在配置类里面（除了controller.Code,这里面有些配置）；

2.除了和本地文件系统交互的函数，其他时候文件分隔符号都是“/”，按道理来说是windows的文件分隔符，没想到这样也能用

## 3.前端文件

配置都在webapp/pages/css/hanbege.js,目前只有一个配置（除了camera相关），就是后端路径；

## 4.mysql

#### 1.基本配置

1.账号：root ；密码：liao

#### 2.库和表

1.库名是jiankong，

表有四张，folder，user，userprofit,shijian;

##### 1.他们的创表代码分别为

###### 1.1 folder表

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

###### 1.2 user表

CREATE TABLE `user` (  
  `username` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,  
  `password` char(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,  
  `id` int unsigned NOT NULL AUTO_INCREMENT,  
  PRIMARY KEY (`id`)  
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

###### 1.3  userprofit表

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

###### 3.4 shijian表

CREATE TABLE `shijian` (  
  `time` datetime DEFAULT '2020-01-02 00:00:00' COMMENT '日期和时间',  
  `distance` int DEFAULT '0' COMMENT '距离'  
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

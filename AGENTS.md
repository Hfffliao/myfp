# AGENTS.md - Guidelines for Agentic Coding in myfp

This file provides guidelines for agents operating in this repository.

## Project Overview

- **Type**: Java Web Application (SpringMVC + MyBatis)
- **Java Version**: 17
- **Build Tool**: Maven
- **Servers**: Jetty 12 (embedded) or Tomcat (WAR deployment)

## Build/Lint/Test Commands

### Build Commands

```bash
# Build with Jetty embedded (JAR output)
mvn clean package -Pjetty-embedded -DskipTests

# Build for Tomcat (WAR output)
mvn clean package -Ptomcat-war -DskipTests

# Full build with tests
mvn clean package -Pjetty-embedded

# Compile only
mvn compile

# Run (Jetty embedded)
java -jar ./target/springmvc-1.0-SNAPSHOT.jar
```

### Test Commands

```bash
# Run all tests
mvn test

# Run a single test class
mvn test -Dtest=FolderControllerTest

# Run a specific test method
mvn test -Dtest=FolderControllerTest#testifUpload_sizeTobig

# Run tests with a specific pattern
mvn test -Dtest=*ServiceImplTest

# Run with verbose output
mvn test -X

# Skip tests during build
mvn package -DskipTests
```


## Code Style Guidelines

### Package Structure

```
love.linyi/
├── controller/         # REST controllers (@RestController)
│   ├── fold/         # Folder-related controllers
│   └── loginandout/  # Authentication controllers
├── service/          # Service interfaces
│   ├── impl/         # Service implementations (@Service)
│   └── folderUtilService/
│       └── impl/     # Utility service implementations
├── dao/              # MyBatis DAOs (@Mapper)
├── domin/            # Domain/Entity classes
├── config/           # Spring configuration classes
│   └── filter/       # Servlet filters
├── exception/        # Custom exceptions
├── netapi/           # Network APIs (TCP, UDP, WebSocket)
│   ├── tcp/
│   ├── udp/
│   └── websocket/
└── common/           # Shared utilities
```
myfp编码
要实现某个功能，
代码逻辑：
1.请求到达后AuthFilter 会校验请求的用户登陆状态，已经登录的放行，以及放行特定不需要登陆的请求；
把用户信息存入common.context.UserContext 
2.接下来是controller，先调用工具校验参数（对于用户输入的路径，需要调用规范化,service的security包），调用service的方法处理业务，然后错误处理，构造返回响应；
3.接下来是service，先判断是否需要在已有的类里写方法，要求结构清晰；

4.1 数据库：有一点要注意，folder表的path字段是文件或者文件夹的父目录，规范是{绝对路径，比如/a  /a/b，但是根目录表示为""(空字符串)；
路径存入数据库前调用security包下的对应方法；
4.2 文件系统：项目使用操作系统的文件系统存文件的内容；和数据库的路径关系是：文件或文件夹在文件系统的路径为Paths.get(System.getProperty("user.home"), "uploads"，username,"数据库中的路径转换为相对路径")，定义在code.java；用户请求中的路径都代表数据库中的路径；


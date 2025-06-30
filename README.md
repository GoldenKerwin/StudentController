# 学生信息管理系统

这是一个基于 Spring Boot + Shiro + MyBatis-Plus + Thymeleaf 实现的简单的学生信息管理系统。系统分为学生和教师两种角色，提供针对不同角色的信息管理和成绩管理功能。

## 技术栈

- **核心框架**: Spring Boot 2.1.9.RELEASE
- **安全框架**: Apache Shiro 1.4.0
- **持久层框架**: MyBatis-Plus 3.0.7.1
- **数据库**: MySQL 8.0
- **模板引擎**: Thymeleaf
- **开发工具**: Lombok, Maven

## 功能模块

系统包含两种角色：**学生** 和 **教师**。

### 通用功能
- **用户注册**: 提供统一的注册页面。
- **用户登录/登出**: 使用 Shiro 进行认证和会话管理。
- **权限控制**: 基于角色的访问控制，不同角色访问不同的功能页面。

### 学生模块 (`角色ID: 1`)
- **个人信息管理**:
    - 首次登录时，若无个人信息，系统会引导至信息添加页面。
    - 学生可以查看、添加和更新自己的个人基本信息（如学号、姓名、班级等）。
- **个人成绩查询**:
    - 学生可以查看自己所有科目的成绩列表。

### 教师模块 (`角色ID: 2`)
- **学生信息管理**:
    - 教师可以查看所有学生的个人信息列表。
    - 教师可以通过学号搜索指定的学生信息。
- **学生成绩管理**:
    - 教师可以查看所有学生的成绩信息。
    - 教师可以通过学号查询指定学生的成绩。
    - 教师可以为学生录入新的成绩。
    - 教师可以修改已存在的学生成绩记录。

## 如何运行

### 1. 环境准备
- Java Development Kit (JDK) 1.8+
- Apache Maven 3.5+
- MySQL 5.7+

### 2. 数据库配置
1.  在本地 MySQL 中创建一个新的数据库，名称为 `student_controller`。
    ```sql
    CREATE DATABASE student_controller;
    ```
2.  进入该数据库，并执行项目根目录中的 `db_create.sql` 脚本来创建所需的表和初始数据。

    **用户表 (`user`)**
    ```sql
    CREATE TABLE `user` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `username` varchar(255) DEFAULT NULL COMMENT '用户名',
      `password` varchar(255) DEFAULT NULL COMMENT '加密后的密码',
      `salt` varchar(255) DEFAULT NULL COMMENT '密码盐',
      `name` varchar(255) DEFAULT NULL COMMENT '真实姓名',
      `sch_number` varchar(255) DEFAULT NULL COMMENT '学号/工号',
      `mail` varchar(255) DEFAULT NULL COMMENT '邮箱',
      `creat_time` datetime DEFAULT NULL COMMENT '创建时间',
      `last_time` datetime DEFAULT NULL COMMENT '上次登录时间',
      `role_id` int(11) DEFAULT 1 COMMENT '角色ID (1:学生, 2:教师)',
      `sch_department` varchar(255) DEFAULT NULL COMMENT '班级/部门',
      `login_count` int(11) DEFAULT 0 COMMENT '登录次数',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    ```
    **学生信息表 (`stu_info`)**
    ```sql
    CREATE TABLE `stu_info` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `sch_number` varchar(255) DEFAULT NULL COMMENT '学号',
      `name` varchar(255) DEFAULT NULL COMMENT '姓名',
      `class_name` varchar(255) DEFAULT NULL COMMENT '班级',
      `sex` varchar(255) DEFAULT NULL COMMENT '性别',
      `address` varchar(255) DEFAULT NULL COMMENT '地址',
      `father_name` varchar(255) DEFAULT NULL COMMENT '父亲姓名',
      `father_phone` varchar(255) DEFAULT NULL COMMENT '父亲电话',
      `mather_name` varchar(255) DEFAULT NULL COMMENT '母亲姓名',
      `mather_phone` varchar(255) DEFAULT NULL COMMENT '母亲电话',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    ```
    **成绩表 (`grade`)**
    ```sql
    CREATE TABLE `grade` (
      `id` int(11) NOT NULL AUTO_INCREMENT,
      `sch_number` varchar(255) DEFAULT NULL COMMENT '学号',
      `sub_name` varchar(255) DEFAULT NULL COMMENT '科目名称',
      `results` double DEFAULT NULL COMMENT '成绩',
      PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    ```

### 3. 应用配置
修改 `src/main/resources/application.yml` 文件，更新数据库连接信息。
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/student_controller?serverTimezone=UTC
    username: YOUR_MYSQL_USERNAME  # 修改为你的数据库用户名
    password: YOUR_MYSQL_PASSWORD  # 修改为你的数据库密码
```

### 4. 启动应用
1.  克隆项目到本地:
    ```bash
    git clone https://github.com/GoldenKerwin/StudentController.git
    ```
2.  使用 Maven 启动项目:
    ```bash
    mvn spring-boot:run
    ```
    或者，你也可以在 IntelliJ IDEA 或 Eclipse 中直接运行 `StudentControllerApplication.java` 的 `main` 方法。

应用启动后，访问 `http://localhost:8080` 开始使用。

- **注册页面**: `http://localhost:8080/reg`
- **登录页面**: `http://localhost:8080/doLogin`

## 项目结构

```
src/main/java/xyz/teikou/
├── controller/       # 控制器层 (Controller)
│   ├── SGradeController.java      # 学生-成绩相关
│   ├── SStuInfoController.java    # 学生-信息相关
│   ├── TGradeController.java      # 教师-成绩相关
│   ├── TStuInfoController.java    # 教师-信息相关
│   └── UserController.java        # 用户登录注册
├── entity/           # 数据库实体类 (Entity)
├── form/             # 表单数据对象 (Form)
├── mapper/           # MyBatis-Plus Mapper 接口
├── service/          # 业务逻辑层 (Service)
│   └── Impl/         # 业务逻辑实现
└── shiro/            # Shiro 安全配置
``` 
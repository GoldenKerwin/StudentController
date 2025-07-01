# 学生信息管理系统

## 1. 项目简介

本项目是一个基于 Spring Boot + MyBatis + Shiro + Thymeleaf 的学生信息管理系统。系统旨在为学校教学管理提供便利，实现了对学生信息、成绩、考勤的全面管理。系统区分了学生、教师、管理员等多种角色，并为不同角色分配了相应权限。

## 2. 功能模块

系统主要包含以下功能模块：

-   **用户管理模块**
    -   用户登录、登出功能。
    -   支持学生、教师、管理员、辅导员等多种角色。
    -   基于角色的权限控制（RBAC），不同角色拥有不同的操作权限。

-   **学生信息管理模块**
    -   **教师/管理员**：
        -   添加、删除、修改、查询学生基本信息。
    -   **学生**：
        -   查看和修改个人信息。

-   **成绩管理模块**
    -   **教师/管理员**：
        -   录入、修改、删除学生各科成绩。
        -   查看所有学生的成绩列表。
        -   按班级、科目进行成绩统计与分析。
    -   **学生**：
        -   查询个人所有科目成绩。

-   **考勤管理模块**
    -   **教师/管理员**：
        -   记录学生考勤情况（正常、迟到、早退、缺席、请假）。
        -   按班级、按日期查询和统计考勤记录。
    -   **学生**：
        -   查看个人考勤记录。

## 3. 技术栈

-   **核心框架**：Spring Boot 2.1.9.RELEASE
-   **安全框架**：Apache Shiro
-   **持久层框架**：MyBatis-Plus
-   **数据库**：MySQL 8.0
-   **模板引擎**：Thymeleaf
-   **API文档**：Swagger2
-   **构建工具**：Maven
-   **开发工具**：Lombok, Commons Lang3

## 4. 快速开始

### 4.1 环境准备

-   Java 8
-   Maven 3.x
-   MySQL 5.7+

### 4.2 步骤

1.  **克隆项目到本地**
    ```bash
    git clone https://github.com/GoldenKerwin/StudentController.git
    cd student_controller
    ```

2.  **创建数据库**
    -   启动你的 MySQL 服务。
    -   创建一个名为 `student_controller` 的数据库。
    -   执行项目根目录下的 `db_create.sql` 文件，它将创建所需的表并插入示例数据。
      ```sql
      -- 使用 MySQL 客户端执行
      source /path/to/your/project/db_create.sql;
      ```

3.  **修改配置**
    -   打开 `src/main/resources/application.yml` 文件。
    -   根据你的本地环境，修改 `spring.datasource` 下的数据库连接信息，特别是 `username` 和 `password`。
      ```yaml
      spring:
        datasource:
          url: jdbc:mysql://localhost:3306/student_controller?serverTimezone=UTC
          username: root       # 修改为你的数据库用户名
          password: 123456   # 修改为你的数据库密码
      ```

4.  **启动项目**
    -   在项目根目录下，使用 Maven 启动项目。
    ```bash
    mvn spring-boot:run
    ```
    -   当看到 Spring Boot 的启动日志后，表示项目已成功启动。

5.  **访问系统**
    -   打开浏览器，访问 `http://localhost:8080`。
    -   你可以使用 `db_create.sql` 中预置的测试账号登录。
        -   **教师账号**: `teacher1` / `123456`
        -   **学生账号**: `student1` / `123456`
        -   **管理员账号**: `admin` / `123456`

## 5. API 文档

项目集成了 Swagger2 用于生成 API 文档。项目成功启动后，你可以通过以下地址访问：

-   [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## 6. 项目截图


### 登录页
![登录页](img/login.png)

### 教师主页
![教师主页](img/teach_index.png)

### 学生主页
![学生主页](img/stu_index.png) 
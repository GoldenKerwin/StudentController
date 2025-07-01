# 学生信息管理系统功能增强记录

## 1. RESTful API 支持

### 功能描述
添加了RESTful API支持，使系统能够通过API被其他应用调用，便于与其他系统集成。

### 实现内容
1. 添加Swagger API文档支持
2. 实现学生信息API接口
3. 实现成绩管理API接口
4. 实现用户管理API接口

### 修改的文件
1. `pom.xml` - 添加Swagger依赖
2. `src/main/java/xyz/teikou/config/SwaggerConfig.java` - 创建Swagger配置类
3. `src/main/java/xyz/teikou/controller/api/StuInfoApiController.java` - 学生信息API控制器
4. `src/main/java/xyz/teikou/controller/api/GradeApiController.java` - 成绩管理API控制器
5. `src/main/java/xyz/teikou/controller/api/UserApiController.java` - 用户管理API控制器

### 使用方式
1. 启动应用后访问 `/swagger-ui/index.html` 查看API文档
2. API接口遵循RESTful设计风格，使用HTTP标准方法（GET/POST/PUT/DELETE）
3. 所有API请求需要进行身份验证

## 2. 成绩分析和统计功能

### 功能描述
增加了成绩统计分析功能，支持对学生成绩进行多维度统计和可视化展示，提供数据分析能力。

### 实现内容
1. 个人成绩统计分析（平均分、最高分、最低分、及格率等）
2. 班级成绩统计分析（班级平均分、各科目统计等）
3. 科目成绩统计（不同科目的成绩对比）
4. 成绩数据可视化展示

### 修改的文件
1. `src/main/java/xyz/teikou/service/GradeService.java` - 添加成绩统计方法
2. `src/main/java/xyz/teikou/service/Impl/GradeServiceImpl.java` - 实现成绩统计方法
3. `src/main/java/xyz/teikou/controller/GradeController.java` - 添加成绩统计控制器方法
4. `src/main/resources/templates/gradeStatistics.html` - 个人成绩统计页面
5. `src/main/resources/templates/classStatistics.html` - 班级成绩统计页面
6. `src/main/resources/templates/subjectStatistics.html` - 科目成绩统计页面

### 使用方式
1. 学生可以在个人成绩页面查看自己的成绩统计分析
2. 教师可以查看班级成绩统计和科目成绩统计
3. 统计结果以图表和表格形式展示

## 3. 更灵活的用户权限和角色管理

### 功能描述
重构了权限和角色管理系统，支持更细粒度的权限控制，增加了更多角色类型。

### 实现内容
1. 重构角色表，支持多种角色类型（学生、教师、管理员、辅导员）
2. 添加权限表，实现细粒度权限控制
3. 实现角色-权限映射关系
4. 优化Shiro权限控制

### 修改的文件
1. `db_create.sql` - 添加角色和权限相关表
2. `src/main/java/xyz/teikou/entity/Permission.java` - 创建权限实体类
3. `src/main/java/xyz/teikou/entity/Role.java` - 修改角色实体类
4. `src/main/java/xyz/teikou/mapper/PermissionMapper.java` - 创建权限Mapper接口
5. `src/main/java/xyz/teikou/service/PermissionService.java` - 创建权限服务接口
6. `src/main/java/xyz/teikou/service/Impl/PermissionServiceImpl.java` - 创建权限服务实现类
7. `src/main/java/xyz/teikou/service/RoleService.java` - 创建角色服务接口
8. `src/main/java/xyz/teikou/service/Impl/RoleServiceImpl.java` - 创建角色服务实现类
9. `src/main/java/xyz/teikou/shiro/UserRealm.java` - 修改Shiro用户Realm，支持新的权限系统
10. `src/main/java/xyz/teikou/shiro/ShiroConfig.java` - 更新Shiro配置，增加权限控制

### 使用方式
1. 系统支持学生、教师、管理员、辅导员四种角色
2. 每种角色拥有不同的权限，可以通过角色-权限映射表进行配置
3. 访问控制基于Shiro注解进行细粒度权限控制

## 4. 学生考勤管理功能

### 功能描述
新增学生考勤管理功能，支持考勤记录、统计和分析。

### 实现内容
1. 考勤记录管理（正常、迟到、早退、缺席、请假）
2. 个人考勤统计分析
3. 班级考勤统计分析
4. 考勤数据可视化展示

### 修改的文件
1. `db_create.sql` - 添加考勤表
2. `src/main/java/xyz/teikou/entity/Attendance.java` - 创建考勤实体类
3. `src/main/java/xyz/teikou/form/AttendanceForm.java` - 创建考勤表单类
4. `src/main/java/xyz/teikou/mapper/AttendanceMapper.java` - 创建考勤Mapper接口
5. `src/main/java/xyz/teikou/service/AttendanceService.java` - 创建考勤服务接口
6. `src/main/java/xyz/teikou/service/Impl/AttendanceServiceImpl.java` - 创建考勤服务实现类
7. `src/main/java/xyz/teikou/controller/AttendanceController.java` - 创建考勤控制器
8. `src/main/resources/templates/recordAttendance.html` - 考勤记录页面
9. `src/main/resources/templates/myAttendance.html` - 个人考勤页面
10. `src/main/resources/templates/classAttendance.html` - 班级考勤页面

### 使用方式
1. 教师可以录入学生考勤记录
2. 学生可以查看自己的考勤记录和统计
3. 教师可以查看班级考勤统计和按日期查询考勤记录
4. 考勤数据以图表和表格形式展示

## 总结

本次增强为学生信息管理系统添加了多项功能，使系统更加完善和实用：

1. 通过RESTful API支持，使系统能够与其他系统集成，提高了系统的扩展性和互操作性。
2. 成绩分析和统计功能，提供了数据分析能力，帮助学生了解自己的学习情况，帮助教师分析班级整体成绩。
3. 更灵活的用户权限和角色管理，使系统能够适应不同类型用户的权限需求，提高了系统的安全性。
4. 学生考勤管理功能，完善了学生管理的功能范围，便于教师掌握学生出勤情况。

这些增强功能使得系统从单纯的信息管理系统向综合性的教学管理平台迈进，更好地满足了教育管理的实际需求。 
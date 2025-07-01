# 学生管理系统功能增强文档

## 一、后端数据校验功能增强

### 简介

后端数据校验是保证数据安全和完整性的重要措施，可以防止非法数据入库，提高系统的稳定性和安全性。

### 实现内容

#### 1. 添加依赖

在 `pom.xml` 中添加 Spring Boot Validation 依赖：

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

#### 2. 表单类增强

在表单类中添加 Bean Validation 注解，对表单数据进行校验：

##### UserForm.java

```java
@Data
public class UserForm {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20个字符之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String password1;

    // 其他字段...
}
```

##### StuForm.java

```java
@Data
public class StuForm {
    @NotBlank(message = "学号不能为空")
    @Pattern(regexp = "^[0-9]{8,12}$", message = "学号格式不正确，应为8-12位数字")
    private String schNumber;

    @NotBlank(message = "地址不能为空")
    private String address;

    // 其他字段...
}
```

##### GradeForm.java (新增)

```java
@Data
public class GradeForm {
    @NotNull(message = "语文成绩不能为空")
    @Min(value = 0, message = "语文成绩不能小于0分")
    @Max(value = 100, message = "语文成绩不能超过100分")
    private Integer chinese;

    // 其他字段...
}
```

#### 3. Controller 增强

在 Controller 层使用 `@Valid` 注解和 `BindingResult` 对象处理校验结果：

```java
@PostMapping("/register")
public ModelAndView register(@Valid UserForm userForm, BindingResult bindingResult) {
    ModelAndView mv = new ModelAndView();
    
    // 处理表单校验错误
    if (bindingResult.hasErrors()) {
        // 处理错误...
        return mv;
    }
    
    // 正常业务逻辑...
}
```

#### 4. 全局异常处理器

创建全局异常处理器，统一处理各种校验异常：

```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleBindException(BindException e) {
        // 处理表单绑定异常
    }

    // 其他异常处理方法...
}
```

## 二、Controller 结构优化

### 问题分析

项目中存在按角色拆分的 Controller 类（如 SGradeController / TGradeController 和 SStuInfoController / TStuInfoController），其中 S 代表学生（Student），T 代表教师（Teacher）。这种设计方式在业务逻辑简单时可行，但随着功能扩展，会导致大量的代码重复，并且不符合面向资源（Resource-Oriented）的接口设计理念。

### 实现内容

#### 1. 合并 Controller 类

将按角色拆分的 Controller 合并为统一的资源 Controller：

- 将 SGradeController 和 TGradeController 合并为 GradeController
- 将 SStuInfoController 和 TStuInfoController 合并为 StuInfoController

#### 2. 使用角色注解控制访问权限

在新的 Controller 方法上，使用 Shiro 的注解来精细化地控制每个操作的访问权限：

```java
@RequestMapping("/myGrades")
@RequiresRoles("1")  // 学生角色
public ModelAndView myGrades(HttpServletRequest request) {
    // 学生查看自己成绩的逻辑
}

@RequestMapping("/findAll")
@RequiresRoles("2")  // 教师角色
public ModelAndView findAll() {
    // 教师查看所有学生成绩的逻辑
}
```

#### 3. 统一请求路径

将原来按角色划分的请求路径改为按资源划分：

- 从 `/student/myGrades` 和 `/teacher/findAll` 变为 `/grade/myGrades` 和 `/grade/findAll`
- 从 `/student/myInfo` 和 `/teacher/allInfo` 变为 `/stuInfo/myInfo` 和 `/stuInfo/allInfo`

### 优势与效果

1. **减少代码冗余**：相似的业务逻辑（如查询成绩）可以复用
2. **职责更清晰**：一个 Controller 负责一类资源的所有操作
3. **更易于维护**：新增或修改功能时，只需改动一个文件
4. **符合 RESTful 设计理念**：以资源为中心组织 API
5. **精细化的权限控制**：通过注解控制不同角色对资源的访问权限

### 涉及文件

- 新建：`GradeController.java` 和 `StuInfoController.java`
- 删除：`SGradeController.java`、`TGradeController.java`、`SStuInfoController.java` 和 `TStuInfoController.java`
- 修改：各模板文件中的表单提交路径和链接地址

## 三、数据库兼容性和动态科目支持

### 问题分析

系统存在两个主要问题：
1. 实体类与数据库结构不匹配：Grade实体类使用了固定的科目字段（chinese、math、english），但数据库表使用了更灵活的结构（sub_name、results）
2. 系统不支持自定义科目：成绩录入和展示时只能使用固定的三个科目（语文、数学、英语）

### 实现内容

#### 1. 修改实体类匹配数据库结构

将Grade实体类修改为与数据库表结构一致：

```java
@Data
@TableName("grade")
public class Grade implements Serializable {
    // ...
    @TableField("sub_name")
    private String subName;

    @TableField("results")
    private Double results;
    // ...
}
```

#### 2. 更新表单类支持动态科目

修改GradeForm类，支持动态科目名称和成绩：

```java
@Data
public class GradeForm {
    // ...
    @NotBlank(message = "科目名称不能为空")
    private String subName;

    @NotNull(message = "成绩不能为空")
    @Min(value = 0, message = "成绩不能小于0分")
    @Max(value = 100, message = "成绩不能超过100分")
    private Double results;
    // ...
}
```

#### 3. 增强Controller异常处理

修改GradeController，优化异常处理，当数据库没有对应记录时返回空列表而不是报错：

```java
@RequestMapping("/findAll")
@RequiresRoles("2")
public ModelAndView findAll() {
    ModelAndView mv = new ModelAndView("allGrades");
    
    try {
        List<Grade> grades = gradeService.selectAllGrade();
        mv.addObject("grades", grades != null ? grades : new ArrayList<>());
    } catch (Exception e) {
        log.error("查询所有成绩时出错: " + e.getMessage(), e);
        mv.addObject("grades", new ArrayList<>());
        mv.addObject("errorMsg", "查询成绩时出错，可能是数据库中没有对应记录");
    }
    
    return mv;
}
```

#### 4. 更新页面模板支持动态科目

修改成绩相关页面，支持动态科目的显示和录入：

- 成绩列表页面显示科目名称和对应成绩，而不是固定的三个科目列
- 成绩录入页面增加科目选择下拉框和自定义输入功能
- 成绩修改页面支持对任意科目的成绩修改

#### 5. 添加自定义科目支持

在录入成绩页面增加自定义科目输入框和相应的JavaScript代码，允许用户输入预设科目外的其他科目名称：

```javascript
document.getElementById('customSubject').addEventListener('input', function() {
    var selectElement = document.querySelector('select[name="subName"]');
    if (this.value.trim() !== '') {
        // 如果自定义科目框有输入，将选择框设为禁用状态
        selectElement.disabled = true;
    } else {
        // 如果自定义科目框为空，恢复选择框
        selectElement.disabled = false;
    }
});
```

### 优势与效果

1. **数据库兼容性**：实体类与数据库表结构完全匹配，消除了SQL错误
2. **科目扩展性**：可以录入和管理任意科目的成绩，不再局限于固定的三个科目
3. **用户体验提升**：用户可以通过下拉选择常用科目，也可以自定义输入新科目
4. **错误处理优化**：当数据库没有记录时，显示友好的提示而不是直接报错
5. **代码健壮性**：增加了异常处理，使系统在异常情况下仍能正常运行

### 涉及文件

- 修改：`Grade.java`、`GradeForm.java`、`GradeController.java`
- 修改：`myGrage.html`、`allGrades.html`、`addGrade.html`、`updateGrades.html`

## 后续优化方向

1. **REST API 改造**：将 Controller 方法改造为更符合 REST 风格的接口
2. **前后端分离**：将前端页面与后端接口分离，前端使用 AJAX 调用后端接口
3. **更精细的权限控制**：根据用户角色和资源关系，实现更细粒度的权限控制
4. **自定义校验注解**：根据业务需求创建更复杂的自定义校验规则
5. **异常处理优化**：完善全局异常处理，提供更友好的错误提示
6. **科目管理功能**：增加科目管理模块，可以集中管理科目信息
7. **成绩统计分析**：增加成绩统计、分析和可视化功能 
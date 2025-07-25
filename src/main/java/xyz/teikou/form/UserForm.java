package xyz.teikou.form;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UserForm {
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度必须在2-20个字符之间")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String password;

    @NotBlank(message = "确认密码不能为空")
    private String password1;

    @NotBlank(message = "姓名不能为空")
    private String name;

    @NotNull(message = "角色ID不能为空")
    private Integer roleId;

    @NotBlank(message = "学号/工号不能为空")
    @Pattern(regexp = "^[0-9]{6,10}$", message = "学号/工号格式不正确，应为6-10位数字")
    private String schNumber;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String mail;

    @NotBlank(message = "学院不能为空")
    private String schDepartment;
}

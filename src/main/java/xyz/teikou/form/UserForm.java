package xyz.teikou.form;

import lombok.Data;

/**
 * Creat by TeiKou
 * 2019/10/9 15:33
 */
@Data
public class UserForm {
    private String username;
    private String password;
    private String password1;
    private String name;
    private Integer roleId;
    private String schNumber;
    private String mail;
    private  String schDepartment;
}

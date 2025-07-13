package xyz.teikou.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
@Data
public class StuForm {
    @NotBlank(message = "学号不能为空")
    @Pattern(regexp = "^[0-9]{8,12}$", message = "学号格式不正确，应为8-12位数字")
    private String schNumber;

    @NotBlank(message = "地址不能为空")
    private String address;

    @NotBlank(message = "父亲姓名不能为空")
    private String fatherName;

    @NotBlank(message = "父亲电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "父亲电话格式不正确")
    private String fatherPhone;

    @NotBlank(message = "母亲姓名不能为空")
    private String matherName;

    @NotBlank(message = "母亲电话不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "母亲电话格式不正确")
    private String matherPhone;
}

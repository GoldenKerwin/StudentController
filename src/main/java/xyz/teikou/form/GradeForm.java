package xyz.teikou.form;

import lombok.Data;

import javax.validation.constraints.*;

/**
 * 成绩表单数据校验
 */
@Data
public class GradeForm {
    
    private Integer id;
    
    @NotBlank(message = "学号不能为空")
    @Pattern(regexp = "^[0-9]{8,12}$", message = "学号格式不正确，应为8-12位数字")
    private String schNumber;

    @NotBlank(message = "科目名称不能为空")
    private String subName;

    @NotNull(message = "成绩不能为空")
    @Min(value = 0, message = "成绩不能小于0分")
    @Max(value = 100, message = "成绩不能超过100分")
    private Double results;

    @NotBlank(message = "考试编号不能为空")
    private String testNo;

    private String operator;
    
    private String updater;
} 
package xyz.teikou.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 考勤表单类
 */
@Data
public class AttendanceForm {
    
    private Integer id;
    
    @NotBlank(message = "学号不能为空")
    private String schNumber;
    
    @NotNull(message = "日期不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;
    
    @NotBlank(message = "考勤状态不能为空")
    private String status;
    
    @NotBlank(message = "课程不能为空")
    private String course;
    
    private String remark;
    
    private String operator;
} 
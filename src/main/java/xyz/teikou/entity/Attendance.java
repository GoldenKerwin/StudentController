package xyz.teikou.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 学生考勤实体类
 */
@Data
@TableName("attendance")
public class Attendance implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    @TableField("sch_number")
    @JsonProperty("schNumber")
    private String schNumber;
    
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @TableField("date")
    private Date date;
    
    @TableField("status")
    @JsonProperty("status")
    private String status;
    
    @TableField("course")
    @JsonProperty("course")
    private String course;
    
    @TableField("remark")
    @JsonProperty("remark")
    private String remark;
    
    @TableField("operator")
    @JsonProperty("operator")
    private String operator;
} 
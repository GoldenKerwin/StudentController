package xyz.teikou.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("stu_info")
public class StuInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("sch_number")
    private String schNumber;

    @TableField("name")
    private String name;

    @TableField("class_name")
    private String className;

    @TableField("sex")
    private String sex;

    @TableField("address")
    private String address;

    @TableField("father_name")
    private String fatherName;

    @TableField("father_phone")
    private String fatherPhone;

    @TableField("mather_name")
    private String matherName;

    @TableField("mather_phone")
    private String matherPhone;
}

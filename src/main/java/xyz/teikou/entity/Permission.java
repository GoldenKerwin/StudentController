package xyz.teikou.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
@Data
@TableName("permission")
public class Permission implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    @TableField("permission_name")
    private String permissionName;
    
    @TableField("permission_desc")
    private String permissionDesc;
    
    @TableField("permission_code")
    private String permissionCode;
} 
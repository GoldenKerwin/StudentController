package xyz.teikou.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author teikou
 * @since 2019-10-09
 */
@Data
@TableName("role")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "role_id", type = IdType.ID_WORKER)
    private Integer roleId;

    @TableField("role_name")
    private String roleName;

    @TableField("role_permission")
    private String rolePermission;
}

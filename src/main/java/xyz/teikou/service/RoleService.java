package xyz.teikou.service;

import xyz.teikou.entity.Role;

import java.util.List;

/**
 * 角色服务接口
 */
public interface RoleService {
    
    /**
     * 获取所有角色
     * @return 角色列表
     */
    List<Role> getAllRoles();
    
    /**
     * 根据ID获取角色
     * @param id 角色ID
     * @return 角色对象
     */
    Role getRoleById(Integer id);
    
    /**
     * 获取角色及其权限
     * @param id 角色ID
     * @return 包含权限的角色对象
     */
    Role getRoleWithPermissions(Integer id);
} 
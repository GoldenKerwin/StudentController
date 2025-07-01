package xyz.teikou.service;

import xyz.teikou.entity.Permission;

import java.util.List;

/**
 * 权限服务接口
 */
public interface PermissionService {
    
    /**
     * 根据角色ID查询对应的权限列表
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> getPermissionsByRoleId(Integer roleId);
    
    /**
     * 根据角色ID查询对应的权限代码列表
     * @param roleId 角色ID
     * @return 权限代码列表
     */
    List<String> getPermissionCodesByRoleId(Integer roleId);
} 
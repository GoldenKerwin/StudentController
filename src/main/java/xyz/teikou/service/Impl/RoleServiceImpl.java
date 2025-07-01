package xyz.teikou.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.teikou.entity.Permission;
import xyz.teikou.entity.Role;
import xyz.teikou.mapper.RoleMapper;
import xyz.teikou.service.PermissionService;
import xyz.teikou.service.RoleService;

import java.util.List;

/**
 * 角色服务实现类
 */
@Service
public class RoleServiceImpl implements RoleService {
    
    @Autowired
    private RoleMapper roleMapper;
    
    @Autowired
    private PermissionService permissionService;
    
    @Override
    public List<Role> getAllRoles() {
        return roleMapper.selectList(null);
    }
    
    @Override
    public Role getRoleById(Integer id) {
        return roleMapper.selectById(id);
    }
    
    @Override
    public Role getRoleWithPermissions(Integer id) {
        Role role = roleMapper.selectById(id);
        if (role != null) {
            List<Permission> permissions = permissionService.getPermissionsByRoleId(id);
            role.setPermissions(permissions);
        }
        return role;
    }
} 
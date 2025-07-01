package xyz.teikou.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.teikou.entity.Permission;
import xyz.teikou.mapper.PermissionMapper;
import xyz.teikou.service.PermissionService;

import java.util.List;

/**
 * 权限服务实现类
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    
    @Autowired
    private PermissionMapper permissionMapper;
    
    @Override
    public List<Permission> getPermissionsByRoleId(Integer roleId) {
        return permissionMapper.selectPermissionsByRoleId(roleId);
    }
    
    @Override
    public List<String> getPermissionCodesByRoleId(Integer roleId) {
        return permissionMapper.selectPermissionCodesByRoleId(roleId);
    }
} 
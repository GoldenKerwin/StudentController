package xyz.teikou.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.teikou.entity.User;
import xyz.teikou.service.PermissionService;
import xyz.teikou.service.UserService;

import java.util.List;

/**
 * Creat by TeiKou
 * 2019/10/9 14:07
 */
@Slf4j
@Component
public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;
    
    @Autowired
    PermissionService permissionService;

    /**
     * 授权
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principalCollection.getPrimaryPrincipal();
        
        // 添加角色
        authorizationInfo.addRole(String.valueOf(user.getRoleId()));
        
        // 添加权限
        List<String> permissions = permissionService.getPermissionCodesByRoleId(user.getRoleId());
        if (permissions != null && !permissions.isEmpty()) {
            authorizationInfo.addStringPermissions(permissions);
        }
        
        return authorizationInfo;
    }

    /**
     * 认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token= (UsernamePasswordToken) authenticationToken;
        User user=userService.findUserByUsername(token.getUsername());
        if (user!=null){
            return new SimpleAuthenticationInfo(user,user.getPassword(),this.getName());
        }
        log.warn("用户 [{}] 不存在", token.getUsername());
        return null;
    }
}

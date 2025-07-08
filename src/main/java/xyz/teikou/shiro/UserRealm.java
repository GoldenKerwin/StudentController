package xyz.teikou.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xyz.teikou.entity.User;
import xyz.teikou.service.PermissionService;
import xyz.teikou.service.UserService;

import java.util.List;

@Slf4j
@Component // 标记为 Spring 组件，以便 Spring 扫描和管理
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    /**
     * 授权：当需要检查用户权限时调用 (如使用 @RequiresRoles)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principalCollection.getPrimaryPrincipal();
        authorizationInfo.addRole(String.valueOf(user.getRoleId()));
        List<String> permissions = permissionService.getPermissionCodesByRoleId(user.getRoleId());
        if (permissions != null && !permissions.isEmpty()) {
            authorizationInfo.addStringPermissions(permissions);
        }
        return authorizationInfo;
    }

    /**
     * 认证：当用户执行 subject.login(token) 时调用
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        User user = userService.findUserByUsername(username);

        // 1. 判断用户是否存在
        if (user == null) {
            throw new UnknownAccountException("用户不存在");
        }

        // 2. 返回包含【加密密码】和【盐】的 AuthenticationInfo 对象
        //    Shiro 会用这个信息和我们配置的 HashedCredentialsMatcher 来自动比对密码
        return new SimpleAuthenticationInfo(
                user,                                  // user 对象
                user.getPassword(),                    // Hashed Credentials: 数据库中存储的【加密后】的密码
                ByteSource.Util.bytes(user.getSalt()), // Salt: 数据库中存储的盐，必须用 ByteSource 包装
                this.getName()                         // Realm Name
        );
    }
}
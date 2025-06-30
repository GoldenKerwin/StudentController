package xyz.teikou.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import xyz.teikou.entity.User;
import xyz.teikou.service.UserService;

import java.util.HashSet;
import java.util.Set;

/**
 * Creat by TeiKou
 * 2019/10/9 12:08
 */
public class UserRealm extends AuthorizingRealm {
    /**
     * @author Teikou
     * @date 2019/10/9 12:10
     *
     */
    @Autowired
    UserService userService;
    @Override //授权
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String username= (String) principalCollection.getPrimaryPrincipal();
        User userByUsername = userService.findUserByUsername(username);
        String role= userByUsername.getRoleId().toString();
        Set<String> roles =new HashSet<>();
        roles.add(role);
        SimpleAuthorizationInfo simpleAuthorizationInfo =new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(roles);
        return simpleAuthorizationInfo;
//        return null;
    }

    @Override  //认证
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //从主体传过来的认证信息获得用户名
        String username = (String) authenticationToken.getPrincipal();
        //通过用户名获取凭证
        String password = userService.findPasswordByUsername(username);

        if (password == null) {
            return null;
        }
        SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(username, password,getName());
//        simpleAuthenticationInfo.setCredentialsSalt(ByteSource.Util.bytes("xsglx"));

        return simpleAuthenticationInfo;
    }
}

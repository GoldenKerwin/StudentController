package xyz.teikou.shiro;


import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * Creat by TeiKou
 * 2019/10/9 12:18
 */
@Configuration
public class ShiroConfig {
    @Bean
    UserRealm userRealm() {
        return new UserRealm();
    }
    @Bean
    DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(userRealm());
        return manager;
    }
    @Bean
    ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
        definition.addPathDefinition("/doLogin", "anon");
        definition.addPathDefinition("/reg","anon");
        definition.addPathDefinition("/register","anon");
        definition.addPathDefinition("/login","anon");
        definition.addPathDefinition("/**", "authc");
        return definition;
    }
}
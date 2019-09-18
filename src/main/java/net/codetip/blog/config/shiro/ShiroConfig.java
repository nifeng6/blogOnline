package net.codetip.blog.config.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager securityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //添加内置过滤器
        /**
         * 常用内置过滤器
         *  anon:无需认证（登录）用户可以直接访问（就相当于游客）
         *  authc:必须要认证才能访问
         *  user：使用了remenberMe的功能的用户可以直接无需登录访问（相当于记住登录状态）
         *  perms:必须获取资源权限才能访问
         *  role：必须获取角色授权才能访问
         *  logout:用户登出，这里不用设置控制器，退出后直接跳转到/
         */
        Map<String,String> filterMap = new LinkedHashMap<String,String>();
        filterMap.put("/login","anon");
        //authc需要 登陆才能访问
//        filterMap.put("/user/login", "anon");
        filterMap.put("/ask/**", "authc");
        filterMap.put("/discuss/ask/**", "authc");
        filterMap.put("/logout","logout");
        //authc需要 登陆才能访问
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        //用户登录页面，当用户没登陆时跳转到此页面，没设置时默认跳转到login.jsp
        shiroFilterFactoryBean.setLoginUrl("/login");
        return shiroFilterFactoryBean;
    }



    /**
     * 创建 安全管理器 SecurityManager
     * @return
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("myReal")MyReal myReal){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myReal);
        return securityManager;
    }//👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇👇这是第二种写法
//    @Bean
//    public SecurityManager securityManager() {
//        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
//        securityManager.setRealm(getRealm());
//        return securityManager;
//    }


    @Bean(name = "myReal")
    public MyReal getRealm(){
        return new MyReal();
    }


    /**
     * 配置 ShiroDialect 用于thymeleaf 和shiro标签配合使用
     * @return
     */
    @Bean
    public ShiroDialect getShiroDialect(){
        return new ShiroDialect();
    }
}
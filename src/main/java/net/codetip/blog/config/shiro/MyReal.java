package net.codetip.blog.config.shiro;

import net.codetip.blog.domain.User;
import net.codetip.blog.mapper.UserMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 自定义Realm
 *
 */
public class MyReal extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;

    /**
     * 执行授权逻辑
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行授权逻辑");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();//获取我当前用户
        info.addRole(user.getUsername());
        return info;
    }


    /**
     * 执行认证逻辑
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
        System.out.println("执行认证逻辑");
        UsernamePasswordToken token = (UsernamePasswordToken)arg0;
        User user = userMapper.findUserByName(token.getUsername());
        //判断是否存在用户
        if (user==null){
            //用户名不存在
            return null;
        }
        //判断密码
        return new SimpleAuthenticationInfo(user,user.getPassword(),"");
    }

}

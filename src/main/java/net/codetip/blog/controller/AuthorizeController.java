package net.codetip.blog.controller;

import net.codetip.blog.domain.GitHubUser;
import net.codetip.blog.domain.User;
import net.codetip.blog.dto.GitHubAccessTokenDto;
import net.codetip.blog.mapper.UserMapper;
import net.codetip.blog.provider.GitHubProvider;
import net.codetip.blog.util.CryptographyUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GitHubProvider gitHubProvider;

    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String GitHubId;

    @Value("${github.client.secret}")
    private String GitHubSecret;

    @Value("${github.client.redirectUri}")
    private String redirectUri;

    @RequestMapping("callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletRequest request){
        GitHubAccessTokenDto gitHubAccessTokenDto = new GitHubAccessTokenDto();
        gitHubAccessTokenDto.setClient_id(GitHubId);
        gitHubAccessTokenDto.setClient_secret(GitHubSecret);
        gitHubAccessTokenDto.setCode(code);
        gitHubAccessTokenDto.setState(state);
        gitHubAccessTokenDto.setRedirect_uri(redirectUri);
        String accessToken = gitHubProvider.getAccessToken(gitHubAccessTokenDto);
        GitHubUser user = gitHubProvider.getUser(accessToken);
        if (user != null){
            User saveUser = new User();
            saveUser.setUpdateDate(new Date());
            saveUser.setCreateDate(new Date());
            saveUser.setImg(user.getAvatarUrl());
            saveUser.setUsername("code_"+user.getId());
            saveUser.setGithubId(user.getId());
            String updatePassword = UUID.randomUUID().toString().replace("-","");
            saveUser.setPassword(CryptographyUtil.md5(updatePassword,"codetip"));//先用随机密码登录，后续自行修改密码
            UsernamePasswordToken shirotoken = new UsernamePasswordToken(saveUser.getUsername().toString(), CryptographyUtil.md5(updatePassword,"codetip"));
            Subject subject = SecurityUtils.getSubject();
            User isUser = userMapper.findGitId(user.getId());
            userMapper.updateGitHubPwd(saveUser);//如果为github登录的话每次都会更新密码，存在不足，必须修补
            try {
                if (isUser.getUsername()!=null){//如果用户存在或者重复的时候
                    request.getSession().setAttribute("githubUser",user);
                    try {
                        subject.login(shirotoken);
                    }catch (Exception ee){}
                }
            }catch (Exception e){//如果用户不存在
                userMapper.addUser(saveUser);
                request.getSession().setAttribute("githubUser",user);

                try {
                    subject.login(shirotoken);
                }catch (Exception ee){}
            }
            return "redirect:/login";
        }else {
            return "redirect:/login";
        }

    }


}

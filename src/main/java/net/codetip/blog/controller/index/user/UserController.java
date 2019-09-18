package net.codetip.blog.controller.index.user;

import com.sun.javafx.collections.MappingChange;
import net.codetip.blog.domain.User;
import net.codetip.blog.dto.GeetestConfig;
import net.codetip.blog.mapper.AskAnswerMapper;
import net.codetip.blog.mapper.AskMapper;
import net.codetip.blog.mapper.UserMapper;
import net.codetip.blog.util.CryptographyUtil;
import net.codetip.blog.util.GeetestLib;
import net.codetip.blog.util.IpUtil;
import net.codetip.blog.util.Verification;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AskMapper askMapper;

    @Autowired
    private AskAnswerMapper askAnswerMapper;

    @RequestMapping("{uid}")
    public ModelAndView index(@PathVariable(name = "uid") int uid){
        ModelAndView mav = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        User userCurrent = (User)subject.getPrincipal();
        if (userCurrent.getId()!=uid){
            return mav;
        }else {
        User user = userMapper.findUser(uid);
        int releaseAsk = askMapper.releaseAsk(uid);
        int releaseAskAnswer = askAnswerMapper.releaseAskAnswer(uid);
        mav.addObject("user",user);
        mav.addObject("releaseAsk",releaseAsk);
        mav.addObject("releaseAskAnswer",releaseAskAnswer);
        mav.setViewName("index/user/index");
        return mav;
        }
    }

    @RequestMapping("changeInfo/{uid}")
    public ModelAndView changeInfo(@PathVariable(name = "uid")int uid){
        ModelAndView mav = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        User userCurrent = (User)subject.getPrincipal();
        if (userCurrent.getId()!=uid){
            return mav;
        }else {
            User user = userMapper.findUser(uid);
            mav.addObject("user",user);

            mav.setViewName("index/user/changeinfo");
            return mav;
        }

    }

    @RequestMapping("changeInfo")
    @ResponseBody
    public JSONObject changeInfoPost(User user)throws Exception{
        JSONObject result = new JSONObject();
        try {
            user.setUpdateDate(new Date());
            userMapper.updateUserInfo(user);
            result.put("success",true);
            return result;
        }catch (Exception e){
            result.put("success",false);
            return result;
        }
    }

    @RequestMapping("changePwd/{uid}")
    public ModelAndView changePwd(@PathVariable(name = "uid")int uid){
        ModelAndView mav = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        User userCurrent = (User)subject.getPrincipal();
        if (userCurrent.getId()!=uid){
            return mav;
        }else {
            User user = userMapper.findUser(uid);
            mav.addObject("user",user);
            mav.setViewName("index/user/changePwd");
            return mav;
        }
    }

    @RequestMapping("changePwd")
    @ResponseBody
    public JSONObject changePwdPost(User user){
        JSONObject result = new JSONObject();
        try {
            user.setPassword(CryptographyUtil.md5(user.getPassword(),"codetip"));
            userMapper.updateUserPwd(user);
            result.put("success",true);
            return result;
        }catch (Exception e){
            result.put("success",false);
            return result;
        }

    }

    @RequestMapping("changeHeadImg/{uid}")
    public ModelAndView changeHeadImg(@PathVariable(name = "uid")int uid){
        ModelAndView mav = new ModelAndView();
        Subject subject = SecurityUtils.getSubject();
        User userCurrent = (User)subject.getPrincipal();
        if (userCurrent.getId()!=uid){
            return mav;
        }else {
            User user = userMapper.findUser(uid);
            mav.addObject("user",user);
            mav.setViewName("index/user/changeHeadImg");
            return mav;
        }

    }


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    @ResponseBody
    public void register(HttpServletRequest request, HttpServletResponse response) throws IOException {
        GeetestLib gtSdk = new GeetestLib(GeetestConfig.getGeetest_id(), GeetestConfig.getGeetest_key(),
                GeetestConfig.isnewfailback());
        String resStr = "{}";
        // 自定义userid
        HttpSession session = request.getSession();

        // User baseUser = (User) session.getAttribute("baseUser");

        // String userid = null;
        // if (Objects.nonNull(baseUser)) {
        // userid = baseUser.getUserName();
        // }
        // 自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("user_id", "username"); // 网站用户id
        param.put("ip_address", IpUtil.getIpAddr(request)); // 传输用户请求验证时所携带的IP

        // 进行验证预处理
        int gtServerStatus = gtSdk.preProcess(param);
        // 将服务器状态设置到session中
        request.getSession().setAttribute(gtSdk.gtServerStatusSessionKey, gtServerStatus);
        // 将userid设置到session中
        request.getSession().setAttribute("userid", "username");
        resStr = gtSdk.getResponseStr();
        PrintWriter out = response.getWriter();

        out.println(resStr);
    }

    @RequestMapping("/test")
    @ResponseBody
    public void LoginTest(HttpServletRequest request, HttpServletResponse response) {
        // 返回的状态
        int verification = Verification.verification(request, response);

        if (verification != Verification.SUCCESS) {
            return;
        }
    }

}

package net.codetip.blog.controller.index;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.javafx.collections.MappingChange;
import net.codetip.blog.config.shiro.ShiroConfig;
import net.codetip.blog.domain.Ask;
import net.codetip.blog.domain.AskAnswer;
import net.codetip.blog.domain.Blogtags;
import net.codetip.blog.domain.User;
import net.codetip.blog.dto.UploadMsg;
import net.codetip.blog.mapper.AskAnswerMapper;
import net.codetip.blog.mapper.AskMapper;
import net.codetip.blog.mapper.BlogTagsMapper;
import net.codetip.blog.mapper.UserMapper;
import net.codetip.blog.util.CryptographyUtil;
import net.codetip.blog.util.Verification;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class MainController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AskAnswerMapper askAnswerMapper;

    @Autowired
    private AskMapper askMapper;

    @Autowired
    private BlogTagsMapper blogTagsMapper;

    @RequestMapping("")
    public String index(){

        return "index/home/index";
    }

    @RequestMapping("discuss")
    public String discuss(Model model,String page,String sort,String askSort){
        if (askSort==null||askSort.equals("")){
            askSort="0";
        }
        if (page==null){
            page="1";
        }
        PageHelper.startPage(Integer.parseInt(page),20);
        List<Map> asks=null;

        if (askSort.equals("0")){
            if (sort==null||sort.equals("new")){
                asks = askMapper.findAsks("create_date");
            }else if(sort.equals("hot")){
                asks = askMapper.findAsks("visit+0");
            }else {
                asks = askMapper.findAsks("create_date");
            }
        }else {
            if (sort==null||sort.equals("new")){
                asks = askMapper.findAsksBySort("create_date",askSort);
            }else if(sort.equals("hot")){
                asks = askMapper.findAsksBySort("visit+0",askSort);
            }else {
                asks = askMapper.findAsksBySort("create_date",askSort);
            }
        }
        List<Map> tags = blogTagsMapper.findHotTags();
        List<Map> hotByDis = askMapper.findByHotNew("visit+0");
        List<Map> dateByDis = askMapper.findByHotNew("create_date");
        List<Map> LimitUsers = userMapper.LimitUsers();
        PageInfo<Map> pageInfo = new PageInfo<>(asks);
        model.addAttribute("askSort",askSort);
        model.addAttribute("asks",asks);
        model.addAttribute("hotByDis",hotByDis);
        model.addAttribute("LimitUsers",LimitUsers);
        model.addAttribute("dateByDis",dateByDis);
        model.addAttribute("sort",sort);
        model.addAttribute("tags",tags);
        model.addAttribute("page",Integer.parseInt(page));
        model.addAttribute("pages",pageInfo.getPages());
        return "index/discuss/index";
    }

    @RequestMapping("discuss/ask")
    public ModelAndView discussAsk(){
        ModelAndView mav = new ModelAndView();
        List<Blogtags> blogtags = blogTagsMapper.findTags();
        mav.addObject("blogtags",blogtags);
        mav.addObject("tags",blogtags);
        mav.setViewName("index/discuss/ask");
        return mav;
    }

    @RequestMapping("discuss/ask/{askId}")
    public ModelAndView askEdit(@PathVariable(name = "askId")int askId){
        System.out.println("进入");
        ModelAndView mav = new ModelAndView();
        List<Blogtags> blogtags = blogTagsMapper.findTags();
        Map askAccess  = askMapper.findAskById(askId);
        String uid = askAccess.get("uid").toString();
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        if (!String.valueOf(user.getId()).equals(uid)){
            System.out.println("不符合");
            return mav;
        }
        mav.addObject("blogtags",blogtags);
        mav.addObject("tags",blogtags);
        mav.addObject("askAccess",askAccess);
        mav.setViewName("index/discuss/askEdit");
        return mav;
    }

    @RequestMapping("askEdit")
    @ResponseBody
    public JSONObject askEditPost(Ask ask){
        JSONObject result = new JSONObject();
        if (ask.getContent()==null||ask.getContent().trim().equals("")||ask.getTitle()==null||ask.getTitle().trim().equals("")||ask.getCates()==null||ask.getCates().trim().equals("")){
            result.put("success",false);
            return result;
        }
        System.out.println(ask.getId());
        System.out.println(ask.getContent());
        System.out.println(ask.getCates());
        System.out.println(ask.getTitle());
        System.out.println();
        try {
            ask.setUpdateDate(new Date());
            askMapper.askEdit(ask);
            result.put("success",true);
            return result;
        }catch (Exception e){
            result.put("success",false);
            return result;
        }
    }

    @RequestMapping("login")
    public String login(){
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        if (user==null){
            return "index/login/index";
        }else {
            return "redirect:/";
        }
    }

    @RequestMapping("userlogin")
    @ResponseBody
    public JSONObject userLogin(String username,String password, HttpServletRequest request, HttpServletResponse response)throws Exception{
        JSONObject result = new JSONObject();
        int verification = Verification.verification(request, response);
        if (verification != Verification.SUCCESS) {
            result.put("success",false);
            return result;
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, CryptographyUtil.md5(password,"codetip"));
        try {
            subject.login(token);
            User user = userMapper.findUserByName(username);
            SecurityUtils.getSubject().getSession().setAttribute("currentUser", user); //把当前用户信息存到session中
            result.put("success",true);
            return result;
        }catch (Exception e){
            result.put("success",false);
            return result;
        }


    }

    @RequestMapping("askContent")
    @ResponseBody
    public JSONObject askContent(Ask ask) throws Exception{
        JSONObject result = new JSONObject();
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        if (ask.getContent()==null||ask.getContent().trim().equals("")||ask.getTitle()==null||ask.getTitle().trim().equals("")||ask.getCates()==null||ask.getCates().trim().equals("")){
            result.put("success",false);
            return result;
        }
        try {
            ask.setCreateDate(new Date());
            ask.setUpdateDate(new Date());
            ask.setUid(user.getId());
            askMapper.insertAsk(ask);
            result.put("success",true);
            return result;
        }catch (Exception e){
            result.put("success",false);
            return result;
        }
    }

    @RequestMapping("question/{id}")
    public ModelAndView question(@PathVariable(name = "id") int id){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index/question/index");
        Map ask = askMapper.findAskById(id);
        List<Map> answers = askAnswerMapper.findAnswersById(id);
        int askCount = askAnswerMapper.askCount(id);
        List tags = Arrays.asList(ask.get("cates").toString().split(","));
        askMapper.addVisit(id);
        mav.addObject("ask",ask);
        mav.addObject("tags",tags);
        mav.addObject("answers",answers);
        mav.addObject("ask_id",id);
        mav.addObject("askCount",askCount);
        return mav;
    }

    @RequestMapping("askAnswer")
    @ResponseBody
    public JSONObject askAnswerContent(AskAnswer askAnswer)throws Exception{
        JSONObject result = new JSONObject();
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        try {
            askAnswer.setCreateDate(new Date());
            askAnswer.setUpdateDate(new Date());
            askAnswer.setUid(user.getId());
            askAnswerMapper.insertAnswer(askAnswer);
            result.put("success",true);
            return result;
        }catch (Exception e){
            result.put("success",false);
            return result;
        }

    }

    @RequestMapping("person/{uid}")
    public ModelAndView person(@PathVariable(name = "uid")int uid,String page){
        if (page==null){
            page="1";
        }
        ModelAndView mav = new ModelAndView();
        PageHelper.startPage(Integer.parseInt(page),10);
        List<Map> asks = askMapper.findAskByUId(uid);
        PageInfo<Map> pageInfo = new PageInfo<>(asks);
        mav.addObject("page",Integer.parseInt(page));
        mav.addObject("pages",pageInfo.getPages());
        mav.addObject("asks",asks);
        User user = userMapper.findUser(uid);
        mav.addObject("user",user);
        mav.setViewName("index/person/index");
        return mav;
    }

    @RequestMapping("register")
    public ModelAndView register(){
        ModelAndView mav = new ModelAndView();

        mav.setViewName("index/register/index");
        return mav;
    }

    @RequestMapping("reg")
    @ResponseBody
    public JSONObject reg(User user, HttpServletRequest request, HttpServletResponse response)throws Exception{
        JSONObject result = new JSONObject();
        int verification = Verification.verification(request, response);
        if (verification != Verification.SUCCESS) {
            result.put("success",false);
            return result;
        }
        User str1 = userMapper.findUserByName(user.getUsername());//用作判断用户名是否存在的查询
        if (user.getUsername()==null||user.getUsername().trim().equals("")){
            result.put("success",false);
            return result;
        }
        try {
            user.setCreateDate(new Date());
            user.setGithubId(0);
            user.setName(user.getUsername());
            user.setUpdateDate(new Date());
            user.setImg("https://blogimg-1256098337.cos.ap-chengdu.myqcloud.com/v2-6afa72220d29f045c15217aa6b275808_hd.jpg");
            user.setPassword(CryptographyUtil.md5(user.getPassword(),"codetip"));
            try {//因为在查询用户是否存在的时候，如果不存在的话，返回的是一个异常，如果不进行抛出，下面的代码就无法执行了
                if (str1.getUsername()!=null){
                    result.put("user",true);
                    result.put("success",false);
                    return result;
                }
            }catch (Exception e){}
            userMapper.addUser(user);
            result.put("success",true);
            return result;
        }catch (Exception e){
            result.put("success",false);
            return result;
        }

    }

}

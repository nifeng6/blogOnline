package net.codetip.blog.controller.index.studyTest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {

    @RequestMapping("studyTest/beginTest")
    public ModelAndView beginTest(){
        ModelAndView mav = new ModelAndView();


        mav.setViewName("index/studyTest/beginTest");
        return mav;
    }

}

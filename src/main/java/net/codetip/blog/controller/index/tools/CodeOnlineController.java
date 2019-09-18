package net.codetip.blog.controller.index.tools;

import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

@Controller
public class CodeOnlineController {
    @RequestMapping("/tools/CodeOnline")
    public ModelAndView CodeOnline(){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index/tools/codeOnline");
        return mav;
    }


    @RequestMapping("/tools/testCode")
    @ResponseBody
    public JSONObject testCode(String s, String codeType)throws Exception{
        JSONObject result = new JSONObject();
        try {
            //中间部分为Socket
            Socket socket = null;
            String Code_Adress = "127.0.0.1";
            if (codeType.equals("python")){
                socket = new Socket(Code_Adress,9000);
            }
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            outputStream.write(s.getBytes());
            int len = inputStream.read(bytes);
            String str = new String(bytes,0,len);
            result.put("code",str);
            //中间部分为Socket
            result.put("success",true);
            return result;
        }catch (Exception e){
            result.put("success",false);
            return result;
        }


    }


}

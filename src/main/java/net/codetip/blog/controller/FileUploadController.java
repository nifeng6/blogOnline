package net.codetip.blog.controller;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import net.codetip.blog.domain.User;
import net.codetip.blog.dto.TencentConfig;
import net.codetip.blog.dto.UploadMsg;
import net.codetip.blog.mapper.UserMapper;
import net.sf.json.JSONObject;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("upload")
public class FileUploadController {

    @Autowired
    private TencentConfig tencentConfig;

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("markDownImgUpload")
    @ResponseBody
    public Object markDownImgUpload(HttpServletRequest request){
        MultipartHttpServletRequest multipartHttpRequest = (MultipartHttpServletRequest) request;
        MultipartFile file = multipartHttpRequest.getFile("editormd-image-file");
        JSONObject result = new JSONObject();
        if (file == null) {
            result.put("errno",1);
            result.put("data","上传失败");
//            result.put("url","test");
            return result;
        }
        String oldFileName = file.getOriginalFilename();
        String eName = oldFileName.substring(oldFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + eName;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        // 1 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(tencentConfig.getAccessKey(), tencentConfig.getSecretKey());
        // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(tencentConfig.getBucket()));
        // 3 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
        String bucketName = tencentConfig.getBucketName();
        // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
        // 大文件上传请参照 API 文档高级 API 上传
        File localFile = null;
        try {//上传成功
            localFile = File.createTempFile("temp", null);
            file.transferTo(localFile);
            // 指定要上传到 COS 上的路径
            String key = "/" + tencentConfig.getPrefix() + "/" + year + "/" + month + "/" + day + "/" + newFileName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
            PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
            System.out.println(tencentConfig.getPath() + putObjectRequest.getKey());
            List list = new ArrayList();
            list.add(tencentConfig.getPath() + putObjectRequest.getKey().toString());
            result.put("data",list);//返回一个数组
            result.put("errno",0);//成功返回0，不成功返回!0
            return result;
        } catch (IOException e) {
            return new UploadMsg(1, e.getMessage(), null);
        } finally {
            // 关闭客户端(关闭后台线程)
            cosclient.shutdown();
        }

    }

    @RequestMapping("accessHeadImg")
    @ResponseBody
    public JSONObject accessHeadImg(@RequestParam("input-file-preview") MultipartFile file){
        Subject subject = SecurityUtils.getSubject();
        User user = (User)subject.getPrincipal();
        JSONObject result = new JSONObject();
        if (file == null) {
            result.put("success",false);
            return result;
        }
        String oldFileName = file.getOriginalFilename();
        String eName = oldFileName.substring(oldFileName.lastIndexOf("."));
        String newFileName = UUID.randomUUID() + eName;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DATE);
        COSCredentials cred = new BasicCOSCredentials(tencentConfig.getAccessKey(), tencentConfig.getSecretKey());
        ClientConfig clientConfig = new ClientConfig(new Region(tencentConfig.getBucket()));
        COSClient cosclient = new COSClient(cred, clientConfig);
        String bucketName = tencentConfig.getBucketName();
        File localFile = null;
        try {
            localFile = File.createTempFile("temp", null);//获取本地文件
            file.transferTo(localFile);
            String key = "/" + tencentConfig.getPrefix() + "/" + year + "/" + month + "/" + day + "/" + newFileName;
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
            PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
            System.out.println(tencentConfig.getPath() + putObjectRequest.getKey());
            System.out.println(user.getId());
            userMapper.updateUserImg(tencentConfig.getPath() + putObjectRequest.getKey(),user.getId());
            result.put("success",true);
            return result;
        }catch (Exception e){
            result.put("success",false);
            return result;
        } finally {
            cosclient.shutdown();
        }

    }
}

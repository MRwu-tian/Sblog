package org.example.controller;


import org.example.bean.BeanUtil;
import org.example.bean.UserInfo;
import org.example.service.UserInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class VlogUserController {
    @Resource(name="userInfoService")
    private UserInfoService userInfoService;
    @Resource
    private UserInfo userInfo;
    @Resource
    private BeanUtil beanUtil;

    @RequestMapping("/getUserInfoList.html/{id}")
    @CrossOrigin
    public BeanUtil getUserInfoList(@PathVariable("id") int id){
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("userInfoList",userInfoService.getAllUserInfo(id));
        return  BeanUtil.ok("成功",map);
    }

    @RequestMapping(value = "/uploadfile")
    @CrossOrigin
    public BeanUtil uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        // 文件名
        String fileName = file.getOriginalFilename();
        // 在file文件夹中创建名为fileName的文件
        assert fileName != null;
        int split = fileName.lastIndexOf(".");
        // 文件后缀，用于判断上传的文件是否是合法的
        String suffix = fileName.substring(split+1,fileName.length());
        //判断文件类型，因为我这边是图片，所以只设置三种合法格式
        String url = "";
        if("jpg".equals(suffix) || "jpeg".equals(suffix) || "png".equals(suffix)) {
            // 正确的类型，保存文件
            try {
                File path = new File(ResourceUtils.getURL("classpath:").getPath());
                File upload = new File(path.getAbsolutePath(), "upload/");
                if(!upload.exists()) {
                    upload.mkdirs();
                }
                String newName = System.currentTimeMillis() + "."+suffix;
                String homepath = "/home/upload/images";
                File savedFile = new File(upload + "/" + newName);
                file.transferTo(savedFile);
                url = savedFile.getAbsolutePath();
                userInfoService.addurl(url);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }else {
            //错误的类型，返回错误提示

        }
        File savedFile;
        return BeanUtil.ok(url);
    }

       @RequestMapping("/updateUserInfo.html")
       @CrossOrigin
       public   BeanUtil updateUserInfo(UserInfo userInfo){
          if(userInfoService.updateUserInfo(userInfo)>0){
              return beanUtil.ok("修改用户成功");

          }else{
              return beanUtil.error("修改用户失败");
          }
       }
}

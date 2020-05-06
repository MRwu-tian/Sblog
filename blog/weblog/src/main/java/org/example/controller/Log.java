package org.example.controller;

import org.example.bean.RespBean;
import org.example.service.LoginServiceImpl;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping
public class Log {

    @Resource
    private LoginServiceImpl loginService;

    @RequestMapping("/test1")
    public String test1(){
        return  "成功";
    }

    @PostMapping("/reg/register/{regname}")
    public String register(@PathVariable("regname") String regname){
        int result = loginService.register(regname);
        if (result==1){
            return "error";
        }else{
            return "ok";
        }
    }

    @PostMapping("/reg/editreg/{regname}/{regpassword}")
    public RespBean editReg(@PathVariable("regname") String regname,@PathVariable("regpassword") String regpassword){
        String username = "用户昵称"+regname;
        String hashpw = BCrypt.hashpw(regpassword, BCrypt.gensalt());
        int result = loginService.editReg(regname,hashpw,username);
        if (result==1){
            return RespBean.ok("成功已为您跳转至登陆页面");
        }else{
            return RespBean.error("失败请稍后重试");
        }
    }

}

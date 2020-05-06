package org.example.controller.img;

import org.example.bean.VerifyCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
public class VerifyCodeController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/code/image")
    public void code(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        VerifyCode vc = new VerifyCode();
        BufferedImage image = vc.getImage();
        String text = vc.getText();
        text = text.toLowerCase();
        redisTemplate.opsForValue().set("IMG : "+ text,text);
        redisTemplate.expire("IMG : "+ text, 1, TimeUnit.MINUTES);
        VerifyCode.output(image, resp.getOutputStream());
    }
}


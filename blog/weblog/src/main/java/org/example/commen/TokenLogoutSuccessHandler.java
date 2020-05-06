package org.example.commen;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bean.RespBean;
import org.example.bean.SUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Component
public class TokenLogoutSuccessHandler implements LogoutSuccessHandler {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        SUser user = (SUser) authentication.getPrincipal();
        String token = redisTemplate.opsForValue().get("SECURITY_USERNAME : " + user.getUsername());
        redisTemplate.expire("SECURITY_USERNAME : " + user.getUsername(), 0, TimeUnit.MICROSECONDS);
        redisTemplate.expire("SECURITY_TOKEN : " + token, 0, TimeUnit.MICROSECONDS);

        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper om = new ObjectMapper();
        PrintWriter out = response.getWriter();
        RespBean respBean = RespBean.ok("用户退出成功");
        out.write(om.writeValueAsString(respBean));
        out.flush();
        out.close();
    }
}
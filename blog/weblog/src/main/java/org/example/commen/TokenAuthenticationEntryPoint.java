package org.example.commen;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bean.RespBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
public class TokenAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        ObjectMapper om = new ObjectMapper();
        PrintWriter out = response.getWriter();
        RespBean respBean = RespBean.ok("用户未登陆");
        out.write(om.writeValueAsString(respBean));
        out.flush();
        out.close();
    }
}

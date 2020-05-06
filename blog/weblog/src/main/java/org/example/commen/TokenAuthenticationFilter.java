package org.example.commen;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bean.RespBean;
import org.example.bean.SUser;
import org.example.bean.Users;
import org.example.mapper.LoginMapper;
import org.example.service.LoginServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class TokenAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private LoginMapper loginMapper;
    //Redis模板
    private StringRedisTemplate redisTemplate;
    //spring自带的Jackson
    private ObjectMapper objectMapper;

    private AuthenticationManager authenticationManager;

    public TokenAuthenticationFilter(AuthenticationManager authenticationManager, StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.redisTemplate = redisTemplate;
        super.setFilterProcessesUrl("/log/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(
                    "Authentication method not supported: " + request.getMethod());
        }
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE) || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)){
            ObjectMapper objectMapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken authenticationToken = null;
            try (InputStream is = request.getInputStream()){
                Map<String,String> map = objectMapper.readValue(is, Map.class);
                authenticationToken = new UsernamePasswordAuthenticationToken(
                        map.get("username"),map.get("password"));
            }catch (IOException e){
                e.printStackTrace();
                authenticationToken = new UsernamePasswordAuthenticationToken("","");
            }finally {
                setDetails(request,authenticationToken);
                return this.getAuthenticationManager().authenticate(authenticationToken);
            }
        }else {
            return super.attemptAuthentication(request,response);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SUser user = (SUser) authResult.getPrincipal();
        String uuid = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("SECURITY_TOKEN : " + uuid, user.getUsername());
        redisTemplate.opsForValue().set("SECURITY_USERNAME : " + user.getUsername(), uuid);
        redisTemplate.expire("SECURITY_TOKEN : " + uuid, 60*5, TimeUnit.MINUTES);
        redisTemplate.expire("SECURITY_USERNAME : " + user.getUsername(), 60, TimeUnit.MINUTES);
        Users user1 = loginMapper.findUserByUsername(user.getUsername());
        Map<String,String> map = new HashMap<>();
        map.put("username",user.getUsername());
        map.put("token",uuid);
        map.put("name",user1.getUsername());
        map.put("picture",user1.getUserPictureIndex());
        response.setContentType("application/json;charset=utf-8");
        RespBean respBean = RespBean.ok("登陆成功", map);
        PrintWriter out = response.getWriter();
        ObjectMapper om = new ObjectMapper();
        out.write(om.writeValueAsString(respBean));
        out.flush();
        out.close();

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(401);
        ObjectMapper om = new ObjectMapper();
        PrintWriter out = response.getWriter();
        RespBean respBean = RespBean.error("用户名或密码不正确");
        out.write(om.writeValueAsString(respBean));
        out.flush();
        out.close();
    }

}


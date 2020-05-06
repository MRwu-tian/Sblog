package org.example.commen;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bean.RespBean;
import org.example.service.LoginDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

@Component
public class TokenAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private LoginDetailServiceImpl userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/log/login")){
            //获取服务器session池中的验证码
            String imageCode = request.getParameter("imageCode");
            imageCode = imageCode.toLowerCase();
            String code = redisTemplate.opsForValue().get("IMG : "+ imageCode);
            if (code==null){
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(401);
                ObjectMapper om = new ObjectMapper();
                PrintWriter out = response.getWriter();
                RespBean respBean = RespBean.error("验证码有误");
                out.write(om.writeValueAsString(respBean));
                out.flush();
                out.close();
                return;
            }else{
                redisTemplate.expire("IMG : " + imageCode, 0, TimeUnit.MICROSECONDS);
            }
        }
        //从请求头中取出token
        String token = request.getHeader("token");
        if(!StringUtils.isEmpty(token)) {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                //从redis中获取用户名
                String username = redisTemplate.opsForValue().get("SECURITY_TOKEN : "+ token);
                //从数据库中根据用户名获取用户
                UserDetails sUser = userDetailsService.loadUserByUsername(username);
                if (sUser != null) {
                    //解析并设置认证信息
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(sUser, null, sUser.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
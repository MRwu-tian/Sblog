package org.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.bean.RespBean;
import org.example.commen.*;
import org.example.service.LoginDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
//开启spring security的注解，也可加在spring boot的main函数上
@EnableWebSecurity
public class SecurityConfin extends WebSecurityConfigurerAdapter {
    @Autowired
    private LoginDetailServiceImpl userDetailsService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 权限鉴定过滤器
     */
    @Autowired
    private TokenAuthorizationFilter authorizationFilter;

    /**
     * 未登录结果处理
     */
    @Autowired
    private TokenAuthenticationEntryPoint authenticationEntryPoint;

    /**
     * 权限不足结果处理
     */
    @Autowired
    private TokenAccessDeniedHandler accessDeniedHandler;

    /**
     * 用户注销成功处理器
     */
    @Autowired
    private TokenLogoutSuccessHandler logoutSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //解决跨域问题
//        http.authorizeRequests().requestMatchers(CorsUtils::isPreFlightRequest).permitAll();
        http
                //关闭session，不再使用
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .csrf().disable()
                //未登录结果处理
                .httpBasic().authenticationEntryPoint(authenticationEntryPoint)
                .and()
                //权限不足结果处理
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler)
                .and()
                //权限设置管理
                .authorizeRequests()
                //放行以下url
                .antMatchers("/reg/register/*","/reg/editreg/*/*","/code/image").permitAll()
                //给对应的url设置权限（只有ADMIN才可以访问，除去ROLE_前缀，spring帮我们处理了）
                //在数据库中用户的role字段是要加ROLE_的ROLE_ADMIN才可以匹配到这里的ADMIN
//                .antMatchers("/test1").hasAnyRole("NOMAL","ADMIN")
                //所有请求都需要授权（除了放行的）
                .anyRequest().authenticated()
                .and()
                //设置登出url
                .logout().logoutUrl("/log/logout")
                //设置登出成功处理器（下面介绍）
                .logoutSuccessHandler(logoutSuccessHandler)
                .and()
                //开启登录
                .formLogin();

        //在UsernamePasswordAuthenticationFilter之前增加鉴权过滤器（需要修改，下面注销时解释）
        http.addFilterBefore(authorizationFilter, LogoutFilter.class);
        //用自定义的授权过滤器覆盖UsernamePasswordAuthenticationFilter
        http.addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Bean
    TokenAuthenticationFilter customAuthenticationFilter() throws Exception {
        TokenAuthenticationFilter filter = new TokenAuthenticationFilter(authenticationManagerBean(), redisTemplate, objectMapper);
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }


    //密码加密器，在授权时，框架为我们解析用户名密码时，密码会通过加密器加密在进行比较
    //将密码加密器交给spring管理，在注册时，密码也是需要加密的，再存入数据库中
    //用户输入登录的密码用加密器加密，再与数据库中查询到的用户密码比较
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth
                //设置使用自己实现的userDetailsService（loadUserByUsername）
                .userDetailsService(userDetailsService)
                //设置密码加密方式
                .passwordEncoder(bCryptPasswordEncoder());
    }
}

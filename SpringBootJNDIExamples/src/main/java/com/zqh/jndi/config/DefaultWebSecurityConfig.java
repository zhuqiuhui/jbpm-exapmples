package com.zqh.jndi.config;

import com.zqh.jndi.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Configuration("kieServerSecurity")
@EnableWebSecurity
public class DefaultWebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserService userService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        //使用BCryptPasswordEncoder对密码进行编码，所有用户密码均为123456
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //编码后为 $2a$10$D0SggGIvmdxpwF5.FfCLGegBscCx/D7s1pi4MgqEyLfm26z8Tm55W
        System.out.println("123456 密码编码: " + passwordEncoder.encode("123456"));
        return passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                // CSRF禁用
                .and().csrf().disable()
                .authorizeRequests()
                // 所有人允许访问 "/kpi/index","/kpi/error"
                .antMatchers("/kpi/index", "/kpi/error", "/kpi/success").permitAll()
                // 用户登录后可访问 "/kpi/**"
                .antMatchers("/kpi/**").authenticated()
                .and().exceptionHandling().accessDeniedHandler((request, response, arg2) -> response.sendRedirect("/spring/kpi/error"))
                .and().httpBasic()
                .and().headers().frameOptions().disable()
                //开启表单登录，即登录界面，登录URL为 /login，登录参数用户名username密码password
                .and().formLogin().usernameParameter("username").passwordParameter("password")
                // 登录表单form中action的地址，也就是处理认证请求的路径
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/kpi/success") //登录认证成功后默认转跳的路径
                .and().logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication arg2)
                            throws IOException, ServletException {
                        response.sendRedirect("/spring/kpi/index");
                    }
                });
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

}

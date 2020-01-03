package com.example.demo.config;

import com.example.demo.interceptor.MyFilterSecurityInterceptor;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

//業務核心
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private MyFilterSecurityInterceptor myFilterSecurityInterceptor;

    @Bean
    UserDetailsService customUserService() { //註冊UserDetailsService 的bean
        return new UserService();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserService()).passwordEncoder(NoOpPasswordEncoder.getInstance()); //user Details Service驗證
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // http.authorizeRequests()每個匹配器按照它們被宣告的順序被考慮。
        http
                .authorizeRequests()
                // 所有使用者均可訪問的資源
                .antMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "**/favicon.ico").permitAll()
                // ROLE_USER的許可權才能訪問的資源
                .antMatchers("/user/**").hasRole("USER")
                // 任何尚未匹配的URL只需要驗證使用者即可訪問
                .anyRequest().authenticated()
                .and()
                .formLogin()
                // 指定登入頁面,授予所有使用者訪問登入頁面
                .loginPage("/login")
                //設定預設登入成功跳轉頁面,錯誤回到login介面
                .defaultSuccessUrl("/index").failureUrl("/login?error").permitAll()
                .and()
                //開啟cookie儲存使用者資料
                .rememberMe()
                //設定cookie有效期
                .tokenValiditySeconds(60 * 60 * 24 * 7)
                //設定cookie的私鑰
                .key("security")
                .and()
                .logout()
                .permitAll();
        //登入攔截器
        http.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class)
                //springsecurity4自動開啟csrf(跨站請求偽造)與restful衝突
                .csrf().disable();
    }
}

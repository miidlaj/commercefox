package com.ecommerce.security;

import com.ecommerce.customer.CustomerUserDetailsService;
import com.ecommerce.security.oauth.CustomerOAuth2UserService;
import com.ecommerce.security.oauth.OAuth2LoginSuccessHandler;
import com.ecommerce.setting.CurrencySettingBag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.CachingUserDetailsService;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomerOAuth2UserService auth2UserService;

    @Autowired
    private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;

    @Bean
    public OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler() {
        return new OAuth2LoginSuccessHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/account_details", "/update_account_details", "/cart","/address_book/**",
                        "/checkout", "/place_order", "/process_paypal_order","/orders/**",
                        "reviews/**","write_review/**", "/post_review").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .usernameParameter("email")
                    .successHandler(databaseLoginSuccessHandler)
                    .permitAll()
                .and()
                .oauth2Login()
                    .loginPage("/login")
                    .userInfoEndpoint()
                    .userService(auth2UserService)
                .and()
                    .successHandler(oAuth2LoginSuccessHandler())
                .and()
                .logout().permitAll()
                .and()
                .rememberMe()
                    .key("1234567890_aBcDeFgHiJkLmNoPqRsTuVwXyZ")
                    .tokenValiditySeconds(14 * 24 * 60 * 60)

                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
        ;

    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/images/**","/js/**","/webjars/**","/assets/**");
    }

    @Bean
    public UserDetailsService userDetailsService(){
        return new CustomerUserDetailsService();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}

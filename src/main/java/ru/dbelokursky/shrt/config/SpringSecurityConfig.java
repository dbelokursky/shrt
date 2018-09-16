package ru.dbelokursky.shrt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        String findUser = "SELECT login, password, enabled FROM shrt_user WHERE login=?";
        String findRoles = "SELECT login, name FROM (shrt_user JOIN user_role USING (user_id)) JOIN role USING (role_id) WHERE login =?";

        auth
                .jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(findUser)
                .authoritiesByUsernameQuery(findRoles);
    }

    @Bean(name = "passwordEncoder")
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login", "/static/**", "/index").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/login")
                .usernameParameter("login")
                .passwordParameter("password")
                .and()
                .exceptionHandling()
                .accessDeniedPage("/accessdenied")
                .and()
                .csrf().disable();
    }
}

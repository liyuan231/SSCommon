package com.example.configuration;

import com.example.component.security.JwtAuthenticationFilter;
import com.example.component.security.LoginFailureHandler;
import com.example.component.security.LoginSuccessHandler;
import com.example.component.security.SimpleAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        jsr250Enabled = true,
        securedEnabled = true
)
public class SpringSecurityConfiguration {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowCredentials(true);// 允许前端携带cookie
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }


    @Configuration
    @Order(1)
    @ConditionalOnProperty(prefix = "jwt.config", name = "enabled")
    public static class UserSpringSecurityConfiguration extends WebSecurityConfigurerAdapter {

        @Autowired(required = false)
        private AuthenticationEntryPoint authenticationEntryPoint;
        //        @Bean
//        public SessionRegistry sessionRegistry() {
//            return new SessionRegistryImpl();
//        }
        @Autowired
        private LoginFailureHandler loginFailureHandler;
        @Autowired
        private LoginSuccessHandler loginSuccessHandler;

        @Autowired
        private SimpleAccessDeniedHandler simpleAccessDeniedHandler;

        @Autowired
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @Autowired
        private UserDetailsService userDetailsService;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            http.cors();
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            http.csrf().disable()
                    .antMatcher("/**")
                    .authorizeRequests().anyRequest().permitAll()
                    .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                    .and()
                    .formLogin()
                    .failureHandler(loginFailureHandler)
                    .successHandler(loginSuccessHandler)
                    .loginProcessingUrl("/api/_user_/_login_")
                    .and()
                    .logout().logoutUrl("/api/_user_/_logout_");
            http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(simpleAccessDeniedHandler);
        }
    }
}

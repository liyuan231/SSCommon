package com.example.configuration;

import com.example.component.security.JwtAuthenticationFilter;
import com.example.component.security.LoginFailureHandler;
import com.example.component.security.LoginSuccessHandler;
import com.example.component.security.SimpleAccessDeniedHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Primary
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }

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
    public static class UserSpringSecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Autowired(required = false)
        private AuthenticationEntryPoint authenticationEntryPoint;

        @Autowired
        @Qualifier("userDetailsServiceImpl")
        private UserDetailsService userDetailsService;

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

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.userDetailsService(userDetailsService);
            http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
            http.cors();
//            http.antMatcher("/api/tagging/user/login").authorizeRequests().anyRequest().permitAll();
            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            http.csrf().disable()
                    .antMatcher("/api/tagging/**")
                    .authorizeRequests().anyRequest().permitAll()
                    .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                    .and()
                    .formLogin()
                    .failureHandler(loginFailureHandler)
                    .successHandler(loginSuccessHandler)
                    .loginProcessingUrl("/api/_tagging_/_user_/login")
                    .and()
                    .logout().logoutUrl("/api/tagging/user/logout");
//                    .and().sessionManagement()
//                    .maximumSessions(1)
//                    .expiredSessionStrategy(new SessionInformationExpiredStrategyImpl(userDetailsService));
            http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
                    .accessDeniedHandler(simpleAccessDeniedHandler);
        }

        /**
         * 当然监听session是否过期，首选这个方法
         *
         * @return
         */
//        private static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

//        @Bean
//        public HttpSessionListener httpSessionListener(TaggingIndonesianServiceImpl taggingService) {
//
//            return new HttpSessionListener() {
//                @Override
//                public void sessionCreated(HttpSessionEvent se) {
//                    logger.info(se.getSession().getId() + " [SESSION CREATED]");
//                }
//
//                @Override
//                public void sessionDestroyed(HttpSessionEvent se) {
//                    SecurityContextImpl securityContext = (SecurityContextImpl) se.getSession().getAttribute(SPRING_SECURITY_CONTEXT);
//                    Authentication authentication = securityContext.getAuthentication();
//                    User user = (User) authentication.getPrincipal();
//                    TaggingUser u = userDetailsService.queryByUsername(user.getUsername(), TaggingUser.Column.id);
//                    taggingService.clearUsingByUserId(u.getId());
//                    logger.info(user.getUsername() + "[SESSION EXPIRED]");
//                }
//            };
//        }
    }

//
//    @Configuration
//    @Order(2)
//    public static class AdminSpringSecurityConfiguration extends WebSecurityConfigurerAdapter {
//
//
//        @Autowired
//        @Qualifier("adminServiceImpl")
//        private AdminServiceImpl userDetailsService;
//
////        @Bean
////        public SessionRegistry sessionRegistry() {
////            return new SessionRegistryImpl();
////        }
//
//        @Autowired
//        private SimpleAuthenticationEntryPoint authenticationEntryPoint;
//
//        @Autowired
//        private JwtAuthenticationFilter jwtAuthenticationFilter;
//
//        @Autowired
//        private LoginFailureHandler loginFailureHandler;
//        @Autowired
//        private LoginSuccessHandler loginSuccessHandler;
//
//        @Autowired
//        private SimpleAuthenticationEntryPoint simpleAuthenticationEntryPoint;
//
//        @Autowired
//        private SimpleAccessDeniedHandler simpleAccessDeniedHandler;
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http.userDetailsService(userDetailsService);
//            http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//            http.cors();
////            http.antMatcher("/api/tagging/user/login").authorizeRequests().anyRequest().permitAll();
//            http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//            http.csrf().disable()
//                    .formLogin()
//                    .failureHandler(loginFailureHandler)
//                    .successHandler(loginSuccessHandler)
//                    .loginPage("/api/tagging/admin/login");
//            http.exceptionHandling().authenticationEntryPoint(simpleAuthenticationEntryPoint)
//                    .accessDeniedHandler(simpleAccessDeniedHandler);
//        }
//    }
}

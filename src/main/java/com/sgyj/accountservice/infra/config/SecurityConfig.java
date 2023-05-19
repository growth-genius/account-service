package com.sgyj.accountservice.infra.config;

import com.sgyj.accountservice.infra.properties.AppProperties;
import com.sgyj.accountservice.infra.properties.JwtProperties;
import com.sgyj.accountservice.infra.security.EntryPointHandler;
import com.sgyj.accountservice.infra.security.Jwt;
import com.sgyj.accountservice.infra.security.JwtAccessDeniedHandler;
import com.sgyj.accountservice.infra.security.JwtAuthenticationProvider;
import com.sgyj.accountservice.infra.security.JwtAuthenticationTokenFilter;
import com.sgyj.accountservice.modules.account.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final Jwt jwt;

    private final JwtProperties jwtProperties;

    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    private final EntryPointHandler unAuthorizedHandler;
    private final AppProperties appProperties;
    @Bean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter(jwtProperties.getHeader(), jwt);
    }

    @Bean
    public JwtAuthenticationProvider jwtAuthenticationProvider(AccountService accountService) {
        return new JwtAuthenticationProvider(accountService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic().disable()                                              // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
            .csrf().disable()                                              // rest api 이므로 csrf 보안이 필요없으므로 disable 처리.
            .cors().disable()
            .exceptionHandling().accessDeniedHandler(jwtAccessDeniedHandler).authenticationEntryPoint(unAuthorizedHandler).and().headers().frameOptions()
            .sameOrigin().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token 으로 인증하므로 세션은 필요없으므로 생성안함.
            .and().authorizeHttpRequests()
            .anyRequest().permitAll().and().addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}

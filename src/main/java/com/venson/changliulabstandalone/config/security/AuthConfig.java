package com.venson.changliulabstandalone.config.security;

import com.venson.changliulabstandalone.adapter.AuthPathAdapter;
import com.venson.changliulabstandalone.service.TokenManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.util.pattern.PathPatternParser;


/**
 * test config to use new feature of spring changliulabstandalone
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Slf4j
@RequiredArgsConstructor
public class AuthConfig {
    private final TokenManager tokenManager;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuthPathAdapter pathAdapter;

//    @Qualifier("delegatedAuthenticationEntryPoint")
    private final AuthenticationEntryPoint authEntryPoint;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((matcher) -> matcher
                        .requestMatchers("/auth/admin/login", "/auth/front/login").anonymous())
                .authorizeHttpRequests((matcher) -> matcher
                        .requestMatchers("*/front/**").permitAll())
                .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
//        authorizeHttpRequests((matchers) ->matchers.requestMatchers(HttpMethod.GET,
//                        "/swagger-ui.html",
//                        "/swagger-ui/",
//                        "/*.html",
//                        "/favicon.ico",
//                        "/*/*.html",
//                        "/*/*.css",
//                        "/*/*.js",
//                        "/swagger-resources/**",
//                        "/v3/api-docs/**"
//                        ).permitAll()).
//        httpBasic().disable().formLogin().disable()
//        http.csrf().disable().httpBasic().disable();

                .sessionManagement((session) ->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
                .addFilterBefore(new AuthTokenFilter(tokenManager, redisTemplate, pathAdapter), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling((httpSecurityExceptionHandlingConfigurer -> httpSecurityExceptionHandlingConfigurer
                        .authenticationEntryPoint(authEntryPoint)));
//                .authenticationEntryPoint(authEntryPoint);
//                .addFilterBefore(new AuthTokenFilter(tokenManager, redisTemplate, pathAdapter), BasicAuthenticationFilter.class);
        return http.build();

    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedMethod("*");
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
//        configuration.addExposedHeader("*");
//        configuration.setAllowedOrigins(List.of("*"));
//        configuration.setAllowedMethods(List.of("GET","POST","OPTIONS"));
//        configuration.setAllowedHeaders(List.of( "authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

//    @Bean
//    CorsConfigurationSource corsConfigurationSource()
//    {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.addAllowedHeader("*");
//        configuration.addAllowedMethod("*");
//        configuration.addAllowedOrigin("*");
//        configuration.addExposedHeader("*");
//        configuration.addExposedHeader("*");
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(List.of("/**"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
//        configuration.setExposedHeaders(List.of("Authorization"));
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
}

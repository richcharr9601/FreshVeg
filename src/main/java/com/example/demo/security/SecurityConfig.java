package com.example.demo.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.security.filter.JwtAuthenticationFilter;
import com.example.demo.security.service.JwtService;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final CustomAccessDeniedHandler customAccessDeniedHandler;

  private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;

  private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
  
  private final JwtService jwtService;

  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final AuthenticationProvider authenticationProvider;
  // private final LogoutHandler logoutHandler;
  


  public SecurityConfig(CustomAccessDeniedHandler customAccessDeniedHandler ,AuthenticationProvider authenticationProvider,JwtAuthenticationFilter jwtAuthenticationFilter ,JwtService jwtService, RestAuthenticationEntryPoint restAuthenticationEntryPoint, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler) {
      this.customAccessDeniedHandler = customAccessDeniedHandler;
      this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
      this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
      this.jwtService = jwtService;
      this.jwtAuthenticationFilter = jwtAuthenticationFilter;
      this.authenticationProvider = authenticationProvider;
      // this.logoutHandler = logoutHandler;

    
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {  
    http
        .csrf()
        .disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .accessDeniedHandler(customAccessDeniedHandler)
            .authenticationEntryPoint(restAuthenticationEntryPoint)
        .and()
        .oauth2Login()
            .successHandler(customAuthenticationSuccessHandler)
            .failureHandler((request, response, exception) -> response.setStatus(HttpServletResponse.SC_BAD_REQUEST))
    ;

    http.authorizeHttpRequests()
  //  .requestMatchers("/**").permitAll();
            .requestMatchers("/product/all").permitAll()
            .requestMatchers("/category/all").permitAll()
            .requestMatchers("/all/{categoryId}").permitAll()
            .requestMatchers("/product/{productId}").permitAll()
            .requestMatchers("/user/**").hasAuthority("ADMIN")
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers("/oauth/**").permitAll()
            .requestMatchers("localhost:8080/product").permitAll()
            .requestMatchers("/checkout/payment-information/**").permitAll()
            .requestMatchers("localhost:8080/address").hasAuthority("USER")
            .requestMatchers("/address/**").hasAuthority("USER")
            .requestMatchers("/product/**").hasAuthority("ADMIN")
            .requestMatchers("/category/**").hasAuthority("ADMIN")
            .requestMatchers("/statistic/**").hasAuthority("ADMIN")
            .requestMatchers("/order").hasAuthority("USER")
            .requestMatchers("/checkout/**").hasAuthority("USER")
            .requestMatchers("/user/**").hasAuthority("USER")
            .requestMatchers("/order/**").hasAuthority("USER");          
    return http.build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    List<String> list = new ArrayList<String>();
    list.add("*");
    configuration.setAllowedOrigins(list);
    configuration.addAllowedHeader("*");
    configuration.addAllowedMethod("*");
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}

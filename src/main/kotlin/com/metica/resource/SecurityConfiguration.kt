package com.metica.resource;


import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile


@Configuration
//@Profile("default")
//@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
//@EnableGlobalAuthentication
class SecurityConfiguration {

//    fun jwtGrantedAuthoritiesConverter(): JwtGrantedAuthoritiesConverter {
//        val converter = JwtGrantedAuthoritiesConverter()
//        converter.setAuthoritiesClaimName("cognito:groups")
//        converter.setAuthorityPrefix("ROLE_")
//        return converter
//    }
//
//    @Bean
//    fun customJwtAuthenticationConverter(): JwtAuthenticationConverter {
//        val converter = JwtAuthenticationConverter()
//        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter())
//        return converter
//    }

//    @Order(1)
//    @Bean
//    fun apiFilterChain(http: HttpSecurity): SecurityFilterChain {
//        http {
//            antMatcher("/api/**")
//            httpBasic { }
//        }
//
//        return http.build()
//    }
//
//    @Bean
//    protected void configure(HttpSecurity http) throws Exception {
//        http.csrf()
//                .and()
//                .authorizeRequests()
//                        .requestMatchers("/actuator/**").permitAll()
//                        .requestMatchers("/").permitAll()
//                        .anyRequest().authenticated()
//                .oauth2ResourceServer();
//        return http.build();
//    }
}
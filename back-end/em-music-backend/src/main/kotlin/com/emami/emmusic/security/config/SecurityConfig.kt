package com.emami.emmusic.security.config

import com.emami.emmusic.security.repo.EmUserRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun userDetailsService(userRepository: EmUserRepository): UserDetailsService {
        return UserDetailsService {
            val user = userRepository.findByUsername(it)
            if (user.isPresent) {
                return@UserDetailsService user.get()
            }
            throw UsernameNotFoundException("User not found")
        }
    }

    @Bean
    fun securityFilterChain(
        httpSecurity: HttpSecurity,
        authenticationProvider: AuthenticationProvider,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
        customAuthenticationException: CustomAuthenticationException
    ): SecurityFilterChain {
        return httpSecurity
            .formLogin {
                it
                    .disable()
            }
            .csrf {
                it
                    .disable()
            }
            .sessionManagement {
                it
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests {
                it
                    .requestMatchers("/api/login", "/api/login/**", "/api/public", "/api/public/**","/error")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
                /*it
                    .anyRequest()
                    .permitAll()*/
//                it
//                    .requestMatchers("/api/private", "/api/private/**")
//                    .authenticated()
//                    .anyRequest()
//                    .permitAll()
//                it
//                    .requestMatchers("/api/private", "/api/private/**")
//                    .authenticated()
//                    .anyRequest()
//                    .permitAll()


            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .exceptionHandling {
                it
                    .authenticationEntryPoint(customAuthenticationException)
            }
            .build()

    }

    @Bean
    fun AuthenticationProvider(
        userDetailsService: UserDetailsService,
        passwordEncoder: PasswordEncoder
    ): AuthenticationProvider {
        val dao = DaoAuthenticationProvider()
        dao.setUserDetailsService(userDetailsService)
        dao.setPasswordEncoder(passwordEncoder)
        return dao
    }

    @Bean
    fun AuthenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }
}
package com.emami.emmusic.security.config

import com.emami.emmusic.security.repo.EmUserRepository
import com.emami.emmusic.security.service.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtService: JwtService,
    private val emUserRepository: EmUserRepository
) : OncePerRequestFilter() {

    @Value("\${em-music.security.is-test}")
    lateinit var isTest: String
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (isTest=="true"){
            val userDetails = emUserRepository.findById(1)
            val authToken =
                UsernamePasswordAuthenticationToken(userDetails, null, userDetails.get().authorities)
            authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authToken
            filterChain.doFilter(request, response)
            return
        }

        val authHeader: String? = request.getHeader("Authentication")
        if (authHeader == null || !authHeader.startsWith("EmJWT ")) {
            filterChain.doFilter(request, response);
            return
        }
        val jwt: String = authHeader.substring(6)
        try {
            val username: String = jwtService.extractUsername(jwt)
            if (SecurityContextHolder.getContext().authentication == null) {
                val userDetails = emUserRepository.findByUsername(username)
                if (userDetails.isPresent && jwtService.isTokenValid(jwt, userDetails.get())) {
                    val authToken =
                        UsernamePasswordAuthenticationToken(userDetails, null, userDetails.get().authorities)
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        } catch (e: Exception) {
            println("error: ${e.message}")
        }
        filterChain.doFilter(request, response)
    }
}
package com.emami.emmusic.security.config

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class CustomAuthenticationException : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {
        response?.contentType = "application/json"
        response?.status = HttpStatus.UNAUTHORIZED.value()
        response?.writer?.format(
            """
                                  {
                                    "errors":[
                                      {
                                        "status": %d,
                                        "title": "%s",
                                        "detail": "%s",
                                        "emamiMessage":true
                                      }
                                    ]
                                  }
            """,
            HttpStatus.UNAUTHORIZED.value(),
            HttpStatus.UNAUTHORIZED.name,
            authException?.message
        )
    }
}

package com.emami.emmusic.security.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date
import java.util.function.Function
import javax.crypto.SecretKey

@Service
class JwtService {
    fun generateToken(extraClaims: MutableMap<String, Any>, user: UserDetails): String {
        return Jwts.builder()
            .claims(extraClaims)
            .subject(user.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + 1000 * 60 * 60))
            .signWith(getSigningKey(), Jwts.SIG.HS256)
            .compact()

    }

    fun <T> extractClaims(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = extractAllClaims(token)
        return claimsResolver.apply(claims)
    }

    fun generateToken(user: UserDetails): String {
        return generateToken(mutableMapOf(), user)
    }

    fun isTokenValid(token: String, user: UserDetails): Boolean {
        val username = extractUsername(token)
        return user.username == username && !isExpired(token)
    }

    private fun isExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    private fun extractExpiration(token: String): Date {
        return extractClaims(token, Claims::getExpiration)
    }

    fun extractUsername(token: String): String {
        return extractClaims(token, Claims::getSubject)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts
            .parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }

    companion object {
        private const val SECRET_KEY = "51a38d9a6fcf8e14163e235615c3fa8b9eaef3e8691a0e82e4feb2954a233133"
    }

    private fun getSigningKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(SECRET_KEY)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}
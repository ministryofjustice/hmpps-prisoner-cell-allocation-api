package uk.gov.justice.digital.hmpps.prisonercellallocationapi.config

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter

class AuthAwareTokenConverter : Converter<Jwt, AbstractAuthenticationToken> {
  private val jwtGrantedAuthoritiesConverter: Converter<Jwt, Collection<GrantedAuthority>> =
    JwtGrantedAuthoritiesConverter()

  companion object {
    const val userName: String = "user_name"
    const val clientId: String = "client_id"
  }

  override fun convert(jwt: Jwt): AbstractAuthenticationToken {
    val claims = jwt.claims
    val principal = findPrincipal(claims)
    val authorities = extractAuthorities(jwt)
    return AuthAwareAuthenticationToken(jwt, principal, authorities)
  }

  private fun findPrincipal(claims: Map<String, Any?>): String {
    return if (claims.containsKey(userName)) {
      claims[userName] as String
    } else {
      claims[clientId] as String
    }
  }

  private fun extractAuthorities(jwt: Jwt): Collection<GrantedAuthority> {
    val authorities = mutableListOf<GrantedAuthority>().apply { addAll(jwtGrantedAuthoritiesConverter.convert(jwt)!!) }
    if (jwt.claims.containsKey("authorities")) {
      @Suppress("UNCHECKED_CAST")
      val claimAuthorities = (jwt.claims["authorities"] as Collection<String>).toList()
      authorities.addAll(claimAuthorities.map(::SimpleGrantedAuthority))
    }
    return authorities.toSet()
  }
}

class AuthAwareAuthenticationToken(
  jwt: Jwt,
  private val aPrincipal: String,
  authorities: Collection<GrantedAuthority>,
) : JwtAuthenticationToken(jwt, authorities) {
  override fun getPrincipal(): String {
    return aPrincipal
  }

  fun hasUserName(): Boolean {
    return this.token.claims["user_name"] != null
  }
}

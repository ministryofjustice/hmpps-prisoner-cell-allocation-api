package uk.gov.justice.digital.hmpps.prisonercellallocationapi.integration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import uk.gov.justice.digital.hmpps.config.JwtAuthHelper
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.utils.RequestFactory

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(JwtAuthHelper::class)
@ActiveProfiles("test", "validate")
abstract class IntegrationTestBase {

  @Suppress("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  lateinit var webTestClient: WebTestClient

  val objectMapper: ObjectMapper by lazy { ObjectMapper().registerModule(JavaTimeModule()) }

  val factory: RequestFactory = RequestFactory()

  @Autowired
  protected lateinit var jwtAuthHelper: JwtAuthHelper

  companion object {
    internal val prisonApiMockServer = PrisonApiMockServer()

    @BeforeAll
    @JvmStatic
    fun startMocks() {
      prisonApiMockServer.start()
    }

    @AfterAll
    @JvmStatic
    fun stopMocks() {
      prisonApiMockServer.stop()
    }
  }

  init {
    // Resolves an issue where Wiremock keeps previous sockets open from other tests causing connection resets
    System.setProperty("http.keepAlive", "false")
  }
  internal fun <S : WebTestClient.RequestHeadersSpec<S>?> WebTestClient.RequestHeadersSpec<S>.withBearerToken(token: String) =
    this.apply { header(HttpHeaders.AUTHORIZATION, token) }

  internal fun setAuthorisationWithUser(
    roles: List<String> = listOf(),
    scopes: List<String> = listOf(),
  ): (HttpHeaders) -> Unit = jwtAuthHelper.setAuthorisation("prisoner-cell-allocation-api-user", roles, scopes)
  internal fun setAuthorisationWithoutUser(
    roles: List<String> = listOf(),
    scopes: List<String> = listOf(),
  ): (HttpHeaders) -> Unit = jwtAuthHelper.setAuthorisation(roles, scopes)

  internal fun getAuthorisation(
    roles: List<String> = listOf(),
    scopes: List<String> = listOf(),
    username: String = "prisoner-cell-allocation-api-user",
  ) = jwtAuthHelper.getAuthorisation(user = username, roles = roles, scopes = scopes)
}

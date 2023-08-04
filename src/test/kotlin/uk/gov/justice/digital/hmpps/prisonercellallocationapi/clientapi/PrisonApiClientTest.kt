package uk.gov.justice.digital.hmpps.prisonercellallocationapi.clientapi

import com.github.tomakehurst.wiremock.client.WireMock
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.eclipse.jetty.client.HttpClient
import org.eclipse.jetty.client.api.Request
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.http.client.reactive.JettyClientHttpConnector
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.http.codec.json.Jackson2JsonDecoder
import org.springframework.http.codec.json.Jackson2JsonEncoder
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.config.ClientErrorResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.config.ClientException
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.integration.PrisonApiMockServer
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMoveResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.MoveToCellSwapRequest
import java.net.URI
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime

class PrisonApiClientTest {
  private lateinit var prisonApiClient: PrisonApiClient

  companion object {
    @JvmField
    internal val mockServer = PrisonApiMockServer()
    private val logger = LoggerFactory.getLogger(this::class.java)

    @BeforeAll
    @JvmStatic
    fun startMocks() {
      mockServer.start()
    }

    @AfterAll
    @JvmStatic
    fun stopMocks() {
      mockServer.stop()
    }
  }

  @BeforeEach
  fun resetStubs() {
    mockServer.resetAll()

    val httpClient: HttpClient = object : HttpClient() {
      override fun newRequest(uri: URI?): Request? {
        val request: Request = super.newRequest(uri)
        return enhance(request)
      }
    }
    val webClient = WebClient.builder()
      .baseUrl("http://localhost:${mockServer.port()}")
      .clientConnector(JettyClientHttpConnector(httpClient))
      .build()

    prisonApiClient = PrisonApiClient(webClient)
  }

  private fun enhance(inboundRequest: Request): Request? {
    val log = StringBuilder()
    // Request Logging
    inboundRequest.onRequestBegin { request ->
      log.append("Request: \n")
        .append("URI: ")
        .append(request.getURI())
        .append("\n")
        .append("Method: ")
        .append(request.getMethod())
    }
    inboundRequest.onRequestHeaders { request ->
      log.append("\nHeaders:\n")
      for (header in request.getHeaders()) {
        log.append(
          """		${header.name} : ${header.value}
""",
        )
      }
    }
    inboundRequest.onRequestContent { request, content ->
      val bufferAsString: String = StandardCharsets.UTF_8.decode(content).toString()
      log.append("Request Body:\n$bufferAsString")
    }
    log.append("\n")

    // Response Logging
    inboundRequest.onResponseBegin { response ->
      log.append("Response:\n")
        .append("Status: ")
        .append(response.getStatus())
        .append("\n")
    }
    inboundRequest.onResponseHeaders { response ->
      log.append("Headers:\n")
      for (header in response.getHeaders()) {
        log.append(
          """		${header.name} : ${header.value}
""",
        )
      }
    }
    inboundRequest.onResponseContent { response, content ->
      val bufferAsString: String = StandardCharsets.UTF_8.decode(content).toString()
      log.append("Response Body:\n$bufferAsString")
    }

    // Add actual log invocation
    logger.info("HTTP ->\n")
    inboundRequest.onRequestSuccess { request -> logger.info(log.toString()) }
    inboundRequest.onResponseSuccess { response -> logger.info(log.toString()) }

    // Return original request
    return inboundRequest
  }

  @Test
  fun `move to cell swap successful submission`() {
    mockServer.stubMoveToCellSwapPositiveResponse(988507)

    val requestBody = MoveToCellSwapRequest(
      "ADM",
      LocalDateTime.of(2023, 8, 1, 10, 0, 0),
    )

    val cellMoveResponse = CellMoveResponse(
      bookingId = 988507,
      agencyId = "ACI",
      assignedLivingUnitId = 411283,
      assignedLivingUnitDesc = "ACI-CSWAP",
      bedAssignmentHistorySequence = null,
    )

    val response = prisonApiClient.moveToCellSwap(988507, requestBody)

    assertThat(response)
      .isEqualTo(cellMoveResponse)

    mockServer.verify(
      WireMock.putRequestedFor(WireMock.urlEqualTo("/api/bookings/988507/move-to-cell-swap")),
    )
  }

  @Test
  fun `move to cell swap request fails`() {
    mockServer.stubCellSwapFails(123456, 400)

    val requestBody = MoveToCellSwapRequest(
      "ADM",
      LocalDateTime.now(),
    )

    assertThatThrownBy {
      prisonApiClient.moveToCellSwap(123456, requestBody)
    }.isEqualTo(
      ClientException(
        response = ClientErrorResponse(
          status = 400,
          userMessage = "The date cannot be in the future",
          developerMessage = "The date cannot be in the future",
        ),
        message = "The date cannot be in the future",
      ),
    )
  }
}

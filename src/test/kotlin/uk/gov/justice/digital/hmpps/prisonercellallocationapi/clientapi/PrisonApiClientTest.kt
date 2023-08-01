package uk.gov.justice.digital.hmpps.prisonercellallocationapi.clientapi

import com.github.tomakehurst.wiremock.client.WireMock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.integration.PrisonApiMockServer
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMoveResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.MoveToCellSwapRequest
import java.time.LocalDateTime

class PrisonApiClientTest {
  private lateinit var prisonApiClient: PrisonApiClient

  companion object {
    @JvmField
    internal val mockServer = PrisonApiMockServer()

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
    val webClient = WebClient.create("http://localhost:${mockServer.port()}")
    prisonApiClient = PrisonApiClient(webClient)
  }

  @Test
  fun `move to cell swap successful submission`() {

    mockServer.stubMoveToCellSwapPositiveResponse(988507)

    val requestBody =  MoveToCellSwapRequest(
      "ADM",
      LocalDateTime.now(),
    )

    val cellMoveResponse = CellMoveResponse(
      bookingId = 988507,
      agencyId = "ACI",
      assignedLivingUnitId = 411283,
      assignedLivingUnitDesc = "ACI-CSWAP",
      bedAssignmentHistorySequence = null,
    )


    val response = prisonApiClient.moveToCellSwap(988507, requestBody )

    assertThat(response).isEqualTo(cellMoveResponse)

    mockServer.verify(
      WireMock.putRequestedFor(WireMock.urlEqualTo("/api/bookings/988507/move-to-cell-swap")),
    )
  }

  @Test
  fun `move to cell swap failed`() {

  }
}


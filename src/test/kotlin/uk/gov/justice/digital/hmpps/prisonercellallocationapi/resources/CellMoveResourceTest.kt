package uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources

import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.MoveToCellSwapRequest
import java.time.LocalDateTime

class CellMoveResourceTest : IntegrationTestBase() {

  @Test
  fun `The person moved to temporary cell`() {

    prisonApiMockServer.stubMoveToCellSwapPositiveResponse(988507)
    webTestClient
      .put()
      .uri("/api/bookings/988507/move-to-cell-swap")
      .headers(
        setAuthorisation(
          roles = listOf("ROLE_VIEW_ARRIVALS"),
          scopes = listOf("read", "write"),
        ),
      )
      .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
      .bodyValue(MoveToCellSwapRequest("ADM", LocalDateTime.of(2023, 8, 1, 10, 0, 0)))
      .exchange()
      .expectStatus().isOk
      .expectBody().json(
        """
        {
          "bookingId": 988507
        }
        """.trimIndent(),
      )
  }
}

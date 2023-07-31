package uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources

import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.MoveToCellSwapRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.integration.IntegrationTestBase
import java.time.LocalDateTime

class CellMoveResourceTest : IntegrationTestBase() {

  @Test
  fun `The person moved to temporary cell`() {
    webTestClient
      .put()
      .uri("/api/bookings/123456/move-to-cell-swap")
      .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
      .bodyValue(MoveToCellSwapRequest("ADM"))
      .exchange()
      .expectStatus().isOk
      .expectBody().json(
        """
        {
          "bookingId": 123456
        }
        """.trimIndent(),
      )
  }
}

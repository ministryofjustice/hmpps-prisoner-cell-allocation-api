package uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources

import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.MoveToCellSwapRequest
import java.time.LocalDateTime

class CellMovementResourceTest : IntegrationTestBase() {

  @Test
  fun `The person moved to temporary cell`() {
    prisonApiMockServer.stubMoveToCellSwapPositiveResponse(988507)
    webTestClient
      .put()
      .uri("/api/bookings/988507/move-to-cell-swap")
      .headers(
        setAuthorisationWithUser(
          roles = listOf("ROLE_MAINTAIN_CELL_MOVEMENTS"),
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
          "bookingId": 988507,
          "agencyId": "ACI",
          "assignedLivingUnitId": 411283,
          "assignedLivingUnitDesc": "ACI-CSWAP"
        }
        """.trimIndent(),
      )
  }

  @Test
  fun `Unauthorized response from prison api should be proxy to the client`() {
    prisonApiMockServer.stubCellSwapUnauthorizedResponse(988507)
    webTestClient
      .put()
      .uri("/api/bookings/988507/move-to-cell-swap")
      .headers(
        setAuthorisationWithUser(
          roles = listOf("ROLE_MAINTAIN_CELL_MOVEMENTS"),
          scopes = listOf("read", "write"),
        ),
      )
      .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
      .bodyValue(MoveToCellSwapRequest("ADM", LocalDateTime.of(2023, 8, 1, 10, 0, 0)))
      .exchange()
      .expectStatus().isUnauthorized
  }

  @Test
  fun `Unauthorized request due to lack of username `() {
    prisonApiMockServer.stubMoveToCellSwapPositiveResponse(988507)
    webTestClient
      .put()
      .uri("/api/bookings/988507/move-to-cell-swap")
      .headers(
        setAuthorisationWithoutUser(
          roles = listOf("ROLE_MAINTAIN_CELL_MOVEMENTS"),
          scopes = listOf("read", "write"),
        ),
      )
      .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
      .bodyValue(MoveToCellSwapRequest("ADM", LocalDateTime.of(2023, 8, 1, 10, 0, 0)))
      .exchange()
      .expectStatus().isUnauthorized
  }

  @Test
  fun `Forbidden request due to wrong role`() {
    prisonApiMockServer.stubMoveToCellSwapPositiveResponse(988507)
    webTestClient
      .put()
      .uri("/api/bookings/988507/move-to-cell-swap")
      .headers(
        setAuthorisationWithoutUser(
          roles = listOf("ROLE_MAINTAIN_CELL_MOVEMENTS_X"),
          scopes = listOf("read", "write"),
        ),
      )
      .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
      .bodyValue(MoveToCellSwapRequest("ADM", LocalDateTime.of(2023, 8, 1, 10, 0, 0)))
      .exchange()
      .expectStatus().isForbidden
  }
}

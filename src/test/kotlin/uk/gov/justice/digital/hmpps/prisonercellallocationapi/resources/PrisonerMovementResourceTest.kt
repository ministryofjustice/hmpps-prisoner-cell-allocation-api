package uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources

import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources.PrisonerMovementResourceTest.MoveDirection.IN
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources.PrisonerMovementResourceTest.MoveDirection.OUT

class PrisonerMovementResourceTest : IntegrationTestBase() {
  @Test
  fun `The person successfully moved into the cell`() {
    val prisonerId = "G7570GE"

    move(IN, factory.aMovementRequest(prisonerId))
      .expectStatus().isOk
      .expectBody()
      .jsonPath("id").exists()
  }

  @Test
  fun `The person unsuccessfully moved into cell due to mismatching prisonerId`() {
    val prisonerId = "G8580GE"

    move(IN, factory.aMovementRequest("Different PrisonerId"), prisonerId).expectStatus().isBadRequest
  }

  @Test
  fun `The person unsuccessfully moved into the cell due to wrong role`() {
    val prisonerId = "G9590GE"

    move(IN, factory.aMovementRequest(prisonerId), role = "ROLE_MAINTAIN_CELL_MOVEMENTS_X").expectStatus().isForbidden
  }

  @Test
  fun `The person successfully moved out from the cell`() {
    val prisonerId = "G2570GE"
    move(OUT, factory.aMovementRequest(prisonerId))
      .expectStatus().isOk
      .expectBody()
      .jsonPath("id").exists()
  }

  @Test
  fun `The person unsuccessfully moved out of cell due to mismatching prisonerId`() {
    val prisonerId = "G8970GE"
    move(OUT, factory.aMovementRequest("Different PrisonerId"), prisonerId).expectStatus().isBadRequest
  }

  @Test
  fun `The person unsuccessfully moved out from the cell due to wrong role`() {
    val prisonerId = "G1570GE"
    move(OUT, factory.aMovementRequest(prisonerId), role = "ROLE_MAINTAIN_CELL_MOVEMENTS_X").expectStatus().isForbidden
  }

  @Test
  fun `The person unsuccessfully moved out of cell due to conflicting occurred_at`() {
    val prisonerId = "J12390D"
    move(OUT, factory.aMovementRequest(prisonerId)).expectStatus().isOk

    move(OUT, factory.aMovementRequest(prisonerId)).expectStatus().is4xxClientError.expectBody().jsonPath("developerMessage").isEqualTo("Unable to save move - prisonerId already has movement recorded at this time")
  }

  private fun move(
    direction: MoveDirection,
    request: CellMovementRequest,
    prisonerId: String? = request.prisonerId,
    role: String = "ROLE_MAINTAIN_CELL_MOVEMENTS",
  ): ResponseSpec {
    return webTestClient
      .post()
      .uri("/api/prisoner/$prisonerId/${direction.uriString}")
      .headers(
        setAuthorisationWithUser(
          roles = listOf(role),
          scopes = listOf("read", "write"),
        ),
      )
      .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
      .bodyValue(request)
      .exchange()
  }
  enum class MoveDirection(val uriString: String) {
    IN("move-in"),
    OUT("move-out"),
  }
}

package uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources

import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.integration.IntegrationTestBase

class PrisonerMovementResourceTest : IntegrationTestBase() {
  @Test
  fun `The person successfully moved into the cell`() {
    val prisonerId = "G7570GE"

    webTestClient
      .post()
      .uri("/api/prisoner/$prisonerId/move-in")
      .headers(
        setAuthorisationWithUser(
          roles = listOf("ROLE_MAINTAIN_CELL_MOVEMENTS"),
          scopes = listOf("read", "write"),
        ),
      )
      .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
      .bodyValue(
        factory.aMovementRequest(prisonerId),
      )
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("id").exists()
  }

  @Test
  fun `The person unsuccessfully moved into cell due to mismatching prisonerId`() {
    val prisonerId = "G7570GE"

    webTestClient
      .post()
      .uri("/api/prisoner/$prisonerId/move-in")
      .headers(
        setAuthorisationWithUser(
          roles = listOf("ROLE_MAINTAIN_CELL_MOVEMENTS"),
          scopes = listOf("read", "write"),
        ),
      )
      .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
      .bodyValue(
        factory.aMovementRequest("Different PrisonerId"),
      )
      .exchange()
      .expectStatus().isBadRequest
  }

  @Test
  fun `The person unsuccessfully moved into the cell due to wrong role`() {
    val prisonerId = "G7570GE"

    webTestClient
      .post()
      .uri("/api/prisoner/$prisonerId/move-in")
      .headers(
        setAuthorisationWithoutUser(
          roles = listOf("ROLE_MAINTAIN_CELL_MOVEMENTS_X"),
          scopes = listOf("read", "write"),
        ),
      )
      .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
      .bodyValue(
        factory.aMovementRequest(prisonerId),
      ).exchange()
      .expectStatus().isForbidden
  }

  @Test
  fun `The person successfully moved out from the cell`() {
    val prisonerId = "G7570GE"
    val result = webTestClient
      .post()
      .uri("/api/prisoner/$prisonerId/move-out")
      .headers(
        setAuthorisationWithUser(
          roles = listOf("ROLE_MAINTAIN_CELL_MOVEMENTS"),
          scopes = listOf("read", "write"),
        ),
      )
      .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
      .bodyValue(factory.aMovementRequest(prisonerId))
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("id").exists()
  }

  @Test
  fun `The person unsuccessfully moved out of cell due to mismatching prisonerId`() {
    val prisonerId = "G7570GE"
    webTestClient
      .post()
      .uri("/api/prisoner/$prisonerId/move-out")
      .headers(
        setAuthorisationWithUser(
          roles = listOf("ROLE_MAINTAIN_CELL_MOVEMENTS"),
          scopes = listOf("read", "write"),
        ),
      )
      .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
      .bodyValue(
        factory.aMovementRequest("Different PrisonerId"),
      )
      .exchange()
      .expectStatus().isBadRequest
  }

  @Test
  fun `The person unsuccessfully moved out from the cell due to wrong role`() {
    val prisonerId = "G7570GE"

    webTestClient
      .post()
      .uri("/api/prisoner/$prisonerId/move-out")
      .headers(
        setAuthorisationWithoutUser(
          roles = listOf("ROLE_MAINTAIN_CELL_MOVEMENTS_X"),
          scopes = listOf("read", "write"),
        ),
      )
      .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
      .bodyValue(
        factory.aMovementRequest(prisonerId),
      ).exchange()
      .expectStatus().isForbidden
  }
}

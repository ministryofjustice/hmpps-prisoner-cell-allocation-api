package uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec
import org.springframework.web.util.DefaultUriBuilderFactory
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.config.ErrorResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources.PrisonerMovementResourceTest.MoveDirection.IN
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources.PrisonerMovementResourceTest.MoveDirection.OUT
import java.util.*

class PrisonerMovementResourceTest : IntegrationTestBase() {
  @Test
  fun `The person successfully moved into the cell`() {
    val prisonerId = "G7570GB"

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
  @Sql("classpath:repository/non-association-in-cell.sql")
  fun `The person unsuccessfully moved into cell due to non association in cell`() {
    val prisonerId = "NA321B"

    val errorResponse: ErrorResponse = getErrorResponse(
      move(IN, factory.aMovementRequest(prisonerId, "NA-CELL-A2"), prisonerId)
        .expectStatus()
        .isEqualTo(422),
    )
    assertThat(errorResponse.validationErrors?.size).isEqualTo(1)
  }

  @Test
  @Sql("classpath:repository/two-non-associations-in-cell.sql")
  fun `The person unsuccessfully moved into cell due to two non associations in cell`() {
    val prisonerId = "NA321B"

    val errorResponse: ErrorResponse = getErrorResponse(
      move(IN, factory.aMovementRequest(prisonerId, "NA-CELL-B3"), prisonerId)
        .expectStatus()
        .isEqualTo(422),
    )
    assertThat(errorResponse.validationErrors?.size).isEqualTo(2)
  }

  @Test
  fun `The person unsuccessfully moved into cell due to exceeding opcap`() {
    val prisonerId = "95371B"
    val errorResponse: ErrorResponse = getErrorResponse(
      move(IN, factory.aMovementRequest(prisonerId, "NA-CELL-0"), prisonerId)
        .expectStatus()
        .isEqualTo(422),
    )
    assertThat(errorResponse.validationErrors?.size).isEqualTo(1)
  }

  @Test
  fun `The person unsuccessfully moved into cell due to wrong category`() {
    val prisonerId = "94321X"
    val errorResponse: ErrorResponse = getErrorResponse(
      move(IN, factory.aMovementRequest(prisonerId), prisonerId)
        .expectStatus()
        .isEqualTo(422),
    )
    assertThat(errorResponse.validationErrors?.size).isEqualTo(1)
  }

  @Test
  fun `The person successfully moved into cell despite wrong category, suppress validation`() {
    val prisonerId = "94321X"
    move(IN, factory.aMovementRequest(prisonerId), prisonerId, force = true)
      .expectStatus()
      .isEqualTo(200)
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
    force: Boolean? = null,
  ): ResponseSpec {
    val uriBuilder = DefaultUriBuilderFactory().builder().path("/api/prisoner/$prisonerId/${direction.uriString}?")
      .queryParamIfPresent("force", Optional.ofNullable(force))

    val uri = uriBuilder.build()

    return webTestClient
      .post()
      .uri(uri.path + uri.query.orEmpty())
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

  private fun getErrorResponse(responseSpec: ResponseSpec): ErrorResponse {
    return responseSpec.expectBody(ErrorResponse::class.java).returnResult().responseBody!!
  }
  enum class MoveDirection(val uriString: String) {
    IN("move-in"),
    OUT("move-out"),
  }
}

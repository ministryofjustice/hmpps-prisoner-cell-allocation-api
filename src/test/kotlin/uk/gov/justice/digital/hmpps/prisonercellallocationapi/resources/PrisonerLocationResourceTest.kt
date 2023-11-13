package uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources

import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.PrisonerSearchRequest

class PrisonerLocationResourceTest : IntegrationTestBase() {

  @Test
  @Sql("classpath:repository/current-prisoner-first-arrival.sql")
  fun `Search for current prisoner with one movement record`() {
    webTestClient
      .post()
      .uri("/api/current-cell")
      .headers(
        setAuthorisationWithUser(
          roles = listOf("ROLE_VIEW_CELL_MOVEMENTS"),
          scopes = listOf("read"),
        ),
      )
      .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
      .bodyValue(
        PrisonerSearchRequest("CURR-1"),
      )
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("prisonerName").isEqualTo("Current Prisoner")
      .jsonPath("cellId").isEqualTo(1)
      .jsonPath("cellDescription").isEqualTo("LII-CELL-A")
  }

  @Test
  @Sql("classpath:repository/current-prisoner-changed-cell.sql")
  fun `Search for current prisoner with movement history`() {
    webTestClient
      .post()
      .uri("/api/current-cell")
      .headers(
        setAuthorisationWithUser(
          roles = listOf("ROLE_VIEW_CELL_MOVEMENTS"),
          scopes = listOf("read"),
        ),
      )
      .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
      .bodyValue(
        PrisonerSearchRequest("CURR-2"),
      )
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("prisonerName").isEqualTo("Current Prisoner Two")
      .jsonPath("cellId").isEqualTo(2)
      .jsonPath("cellDescription").isEqualTo("LII-CELL-B")
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-one-residence.sql")
  fun `Search for former prisoner with one residence`() {
    webTestClient
      .post()
      .uri("/api/current-cell")
      .headers(
        setAuthorisationWithUser(
          roles = listOf("ROLE_VIEW_CELL_MOVEMENTS"),
          scopes = listOf("read"),
        ),
      )
      .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
      .bodyValue(
        PrisonerSearchRequest("LEFT-1"),
      )
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-multiple-residences.sql")
  fun `Search for former prisoner with multiple residences`() {
    webTestClient
      .post()
      .uri("/api/current-cell")
      .headers(
        setAuthorisationWithUser(
          roles = listOf("ROLE_VIEW_CELL_MOVEMENTS"),
          scopes = listOf("read"),
        ),
      )
      .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
      .bodyValue(
        PrisonerSearchRequest("LEFT-2"),
      )
      .exchange()
      .expectStatus().isNotFound
  }
}

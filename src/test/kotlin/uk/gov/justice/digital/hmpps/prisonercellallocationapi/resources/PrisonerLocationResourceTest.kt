package uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources

import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.integration.IntegrationTestBase

class PrisonerLocationResourceTest : IntegrationTestBase() {

  @Test
  @Sql("classpath:repository/current-prisoner-first-arrival.sql")
  fun `Search for current prisoner with one movement record`() {
    webTestClient
      .get()
      .uri("/api/prisoner/CURR-1/current-cell")
      .headers(
        setAuthorisationWithUser(
          roles = listOf("ROLE_VIEW_CELL_MOVEMENTS"),
          scopes = listOf("read"),
        ),
      )
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("prisonerName").isEqualTo("Current Prisoner")
      .jsonPath("nomisCellId").isEqualTo("LII-CELL-A")
  }

  @Test
  @Sql("classpath:repository/current-prisoner-changed-cell.sql")
  fun `Search for current prisoner with movement history`() {
    webTestClient
      .get()
      .uri("/api/prisoner/CURR-2/current-cell")
      .headers(
        setAuthorisationWithUser(
          roles = listOf("ROLE_VIEW_CELL_MOVEMENTS"),
          scopes = listOf("read"),
        ),
      )
      .exchange()
      .expectStatus().isOk
      .expectBody()
      .jsonPath("prisonerName").isEqualTo("Current Prisoner Two")
      .jsonPath("nomisCellId").isEqualTo("LII-CELL-B")
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-one-residence.sql")
  fun `Search for former prisoner with one residence`() {
    webTestClient
      .get()
      .uri("/api/prisoner/LEFT-1/current-cell")
      .headers(
        setAuthorisationWithUser(
          roles = listOf("ROLE_VIEW_CELL_MOVEMENTS"),
          scopes = listOf("read"),
        ),
      )
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-multiple-residences.sql")
  fun `Search for former prisoner with multiple residences`() {
    webTestClient
      .get()
      .uri("/api/prisoner/LEFT-2/current-cell")
      .headers(
        setAuthorisationWithUser(
          roles = listOf("ROLE_VIEW_CELL_MOVEMENTS"),
          scopes = listOf("read"),
        ),
      )
      .exchange()
      .expectStatus().isNotFound
  }

  @Test
  fun `Forbidden request due to wrong role`() {
    webTestClient
      .get()
      .uri("/api/prisoner/LEFT-2/current-cell")
      .headers(
        setAuthorisationWithUser(
          roles = listOf("ROLE_VIEW_CELL_MOVEMENTS_X"),
          scopes = listOf("read"),
        ),
      )
      .exchange()
      .expectStatus().isForbidden
  }
}

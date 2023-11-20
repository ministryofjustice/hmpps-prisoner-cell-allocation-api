package uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources

import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.integration.IntegrationTestBase

class CellOccupancyResourceTest : IntegrationTestBase() {

  @Test
  @Sql("classpath:repository/current-prisoner-changed-cell.sql")
  fun `get list of people in the cell`() {
    webTestClient
      .get()
      .uri("/api/cell/2/occupancy")
      .headers(
        setAuthorisationWithUser(
          roles = listOf("ROLE_VIEW_CELL_MOVEMENTS"),
          scopes = listOf("read"),
        ),
      )
      .exchange()
      .expectStatus().isOk
      .expectBody().json(
        """
        [{"prisonerId":"CURR-2"}]
        """.trimIndent(),
      )
  }

  @Test
  fun `Forbidden request due to wrong role`() {
    webTestClient
      .get()
      .uri("/api/cell/1/occupancy")
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

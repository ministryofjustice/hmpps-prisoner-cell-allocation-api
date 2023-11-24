package uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.Direction
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.MovementHistoryResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources.MovementHistoryTestBase.historyType.CELL
import java.time.LocalDateTime
import java.time.Month

class CellOccupancyHistoryResourceTest : MovementHistoryTestBase() {

  @Test
  @Sql("classpath:repository/current-prisoner-first-arrival.sql")
  fun `Search for cell with one movement record`() {
    val testCellId = "LII-CELL-A"
    val response = getHistoryForCell(testCellId)
    assertThat(response.page).isEqualTo(0)
    assertThat(response.pageSize).isEqualTo(10)
    assertThat(response.movements.size).isEqualTo(1)

    val movement = response.movements[0]

    assertThat(movement.id).isEqualTo(1)
    assertThat(movement.agency).isEqualTo("LII")
    assertThat(movement.prisonerId).isEqualTo("CURR-1")
    assertThat(movement.prisonerName).isEqualTo("Current Prisoner")
    assertThat(movement.userId).isEqualTo("USER1")
    assertThat(movement.dateTime).isEqualTo(LocalDateTime.of(2020, Month.JANUARY, 1, 1, 1, 1))
    assertThat(movement.reason).isEqualTo("Test data")
    assertThat(movement.direction).isEqualTo(Direction.IN)
    assertThat(movement.nomisCellId).isEqualTo(testCellId)
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-one-residence.sql")
  fun `Search for former prisoner with one residence`() {
    val testCellId = "LII-CELL-A"
    val response = getHistoryForCell(testCellId)
    assertThat(response.page).isEqualTo(0)
    assertThat(response.pageSize).isEqualTo(10)
    assertThat(response.movements.size).isEqualTo(2)

    response.movements.forEach {
      assertThat(it.prisonerId == "LEFT-1")
      assertThat(it.prisonerName == "Former Prisoner")
      assertThat(it.nomisCellId == testCellId)
    }

    assertThat(response.movements[0].direction).isEqualTo(Direction.OUT)
    assertThat(response.movements[1].direction).isEqualTo(Direction.IN)
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-multiple-residences.sql")
  fun `Search for former prisoner with multiple residences paged response page size 1`() {
    val testCellId = "LII-CELL-B"
    val response = getHistoryForCell(testCellId, pageSize = 1)
    assertThat(response.page).isEqualTo(0)
    assertThat(response.pageSize).isEqualTo(1)
    assertThat(response.movements.size).isEqualTo(1)

    response.movements.forEach { assertThat(it.prisonerId == testCellId) }

    response.movements[0].let {
      assertThat(it.direction == Direction.OUT)
      assertThat(it.nomisCellId == "LII-CELL-B")
    }
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-multiple-residences.sql")
  fun `Search for former prisoner with multiple residences paged response page size 1 second page`() {
    val testCellId = "LII-CELL-A"
    val response = getHistoryForCell(testCellId, pageSize = 1, page = 1)
    assertThat(response.page).isEqualTo(1)
    assertThat(response.pageSize).isEqualTo(1)
    assertThat(response.movements.size).isEqualTo(1)

    response.movements[0].let {
      assertThat(it.direction == Direction.IN)
      assertThat(it.nomisCellId == "LII-CELL-A")
    }
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-multiple-residences.sql")
  fun `Search for former prisoner with multiple residences with date threshold`() {
    val testCellId = "LII-CELL-B"
    val response = getHistoryForCell(testCellId, dateFrom = "2021-01-01")
    assertThat(response.page).isEqualTo(0)
    assertThat(response.pageSize).isEqualTo(10)
    assertThat(response.movements.size).isEqualTo(1)

    response.movements[0].let {
      assertThat(it.prisonerId == testCellId)
      assertThat(it.direction == Direction.OUT)
      assertThat(it.nomisCellId == "LII-CELL-B")
    }
  }

  @Test
  fun `Forbidden request due to wrong role`() {
    webTestClient
      .get()
      .uri("/api/nomis-cell/LEFT-2/history")
      .headers(
        setAuthorisationWithUser(
          roles = listOf("ROLE_VIEW_CELL_MOVEMENTS_X"),
          scopes = listOf("read"),
        ),
      )
      .exchange()
      .expectStatus().isForbidden
  }
  private fun getHistoryForCell(
    testCellId: String,
    page: Int? = null,
    pageSize: Int? = null,
    dateFrom: String? = null,
  ): MovementHistoryResponse {
    return getMovementHistory(CELL, testCellId, page, pageSize, dateFrom)
  }
}

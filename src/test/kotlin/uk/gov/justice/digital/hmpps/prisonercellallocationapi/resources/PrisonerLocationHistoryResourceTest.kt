package uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.Direction
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.MovementHistoryResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources.MovementHistoryTestBase.historyType.PRISONER
import java.time.LocalDateTime
import java.time.Month

class PrisonerLocationHistoryResourceTest : MovementHistoryTestBase() {

  @Test
  @Sql("classpath:repository/current-prisoner-first-arrival.sql")
  fun `Search for current prisoner with one movement record`() {
    val testPrisonerId = "CURR-1"
    val response = getHistoryForPrisoner(testPrisonerId)
    assertThat(response.page).isEqualTo(0)
    assertThat(response.pageSize).isEqualTo(10)
    assertThat(response.movements.size).isEqualTo(1)

    val movement = response.movements[0]

    assertThat(movement.id).isEqualTo(1)
    assertThat(movement.agency).isEqualTo("LII")
    assertThat(movement.prisonerId).isEqualTo(testPrisonerId)
    assertThat(movement.prisonerName).isEqualTo("Current Prisoner")
    assertThat(movement.userId).isEqualTo("USER1")
    assertThat(movement.dateTime).isEqualTo(LocalDateTime.of(2020, Month.JANUARY, 1, 1, 1, 1))
    assertThat(movement.reason).isEqualTo("Test data")
    assertThat(movement.direction).isEqualTo(Direction.IN)
  }

  @Test
  @Sql("classpath:repository/current-prisoner-changed-cell.sql")
  fun `Search for current prisoner with movement history`() {
    val testPrisonerId = "CURR-2"
    val response = getHistoryForPrisoner(testPrisonerId)
    assertThat(response.page).isEqualTo(0)
    assertThat(response.pageSize).isEqualTo(10)
    assertThat(response.movements.size).isEqualTo(3)

    response.movements.forEach {
      assertThat(it.prisonerId == testPrisonerId)
      assertThat(it.prisonerName == "Current Prisoner 2")
    }

    assertThat(response.movements[0].direction).isEqualTo(Direction.IN)
    assertThat(response.movements[0].nomisCellId).isEqualTo("LII-CELL-B")
    assertThat(response.movements[1].direction).isEqualTo(Direction.OUT)
    assertThat(response.movements[1].nomisCellId).isEqualTo("LII-CELL-A")
    assertThat(response.movements[2].direction).isEqualTo(Direction.IN)
    assertThat(response.movements[2].nomisCellId).isEqualTo("LII-CELL-A")
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-one-residence.sql")
  fun `Search for former prisoner with one residence`() {
    val testPrisonerId = "LEFT-1"
    val response = getHistoryForPrisoner(testPrisonerId)
    assertThat(response.page).isEqualTo(0)
    assertThat(response.pageSize).isEqualTo(10)
    assertThat(response.movements.size).isEqualTo(2)

    response.movements.forEach {
      assertThat(it.prisonerId == testPrisonerId)
      assertThat(it.prisonerName == "Former Prisoner")
      assertThat(it.nomisCellId == "LII-CELL-A")
    }

    assertThat(response.movements[0].direction).isEqualTo(Direction.OUT)
    assertThat(response.movements[1].direction).isEqualTo(Direction.IN)
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-multiple-residences.sql")
  fun `Search for former prisoner with multiple residences`() {
    val testPrisonerId = "LEFT-2"
    val response = getHistoryForPrisoner(testPrisonerId)
    assertThat(response.page).isEqualTo(0)
    assertThat(response.pageSize).isEqualTo(10)
    assertThat(response.movements.size).isEqualTo(4)

    response.movements.forEach { assertThat(it.prisonerId == testPrisonerId) }

    response.movements[0].let {
      assertThat(it.direction == Direction.OUT)
      assertThat(it.nomisCellId == "LII-CELL-B")
    }
    response.movements[1].let {
      assertThat(it.direction == Direction.IN)
      assertThat(it.nomisCellId == "LII-CELL-B")
    }
    response.movements[2].let {
      assertThat(it.direction == Direction.OUT)
      assertThat(it.nomisCellId == "LII-CELL-A")
    }
    response.movements[3].let {
      assertThat(it.direction == Direction.IN)
      assertThat(it.nomisCellId == "LII-CELL-A")
    }
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-multiple-residences.sql")
  fun `Search for former prisoner with multiple residences paged response page size 2`() {
    val testPrisonerId = "LEFT-2"
    val response = getHistoryForPrisoner(testPrisonerId, pageSize = 2)
    assertThat(response.page).isEqualTo(0)
    assertThat(response.pageSize).isEqualTo(2)
    assertThat(response.movements.size).isEqualTo(2)

    response.movements.forEach { assertThat(it.prisonerId == testPrisonerId) }

    response.movements[0].let {
      assertThat(it.direction == Direction.OUT)
      assertThat(it.nomisCellId == "LII-CELL-B")
    }
    response.movements[1].let {
      assertThat(it.direction == Direction.IN)
      assertThat(it.nomisCellId == "LII-CELL-B")
    }
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-multiple-residences.sql")
  fun `Search for former prisoner with multiple residences paged response page size 2 second page`() {
    val testPrisonerId = "LEFT-2"
    val response = getHistoryForPrisoner(testPrisonerId, pageSize = 2, page = 1)
    assertThat(response.page).isEqualTo(1)
    assertThat(response.pageSize).isEqualTo(2)
    assertThat(response.movements.size).isEqualTo(2)

    response.movements.forEach { assertThat(it.prisonerId == testPrisonerId) }

    response.movements[0].let {
      assertThat(it.direction == Direction.OUT)
      assertThat(it.nomisCellId == "LII-CELL-A")
    }
    response.movements[1].let {
      assertThat(it.direction == Direction.IN)
      assertThat(it.nomisCellId == "LII-CELL-A")
    }
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-multiple-residences.sql")
  fun `Search for former prisoner with multiple residences with date threshold`() {
    val testPrisonerId = "LEFT-2"
    val response = getHistoryForPrisoner(testPrisonerId, dateFrom = "2021-01-01")
    assertThat(response.page).isEqualTo(0)
    assertThat(response.pageSize).isEqualTo(10)
    assertThat(response.movements.size).isEqualTo(1)

    response.movements[0].let {
      assertThat(it.prisonerId == testPrisonerId)
      assertThat(it.direction == Direction.OUT)
      assertThat(it.nomisCellId == "LII-CELL-B")
    }
  }

  @Test
  fun `Forbidden request due to wrong role`() {
    webTestClient
      .get()
      .uri("/api/prisoner/LEFT-2/history")
      .headers(
        setAuthorisationWithUser(
          roles = listOf("ROLE_VIEW_CELL_MOVEMENTS_X"),
          scopes = listOf("read"),
        ),
      )
      .exchange()
      .expectStatus().isForbidden
  }

  private fun getHistoryForPrisoner(
    testPrisonerId: String,
    page: Int? = null,
    pageSize: Int? = null,
    dateFrom: String? = null,
  ): MovementHistoryResponse {
    return getMovementHistory(PRISONER, testPrisonerId, page, pageSize, dateFrom)
  }
}

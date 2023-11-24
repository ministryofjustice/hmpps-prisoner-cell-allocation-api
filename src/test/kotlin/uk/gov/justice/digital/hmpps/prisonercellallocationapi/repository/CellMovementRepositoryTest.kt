package uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.transaction.TestTransaction
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.CellMovement
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.Direction
import java.time.LocalDateTime

class CellMovementRepositoryTest : RepositoryTest() {
  @Autowired
  lateinit var repository: CellMovementRepository
  val allResults: Pageable = Pageable.ofSize(Int.MAX_VALUE)

  @AfterEach
  fun afterEach() {
    repository.deleteAll()
  }

  @Test
  fun `move to cell`() {
    val cellMovementRecord = createCellMovementRecord(Direction.IN)

    val persistedCellMovementRecord = repository.save(cellMovementRecord)
    TestTransaction.flagForCommit()
    TestTransaction.end()

    assertThat(persistedCellMovementRecord.id).isNotNull
  }

  @Test
  fun `move out from cell`() {
    val cellMovementRecord = createCellMovementRecord(Direction.OUT)

    val persistedCellMovementRecord = repository.save(cellMovementRecord)
    TestTransaction.flagForCommit()
    TestTransaction.end()

    assertThat(persistedCellMovementRecord.id).isNotNull
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-multiple-residences.sql")
  fun `find first by prisoner id order by dateTime desc and id desc`() {
    val result = repository.findFirstByPrisonerIdOrderByDateTimeDescIdDesc("LEFT-2")
    assertThat(result).isPresent
    assertThat(result.get()).isEqualTo(
      CellMovement(
        410, "LII", "LII-CELL-B", "LEFT-2", "Former Prisoner", "USER1",
        LocalDateTime.of(2021, 1, 4, 1, 1, 1), "Test data - stay two out", Direction.OUT,
      ),
    )
  }

  @Test
  fun `result not present for prisoner id order by dateTime desc and id desc`() {
    val result = repository.findFirstByPrisonerIdOrderByDateTimeDescIdDesc("LEFT-2")
    assertThat(result).isNotPresent
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-one-residence.sql")
  fun `find cell occupancy with a vacated prisoner returns empty list`() {
    val result = repository.findAllByPrisonerWhoseLastMovementWasIntoThisCell("LII-CELL-A")
    assertThat(result).isEmpty()
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-one-residence.sql")
  fun `find movement history for empty cell two movements`() {
    val result = repository.findByNomisCellIdIgnoreCase("LII-CELL-A", allResults)
    assertThat(result).hasSize(2)
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-one-residence.sql")
  fun `find movement history for empty cell with max date between arrival and release returns one movements`() {
    val maxTime = LocalDateTime.of(2020, 1, 3, 12, 30)
    val result = repository.findByPrisonerIdIgnoreCaseAndDateTimeGreaterThanEqual("LII-CELL-A", maxTime, allResults)
    assertThat(result).hasSize(1)
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-one-residence.sql")
  fun `find movement history for vacated prisoner returns two movements`() {
    val result = repository.findByPrisonerIdIgnoreCase("LEFT-1", allResults)
    assertThat(result).hasSize(2)
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-one-residence.sql")
  fun `find movement history for vacated prisoner with max date between arrival and release returns one movements`() {
    val maxTime = LocalDateTime.of(2020, 1, 3, 12, 30)
    val result = repository.findByPrisonerIdIgnoreCaseAndDateTimeGreaterThanEqual("LEFT-1", maxTime, allResults)
    assertThat(result).hasSize(1)
  }

  private fun createCellMovementRecord(direction: Direction) = CellMovement(
    id = null,
    agency = "Agency",
    nomisCellId = "LII-CELL-A",
    prisonerId = " prisoner Id",
    prisonerName = "Adam Walker",
    userId = "user Id",
    dateTime = LocalDateTime.now(),
    reason = "Reason",
    direction = direction,
  )
}

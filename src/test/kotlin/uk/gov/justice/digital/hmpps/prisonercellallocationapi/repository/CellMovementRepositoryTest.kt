package uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.transaction.TestTransaction
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.CellMovement
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.Direction
import java.time.LocalDateTime

class CellMovementRepositoryTest : RepositoryTest() {
  @Autowired
  lateinit var repository: CellMovementRepository

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
        410, "LII", 1, "LII-CELL-A", "LEFT-2", "Former Prisoner", "USER1",
        LocalDateTime.of(2021, 1, 4, 1, 1, 1), "Test data", Direction.OUT,
      ),
    )
  }

  @Test
  fun `result not present for prisoner id order by dateTime desc and id desc`() {
    val result = repository.findFirstByPrisonerIdOrderByDateTimeDescIdDesc("LEFT-2")
    assertThat(result).isNotPresent
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-multiple-residences.sql")
  fun `find all by prisoner id and id greater order by dateTime desc and id desc`() {
    val result = repository.findAllByPrisonerIdAndIdGreaterThan("LEFT-2", 49)
    assertThat(result).isEqualTo(
      listOf(
        CellMovement(
          410, "LII", 1, "LII-CELL-A", "LEFT-2", "Former Prisoner", "USER1",
          LocalDateTime.of(2021, 1, 4, 1, 1, 1), "Test data", Direction.OUT,
        ),
      ),
    )
  }

  @Test
  @Sql("classpath:repository/vacated-prisoner-one-residence.sql")
  fun `find all by prisoner id order by dateTime desc and id desc`() {
    val result = repository.findAllByCellIdOrderByDateTimeDescIdDesc(1)
    assertThat(result).isEqualTo(
      listOf(
        CellMovement(
          36, "LII", 1, "LII-CELL-A", "LEFT-1", "Former Prisoner", "USER1",
          LocalDateTime.of(2020, 1, 4, 1, 1, 1), "Test data", Direction.OUT,
        ),
        CellMovement(
          35, "LII", 1, "LII-CELL-A", "LEFT-1", "Former Prisoner", "USER1",
          LocalDateTime.of(2020, 1, 3, 1, 1, 1), "Test data", Direction.IN,
        ),
      ),
    )
  }

  @Test
  fun `find all by prisoner id order by dateTime desc and id desc returns empty list`() {
    val result = repository.findAllByCellIdOrderByDateTimeDescIdDesc(1)
    assertThat(result).isEqualTo(listOf<CellMovement>())
  }

  fun createCellMovementRecord(direction: Direction) = CellMovement(
    id = null,
    agency = "Agency",
    cellId = 1234,
    cellDescription = "Cell Desc",
    prisonerId = " prisoner Id",
    prisonerName = "Adam Walker",
    userId = "user Id",
    dateTime = LocalDateTime.now(),
    reason = "Reason",
    direction = direction,
  )
}

package uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.transaction.TestTransaction
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.CellMovement
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.Direction
import java.time.LocalDateTime

class MoveToCellRepositoryTest : RepositoryTest() {
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

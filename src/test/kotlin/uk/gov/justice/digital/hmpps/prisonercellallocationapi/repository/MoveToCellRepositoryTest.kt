package uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.transaction.TestTransaction
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.CellMovement
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
    val cellMovementRecord = createCellMovementRecord(cellIncluded = true)

    val persistedCellMovementRecord = repository.save(cellMovementRecord)
    TestTransaction.flagForCommit()
    TestTransaction.end()

    assertThat(persistedCellMovementRecord.id).isNotNull
  }

  @Test
  fun `move out from cell`() {
    val cellMovementRecord = createCellMovementRecord(cellIncluded = false)

    val persistedCellMovementRecord = repository.save(cellMovementRecord)
    TestTransaction.flagForCommit()
    TestTransaction.end()

    assertThat(persistedCellMovementRecord.id).isNotNull
  }

  fun createCellMovementRecord(cellIncluded: Boolean) = CellMovement(
    id = null,
    agency = "Agency",
    cellId = if (cellIncluded) 1234 else null,
    cellDescription = if (cellIncluded) "Cell Desc" else null,
    prisonerId = " prisoner Id",
    prisonerName = "Adam Walker",
    userId = "user Id",
    dateTime = LocalDateTime.now(),
    reason = "Reason",
  )
}

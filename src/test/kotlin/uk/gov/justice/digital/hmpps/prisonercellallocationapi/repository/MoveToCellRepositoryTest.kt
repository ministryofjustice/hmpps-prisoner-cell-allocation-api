package uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.transaction.TestTransaction
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.MoveToCell
import java.time.LocalDateTime

class MoveToCellRepositoryTest : RepositoryTest() {
  @Autowired
  lateinit var repository: MoveToCellRepository

  @AfterEach
  fun afterEach() {
    repository.deleteAll()
  }

  @Test
  fun `can insert move to cell record`() {
    val moveToCellRecord = moveToCellRecord()

    val persistedMoveToCellRecord = repository.save(moveToCellRecord)
    TestTransaction.flagForCommit()
    TestTransaction.end()

    assertThat(persistedMoveToCellRecord.id).isNotNull
  }

  fun moveToCellRecord() = MoveToCell(
    id = null,
    agency = "Agency",
    cellId = 1234,
    cellDescription = "Cell Desc",
    prisonerId = " prisoner Id",
    prisonerName = "Adam Walker",
    userId = "user Id",
    dateTime = LocalDateTime.now(),
    reason = "Reason",
  )
}

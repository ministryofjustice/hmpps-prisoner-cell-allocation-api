package uk.gov.justice.digital.hmpps.prisonercellallocationapi.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.MoveToCell
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.MoveToCellRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository.MoveToCellRepository
import java.time.LocalDateTime

class MoveToCellServiceTest {
  private val moveToCellRepository: MoveToCellRepository = mock()
  private val moveToCellService = MoveToCellService(moveToCellRepository)

  @Test
  fun `Create move to cell record`() {
    val moveToCellRequest = MoveToCellRequest(
      agency = "Agency",
      cellId = 1,
      cellDescription = "Cell Description",
      prisonerId = "123",
      prisonerName = "Prisoner Name",
      userId = "user Id",
      dateTime = LocalDateTime.of(2023, 11, 7, 12, 0),
      reason = "reason",
    )
    whenever(moveToCellRepository.save(any())).thenReturn(
      MoveToCell(
        id = 1,
        agency = moveToCellRequest.agency,
        cellId = moveToCellRequest.cellId,
        cellDescription = moveToCellRequest.cellDescription,
        prisonerId = moveToCellRequest.prisonerId,
        prisonerName = moveToCellRequest.prisonerName,
        userId = moveToCellRequest.userId,
        dateTime = moveToCellRequest.dateTime,
        reason = moveToCellRequest.reason,

      ),
    )
    val result = moveToCellService.save(moveToCellRequest)

    verify(moveToCellRepository).save(
      MoveToCell(
        id = null,
        agency = moveToCellRequest.agency,
        cellId = moveToCellRequest.cellId,
        cellDescription = moveToCellRequest.cellDescription,
        prisonerId = moveToCellRequest.prisonerId,
        prisonerName = moveToCellRequest.prisonerName,
        userId = moveToCellRequest.userId,
        dateTime = moveToCellRequest.dateTime,
        reason = moveToCellRequest.reason,

      ),
    )
    assertThat(result.id).isEqualTo(1)
  }
}

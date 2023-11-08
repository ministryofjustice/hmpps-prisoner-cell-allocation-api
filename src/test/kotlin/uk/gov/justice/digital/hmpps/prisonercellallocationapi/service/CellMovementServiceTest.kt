package uk.gov.justice.digital.hmpps.prisonercellallocationapi.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.CellMovement
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository.CellMovementRepository
import java.time.LocalDateTime

class CellMovementServiceTest {
  private val cellMovementRepository: CellMovementRepository = mock()
  private val cellMovementService = CellMovementService(cellMovementRepository)

  @Test
  fun `Create cell movement record`() {
    val request = CellMovementRequest(
      agency = "Agency",
      cellId = 1,
      cellDescription = "Cell Description",
      prisonerId = "123",
      prisonerName = "Prisoner Name",
      userId = "user Id",
      dateTime = LocalDateTime.of(2023, 11, 7, 12, 0),
      reason = "Reason",
    )
    whenever(cellMovementRepository.save(any())).thenReturn(
      CellMovement(
        id = 1,
        agency = request.agency,
        cellId = request.cellId,
        cellDescription = request.cellDescription,
        prisonerId = request.prisonerId,
        prisonerName = request.prisonerName,
        userId = request.userId,
        dateTime = request.dateTime,
        reason = request.reason,

      ),
    )
    val result = cellMovementService.save(request)

    verify(cellMovementRepository).save(
      CellMovement(
        id = null,
        agency = request.agency,
        cellId = request.cellId,
        cellDescription = request.cellDescription,
        prisonerId = request.prisonerId,
        prisonerName = request.prisonerName,
        userId = request.userId,
        dateTime = request.dateTime,
        reason = request.reason,

      ),
    )
    assertThat(result.id).isEqualTo(1)
  }
}

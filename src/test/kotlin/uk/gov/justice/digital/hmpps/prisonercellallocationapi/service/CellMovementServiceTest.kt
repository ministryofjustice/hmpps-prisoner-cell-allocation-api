package uk.gov.justice.digital.hmpps.prisonercellallocationapi.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.CellMovement
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.Direction
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.PrisonerSearchRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.PrisonerSearchResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository.CellMovementRepository
import java.time.LocalDateTime
import java.util.Optional

class CellMovementServiceTest {
  private val cellMovementRepository: CellMovementRepository = mock()
  private val cellMovementService = CellMovementService(cellMovementRepository)

  @Test
  fun `Successful move in movement`() {
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
        direction = Direction.IN,
      ),
    )
    val result = cellMovementService.moveIn(request)

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
        direction = Direction.IN,
      ),
    )
    assertThat(result.id).isEqualTo(1)
  }

  @Test
  fun `Successful move out movement`() {
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
        direction = Direction.OUT,
      ),
    )
    val result = cellMovementService.moveOut(request)

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
        direction = Direction.OUT,
      ),
    )
    assertThat(result.id).isEqualTo(1)
  }

  @Test
  fun `Find currently housed prisoner`() {
    val prisonerId = "12345"
    val request = PrisonerSearchRequest(prisonerId)

    val lastMovement = CellMovement(
      id = 1,
      agency = "Agency",
      cellId = 1,
      cellDescription = "Current Cell",
      prisonerId = prisonerId,
      prisonerName = "Prisoner Name",
      userId = "user12",
      dateTime = LocalDateTime.of(2023, 11, 7, 12, 0),
      reason = "Reason",
      direction = Direction.IN,
    )
    whenever(cellMovementRepository.findFirstByPrisonerIdOrderByDateTimeDesc(any())).thenReturn(
      Optional.of(lastMovement),
    )

    val result = cellMovementService.findByPrisonerId(request)

    verify(cellMovementRepository).findFirstByPrisonerIdOrderByDateTimeDesc(prisonerId)

    assertPrisonerSearchResponse(
      PrisonerSearchResponse(
        lastMovement.id!!,
        lastMovement.prisonerId!!,
        lastMovement.prisonerName!!,
        lastMovement.cellId!!,
        lastMovement.cellDescription!!,
      ),
      result,
    )
  }

  @Test
  fun `Find released prisoner`() {
    val prisonerId = "12345"
    val request = PrisonerSearchRequest(prisonerId)

    val lastMovement = CellMovement(
      id = 1,
      agency = "Agency",
      cellId = 1,
      cellDescription = "Current Cell",
      prisonerId = prisonerId,
      prisonerName = "Prisoner Name",
      userId = "user12",
      dateTime = LocalDateTime.of(2023, 11, 7, 12, 0),
      reason = "Reason",
      direction = Direction.OUT,
    )
    whenever(cellMovementRepository.findFirstByPrisonerIdOrderByDateTimeDesc(any())).thenReturn(
      Optional.of(lastMovement),
    )

    assertThrows(RuntimeException::class.java) {
      cellMovementService.findByPrisonerId(request)
    }

    verify(cellMovementRepository).findFirstByPrisonerIdOrderByDateTimeDesc(prisonerId)
  }

  private fun assertPrisonerSearchResponse(expected: PrisonerSearchResponse, actual: PrisonerSearchResponse) {
    assertThat(actual.id).isEqualTo(expected.id)
    assertThat(actual.prisonerId).isEqualTo(expected.prisonerId)
    assertThat(actual.prisonerName).isEqualTo(expected.prisonerName)
    assertThat(actual.cellId).isEqualTo(expected.cellId)
    assertThat(actual.cellDescription).isEqualTo(expected.cellDescription)
  }
}

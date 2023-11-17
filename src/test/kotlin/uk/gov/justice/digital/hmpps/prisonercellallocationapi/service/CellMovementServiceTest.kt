package uk.gov.justice.digital.hmpps.prisonercellallocationapi.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.CellMovement
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.Direction
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository.CellMovementRepository
import java.time.LocalDateTime

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
<<<<<<< Updated upstream
=======

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
    whenever(cellMovementRepository.findFirstByPrisonerIdOrderByDateTimeDescIdDesc(any())).thenReturn(
      Optional.of(lastMovement),
    )

    val result = cellMovementService.findByPrisonerId(request)

    verify(cellMovementRepository).findFirstByPrisonerIdOrderByDateTimeDescIdDesc(prisonerId)

    assertPrisonerSearchResponse(
      PrisonerSearchResponse(
        lastMovement.id!!,
        lastMovement.prisonerId,
        lastMovement.prisonerName,
        lastMovement.cellId,
        lastMovement.cellDescription,
      ),
      result,
    )
  }

  @Test
  fun `Get cell occupancy`() {
    val repoResults = listOf(
      CellMovement(
        4,
        "LLI",
        1,
        "CELL-1-1",
        "D1234",
        "John Smith",
        "USER1",
        LocalDateTime.of(2023, 11, 16, 12, 0),
        "Release",
        Direction.OUT,
      ),
      CellMovement(
        3,
        "LLI",
        1,
        "CELL-1-1",
        "D1234",
        "John Smith",
        "USER1",
        LocalDateTime.of(2021, 11, 16, 12, 0),
        "In",
        Direction.IN,
      ),

      CellMovement(
        2,
        "LLI",
        1,
        "CELL-1-1",
        "D2234",
        "John Smith II",
        "USER1",
        LocalDateTime.of(2023, 11, 16, 12, 0),
        "In",
        Direction.IN,
      ),
    )
    whenever(cellMovementRepository.findAllByCellIdOrderByDateTimeDescIdDesc(any())).thenReturn(repoResults)
    whenever(cellMovementRepository.findAllByPrisonerIdAndIdGreaterThan(any(), any())).thenReturn(listOf())

    val result = cellMovementService.getOccupancy(1)

    assertThat(result.size).isEqualTo(1)
  }

  @Test
  fun `Get cell occupancy when move out data are missed`() {
    val cellRepoResults = listOf(
      CellMovement(
        4,
        "LLI",
        1,
        "CELL-1-1",
        "D1234",
        "John Smith",
        "USER1",
        LocalDateTime.of(2023, 11, 16, 12, 0),
        "In",
        Direction.IN,
      ),
    )
    val prisonerRepoResults = listOf(
      CellMovement(
        4,
        "LLI",
        2,
        "CELL-1-2",
        "D1234",
        "John Smith",
        "USER1",
        LocalDateTime.of(2023, 11, 17, 12, 0),
        "In",
        Direction.IN,
      ),
    )
    whenever(cellMovementRepository.findAllByCellIdOrderByDateTimeDescIdDesc(any())).thenReturn(cellRepoResults)
    whenever(cellMovementRepository.findAllByPrisonerIdAndIdGreaterThan(any(), any())).thenReturn(prisonerRepoResults)

    val result = cellMovementService.getOccupancy(1)

    assertThat(result.size).isEqualTo(0)
  }

  @Test
  fun `Get cell occupancy when no data`() {
    whenever(cellMovementRepository.findAllByCellIdOrderByDateTimeDescIdDesc(any())).thenReturn(listOf())
    whenever(cellMovementRepository.findAllByPrisonerIdAndIdGreaterThan(any(), any())).thenReturn(listOf())

    val result = cellMovementService.getOccupancy(1)

    assertThat(result.size).isEqualTo(0)
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
    whenever(cellMovementRepository.findFirstByPrisonerIdOrderByDateTimeDescIdDesc(any())).thenReturn(
      Optional.of(lastMovement),
    )

    assertThrows(RuntimeException::class.java) {
      cellMovementService.findByPrisonerId(request)
    }

    verify(cellMovementRepository).findFirstByPrisonerIdOrderByDateTimeDescIdDesc(prisonerId)
  }

  private fun assertPrisonerSearchResponse(expected: PrisonerSearchResponse, actual: PrisonerSearchResponse) {
    assertThat(actual.id).isEqualTo(expected.id)
    assertThat(actual.prisonerId).isEqualTo(expected.prisonerId)
    assertThat(actual.prisonerName).isEqualTo(expected.prisonerName)
    assertThat(actual.cellId).isEqualTo(expected.cellId)
    assertThat(actual.cellDescription).isEqualTo(expected.cellDescription)
  }
>>>>>>> Stashed changes
}

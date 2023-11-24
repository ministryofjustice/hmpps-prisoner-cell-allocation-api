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
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.MovementHistoryRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.PrisonerSearchResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository.CellMovementRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Optional

class CellMovementServiceTest {
  private val cellMovementRepository: CellMovementRepository = mock()
  private val cellMovementService = CellMovementService(cellMovementRepository)

  @Test
  fun `Successful move in movement`() {
    val request = CellMovementRequest(
      agency = "Agency",
      nomisCellId = "LII-CELL-A",
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
        nomisCellId = "LII-CELL-A",
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
        nomisCellId = request.nomisCellId,
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
      nomisCellId = "LII-CELL-A",
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
        nomisCellId = request.nomisCellId,
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
        nomisCellId = request.nomisCellId,
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

    val lastMovement = CellMovement(
      id = 1,
      agency = "Agency",
      nomisCellId = "LII-CELL-A",
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

    val result = cellMovementService.findByPrisonerId(prisonerId)

    verify(cellMovementRepository).findFirstByPrisonerIdOrderByDateTimeDescIdDesc(prisonerId)

    assertPrisonerSearchResponse(
      PrisonerSearchResponse(
        lastMovement.id!!,
        lastMovement.prisonerId,
        lastMovement.prisonerName,
        lastMovement.nomisCellId,
      ),
      result,
    )
  }

  @Test
  fun `Get cell occupancy`() {
    val repoResults = listOf(
      CellMovement(
        3,
        "LLI",
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
        "CELL-1-1",
        "D2234",
        "John Smith II",
        "USER1",
        LocalDateTime.of(2023, 11, 16, 12, 0),
        "In",
        Direction.IN,
      ),
    )
    whenever(cellMovementRepository.findAllByPrisonerWhoseLastMovementWasIntoThisCell(any())).thenReturn(repoResults)
    val result = cellMovementService.getOccupancy("CELL-1-1")

    assertThat(result.size).isEqualTo(2)
  }

  @Test
  fun `Get cell history for prisoner`() {
    val repoResults = listOf(
      CellMovement(
        3,
        "LLI",
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
        "CELL-1-1",
        "D1234",
        "John Smith II",
        "USER1",
        LocalDateTime.of(2023, 11, 16, 12, 0),
        "Release",
        Direction.OUT,
      ),
    )
    whenever(cellMovementRepository.findByPrisonerIdIgnoreCase(any(), any())).thenReturn(repoResults)
    val result = cellMovementService.findHistoryByPrisonerId(MovementHistoryRequest("D1234", 0, 1))

    assertThat(result.movements.size).isEqualTo(2)
  }

  @Test
  fun `Get cell history for prisoner with date threshold`() {
    val repoResults = listOf(
      CellMovement(
        3,
        "LLI",
        "CELL-1-1",
        "D1234",
        "John Smith",
        "USER1",
        LocalDateTime.of(2021, 11, 16, 12, 0),
        "In",
        Direction.IN,
      ),
    )
    whenever(cellMovementRepository.findByPrisonerIdIgnoreCaseAndDateTimeGreaterThanEqual(any(), any(), any())).thenReturn(repoResults)
    val result = cellMovementService.findHistoryByPrisonerId(MovementHistoryRequest("D1234", 0, 1, LocalDate.MIN))

    assertThat(result.movements.size).isEqualTo(1)
  }

  @Test
  fun `Get cell occupancy when result is empty`() {
    whenever(cellMovementRepository.findAllByPrisonerWhoseLastMovementWasIntoThisCell(any())).thenReturn(listOf())
    val result = cellMovementService.getOccupancy("CELL-1-1")
    assertThat(result.size).isEqualTo(0)
  }

  @Test
  fun `Find released prisoner`() {
    val prisonerId = "12345"

    val lastMovement = CellMovement(
      id = 1,
      agency = "Agency",
      nomisCellId = "CELL-1-1",
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
      cellMovementService.findByPrisonerId(prisonerId)
    }

    verify(cellMovementRepository).findFirstByPrisonerIdOrderByDateTimeDescIdDesc(prisonerId)
  }

  private fun assertPrisonerSearchResponse(expected: PrisonerSearchResponse, actual: PrisonerSearchResponse) {
    assertThat(actual.id).isEqualTo(expected.id)
    assertThat(actual.prisonerId).isEqualTo(expected.prisonerId)
    assertThat(actual.prisonerName).isEqualTo(expected.prisonerName)
    assertThat(actual.nomisCellId).isEqualTo(expected.nomisCellId)
  }
}

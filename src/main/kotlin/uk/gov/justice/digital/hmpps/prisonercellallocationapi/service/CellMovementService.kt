package uk.gov.justice.digital.hmpps.prisonercellallocationapi.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.CellMovement
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.Direction
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.PrisonerSearchRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.PrisonerSearchResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository.CellMovementRepository

@Service
@Transactional
class CellMovementService(
  private val cellMovementRepository: CellMovementRepository,
) {
  fun moveIn(request: CellMovementRequest): CellMovementResponse {
    val cellMovement = cellMovementRepository.save(
      createCellMovement(request, Direction.IN),
    )
    return CellMovementResponse(cellMovement.id!!)
  }

  fun moveOut(request: CellMovementRequest): CellMovementResponse {
    val cellMovement = cellMovementRepository.save(
      createCellMovement(request, Direction.OUT),
    )
    return CellMovementResponse(cellMovement.id!!)
  }

  private fun createCellMovement(request: CellMovementRequest, direction: Direction) = CellMovement(
    agency = request.agency,
    cellId = request.cellId,
    cellDescription = request.cellDescription,
    prisonerId = request.prisonerId,
    prisonerName = request.prisonerName,
    userId = request.userId,
    dateTime = request.dateTime,
    reason = request.reason,
    direction = direction,
  )

  fun findByPrisonerId(request: PrisonerSearchRequest): PrisonerSearchResponse {
    val lastMovement = cellMovementRepository.findFirstByPrisonerIdOrderByDateTimeDesc(request.prisonerId)

    return if (lastMovement.isEmpty || lastMovement.get().direction == Direction.OUT) {
      throw RuntimeException("No current cell allocation found for given prisoner id")
    } else {
      val lm = lastMovement.get()
      PrisonerSearchResponse(
        id = lm.id!!,
        cellDescription = lm.cellDescription!!,
        cellId = lm.cellId!!,
        prisonerId = lm.prisonerId!!,
        prisonerName = lm.prisonerName!!,
      )
    }
  }
}

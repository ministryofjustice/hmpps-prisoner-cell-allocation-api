package uk.gov.justice.digital.hmpps.prisonercellallocationapi.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.CellMovement
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.MoveType
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository.CellMovementRepository

@Service
@Transactional
class CellMovementService(
  private val cellMovementRepository: CellMovementRepository,
) {
  fun moveIn(request: CellMovementRequest): CellMovementResponse {
    val cellMovement = cellMovementRepository.save(
      createCellMovement(request, MoveType.IN),
    )
    return CellMovementResponse(cellMovement.id!!)
  }

  fun moveOut(request: CellMovementRequest): CellMovementResponse {
    val cellMovement = cellMovementRepository.save(
      createCellMovement(request, MoveType.OUT),
    )
    return CellMovementResponse(cellMovement.id!!)
  }

  private fun createCellMovement(request: CellMovementRequest, moveType: MoveType) = CellMovement(
    agency = request.agency,
    cellId = request.cellId,
    cellDescription = request.cellDescription,
    prisonerId = request.prisonerId,
    prisonerName = request.prisonerName,
    userId = request.userId,
    dateTime = request.dateTime,
    reason = request.reason,
    moveType = moveType,
  )
}

package uk.gov.justice.digital.hmpps.prisonercellallocationapi.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.CellMovement
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository.CellMovementRepository

@Service
@Transactional
class CellMovementService(
  private val cellMovementRepository: CellMovementRepository,
) {
  fun save(request: CellMovementRequest): CellMovementResponse {
    val cellMovement = cellMovementRepository.save(
      CellMovement(
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
    return CellMovementResponse(cellMovement.id!!)
  }
}

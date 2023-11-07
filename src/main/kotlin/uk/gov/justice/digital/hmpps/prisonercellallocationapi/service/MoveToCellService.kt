package uk.gov.justice.digital.hmpps.prisonercellallocationapi.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.MoveToCell
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.MoveToCellRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.MoveToCellResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository.MoveToCellRepository

@Service
@Transactional
class MoveToCellService(
  private val moveToCellRepository: MoveToCellRepository,
) {
  fun save(request: MoveToCellRequest): MoveToCellResponse {
    val moveToCell = moveToCellRepository.save(
      MoveToCell(
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
    return MoveToCellResponse(moveToCell.id!!)
  }
}

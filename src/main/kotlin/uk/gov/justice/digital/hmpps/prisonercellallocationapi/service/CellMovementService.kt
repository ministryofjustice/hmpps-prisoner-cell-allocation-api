package uk.gov.justice.digital.hmpps.prisonercellallocationapi.service

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Order
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.config.ClientException
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.CellMovement
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.Direction
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.MovementHistoryResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.PrisonerResponse
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

  fun findByPrisonerId(prisonerId: String): PrisonerSearchResponse {
    val lastMovement = cellMovementRepository.findFirstByPrisonerIdOrderByDateTimeDescIdDesc(prisonerId)

    return if (lastMovement.isEmpty || lastMovement.get().direction == Direction.OUT) {
      throw ClientException(
        404,
        "Prisoner has no cell allocation",
        "No current cell allocation found for given prisoner id",
      )
    } else {
      val lm = lastMovement.get()
      PrisonerSearchResponse(
        id = lm.id!!,
        cellDescription = lm.cellDescription,
        cellId = lm.cellId,
        prisonerId = lm.prisonerId,
        prisonerName = lm.prisonerName,
      )
    }
  }
  fun findHistoryByPrisonerId(request: PrisonerSearchRequest): MovementHistoryResponse {
    val pageRequest = PageRequest.of(request.page!!, request.pageSize!!, Sort.by(Order.desc("date_time")))

    val movements = if (request.dateFrom == null) {
      cellMovementRepository.findByPrisonerIdIgnoreCaseOrderByDateTimeDesc(request.prisonerId, pageRequest)
    } else {
      cellMovementRepository.findByPrisonerIdAndDateTimeGreaterThanEqualOrderByDateTimeDesc(
        request.prisonerId,
        request.dateFrom!!.atStartOfDay(),
        pageRequest,
      )
    }
    return MovementHistoryResponse(movements, pageRequest.pageNumber, pageRequest.pageSize)
  }

  fun getOccupancy(cellId: Long): List<PrisonerResponse> {
    val list = cellMovementRepository.findAllByPrisonerWhoseLastMovementWasIntoThisCell(cellId)
    return list.map { PrisonerResponse(it.prisonerId, it.prisonerName) }
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
}

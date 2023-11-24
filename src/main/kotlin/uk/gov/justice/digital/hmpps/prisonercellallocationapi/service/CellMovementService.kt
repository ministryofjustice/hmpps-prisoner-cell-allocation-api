package uk.gov.justice.digital.hmpps.prisonercellallocationapi.service

import org.slf4j.LoggerFactory
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
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.MovementHistoryRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.MovementHistoryResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.PrisonerResponse
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
        nomisCellId = lm.nomisCellId,
        prisonerId = lm.prisonerId,
        prisonerName = lm.prisonerName,
      )
    }
  }
  fun findHistoryByPrisonerId(request: MovementHistoryRequest): MovementHistoryResponse {
    val pageRequest = createPageRequest(request)

    val movements = if (request.dateFrom == null) {
      log.info("Finding history for prisonerId [{}], no time limit", request.objectId)
      cellMovementRepository.findByPrisonerIdIgnoreCase(request.objectId, pageRequest)
    } else {
      log.info("Finding history for prisonerId [{}] since [{}]", request.objectId, request.dateFrom!!.atStartOfDay())
      cellMovementRepository.findByPrisonerIdIgnoreCaseAndDateTimeGreaterThanEqual(
        request.objectId,
        request.dateFrom!!.atStartOfDay(),
        pageRequest,
      )
    }
    return MovementHistoryResponse(movements, pageRequest.pageNumber, pageRequest.pageSize)
  }

  fun findHistoryByNomisCellId(request: MovementHistoryRequest): MovementHistoryResponse {
    val pageRequest = createPageRequest(request)
    val movements = if (request.dateFrom == null) {
      log.info("Finding history for nomisCellId [{}], no time limit", request.objectId)
      cellMovementRepository.findByNomisCellIdIgnoreCase(request.objectId, pageRequest)
    } else {
      log.info("Finding history for nomisCellId [{}] since [{}]", request.objectId, request.dateFrom!!.atStartOfDay())
      cellMovementRepository.findByNomisCellIdIgnoreCaseAndDateTimeGreaterThanEqual(
        request.objectId,
        request.dateFrom!!.atStartOfDay(),
        pageRequest,
      )
    }
    return MovementHistoryResponse(movements, pageRequest.pageNumber, pageRequest.pageSize)
  }

  fun getOccupancy(nomisCellId: String): List<PrisonerResponse> {
    val list = cellMovementRepository.findAllByPrisonerWhoseLastMovementWasIntoThisCell(nomisCellId)
    // We should check the given prisoner still has this cell as their current cell
    return list.map { PrisonerResponse(it.prisonerId, it.prisonerName) }
  }

  private fun createCellMovement(request: CellMovementRequest, direction: Direction) = CellMovement(
    agency = request.agency,
    nomisCellId = request.nomisCellId,
    prisonerId = request.prisonerId,
    prisonerName = request.prisonerName,
    userId = request.userId,
    dateTime = request.dateTime,
    reason = request.reason,
    direction = direction,
  )

  private fun createPageRequest(request: MovementHistoryRequest) = PageRequest.of(
    request.page,
    request.pageSize,
    Sort.by(Order.desc("dateTime")),
  )

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}

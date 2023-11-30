package uk.gov.justice.digital.hmpps.prisonercellallocationapi.service

import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Sort.Order
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.config.ConflictingMovementException
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.config.NoCurrentAllocationException
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.CellMovement
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.Direction
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.MovementHistoryRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.MovementHistoryResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.PrisonerResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.PrisonerSearchResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository.CellMovementRepository
import java.time.Clock
import java.time.LocalDateTime

@Service
@Transactional
class CellMovementService(
  private val cellMovementRepository: CellMovementRepository,
  private val clock: Clock,
) {
  fun moveIn(request: CellMovementRequest): CellMovementResponse {
    val cellMovement = attemptMove(
      createCellMovement(request, Direction.IN),
    )
    return CellMovementResponse(cellMovement.id!!)
  }

  fun moveOut(request: CellMovementRequest): CellMovementResponse {
    val cellMovement = attemptMove(
      createCellMovement(request, Direction.OUT),
    )
    return CellMovementResponse(cellMovement.id!!)
  }

  private fun attemptMove(movement: CellMovement): CellMovement {
    try {
      return cellMovementRepository.save(movement)
    } catch (e: DataIntegrityViolationException) {
      throw ConflictingMovementException(message = "Unable to save move - prisonerId already has movement recorded at this time")
    }
  }

  fun findByPrisonerId(prisonerId: String): PrisonerSearchResponse {
    val lastMovement = cellMovementRepository.findFirstByPrisonerIdOrderByOccurredAtDesc(prisonerId)

    return if (lastMovement.isEmpty || lastMovement.get().direction == Direction.OUT) {
      throw NoCurrentAllocationException(
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
      cellMovementRepository.findByPrisonerIdIgnoreCaseAndOccurredAtGreaterThanEqual(
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
      cellMovementRepository.findByNomisCellIdIgnoreCaseAndOccurredAtGreaterThanEqual(
        request.objectId,
        request.dateFrom!!.atStartOfDay(),
        pageRequest,
      )
    }
    return MovementHistoryResponse(movements, pageRequest.pageNumber, pageRequest.pageSize)
  }

  fun getOccupancy(nomisCellId: String): List<PrisonerResponse> {
    val list = cellMovementRepository.findAllByPrisonerWhoseLastMovementWasIntoThisCell(nomisCellId)
    return list.map { PrisonerResponse(it.prisonerId, it.prisonerName) }
  }

  private fun createCellMovement(request: CellMovementRequest, direction: Direction) = CellMovement(
    agency = request.agency,
    nomisCellId = request.nomisCellId,
    prisonerId = request.prisonerId,
    prisonerName = request.prisonerName,
    userId = request.userId,
    occurredAt = request.occurredAt,
    reason = request.reason,
    direction = direction,
    recordedAt = LocalDateTime.now(clock),
  )

  private fun createPageRequest(request: MovementHistoryRequest) = PageRequest.of(
    request.page,
    request.pageSize,
    Sort.by(Order.desc("occurredAt")),
  )

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}

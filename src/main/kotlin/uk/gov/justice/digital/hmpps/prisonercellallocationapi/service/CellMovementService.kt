package uk.gov.justice.digital.hmpps.prisonercellallocationapi.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.CellMovement
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.Direction
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementResponse
<<<<<<< Updated upstream
=======
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.PrisonerResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.PrisonerSearchRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.PrisonerSearchResponse
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======

  fun findByPrisonerId(request: PrisonerSearchRequest): PrisonerSearchResponse {
    val lastMovement = cellMovementRepository.findFirstByPrisonerIdOrderByDateTimeDescIdDesc(request.prisonerId)

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
        cellDescription = lm.cellDescription!!,
        cellId = lm.cellId!!,
        prisonerId = lm.prisonerId!!,
        prisonerName = lm.prisonerName!!,
      )
    }
  }

  fun getOccupancy(cellId: Long): List<PrisonerResponse> {
    val list = cellMovementRepository.findAllByCellIdOrderByDateTimeDescIdDesc(cellId)

    val map = list.groupBy({ it.prisonerId }, { it })
      .filterValues { it.first().direction == Direction.IN }

    return map.filter {
      cellMovementRepository.findAllByPrisonerIdAndIdGreaterThan(it.key, it.value.first().id!!).isEmpty()
    }
      .map { PrisonerResponse(it.key) }
  }
>>>>>>> Stashed changes
}

package uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.CellMovement
<<<<<<< Updated upstream

@Repository
interface CellMovementRepository : JpaRepository<CellMovement, Long>
=======
import java.util.Optional

@Repository
interface CellMovementRepository : JpaRepository<CellMovement, Long> {

  fun findFirstByPrisonerIdOrderByDateTimeDescIdDesc(prisonerId: String): Optional<CellMovement>
  fun findAllByPrisonerIdAndIdGreaterThan(prisonerId: String, id: Long): List<CellMovement>
  fun findAllByCellIdOrderByDateTimeDescIdDesc(cellId: Long): List<CellMovement>
}
>>>>>>> Stashed changes

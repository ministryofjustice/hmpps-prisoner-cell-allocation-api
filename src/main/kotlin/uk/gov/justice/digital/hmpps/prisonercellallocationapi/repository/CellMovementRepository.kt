package uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.CellMovement
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.Direction
import java.util.Optional

@Repository
interface CellMovementRepository : JpaRepository<CellMovement, Long> {

  fun findFirstByPrisonerIdOrderByDateTimeDescIdDesc(prisonerId: String): Optional<CellMovement>
  fun findAllByPrisonerIdAndIdGreaterThan(prisonerId: String, id: Long): List<CellMovement>
  fun findAllByCellIdOrderByDateTimeDescIdDesc(cellId: Long): List<CellMovement>

  @Query(value = "SELECT * FROM (SELECT DISTINCT ON (prisoner_id) * from cell_movement order by prisoner_id, date_time DESC) as prisoner_last_movements WHERE direction = 'IN' and cell_id = ?1",
    nativeQuery = true
  )
  fun findAllByPrisonerWhoseLastMovementWasIntoThisCell(cellId: Long): List<CellMovement>


}

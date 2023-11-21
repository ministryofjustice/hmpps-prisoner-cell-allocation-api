package uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.CellMovement
import java.time.LocalDateTime
import java.util.Optional

@Repository
interface CellMovementRepository : JpaRepository<CellMovement, Long> {

  fun findFirstByPrisonerIdOrderByDateTimeDescIdDesc(prisonerId: String): Optional<CellMovement>

  @Query(
    value = "SELECT * FROM (SELECT DISTINCT ON (prisoner_id) * from cell_movement order by prisoner_id, date_time DESC) as prisoner_last_movements WHERE direction = 'IN' and nomis_cell_id = ?1",
    nativeQuery = true,
  )
  fun findAllByPrisonerWhoseLastMovementWasIntoThisCell(cellId: String): List<CellMovement>

  fun findByPrisonerIdIgnoreCaseOrderByDateTimeDesc(prisonerId: String, pageable: Pageable): List<CellMovement>

  fun findByPrisonerIdIgnoreCaseAndDateTimeGreaterThanEqualOrderByDateTimeDesc(prisonerId: String, dateTime: LocalDateTime, pageable: Pageable): List<CellMovement>
}

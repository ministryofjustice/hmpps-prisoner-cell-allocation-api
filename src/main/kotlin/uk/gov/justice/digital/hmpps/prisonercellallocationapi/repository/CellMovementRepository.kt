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

  fun findFirstByPrisonerIdOrderByOccurredAtDesc(prisonerId: String): Optional<CellMovement>

  @Query(
    value = "SELECT * FROM (SELECT DISTINCT ON (prisoner_id) * from cell_movement order by prisoner_id, occurred_at DESC) as prisoner_last_movements WHERE direction = 'IN' and nomis_cell_id = ?1",
    nativeQuery = true,
  )
  fun findAllByPrisonerWhoseLastMovementWasIntoThisCell(cellId: String): List<CellMovement>

  fun findByPrisonerIdIgnoreCase(prisonerId: String, pageable: Pageable): List<CellMovement>

  fun findByPrisonerIdIgnoreCaseAndOccurredAtGreaterThanEqual(prisonerId: String, occurredAt: LocalDateTime, pageable: Pageable): List<CellMovement>

  fun findByNomisCellIdIgnoreCase(nomisCellId: String, pageable: Pageable): List<CellMovement>

  fun findByNomisCellIdIgnoreCaseAndOccurredAtGreaterThanEqual(prisonerId: String, occurredAt: LocalDateTime, pageable: Pageable): List<CellMovement>
}

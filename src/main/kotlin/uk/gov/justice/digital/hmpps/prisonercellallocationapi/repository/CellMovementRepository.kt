package uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.CellMovement
import java.util.*

@Repository
interface CellMovementRepository : JpaRepository<CellMovement, Long> {

  fun findFirstByPrisonerIdOrderByDateTimeDescIdDesc(prisonerId: String): Optional<CellMovement>
}

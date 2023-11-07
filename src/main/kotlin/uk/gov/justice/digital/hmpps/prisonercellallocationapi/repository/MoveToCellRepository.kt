package uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.MoveToCell

@Repository
interface MoveToCellRepository : JpaRepository<MoveToCell, Long>

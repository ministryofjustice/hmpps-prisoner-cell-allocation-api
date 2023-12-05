package uk.gov.justice.digital.hmpps.prisonercellallocationapi.service

import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
abstract class PrisonerDetailsService {

  abstract fun getDetails(prisonerId: String): PrisonerDetails

  data class PrisonerDetails(
    var prisonerId: String,
    var firstName: String,
    var lastName: String,
    var category: String,
    var dateOfBirth: LocalDate? = null,
    var nonAssociations: List<String>? = emptyList(),
  )
}

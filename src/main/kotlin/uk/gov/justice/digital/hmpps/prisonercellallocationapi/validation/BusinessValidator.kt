package uk.gov.justice.digital.hmpps.prisonercellallocationapi.validation

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.config.BusinessValidationException

@Component
abstract class BusinessValidator {

  @Throws(BusinessValidationException::class)
  abstract fun checkMovePossible(prisonerId: String, locationNomisId: String, nothrow: Boolean?)

  enum class ValidationReason(val message: String) {
    NO_SPACE("No space in given cell"),
    NON_ASSOCIATION_IN_CELL("A Non-Association of the prisoner is resident in the cell"),
    NON_ASSOCIATION_OF_RESIDENT("The prisoner is a Non-Association of an existing resident of the cell"),
    EXCEEDS_CELL_CATEGORY("The prisoner's category exceeds the category of the cell"),
  }

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }
}

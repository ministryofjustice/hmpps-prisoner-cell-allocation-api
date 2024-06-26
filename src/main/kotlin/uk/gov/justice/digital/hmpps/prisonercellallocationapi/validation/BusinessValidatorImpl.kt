package uk.gov.justice.digital.hmpps.prisonercellallocationapi.validation

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.config.BusinessValidationException
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.service.LocationDetailsService
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.service.PrisonerDetailsService

@Component
@Profile("validate")
class BusinessValidatorImpl(
  private val locationDetailsService: LocationDetailsService,
  private val prisonerDetailsService: PrisonerDetailsService,
  private val cellValidator: CellValidator,
  private val nonAssociationsValidator: NonAssociationsValidator,
) : BusinessValidator() {

  @Throws(BusinessValidationException::class)
  override fun checkMovePossible(prisonerId: String, locationNomisId: String, nothrow: Boolean?) {
    val prisonerDetails = prisonerDetailsService.getDetails(prisonerId)
    val locationDetails = locationDetailsService.getDetails(locationNomisId)

    val validators: List<AbstractValidator> = listOf(cellValidator, nonAssociationsValidator)

    val validationFailures = validators.flatMap { it.validate(prisonerDetails, locationDetails) }

    if (validationFailures.isNotEmpty()) {
      if (nothrow == true) {
        log.warn("Suppressing BusinessValidationException")
      } else {
        throw BusinessValidationException(validationFailures = validationFailures)
      }
    }
  }
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

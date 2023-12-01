package uk.gov.justice.digital.hmpps.prisonercellallocationapi.validation

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.service.LocationDetailsService
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.service.PrisonerDetailsService

abstract class AbstractValidator {

  abstract fun validate(
    prisonerDetails: PrisonerDetailsService.PrisonerDetails,
    locationDetails: LocationDetailsService.LocationDetails,
  ): List<BusinessValidator.ValidationReason>

  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }
}

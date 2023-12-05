package uk.gov.justice.digital.hmpps.prisonercellallocationapi.validation

import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository.CellMovementRepository
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.service.LocationDetailsService
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.service.PrisonerDetailsService
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.service.PrisonerDetailsService.PrisonerDetails
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.validation.BusinessValidator.ValidationReason

@Component
class NonAssociationsValidator(
  private val prisonerDetailsService: PrisonerDetailsService,
  private val repository: CellMovementRepository,
) : AbstractValidator() {

  override fun validate(
    prisonerDetails: PrisonerDetails,
    locationDetails: LocationDetailsService.LocationDetails,
  ): List<ValidationReason> {
    log.info("Validating non associations")
    val currentOccupants = repository.findAllByPrisonerWhoseLastMovementWasIntoThisCell(locationDetails.nomisLocationId)
    val occupantDetails = currentOccupants.map { prisonerDetailsService.getDetails(it.prisonerId) }
    return ensureNoNonAssociationConflicts(prisonerDetails, occupantDetails)
  }

  private fun ensureNoNonAssociationConflicts(
    prisoner: PrisonerDetails,
    occupants: List<PrisonerDetails>,
  ): List<ValidationReason> {
    return ensurePrisonerIsNotInCurrentOccupantsNonAssociations(prisoner, occupants).plus(
      ensureCurrentOccupantsAreNotInPrisonersNonAssociations(prisoner, occupants),
    )
  }

  private fun ensurePrisonerIsNotInCurrentOccupantsNonAssociations(
    prisoner: PrisonerDetails,
    currentOccupants: List<PrisonerDetails>,
  ): List<ValidationReason> {
    return currentOccupants.filter { isNonAssociation(it, prisoner.prisonerId) }.map {
      log.warn("Occupant ${it.prisonerId} has ${prisoner.prisonerId} listed as a non-association")
      ValidationReason.NON_ASSOCIATION_OF_RESIDENT
    }
  }

  private fun ensureCurrentOccupantsAreNotInPrisonersNonAssociations(
    prisoner: PrisonerDetails,
    currentOccupants: List<PrisonerDetails>,
  ): List<ValidationReason> {
    return currentOccupants.filter { isNonAssociation(prisoner, it.prisonerId) }.map {
      log.warn("Occupant ${it.prisonerId} is listed as a non-association of ${prisoner.prisonerId}")
      ValidationReason.NON_ASSOCIATION_IN_CELL
    }
  }

  private fun isNonAssociation(prisoner: PrisonerDetails, prisonerId: String): Boolean {
    return (prisoner.nonAssociations?.contains(prisonerId) == true)
  }
}

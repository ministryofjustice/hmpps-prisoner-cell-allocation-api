package uk.gov.justice.digital.hmpps.prisonercellallocationapi.validation

import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository.CellMovementRepository
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.service.LocationDetailsService
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.service.PrisonerDetailsService

@Component
class NonAssociationsValidator(
  private val prisonerDetailsService: PrisonerDetailsService,
  private val repository: CellMovementRepository,
) : AbstractValidator() {

  override fun validate(
    prisonerDetails: PrisonerDetailsService.PrisonerDetails,
    locationDetails: LocationDetailsService.LocationDetails,
  ): List<BusinessValidator.ValidationReason> {
    val currentOccupants = repository.findAllByPrisonerWhoseLastMovementWasIntoThisCell(locationDetails.nomisLocationId)
    val occupantDetails = currentOccupants.map { o -> prisonerDetailsService.getDetails(o.prisonerId) }
    return ensureNoNonAssociationConflicts(prisonerDetails, occupantDetails)
  }

  private fun ensureNoNonAssociationConflicts(
    prisoner: PrisonerDetailsService.PrisonerDetails,
    occupants: List<PrisonerDetailsService.PrisonerDetails>,
  ): List<BusinessValidator.ValidationReason> {
    return ensurePrisonerIsNotInCurrentOccupantsNonAssociations(prisoner, occupants).plus(
      ensureCurrentOccupantsAreNotInPrisonersNonAssociations(prisoner, occupants),
    )
  }

  private fun ensurePrisonerIsNotInCurrentOccupantsNonAssociations(
    prisoner: PrisonerDetailsService.PrisonerDetails,
    currentOccupants: List<PrisonerDetailsService.PrisonerDetails>,
  ): List<BusinessValidator.ValidationReason> {
    return currentOccupants.map { occupant ->
      isNonAssociation(occupant, prisoner.prisonerId).let {
        log.warn("Occupant ${occupant.prisonerId} has ${prisoner.prisonerId} listed as a non-association")
        BusinessValidator.ValidationReason.NON_ASSOCIATION_OF_RESIDENT
      }
    }
  }

  private fun ensureCurrentOccupantsAreNotInPrisonersNonAssociations(
    prisoner: PrisonerDetailsService.PrisonerDetails,
    currentOccupants: List<PrisonerDetailsService.PrisonerDetails>,
  ): List<BusinessValidator.ValidationReason> {
    return currentOccupants.map { occupant ->
      isNonAssociation(prisoner, occupant.prisonerId).let {
        log.warn("Occupant ${occupant.prisonerId} is listed as a non-association of ${prisoner.prisonerId}")
        BusinessValidator.ValidationReason.NON_ASSOCIATION_IN_CELL
      }
    }
  }

  private fun isNonAssociation(prisoner: PrisonerDetailsService.PrisonerDetails, prisonerId: String): Boolean {
    return (prisoner.nonAssociations?.contains(prisonerId) == true)
  }
}

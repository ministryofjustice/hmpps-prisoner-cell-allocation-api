package uk.gov.justice.digital.hmpps.prisonercellallocationapi.validation

import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.repository.CellMovementRepository
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.service.LocationDetailsService
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.service.PrisonerDetailsService
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.validation.BusinessValidator.ValidationReason

@Component
class CellValidator(private val repository: CellMovementRepository) : AbstractValidator() {
  override fun validate(
    prisonerDetails: PrisonerDetailsService.PrisonerDetails,
    locationDetails: LocationDetailsService.LocationDetails,
  ): List<ValidationReason> {
    return listOfNotNull(
      ensureCellCategorySufficient(prisonerDetails, locationDetails),
      ensureCellHasCapacity(locationDetails),
    )
  }

  private fun ensureCellHasCapacity(location: LocationDetailsService.LocationDetails): ValidationReason? {
    val currentOccupancy = repository.findAllByPrisonerWhoseLastMovementWasIntoThisCell(location.nomisLocationId).size
    if (currentOccupancy >= location.operationalCapacity) {
      log.warn("Cell ${location.nomisLocationId} has capacity of ${location.operationalCapacity}. Current occupancy is $currentOccupancy")
      return ValidationReason.NO_SPACE
    }
    return null
  }

  private fun ensureCellCategorySufficient(
    prisoner: PrisonerDetailsService.PrisonerDetails,
    location: LocationDetailsService.LocationDetails,
  ): ValidationReason? {
    // This is just equals for now - needs to be improved
    if (location.category != prisoner.category) {
      log.warn("Cell Category ${location.category} does not match prisoner's category: ${prisoner.category}")
      return ValidationReason.EXCEEDS_CELL_CATEGORY
    }
    return null
  }
}

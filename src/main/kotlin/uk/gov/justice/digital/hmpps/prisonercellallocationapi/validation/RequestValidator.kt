package uk.gov.justice.digital.hmpps.prisonercellallocationapi.validation

import jakarta.validation.ValidationException
import org.springframework.stereotype.Component
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementRequest

@Component
class RequestValidator {

  @Throws(ValidationException::class)
  fun validateMatchingPrisonerIds(prisonerId: String, movementRequest: CellMovementRequest) {
    if (!prisonerId.equals(movementRequest.prisonerId, true)) {
      throw ValidationException("Provided prisonerIds do not match")
    }
  }
}

package uk.gov.justice.digital.hmpps.prisonercellallocationapi.validation

import jakarta.validation.ValidationException
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.utils.RequestFactory

class RequestValidatorTest {

  private val validator: RequestValidator = RequestValidator()
  private val requestFactory = RequestFactory()

  @Test
  fun `matching PrisonerIds accepted`() {
    val prisonerId = "A1234B"
    val request = requestFactory.aMovementRequest(prisonerId)

    assertDoesNotThrow("Exception thrown when validation is expected to pass") {
      validator.validateMatchingPrisonerIds(prisonerId, request)
    }
  }

  @Test
  fun `matching PrisonerIds accepted ignoring case`() {
    val prisonerId = "A1234C"
    val request = requestFactory.aMovementRequest(prisonerId)

    assertDoesNotThrow("Exception thrown when validation is expected to pass") {
      validator.validateMatchingPrisonerIds(prisonerId.lowercase(), request)
    }
  }

  @Test
  fun `different PrisonerIds throw ValidationException`() {
    val prisonerId = "A1234B"
    val request = requestFactory.aMovementRequest("C7890Z")

    assertThrows<ValidationException> ("Expected a ValidationException to be thrown") {
      validator.validateMatchingPrisonerIds(prisonerId.lowercase(), request)
    }
  }
}

package uk.gov.justice.digital.hmpps.prisonercellallocationapi.config

import uk.gov.justice.digital.hmpps.prisonercellallocationapi.validation.BusinessValidator.ValidationReason

abstract class ClientException(
  open val status: Int? = null,
  open val userMessage: String? = null,
  open val developerMessage: String? = null,
) : RuntimeException()

data class NoBodyClientException(
  val response: Int,
) : RuntimeException()

data class UnauthorizedException(
  override val message: String,
) : RuntimeException()

data class ConflictingMovementException(
  override val message: String,
) : ClientException(status = 400, developerMessage = message, userMessage = "Prisoner Movement already recorded at this time")

data class NoCurrentAllocationException(
  override val status: Int = 404,
  override val userMessage: String?,
  override val developerMessage: String?,
) : ClientException(status, userMessage, developerMessage)

data class BusinessValidationException(
  override val message: String = "One or more business validation rules were not met",
  val validationFailures: List<ValidationReason>,
) : Exception(message)

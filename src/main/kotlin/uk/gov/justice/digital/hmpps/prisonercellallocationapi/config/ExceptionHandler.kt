package uk.gov.justice.digital.hmpps.prisonercellallocationapi.config

import jakarta.validation.ValidationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.FORBIDDEN
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {
  @ExceptionHandler(ValidationException::class)
  fun handleValidationException(e: Exception): ResponseEntity<ErrorResponse> {
    log.info("Validation exception: {}", e.message)
    return ResponseEntity
      .status(BAD_REQUEST)
      .body(
        ErrorResponse(
          status = BAD_REQUEST,
          userMessage = "Validation failure: ${e.message}",
          developerMessage = e.message,
        ),
      )
  }

  @ExceptionHandler(BusinessValidationException::class)
  fun handleBusinessValidationException(e: BusinessValidationException): ResponseEntity<ErrorResponse> {
    log.info("Validation exception: {}", e.message)
    return ResponseEntity
      .status(UNPROCESSABLE_ENTITY)
      .body(
        ErrorResponse(
          status = UNPROCESSABLE_ENTITY,
          userMessage = "Validation failure: ${e.message}",
          developerMessage = e.message,
          validationErrors = e.validationFailures.map { it.message },
        ),
      )
  }

  @ExceptionHandler(ClientException::class, ConflictingMovementException::class)
  fun handleClientException(e: ClientException): ResponseEntity<ErrorResponse> {
    log.warn(
      "Client exception: status {} userMessage {} developerMessage {}",
      e.status,
      e.userMessage,
      e.developerMessage,
    )
    return ResponseEntity
      .status(e.status ?: INTERNAL_SERVER_ERROR.value())
      .body(
        ErrorResponse(
          status = e.status ?: INTERNAL_SERVER_ERROR.value(),
          userMessage = e.userMessage,
          developerMessage = e.developerMessage,
        ),
      )
  }

  @ExceptionHandler(NoBodyClientException::class)
  fun handleClientErrorException(e: NoBodyClientException): ResponseEntity<ErrorResponse?>? {
    log.warn(
      "Client exception: message {} ",
      e.message,
      e,
    )
    return ResponseEntity
      .status(e.response)
      .body(
        ErrorResponse(
          status = e.response,
          userMessage = "Unexpected error: ${e.message}",
          developerMessage = e.message,
        ),
      )
  }

  @ExceptionHandler(UnauthorizedException::class)
  fun handleClientErrorException(e: UnauthorizedException): ResponseEntity<ErrorResponse?>? {
    log.warn(
      "Unauthorized exception: message {} ",
      e.message,
      e,
    )
    return ResponseEntity.status(UNAUTHORIZED)
      .body(
        ErrorResponse(
          status = UNAUTHORIZED,
          userMessage = "Error: ${e.message}",
          developerMessage = e.message,
        ),
      )
  }

  @ExceptionHandler(AccessDeniedException::class)
  fun handleAccessDeniedException(e: AccessDeniedException): ResponseEntity<ErrorResponse?>? {
    log.warn(
      "Access Denied exception: message {} ",
      e.message,
      e,
    )
    return ResponseEntity.status(FORBIDDEN)
      .body(
        ErrorResponse(
          status = FORBIDDEN,
          userMessage = "Error: ${e.message}",
          developerMessage = e.message,
        ),
      )
  }

  @ExceptionHandler(java.lang.Exception::class)
  fun handleException(e: java.lang.Exception): ResponseEntity<ErrorResponse?>? {
    log.error("Unexpected exception", e)
    return ResponseEntity
      .status(INTERNAL_SERVER_ERROR)
      .body(
        ErrorResponse(
          status = INTERNAL_SERVER_ERROR,
          userMessage = "Unexpected error: ${e.message}",
          developerMessage = e.message,
        ),
      )
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}

data class ErrorResponse(
  val status: Int,
  val userMessage: String? = null,
  val developerMessage: String? = null,
  val validationErrors: List<String>? = null,

) {
  constructor(
    status: HttpStatus,
    userMessage: String? = null,
    developerMessage: String? = null,
    validationErrors: List<String>? = null,
  ) :
    this(status.value(), userMessage, developerMessage, validationErrors)
}

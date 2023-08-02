package uk.gov.justice.digital.hmpps.prisonercellallocationapi.config

data class ClientErrorResponse(
  val status: Int? = null,
  val userMessage: String? = null,
  val developerMessage: String? = null,
  val errorCode: Int? = null,
  val moreInfo: String? = null,
)

data class ClientException(
  val response: ClientErrorResponse,
  override val message: String,
) : RuntimeException(message)

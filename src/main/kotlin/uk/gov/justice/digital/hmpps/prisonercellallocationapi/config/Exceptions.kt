package uk.gov.justice.digital.hmpps.prisonercellallocationapi.config

data class ClientException(
  val status: Int? = null,
  val userMessage: String? = null,
  val developerMessage: String? = null,
) : RuntimeException()

data class NoBodyClientException(
  val response: Int,
) : RuntimeException()

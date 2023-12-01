package uk.gov.justice.digital.hmpps.prisonercellallocationapi.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("test", "stubbed")
class PrisonerDetailsServiceStub : PrisonerDetailsService() {

  override fun getDetails(prisonerId: String): PrisonerDetails {
    if (prisonerId.endsWith("9")) { throw RuntimeException("Prisoner Details not found") }
    return PrisonerDetails(prisonerId, "Stub", "Prisoner", prisonerId.last().toString().uppercase(), nonAssociations = nonassoc(prisonerId))
  }

  private fun nonassoc(prisonerId: String): List<String> {
    return if (prisonerId.startsWith("NA")) {
      log.info("Returning prisoner with non-associations")
      listOf("12345F", "12345J", "98765H")
    } else {
      emptyList()
    }
  }
  companion object {
    val log: Logger = LoggerFactory.getLogger(this::class.java)
  }
}

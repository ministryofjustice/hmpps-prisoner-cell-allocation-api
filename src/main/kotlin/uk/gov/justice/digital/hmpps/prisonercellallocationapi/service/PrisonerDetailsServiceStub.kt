package uk.gov.justice.digital.hmpps.prisonercellallocationapi.service

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("test", "stubbed")
class PrisonerDetailsServiceStub : PrisonerDetailsService() {

  override fun getDetails(prisonerId: String): PrisonerDetails {
    if (prisonerId.endsWith("9")) { throw RuntimeException("Prisoner Details not found") }
    return PrisonerDetails(prisonerId, "Stub", "Prisoner", prisonerId.last().toString().uppercase())
  }
}

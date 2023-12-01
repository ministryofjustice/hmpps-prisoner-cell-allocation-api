package uk.gov.justice.digital.hmpps.prisonercellallocationapi.service

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service

@Service
@Profile("test", "stubbed")
class LocationDetailsServiceStub : LocationDetailsService() {

  override fun getDetails(nomisLocationId: String): LocationDetails {
    if (nomisLocationId.endsWith("9")) { throw RuntimeException("Cell Details not found") }

    return LocationDetails(nomisLocationId, 2, true, "B", true)
  }
}

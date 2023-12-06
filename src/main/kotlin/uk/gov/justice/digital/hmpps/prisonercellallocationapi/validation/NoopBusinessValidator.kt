package uk.gov.justice.digital.hmpps.prisonercellallocationapi.validation

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("!validate")
class NoopBusinessValidator : BusinessValidator() {

  override fun checkMovePossible(prisonerId: String, locationNomisId: String, nothrow: Boolean?) {
    log.warn("Business Validation not enabled")
  }
}

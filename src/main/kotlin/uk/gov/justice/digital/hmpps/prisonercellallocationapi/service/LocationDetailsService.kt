package uk.gov.justice.digital.hmpps.prisonercellallocationapi.service

import org.springframework.stereotype.Service

@Service
abstract class LocationDetailsService {

  abstract fun getDetails(nomisLocationId: String): LocationDetails

  data class LocationDetails(
    var nomisLocationId: String,
    var operationalCapacity: Int,
    var active: Boolean,
    var category: String,
    var residential: Boolean,
  )
}

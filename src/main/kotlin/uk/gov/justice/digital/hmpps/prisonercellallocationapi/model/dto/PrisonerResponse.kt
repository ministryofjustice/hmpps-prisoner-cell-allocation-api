package uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto

import io.swagger.v3.oas.annotations.media.Schema

data class PrisonerResponse(

  @Schema(
    description = "The unique prison number for the person being searched for",
    example = "G7685GO",
  )
  var prisonerId: String,

  @Schema(
    description = "The name of the person as recorded for the given movement",
    example = "G7685GO",
  )
  var prisonerName: String,

)

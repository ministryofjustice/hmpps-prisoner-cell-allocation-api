package uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto

import io.swagger.v3.oas.annotations.media.Schema

data class PrisonerSearchResponse(

  @Schema(
    description = "Move to cell record id",
    example = "1234",
  )
  var id: Long,

  @Schema(
    description = "The unique prison number for the person being searched for",
    example = "G7685GO",
  )
  var prisonerId: String,

  @Schema(
    description = "The name of the prisoner as recorded at their most recent cell movement",
    example = "Jim Jones",
  )
  var prisonerName: String,

  @Schema(
    description = "The Nomis name for the cell the person is assigned to",
    example = "A-1-2-003",
  )
  var nomisCellId: String,
)

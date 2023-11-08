package uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto

import io.swagger.v3.oas.annotations.media.Schema

data class CellMovementResponse(
  @Schema(
    description = "Move to cell record id",
    example = "1234",
  )
  var id: Long,
)

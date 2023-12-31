package uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CellMovementRequest(

  @Schema(
    description = "The establishment",
    example = "MDI",
  )
  var agency: String,

  @Schema(
    description = "The cell name in Nomis the person is being assigned to e.g",
    example = "A-1-2-003",
  )
  var nomisCellId: String,

  @Schema(
    description = "The unique prison number for the person being assigned the cell",
    example = "G7685GO",
  )
  var prisonerId: String,

  @Schema(
    description = "The name of the person being assigned the cell",
    example = "Aishard Odnairab",
  )
  var prisonerName: String,

  @Schema(
    description = "The id of the person carrying out the move",
    example = "14567",
  )
  var userId: String,

  @Schema(
    description = "Date and time of the movement",
    example = "2022-01-18T08:00:00",
  )
  var occurredAt: LocalDateTime,

  @Schema(
    description = "The movement reason",
    example = "General moves",
  )
  var reason: String,

)

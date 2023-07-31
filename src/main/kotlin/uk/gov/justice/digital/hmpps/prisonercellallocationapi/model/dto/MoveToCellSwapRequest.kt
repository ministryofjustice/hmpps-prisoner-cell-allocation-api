package uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto

import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

data class MoveToCellSwapRequest(
  @Schema(
    description = "The reason code for the move (from reason code domain CHG_HOUS_RSN) (defaults to ADM)",
    example = "ADM",
  )
  var reasonCode: String? = null,

  @Schema(description = "The date / time of the move (defaults to current)", example = "2020-03-24T12:13:40")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  val dateTime: LocalDateTime? = null,
)

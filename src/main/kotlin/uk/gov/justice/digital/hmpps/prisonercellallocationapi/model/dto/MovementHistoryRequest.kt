package uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_NULL)
data class MovementHistoryRequest(

  @Schema(
    description = "The NOMIS identifier for the cell or person being searched for",
    example = "ABC-1-1-1 or A12345D",
  )
  var objectId: String,

  @Schema(
    description = "The zero indexed page number to return",
    required = false,
  )
  var page: Int,

  @Schema(
    description = "The number of movements to include per page of results",
    required = false,
  )
  var pageSize: Int,

  @Schema(
    description = "The parameters for historical movements for the given cell",
    required = false,
  )
  var dateFrom: LocalDate? = null,
)

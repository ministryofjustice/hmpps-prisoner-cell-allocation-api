package uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PrisonerSearchRequest(

  @Schema(
    description = "The unique prison number for the person being searched for",
    example = "G7685GO",
  )
  var prisonerId: String,

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
    description = "The parameters for historical movements for the given person",
    required = false,
  )
  var dateFrom: LocalDate? = null,
)

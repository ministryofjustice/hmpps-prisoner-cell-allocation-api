package uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema

@JsonInclude(JsonInclude.Include.NON_NULL)
data class PrisonerSearchRequest(

  @Schema(
    description = "The unique prison number for the person being searched for",
    example = "G7685GO",
  )
  var prisonerId: String,
)

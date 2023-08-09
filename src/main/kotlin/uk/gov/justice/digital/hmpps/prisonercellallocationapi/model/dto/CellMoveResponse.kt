package uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto

import com.fasterxml.jackson.annotation.JsonInclude
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode

@Schema(description = "Cell move response")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class CellMoveResponse(

  @Schema(requiredMode = RequiredMode.REQUIRED, description = "Unique, numeric booking id.", example = "1234134")
  val bookingId: Long? = null,

  @Schema(
    requiredMode = RequiredMode.REQUIRED,
    description = "Identifier of agency that offender is associated with.",
    example = "MDI",
  )
  val agencyId: String? = null,

  @Schema(description = "Identifier of living unit (e.g. cell) that offender is assigned to.", example = "123123")
  val assignedLivingUnitId: Long? = null,

  @Schema(description = "Description of living unit (e.g. cell) that offender is assigned to.", example = "MDI-1-1-3")
  val assignedLivingUnitDesc: String? = null,

  @Schema(description = "Bed assignment sequence associated with the entry created for this cell move ", example = "2")
  val bedAssignmentHistorySequence: Int? = null,

)

package uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.clientapi.PrisonApiClient
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.config.ErrorResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMoveResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.MoveToCellSwapRequest

@RestController
@RequestMapping(value = ["/api/"], produces = ["application/json"])
class CellMoveResource(
  private val prisonApiClient: PrisonApiClient,
) {

  @Operation(
    summary = "Move the person temporarily out of cell",
    description = "Move the person temporarily out of the cell",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "Move accepted",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = CellMoveResponse::class),
          ),
        ],
      ),
      ApiResponse(
        responseCode = "401",
        description = "Unauthorized to access this endpoint",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "403",
        description = "Incorrect permissions to retrieve",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = ErrorResponse::class))],
      ),
      ApiResponse(
        responseCode = "500",
        description = "Unexpected error",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = ErrorResponse::class),
          ),
        ],
      ),
    ],
  )
  @PutMapping(path = ["/bookings/{bookingId}/move-to-cell-swap"])
  @PreAuthorize("hasRole('ROLE_CELL_MOVE')")
  fun moveToCellSwap(
    @Schema(description = "Booking Id", example = "123345", required = true)
    @PathVariable
    bookingId: Long,

    @RequestBody
    @Valid
    @NotNull
    moveToCellSwapRequest: MoveToCellSwapRequest,
  ): CellMoveResponse = prisonApiClient.moveToCellSwap(bookingId, moveToCellSwapRequest)
}

package uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.config.ErrorResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.PrisonerSearchRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.PrisonerSearchResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.service.CellMovementService

@RestController
@RequestMapping(value = ["/api/prisoner/"], produces = ["application/json"])
class PrisonerLocationResource(
  private val cellMovementService: CellMovementService,
) {
  @PreAuthorize("hasRole('ROLE_VIEW_CELL_MOVEMENTS')")
  @Operation(
    summary = "View current cell for a given prisoner ID",
    description = "Display the current cell of a resident prisoner. The role ROLE_VIEW_CELL_MOVEMENTS is required to perform this operation.",
    responses = [
      ApiResponse(
        responseCode = "200",
        description = "Cell allocation found",
        content = [
          Content(
            mediaType = "application/json",
            schema = Schema(implementation = PrisonerSearchResponse::class),
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
        responseCode = "404",
        description = "Active cell allocation for prisoner not found",
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
  @GetMapping(path = ["{prisonerId}/current-cell"])
  fun getCurrentCell(
    @PathVariable
    @Valid
    @NotNull
    prisonerId: String,
  ): PrisonerSearchResponse = cellMovementService.findByPrisonerId(PrisonerSearchRequest(prisonerId))
}

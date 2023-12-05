package uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.config.ErrorResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMoveResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementRequest
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.service.CellMovementService
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.validation.BusinessValidator
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.validation.RequestValidator

@RestController
@RequestMapping(value = ["/api/prisoner/{prisonerId}"], produces = ["application/json"])
class PrisonerMovementResource(
  private val requestValidator: RequestValidator,
  private val businessValidator: BusinessValidator,
  private val cellMovementService: CellMovementService,
) {

  @PreAuthorize("hasRole('ROLE_MAINTAIN_CELL_MOVEMENTS')")
  @Operation(
    summary = "Move a person into a cell",
    description = "Move a person into a cell. The role ROLE_MAINTAIN_CELL_MOVEMENTS is required to perform this operation.",
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
  @PostMapping(path = ["/move-in"])
  fun moveIntoCell(
    @PathVariable
    @NotNull
    prisonerId: String,
    @RequestBody
    @Valid
    @NotNull
    cellMovementRequest: CellMovementRequest,
  ): CellMovementResponse {
    log.info("Request to move prisoner [{}] into cell", prisonerId)
    requestValidator.validateMatchingPrisonerIds(prisonerId, cellMovementRequest)
    businessValidator.checkMovePossible(prisonerId, cellMovementRequest.nomisCellId)

    return cellMovementService.moveIn(cellMovementRequest)
  }

  @PreAuthorize("hasRole('ROLE_MAINTAIN_CELL_MOVEMENTS')")
  @Operation(
    summary = "Move a person out from a cell",
    description = "Move a person out from a cell. The role ROLE_MAINTAIN_CELL_MOVEMENTS is required to perform this operation.",
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
  @PostMapping(path = ["/move-out"])
  fun moveOutCell(
    @PathVariable
    @NotNull
    prisonerId: String,
    @RequestBody
    @Valid
    @NotNull
    cellMovementRequest: CellMovementRequest,
  ): CellMovementResponse {
    log.info("Request to move prisoner [{}] out of cell", prisonerId)
    requestValidator.validateMatchingPrisonerIds(prisonerId, cellMovementRequest)
    return cellMovementService.moveOut(cellMovementRequest)
  }

  companion object {
    private val log = LoggerFactory.getLogger(this::class.java)
  }
}

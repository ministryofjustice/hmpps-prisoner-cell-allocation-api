package uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto

import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.CellMovement

data class MovementHistoryResponse(
  val movements: List<CellMovement>,
  val page: Int,
  val pageSize: Int,
)

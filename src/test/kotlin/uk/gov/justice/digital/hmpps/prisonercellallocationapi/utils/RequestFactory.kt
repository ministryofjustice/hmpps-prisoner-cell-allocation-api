package uk.gov.justice.digital.hmpps.prisonercellallocationapi.utils

import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMovementRequest
import java.time.LocalDateTime

class RequestFactory {

  fun aMovementRequest(prisonerId: String): CellMovementRequest = CellMovementRequest(
    "MDI",
    123,
    "Cell Description",
    prisonerId,
    "Batz Jerel",
    "guardian id",
    LocalDateTime.of(2023, 8, 1, 10, 0, 0),
    "Reason",
  )
}

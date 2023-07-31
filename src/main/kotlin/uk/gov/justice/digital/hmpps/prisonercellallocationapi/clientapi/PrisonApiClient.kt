package uk.gov.justice.digital.hmpps.prisonercellallocationapi.clientapi

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMoveResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.MoveToCellSwapRequest
import java.security.PrivateKey

@Component
class PrisonApiClient(
//    @Qualifier("prisonApiWebClient")
    private val webClient: WebClient
) {

    fun moveToCellSwap(bookingId: Long, detail: MoveToCellSwapRequest): CellMoveResponse =
        webClient.put()
            .uri("/api/bookings/$bookingId/move-to-cell-swap")
            .bodyValue(detail)
            .retrieve()
            .bodyToMono(CellMoveResponse::class.java)
            .block() ?: throw IllegalStateException("No response from prison api")
}

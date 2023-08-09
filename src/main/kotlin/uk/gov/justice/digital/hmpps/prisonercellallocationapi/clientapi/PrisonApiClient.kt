package uk.gov.justice.digital.hmpps.prisonercellallocationapi.clientapi

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.config.ClientException
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.config.NoBodyClientException
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.CellMoveResponse
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.MoveToCellSwapRequest

@Component
class PrisonApiClient(
  @Qualifier("prisonApiWebClient")
  private val webClient: WebClient,
) {

  fun moveToCellSwap(bookingId: Long, detail: MoveToCellSwapRequest): CellMoveResponse =
    webClient.put()
      .uri("/api/bookings/$bookingId/move-to-cell-swap")
      .bodyValue(detail)
      .retrieve()
      .onStatus({ httpStatus -> httpStatus.value() == 401 }) { throw NoBodyClientException(response = 401) }
      .onStatus({ httpStatus -> httpStatus.value() == 403 }) { throw NoBodyClientException(response = 403) }
      .onStatus({ httpStatus -> httpStatus.is4xxClientError }) { response ->
        response.bodyToMono(ClientException::class.java)
      }
      .bodyToMono(CellMoveResponse::class.java)
      .block() ?: throw IllegalStateException("No response from prison api")
}

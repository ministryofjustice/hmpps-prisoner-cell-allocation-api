package uk.gov.justice.digital.hmpps.prisonercellallocationapi.integration

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.put
import org.springframework.http.MediaType
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.utils.loadJson
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class PrisonApiMockServer : WireMockServer(9005) {

  fun stubMoveToCellSwapPositiveResponse(bookingId: Long) {

    stubFor(
      put("/api/bookings/$bookingId/move-to-cell-swap")
        .willReturn(
          aResponse()
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .withStatus(200)
            .withBody(
              """
              {
                  "bookingId": 988507,
                  "agencyId": "ACI",
                  "assignedLivingUnitId": 411283,
                  "assignedLivingUnitDesc": "ACI-CSWAP",
                  "bedAssignmentHistorySequence": null
              }

            """.trimIndent(),
            ),
        ),
    )
  }
}

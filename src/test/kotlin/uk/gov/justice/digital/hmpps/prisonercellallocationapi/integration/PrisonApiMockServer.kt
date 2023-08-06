package uk.gov.justice.digital.hmpps.prisonercellallocationapi.integration

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.put
import org.springframework.http.MediaType

class PrisonApiMockServer : WireMockServer(9005) {

  fun stubMoveToCellSwapPositiveResponse(bookingId: Long) {
    stubFor(
      put("/api/bookings/$bookingId/move-to-cell-swap")
        .withRequestBody(
          WireMock.equalToJson(
            """{"reasonCode":"ADM","dateTime":[2023,8,1,10,0]}""",
          ),
        )
        .willReturn(
          aResponse()
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .withStatus(200)
            .withBody(
              """
            {
              "bookingId": $bookingId,
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

  fun stubCellSwapNegativeResponse(bookingId: Long) {
    stubFor(
      put("/api/bookings/$bookingId/move-to-cell-swap")
        .willReturn(
          aResponse()
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .withStatus(400)
            .withBody(
              """{
                "status": 400,
                "userMessage": "The date cannot be in the future",
                "developerMessage": "The date cannot be in the future"
                }""",
            ),
        ),
    )
  }

  fun stubCellSwapLocationNotFoundResponse(bookingId: Long) {
    stubFor(
      put("/api/bookings/$bookingId/move-to-cell-swap")
        .willReturn(
          aResponse()
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .withStatus(404)
            .withBody(
              """{
                 "status": 404,
                 "userMessage": "CSWAP location not found for NMI",
                 "developerMessage": "CSWAP location not found for NMI"
                  }""",
            ),
        ),
    )
  }

  fun stubCellSwapUnauthorizedResponse(bookingId: Long) {
    stubFor(
      put("/api/bookings/$bookingId/move-to-cell-swap")
        .willReturn(
          aResponse()
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .withStatus(401),
        ),
    )
  }
}

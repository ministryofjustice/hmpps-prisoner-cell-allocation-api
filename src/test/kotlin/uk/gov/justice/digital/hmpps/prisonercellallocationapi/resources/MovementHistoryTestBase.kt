package uk.gov.justice.digital.hmpps.prisonercellallocationapi.resources

import org.springframework.web.util.DefaultUriBuilderFactory
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.integration.IntegrationTestBase
import uk.gov.justice.digital.hmpps.prisonercellallocationapi.model.dto.MovementHistoryResponse
import java.util.Optional.ofNullable

abstract class MovementHistoryTestBase : IntegrationTestBase() {

  internal fun getMovementHistory(
    type: historyType,
    objectId: String,
    page: Int? = null,
    pageSize: Int? = null,
    dateFrom: String? = null,
  ): MovementHistoryResponse {
    val uriBuilder = DefaultUriBuilderFactory().builder().path("/api/${type.uriString}/$objectId/history?")
    uriBuilder.queryParamIfPresent("page", ofNullable(page))
    uriBuilder.queryParamIfPresent("pageSize", ofNullable(pageSize))
    uriBuilder.queryParamIfPresent("dateFrom", ofNullable(dateFrom))
    val uri = uriBuilder.build()

    return webTestClient
      .get()
      .uri(uri.path + uri.query.orEmpty())
      .headers(
        setAuthorisationWithUser(
          roles = listOf("ROLE_VIEW_CELL_MOVEMENTS"),
          scopes = listOf("read"),
        ),
      )
      .exchange()
      .expectStatus().isOk
      .expectBody(MovementHistoryResponse::class.java)
      .returnResult().responseBody!!
  }

  enum class historyType(val uriString: String) {
    PRISONER("prisoner"),
    CELL("nomis-cell"),
  }
}

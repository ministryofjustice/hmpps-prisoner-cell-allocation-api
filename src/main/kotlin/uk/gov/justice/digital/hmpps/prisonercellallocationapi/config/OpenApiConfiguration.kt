package uk.gov.justice.digital.hmpps.prisonercellallocationapi.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.boot.info.BuildProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfiguration(buildProperties: BuildProperties) {
  private val version: String = buildProperties.version

  @Bean
  fun customOpenAPI(): OpenAPI = OpenAPI()
    .servers(
      listOf(
        Server().url("https://cell-allocation-api.prison.service.justice.gov.uk/").description("Prod"),
        Server().url("https://cell-allocation-api-preprod.prison.service.justice.gov.uk/").description("PreProd"),
        Server().url("https://cell-allocation-api-dev.prison.service.justice.gov.uk/").description("Development"),
        Server().url("http://localhost:8080").description("Local"),
      ),
    )
    .info(
      Info().title("Cell allocation API")
        .version(version)
        .description("Enabling prisoner cell movements")
        .contact(Contact().name("HMPPS Digital Studio").email("feedback@digital.justice.gov.uk")),
    )
}

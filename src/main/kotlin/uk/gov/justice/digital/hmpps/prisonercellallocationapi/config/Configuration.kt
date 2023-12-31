package uk.gov.justice.digital.hmpps.prisonercellallocationapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock
import java.time.ZoneId

@Configuration
class Configuration {
  @Bean
  fun clock(): Clock {
    return Clock.system(ZoneId.of("Europe/London"))
  }
}

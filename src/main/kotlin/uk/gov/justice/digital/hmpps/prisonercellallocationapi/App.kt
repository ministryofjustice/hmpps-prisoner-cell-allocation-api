package uk.gov.justice.digital.hmpps.prisonercellallocationapi

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication()
class App

fun main(args: Array<String>) {
  runApplication<App>(*args)
}

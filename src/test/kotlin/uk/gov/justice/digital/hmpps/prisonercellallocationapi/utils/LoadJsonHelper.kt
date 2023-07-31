package uk.gov.justice.digital.hmpps.prisonercellallocationapi.utils

fun String.loadJson(testInstance: Any): String =
  testInstance::class.java.getResource("$this.json")?.readText()
    ?: throw AssertionError("file $this.json not found")

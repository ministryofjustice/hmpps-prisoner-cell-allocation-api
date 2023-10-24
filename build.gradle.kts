plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "5.7.0"
  kotlin("plugin.spring") version "1.9.10"
}

val jsonwebtokenVersion by extra("0.12.3")
val springBootStarterOauth2Version by extra("3.1.5")

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

dependencies {
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
  implementation("org.springframework.boot:spring-boot-starter-webflux:$springBootStarterOauth2Version")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:$springBootStarterOauth2Version")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-client:$springBootStarterOauth2Version")

  testImplementation("org.wiremock:wiremock-standalone:3.2.0")
  testImplementation("org.eclipse.jetty:jetty-reactive-httpclient:3.0.8")
  testImplementation("org.springframework.security:spring-security-test")
  testImplementation("io.jsonwebtoken:jjwt-api:$jsonwebtokenVersion")
  testImplementation("io.jsonwebtoken:jjwt-impl:$jsonwebtokenVersion")
  testImplementation("io.jsonwebtoken:jjwt-orgjson:$jsonwebtokenVersion")
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(19))
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
      apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_1_9)
      jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_19)
    }
  }
}

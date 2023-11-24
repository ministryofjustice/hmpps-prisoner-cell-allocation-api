plugins {
  id("uk.gov.justice.hmpps.gradle-spring-boot") version "5.9.0"
  kotlin("plugin.spring") version "1.9.20"
  kotlin("plugin.jpa") version "1.9.20"
}

val jsonwebtokenVersion by extra("0.12.3")
val springBootStarterOauth2Version by extra("3.1.5")

configurations {
  testImplementation { exclude(group = "org.junit.vintage") }
}

dependencies {
  implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-webflux:$springBootStarterOauth2Version")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server:$springBootStarterOauth2Version")
  implementation("org.springframework.boot:spring-boot-starter-oauth2-client:$springBootStarterOauth2Version")

  runtimeOnly("org.flywaydb:flyway-core")
  runtimeOnly("org.postgresql:postgresql:42.5.4")
  testRuntimeOnly("com.h2database:h2:2.2.220")

  testImplementation("org.wiremock:wiremock-standalone:3.2.0")
  testImplementation("org.eclipse.jetty:jetty-reactive-httpclient:3.0.8")
  testImplementation("org.springframework.security:spring-security-test")
  testImplementation("io.jsonwebtoken:jjwt-api:$jsonwebtokenVersion")
  testImplementation("io.jsonwebtoken:jjwt-impl:$jsonwebtokenVersion")
  testImplementation("io.jsonwebtoken:jjwt-orgjson:$jsonwebtokenVersion")
}

java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    compilerOptions {
      apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
      jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
      languageVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_1)
    }
  }
}
